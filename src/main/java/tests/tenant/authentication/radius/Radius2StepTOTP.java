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


public class Radius2StepTOTP extends BaseAPITest {

	
	@DataProvider(name = "radiusTOTPUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radius2Stopt1", "Safewalk1",  Config.radiusServerHost},  //LDAP Users request to Broker radius
			{"radius2Stopt4", "Safewalk1",  Config.radiusBrokerHost},
			{"intradius2Stopt5", "Safewalk1",  Config.radiusBrokerHost},  //Internal Users request to Broker radius
			{"intradius2Stopt3", "Safewalk1",  Config.radiusServerHost},  //Internal Users request to Server radius

		};
	}
	

	@Test(dataProvider="radiusTOTPUsers", priority = 2)
	public void test1(String username, String password,  String host)  throws Exception {
		API.setTOTPInputMethod("");
	}
		
	@Test(dataProvider = "radiusTOTPUsers", priority = 3)
	@Report(name= "Radius 2 step TOTP valid otp", description = "API user enters a password and a valid OTP as second step, it is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

        //User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authentication
	    Thread.sleep(40000);
		String status =  RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_CHALLENGE");
	    String otp =  mobile.getTOTPCode(Instant.now());
	    status =  RadiusAuth.authenticate(username, otp, host);
		assertEquals(status, "ACCESS_ALLOWED");
	}
	
	@Test(dataProvider = "radiusTOTPUsers", priority = 4)
	@Report(name= "Radius 2 step TOTP invalid otp", description = "API user enters a password  and a random 6 digits OTP as second step, it is expected to be DENIED")
	public void test3(String username, String password,  String host)  throws Exception {

        //User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authentication
	    Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
	    String status =  RadiusAuth.authenticate(username, invalidOTP, host);	    
		assertEquals(status, "ACCESS_DENIED");	
		
	}
	
	@Test(dataProvider = "radiusTOTPUsers", priority = 5)
	@Report(name= "Radius 2 step TOTP temp lock", description = "API user enters a password and random 6 digits OTP as second step, repeat 6 times, then authenticates with a valid OTP, it is expected to be DENIED, then reset the failed attepms and authenticates, its expected to be ALOLWED ")
	public void test4(String username, String password,  String host ) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    //Authentication
	    Thread.sleep(40000);
		for(int i = 0; i< 4;i++) {
			String status =  RadiusAuth.authenticateTimeout(username, password, 400000, host);
			assertEquals(status, "ACCESS_CHALLENGE");	
			
			String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
			 status =  RadiusAuth.authenticateTimeout(username, invalidOTP, 400000, host);
		    assertEquals(status, "ACCESS_DENIED");
		    
		}
		Thread.sleep(40000);
	    API.resetFailedAttemptsCount(username);
	  
	    String  otp =  mobile.getTOTPCode(Instant.now());
	    String status =  RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_CHALLENGE");	
		
		status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_ALLOWED");
			
	}
	
	
	@Test(dataProvider = "radiusTOTPUsers", priority = 6)
	@Report(name= "Radius 2 step TOTP valid OTP in acceptance tolerance window", description = "API user enters a password  and a valid OTP with two minutes forward drift as second step,it is expected to be ALLOWED")
	public void test5(String username, String password,  String host) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);

		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    //Authentication
	    String  status =  RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_CHALLENGE");		
			
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		String otp = mobile.getTOTPCode(instant);
	
		status =  RadiusAuth.authenticate(username, otp, host);
		
	}
	
	@Test(dataProvider = "radiusTOTPUsers", priority = 7)
	@Report(name= "Radius 2 step TOTP valid OTP device out fo sync", description = "API user enters a password  and a valid OTP with five minutes backward drift as second step,it is to get a CHALLENGE, the generates a new valid otp and it is expected to be ALLOWED")
	public void test6(String username, String password,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    //Authentication
		Thread.sleep(40000);
		String  status =  RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_CHALLENGE");	
		Instant instant = Instant.now().minus(5, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
	    status =  RadiusAuth.authenticateTimeout(username, otp, 400000, host);
	    assertEquals(status, "ACCESS_CHALLENGE");		
	    otp =  mobile.getTOTPCode(Instant.now());
	    status =  RadiusAuth.authenticateTimeout(username, otp, 400000, host);
		assertEquals(status, "ACCESS_ALLOWED");	
	}
			
	@Test(dataProvider = "radiusTOTPUsers", priority = 7)
	@Report(name= "Radius PAssword+TOTP as first step (only password expected as first step", description = "API user enters a password +OTP with suffix-6 password concatenation  as first step, it is expected to be DENIED")
	public void test7(String username, String password,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    //Authentication
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		String status =  RadiusAuth.authenticate(username, password+otp, host);
	    assertEquals(status, "ACCESS_DENIED");		
			
  }
	
	@Test(dataProvider = "radiusTOTPUsers", priority = 7)
	@Report(name= "Radius OTP as first step (only password expected as first step", description = "API user enters a OTP as first step, it is expected to be DENIED")
	public void test8(String username, String password,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    //Authentication
    	Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());	
		String status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_DENIED");	
		
  }
	 
}



