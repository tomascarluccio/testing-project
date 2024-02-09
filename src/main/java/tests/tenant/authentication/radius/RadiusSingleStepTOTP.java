package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.RadiusAuth;
import utils.UserAPI;


public class RadiusSingleStepTOTP extends BaseAPITest {

	
	@DataProvider(name = "radiusTOTPUsers2", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radius1steptopt", Config.tnPassword, Config.radiusServerHost},  //LDAP Users request to Server radius
			{"radius1steptopt2", Config.tnPassword, Config.radiusBrokerHost},  //LDAP Users request to Broker radius
			{"intradius1steptopt5", Config.tnPassword, Config.radiusServerHost},  //internal Users request to Server radius
			{"intradius1steptopt7", Config.tnPassword, Config.radiusBrokerHost},  //internal Users request to Server radius

		};
	}


	@Test(dataProvider = "radiusTOTPUsers2", priority = 3)
	@Report(name= "Radius TOTP password single step allowed", description = "API user enters valid otp as single step it is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
				
	    //Authentication
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		String status =  RadiusAuth.authenticate(username, otp, host);
		assertEquals(status, "ACCESS_ALLOWED");
		
	}
	
	@Test(dataProvider = "radiusTOTPUsers2", priority = 4)
	@Report(name ="Radius TOTP password single step invalid otp", description = "API user enters invalid otp as single step it is expected to be DENIED")
	public void test3(String username, String password,  String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		
	    //Authentication
	    Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		String status =  RadiusAuth.authenticateTimeout(username, invalidOTP, 300000, host);
		assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider = "radiusTOTPUsers2", priority = 5)
	@Report(name ="Radius TOTP user temp lock", description = "API user enters invalid otp 6 times it is expected TMP_LOCKDOWN then resets falied attemps it is expected to be ALLOWED after")
	public void test4(String username, String password,  String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
				
	    //Authentication
		Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		for(int i = 0; i< 5;i++) {
		    invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		    Thread.sleep(5000);
			String status =  RadiusAuth.authenticateTimeout(username, invalidOTP, 400000, host);
		    assertEquals(status, "ACCESS_DENIED");
		    
		}
		
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		String status =  RadiusAuth.authenticateTimeout(username, invalidOTP, 400000, host);
	    assertEquals(status, "ACCESS_DENIED");
	    API.resetFailedAttemptsCount(username);
		Thread.sleep(40000);
		otp =  mobile.getTOTPCode(Instant.now());
		status =  RadiusAuth.authenticateTimeout(username, otp, 400000, host);
	    assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
	}
	
	
	@Test(dataProvider = "radiusTOTPUsers2", priority = 6)
	@Report(name ="RadiusTOTP valid OTP in acceptance tolerance window future", description = "API user enters a valid OTP with two minutes forward drift,it is expected to be ALLOWED")
	public void test5(String username, String password,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		  
	    //Authentication
		Thread.sleep(40000);
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
		
		String status =  RadiusAuth.authenticate(username, otp, host);
		assertEquals(status, "ACCESS_ALLOWED");	
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider = "radiusTOTPUsers2", priority = 7)
	@Report(name ="Radius TOTP valid OTP device out fo sync", description = "API user a valid OTP with five minutes backward drift,it is expected to get a CHALLENGE, the generates a new valid otp and it is expected to be ALLOWED")
	public void test6(String username, String password,  String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		
	    //Authentication
		Thread.sleep(40000);
		Instant instant = Instant.now().minus(5, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
		String status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_CHALLENGE");		
		
	    otp =  mobile.getTOTPCode(Instant.now());
	    status =  RadiusAuth.authenticate(username, otp, host);
		assertEquals(status, "ACCESS_ALLOWED");	
	
	}
	
			
}



