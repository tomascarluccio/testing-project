package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.Response;
import utils.UserAPI;

public class API2StepBackupCode extends BaseAPITest {

	@DataProvider(name = "backupCodeDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"backuptoken2s1@"+Config.ldapDomain, Config.tnPassword},  //LDAP Users
			{"backuptoken2s2@"+Config.ldapDomain, Config.tnPassword},
			{"backuptoken2s5@", Config.tnPassword},  // Internal Users
			{"backuptoken2s6@", Config.tnPassword},
		};
	}

	@Test(dataProvider = "backupCodeDataProvider", priority = 3)
	@Report(name="2 Step backup token -  password authentication setting not allowed", description="API user enters first password then the backcode is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, false);

	    //Registration
	    String backupCode = API.getBackupCode(username);
	    assertNotNull(backupCode, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, true);
	    
	    //Authentication
		Response challenge03 = API.authenticate(username,password);
		assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
		assertEquals(API.getTransactinsLogReason(challenge03.getProperty("transaction-id")), "ADDITIONAL_CODE_REQUIRED");
		
		Response response10 = API.authenticate(username, backupCode);
		assertEquals(response10.getCode(), 200, "Failed to accept 2step  backup token");
		API.releaseLicenses(username);

	}

	@Test(dataProvider = "backupCodeDataProvider", priority = 4)
	@Report(name="2 Step backup token -  password authentication setting allowed", description="API user enters first password then the backcode is expected to be ALLOWED")
	public void test3(String username, String password) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		String backupCode = API.getBackupCode(username);
		assertNotNull(backupCode, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, true);
		
	    //Authentication
		Response challenge03 = API.authenticate(username,password);
		assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
		assertEquals(API.getTransactinsLogReason(challenge03.getProperty("transaction-id")),"ADDITIONAL_CODE_REQUIRED");
		Response response10 = API.authenticate(username, backupCode);
		assertEquals(response10.getCode(), 200, "Failed to get challenge with static pasword");
		API.releaseLicenses(username);

	}


//	@Test(dataProvider = "backupCodeDataProvider", priority = 5)
//	@Report(name="2 STEP BACKUP TOKEN AS FIRST STEP - DENIED", description="Not possible to automate because the challenge is generated when the code is created via api")
//	public void test4(String username, String password) throws Exception {
//		API.deleteUser(username);
//		Response response01 = API.createUser(username, password);
//		assertEquals(response01.getCode(), 201, API.releaseLicenses(username);

//		Response response02 = API.allowAuthWithPassword(username, true);
//		assertEquals(response02.getCode(), 200, "Failed to allow auth with password");
//		String backupCode = API.getBackupCode(username);
//	    API.setBackupCodePasswordRequired(username, false);
//		Response response10 = API.authenticate(username, backupCode);
//		assertEquals(response10.getCode(), 401, "Failed to deny backtoken as first step  backup token");
//		assertEquals(API.getTransactinsLogReason(response10.getProperty("transaction-id")), "INVALID_CREDENTIALS");
//
//	}

	@Test(dataProvider = "backupCodeDataProvider", priority = 5)
	@Report(name="2 Step backup token -  password authentication setting allowed", description="API user enters first password then the backcode is expected to be ALLOWED")
	public void test5(String username, String password) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);

		UserAPI.allowPassword(username, true);

		//Registration
		String backupCode = API.getBackupCode(username);
		assertNotNull(backupCode, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, true);
	
	    //Authentication
		Response challenge03 = API.authenticate(username,password);
		assertEquals(challenge03.getCode(), 401, "Failed to get challenge with static pasword");
		Response response10 = API.authenticate(username, backupCode);
		assertEquals(response10.getCode(), 200, "Failed to accept 2step  backup token");
		String backupCode2 = API.getBackupCode(username);
		assertNotNull(backupCode2, "Failed to generate  backup token");

	  	Response challenge = API.authenticate(username,password);
		assertEquals(challenge.getCode(), 401, "Failed to get challenge with static pasword");
		assertEquals(API.getTransactinsLogReason(challenge03.getProperty("transaction-id")), "ADDITIONAL_CODE_REQUIRED");
		Response response = API.authenticate(username, backupCode);
		assertEquals(response.getCode(), 401, "Failed to accept 2step  backup token");

	}
}
