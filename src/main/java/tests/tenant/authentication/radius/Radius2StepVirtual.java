package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.RadiusAuth;
import utils.SMSClient;
import utils.UserAPI;


public class Radius2StepVirtual extends BaseAPITest {

	
	@DataProvider(name = "radiusVirtualUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radiusVirtual1", "Safewalk1",  Config.radiusBrokerHost}, //LDAP Users request to Broker radius
			{"radiusVirtual4", "Safewalk1",   Config.radiusServerHost},
			{"intradiusVirtual5", "Safewalk1",   Config.radiusBrokerHost},  //Internal Users request to Broker radius
		};
	}
	

	@Test(dataProvider = "radiusVirtualUsers", priority = 3)
	@Report(name= "Radius 2 step virtual valid otp", description = "API user enters a password and a valid OTP as second step, it is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);	
		String randomMobile = new DecimalFormat("000000000").format(new Random().nextInt(9999999));
		API.setUserPersonalInformation(username, "safewalk.integration@gmail.com", randomMobile);
		
		//Registration
		API.asociateLicense(username, "Virtual");
	    API.setVirtualPasswordRequired(username, true);
	    
	    //Authentication
	    
	    String  status =  RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_CHALLENGE");
		Thread.sleep(5000);
		String otp = SMSClient.getMessage(randomMobile);System.out.println(otp + " otp       ");	
        status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_ALLOWED");	
	}
	
	@Test(dataProvider = "radiusVirtualUsers", priority = 4)
	@Report(name= "RADIUS 2 STEP VIRTUAL INVALID OTP - DENIED", description = "No description")
	public void test3(String username, String password, String host) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);	
		String randomMobile = new DecimalFormat("000000000").format(new Random().nextInt(9999999));
		API.setUserPersonalInformation(username, "safewalk.integration@gmail.com", randomMobile);
		
		//Registration
		API.asociateLicense(username, "Virtual");
	    API.setVirtualPasswordRequired(username, true);

	    //Authentication
	    String  status =  RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_CHALLENGE");
		Thread.sleep(30000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		status =  RadiusAuth.authenticate(username, invalidOTP, host);
	    assertEquals(status, "ACCESS_DENIED");
		
	}
	 
}



