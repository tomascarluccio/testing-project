package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.RadiusAuth;
import utils.Response;
import utils.SMSClient;
import utils.UserAPI;

public class RadiusPushFallbackTest extends BaseAPITest {

	@DataProvider(name = "radiusFallbackUsers", parallel=true )
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radiusfallback1", Config.tnPassword,  Config.radiusBrokerHost},  //LDAP Users request to Broker radius
			{"radiusfallback4", Config.tnPassword,  Config.radiusServerHost},
			{"intradiusfallback5", Config.tnPassword,  Config.radiusBrokerHost},  //Internal Users request to Broker radius
			};
	}

		
	@Test(dataProvider = "radiusFallbackUsers", priority = 2)
	@Report(name="Radius Fast Auth Push Sync Timeout fallback TOTP", description = "API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);	
		API.asociateLicense(username, "TOTP:Mobile");
		
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
		
		//wait for the next otp
		Thread.sleep(40000); 
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		mobile.registerFastAuth( username, mobile.getTOTPCode(Instant.now()) );	
		API.setTOTPPasswordRequired(username, true);
		
		//Authentication
		String stat = RadiusAuth.authenticate(username, password, host);
	    String otp = mobile.getTOTPCode(Instant.now());
	    String status =  RadiusAuth.authenticate(username, otp, host);	
	    assertEquals(status, "ACCESS_ALLOWED");	
		
	}
	


	@Test(dataProvider = "radiusFallbackUsers", priority = 4)
	@Report(name="Radius Fast Auth Push Sync Timeout fallback backupcode", description = "API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void test3(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
				
		//Registration
		String backupcode = API.getBackupCode(username);
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");	
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		
		//Authentication

		//Authentication
		String stat = RadiusAuth.authenticate(username, password, host);

		RadiusAuth.authenticateTimeout(username, password, 91000, host);
		stat =  RadiusAuth.authenticate(username, backupcode, host);
	    assertEquals(stat, "ACCESS_ALLOWED");
		
	}
	
	
	@Test(dataProvider = "radiusFallbackUsers", priority = 6)
	@Report(name="Radius Fast Auth Push Sync Timeout fallback virtual", description = "API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void test4(String username, String password, String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		String randomMobile = new DecimalFormat("00000000").format(new Random().nextInt(99999999));
		API.setUserPersonalInformation(username, "test@example.com", randomMobile);
	   
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		Response response05 = API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		mobile.registerFastAuth( username, password );	
		API.asociateLicense(username, "Virtual");
        API.setVirtualPasswordRequired(username, true);
	    
        //Authentication
        RadiusAuth.authenticateTimeout(username, password, 91000, host);
	 
        Thread.sleep(4000);
    	String otp = SMSClient.getMessage(randomMobile);
        String status =  RadiusAuth.authenticate(username, otp, host);
  	    assertEquals(status, "ACCESS_ALLOWED");		
	}
	
}
