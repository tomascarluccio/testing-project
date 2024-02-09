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

public class APISingleStepBackupCode extends BaseAPITest {

	@DataProvider(name = "backupCodeDataProvider2",  parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"backup1step1@"+Config.ldapDomain, Config.tnPassword},  //LDAP Users
			{"backup1step2@"+Config.ldapDomain, Config.tnPassword},
			{"backup1step5@", Config.tnPassword},   //Internal Users
			{"backup1step6@", Config.tnPassword},
		};
	}


	@Test(dataProvider = "backupCodeDataProvider2", priority = 3)
	@Report(name="1 step backup code with passsword authentication setting not allowed", description="API user enters valid backup token as first step, it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {

		//Users Setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, false);

	    //Registration
	    String backupCode = API.getBackupCode(username);
	    assertNotNull(backupCode, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, false);
	  
	    //Authentication
		Response response10 = API.authenticate(username, backupCode);
		assertEquals(response10.getCode(), 200, "Failed to deny used  backup token");

	}

	@Test(dataProvider = "backupCodeDataProvider2", priority = 4)
	@Report(name="1 step backup code  passsword authentication setting enabled", description="API user enters valid backup token as first step, it is expected to be ALLOWED")
	public void test3(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

	    //Registration
	    String backupCode = API.getBackupCode(username);
		assertNotNull(backupCode, "Failed to generate  backup token");

	    //Authentication
	    Response response10 = API.authenticate(username, backupCode);
		assertEquals(response10.getCode(), 200, "Failed to deny used  backup token");

		
	}
	
	@Test(dataProvider = "backupCodeDataProvider2", priority = 5)
	@Report(name="1 step used backup code", description="API user enters a backcode and its allowed, then generates a new back code, and tries to authenticate with the first one (used previosuly), it is expected to be DENIED")
	public void test4(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

	    //Registration
	    String backupCode = API.getBackupCode(username);
	    assertNotNull(backupCode, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, false);
	  
	    //Authentication
		Response response09 = API.authenticate(username, backupCode);
		assertEquals(response09.getCode(), 200, "Failed to authenticate valid backup token");

		String backupCode2 = API.getBackupCode(username);
		assertNotNull(backupCode2, "Failed to generate  backup token");

	    API.setBackupCodePasswordRequired(username, false);

		Response response13 = API.authenticate(username, backupCode);
		assertEquals(response13.getCode(), 401, "Failed to deny expired/used backup token");


	}


}
