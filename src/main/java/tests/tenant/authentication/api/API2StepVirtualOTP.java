package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.Response;
import utils.SMSClient;
import utils.UserAPI;

public class API2StepVirtualOTP extends BaseAPITest {

	@DataProvider(name = "virtualDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"virtual2step1@"+Config.ldapDomain, Config.tnPassword}, //LDAP Users
			{"virtual2step2@"+Config.ldapDomain, Config.tnPassword},
			{"virtual2step5@", Config.tnPassword},  // Internal Users
			{"virtual2step6@", Config.tnPassword},
			
		};
	}


	@Test(dataProvider = "virtualDataProvider", priority = 3)
	@Report(name="2 step Virtual valid otp", description="API user enters a password and a valid OTP as second step, it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.allowPassword(username, true);

		String randomMobile = new DecimalFormat("000000000").format(new Random().nextInt(9999999));
		API.setUserPersonalInformation(username, "safewalk.integration@gmail.com", randomMobile);
		
		//Registration
		API.asociateLicense(username, "Virtual");
	    API.setVirtualPasswordRequired(username, true);
	    
	    //Authentication
	    Response challenge03 = API.authenticate(username ,password);
		assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
		assertEquals(API.getTransactinsLogReason(challenge03.getProperty("transaction-id")), "OTP_REQUIRED");
		Thread.sleep(5000);
		String otp = SMSClient.getMessage(randomMobile);System.out.println(otp + " otp       ");	
		Response response10 = API.authenticate(username, otp);
		assertEquals(response10.getCode(), 200, "Failed to accept 2 step virtual otp");	
		
	}
	
	@Test(dataProvider = "virtualDataProvider", priority = 4)
	@Report(name="2 step Virtual invalid otp", description="API user enters a password  and a random 6 digits OTP as second step, it is expected to be DENIED")
	public void test3(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.allowPassword(username, true);
		String randomMobile = new DecimalFormat("000000000").format(new Random().nextInt(9999999));
		API.setUserPersonalInformation(username, "safewalk.integration@gmail.com", randomMobile);
				
		//Registration
		API.asociateLicense(username, "Virtual");
		API.setVirtualPasswordRequired(username, true);
			   
		//Authentication
	    Response challenge03 = API.authenticate(username ,password);
		assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
		//		assertEquals(API.getTransactinsLogReason(challenge03.getProperty("transaction-id")), "OTP_REQUIRED");
		Thread.sleep(30000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		Response response10 = API.authenticate(username, invalidOTP);
		assertEquals(response10.getCode(), 401, "Failed to deny 2 step  virtual otp");	
		
	}

}