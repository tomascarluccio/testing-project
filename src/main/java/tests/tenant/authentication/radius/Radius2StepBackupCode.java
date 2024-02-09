package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.RadiusAuth;
import utils.UserAPI;


public class Radius2StepBackupCode extends BaseAPITest {

	
	@DataProvider(name = "radiusBackupCodeUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"r2backupcode1", "Safewalk1", Config.radiusBrokerHost},  //LDAP Users request to Broker radius
			{"r2backupcode2", "Safewalk1", Config.radiusServerHost},
			{"intr2intbackupcode1", "Safewalk1", Config.radiusBrokerHost}, //Internal Users request to Broker radius
		};
	}

	
	@Test(dataProvider = "radiusBackupCodeUsers", priority = 3)
	@Report(name= "Radius 2 Step backup token - password authentication setting not allowed", description = "API user enters first password then the backcode is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, false);
		
	    //Registration
	    String backupCode = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, true);
		
	    //Authentication
		String status =  RadiusAuth.authenticate(username, password, host);
	  	assertEquals(status, "ACCESS_CHALLENGE");	
	    status =  RadiusAuth.authenticate(username, backupCode, host);
	 	assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider = "radiusBackupCodeUsers", priority = 4)
	@Report(name= "Radius 2 Step backup token - password authentication allowed", description = "API user enters first password then the backcode is expected to be ALLOWED")
	public void test3(String username, String password, String host) throws Exception {

        //Users Setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		
	    //Registration
	    String backupCode = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, true);
	
	    //Authentication
	    String status =  RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_CHALLENGE");
	  			
	    status =  RadiusAuth.authenticate(username, backupCode, host);
	 	assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
		
		
		
	  }
	
	
//	@Test(dataProvider = "radiusBackupCodeUsers", priority = 5)
//	@Report(name="RADIUS 2 STEP WITH BACKUP TOKEN AS FIRST STEP", description="Not possible to automate because the challenge is generated when the code is created via api")
//	public void test4(String username, String password) throws Exception {
//
//		API.deleteUser(username);	
//		Response response01 = API.createUser(username, password);
//		assertEquals(response01.getCode(), 201, API.releaseLicenses(username);
	
//	
//		API.allowAuthWithPassword(username, true);
//		String backupCode = API.getBackupCode(username);
//	    API.setBackupCodePasswordRequired(username, true); 			
//		
//	    String status =  RadiusAuth.authenticate(username, backupCode);
//	 	assertEquals(status, "ACCESS_DENIED");
//	 	
//	}
	
	@Test(dataProvider = "radiusBackupCodeUsers", priority = 5)
	@Report(name= "Radius 2 Step used expired backup token", description = "API user enters first password then the backcode and its allowed, then generates a new back code, and tries to authenticate with the first one (used previosuly), it is expected to be INVALID_CREDENTIALS")
	public void test6(String username, String password, String host) throws Exception {

        //Users Setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, false);
		
	    //Registration
	    String backupCode = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, true);
		
	    //Authentication
	    String status =  RadiusAuth.authenticate(username, password, host);
     	assertEquals(status, "ACCESS_CHALLENGE");
     	status =  RadiusAuth.authenticate(username, backupCode, host);
		assertEquals(status, "ACCESS_ALLOWED");
		String backupCode2 = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, true);
	    status =  RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_CHALLENGE");
	    status =  RadiusAuth.authenticate(username, backupCode, host);
	    assertEquals(status, "ACCESS_DENIED");
		
		
	}
	
}



