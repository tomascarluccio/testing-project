package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.Response;
import utils.SMSClient;
import utils.UserAPI;

public class APIPushFallbackTest extends BaseAPITest {

	@DataProvider(name = "fallbackUsers", parallel=true )
	public Object[][] getUserData() {
		return new Object[][] { 
			{"fallback1@"+Config.ldapDomain, Config.tnPassword}, //LDAP Users
			{"fallback2@"+Config.ldapDomain, Config.tnPassword},
			{"fallback5@", Config.tnPassword},  //Internal Users
			{"fallback6@", Config.tnPassword},
		};
	}


	@Test(dataProvider = "fallbackUsers", priority = 2)
	@Report(name="Fast Auth Push Sync Timeout fallback TOTP", description="API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {

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
		
		//Authentication
		Response response10 = API.authenticate(username, password);
		assertEquals(response10.getCode(), 401, "Push fallback to TOTP code failed");	
		   
		String otp = mobile.getTOTPCode(Instant.now());
		Response response12 = API.authenticate(username, otp);
		assertEquals(response12.getCode(), 200, "Confirm push fallback with otp  failed");	

	}
	
	@Test(dataProvider = "fallbackUsers", priority = 3)
	@Report(name="Fast Auth Push ASync Timeout fallback TOTP", description="API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void test3(String username, String password) throws Exception {

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
		Thread.sleep(40000);
		API.setTOTPPasswordRequired(username, true);
		
		Response response = null;;
		try {
			response = API.authenticateAsyncPush(username, password, 300000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
	  }	
				
    	String otp = mobile.getTOTPCode(Instant.now());
		Response response1 = API.authenticate(username, otp);
		assertEquals(response1.getCode(), 200, "Failed to authenticate");
	
	}

	
	@Test(dataProvider = "fallbackUsers", priority = 4)
	@Report(name="Fast Auth Push Sync Timeout fallback Back Code", description="API user enters credentials and wait fot timeout, then enters a backup code, it is expected to be ALLOWED")
	public void test4(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);

		API.allowAuthWithPassword(username, true);
		//Registration
		String backupcode = API.getBackupCode(username);

		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		API.setBackupCodePasswordRequired(username, false);

		//Authentication
		Response response = API.authenticate(username, password);
		assertEquals(response.getCode(), 401, "Failed to authenticate");
		JSONParser parser = new JSONParser();
    	JSONObject json = (JSONObject) parser.parse(response.getContent());
		String code = (String) json.get("code");

		Response response12 = API.authenticate(username, backupcode);
		assertEquals(response12.getCode(), 200, "confirm push fallback with otp  failed");
		API.releaseLicenses(username);
		
	
	}
	
	@Test(dataProvider = "fallbackUsers", priority = 5)
	@Report(name="Fast Auth Push ASync Timeout fallback Back Code", description="API user enters credentials and wait fot timeout, then enters a backup code, it is expected to be ALLOWED")
	public void test5(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);

		API.allowAuthWithPassword(username, true);
		//Registration
		String backupcode = API.getBackupCode(username);
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		API.setBackupCodePasswordRequired(username, false);
		Response response = null;
		try {
			response = API.authenticateAsyncPush(username, password, 300000);
		} catch (Exception e) {
		}

    	Response response1 = API.authenticate(username, backupcode);
		assertEquals(response1.getCode(), 200, "Failed to authenticate");

	}

	@Test(dataProvider = "fallbackUsers", priority = 6)
	@Report(name="Fast Auth Push Sync Timeout fallback virtual otp", description="API user enters credentials and wait fot timeout, then enters a virtual otp, it is expected to be ALLOWED")
	public void test6(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		String randomMobile = new DecimalFormat("00000000").format(new Random().nextInt(99999999));
		API.setUserPersonalInformation(username, "test@example.com", randomMobile);

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		mobile.registerFastAuth( username, password );	
		API.asociateLicense(username, "Virtual");
        API.setVirtualPasswordRequired(username, true);
    
        //Authentication
        Response challenge03 = API.authenticate(username ,password);
        assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
        Thread.sleep(4000);
        String otp = SMSClient.getMessage(randomMobile);
        Response challenge04 = API.authenticate(username ,otp);
		assertEquals(challenge04.getCode(), 200, "Failed to confirm challenge with virtual otp");
		}
	
	
	@Test(dataProvider = "fallbackUsers", priority = 7)
	@Report(name="Fast Auth Push ASync Timeout fallback virtual otp", description="API user enters credentials and wait fot timeout, then enters a virtual otp, it is expected to be ALLOWED")
	public void test7(String username, String password) throws Exception {

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
		   
		//Authenticacion	
		try {
			Response response = API.authenticateAsyncPush(username, password, 300000);
			assertEquals(response.getCode(), 200, "Failed to authenticate");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response.getContent());
			String code = (String) json.get("code");
			System.out.println(code);
		} catch (Exception e) {
		}

		Thread.sleep(4000);
		String otp = SMSClient.getMessage(randomMobile);
		Response response1 = API.authenticate(username, otp);
		assertEquals(response1.getCode(), 200, "Failed to authenticate");
		API.releaseLicenses(username);

	}

}
