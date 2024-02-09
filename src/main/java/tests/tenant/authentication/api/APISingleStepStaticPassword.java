package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.LDAPClient;
import utils.Response;
import utils.UserAPI;


public class APISingleStepStaticPassword extends BaseAPITest {

	@DataProvider(name = "staticPasswordUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"staticpassword1@"+Config.ldapDomain,  "Safewalk1", "CN=staticpassword1,CN=Users, DC=safewalk,DC=training"}, //LDAP Users
			{"staticpassword2@"+Config.ldapDomain,  "Safewalk1", "CN=staticpassword2,CN=Users, DC=safewalk,DC=training"},
				{"staticpassword5",  "Safewalk1", ""},  //Internal Users
			{"staticpassword6",  "Safewalk1", ""},
		};
	}
	
	
	@Test(dataProvider = "staticPasswordUsers", priority = 3)
	@Report(name="Static password default", description="API user enters valid credentials with password not allowed setting  it is expected to be DENIED")
	public void test2(String username, String password, String userDn) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
//		UserAPI.allowPassword(username, true);
		
		//Authentication
		Response response1 = API.authenticate(username, password);
		assertEquals(response1.getCode(), 401, "Failed to deny static password not allowed "+username);
		assertEquals(API.getTransactinsLogReason(response1.getProperty("transaction-id")), "PSW_NOT_ALLOWED");

	}

	@Test(dataProvider = "staticPasswordUsers", priority = 4)
	@Report(name="Static password allowed", description="API user enters valid credentials with password allowed setting  it is expected to be ALLOWED")
	public void test3(String username, String password, String userDn) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

		//Authentication
		Response response1 = API.authenticate(username, password);
		assertEquals(response1.getCode(), 200, "Failed to allow static password allowed "+username);
//		assertEquals(API.getTransactinsLogReason(response1.getProperty("transaction-id")), "ACCESS_ALLOWED");

		}

	@Test(dataProvider = "staticPasswordUsers", priority = 5)
	@Report(name="Static password- password expired", description="API user enters expired credentials with password expired setting disabled it is expected to be LDAP_USER_PASSWORD_EXPIRED")
	public void test4(String username, String password, String userDn) throws Exception {
		username = UserAPI.getRandomUsername(username);
		//User setup
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.allowWithPasswordExpired(username, false);

		//Authentication
    	if(!username.contains(Config.ldapDomain)) {
          String dn = userDn.replace("X", username);
		  System.out.println(dn);
          LDAPClient.setUserMustChangePassword(dn);
          Response response04 = API.authenticate(username, password);
          assertEquals(response04.getCode(), 401, "Failed to authenticate static password expired "+username);
          System.out.println(response04.getProperty("transaction-id"));
          assertEquals(API.getTransactinsLogReason(response04.getProperty("transaction-id")), "LDAP_USER_PASSWORD_EXPIRED");
     }

	}

	@Test(dataProvider = "staticPasswordUsers", priority = 6)
	@Report(name="Static password - passowrd expired allowed", description="API user enters expired credentials with password expired setting enabled it is expected to be ALLOWED")
	public void test5(String username, String password, String userDn) throws Exception {
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

		if(!userDn.equals("internal")) {

			LDAPClient.setUserMustChangePassword(userDn);
			Response response04 = API.authenticate(username, password);
			assertEquals(response04.getCode(), 401, "Failed to authenticate static password expired "+username);
			System.out.println(response04.getProperty("transaction-id"));
			assertEquals(API.getTransactinsLogReason(response04.getProperty("transaction-id")), "LDAP_USER_PASSWORD_EXPIRED");
		}
	}


	@Test(dataProvider = "staticPasswordUsers", priority = 7)
	@Report(name="Static password user temp lock", description="API user enters invalid credentials 6 times it is expected TMP_LOCKDOWN then resets failed attemps it is expected to be ALLOWED after ")
	public void test6(String username, String password, String userDn) throws Exception {
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

		//Authentication
		for(int i = 0; i< 5;i++) {
			Response response06 = API.authenticate(username, password + "+");
			assertEquals(response06.getCode(), 401, "temp Locked User allowed");
			assertEquals(API.getTransactinsLogReason(response06.getProperty("transaction-id")), "INVALID_CREDENTIALS");
		}

		Response response07 = API.authenticate(username, password);
		assertEquals(response07.getCode(), 401, "Locked User allowed");
		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "TMP_LOCKDOWN");
		API.resetFailedAttemptsCount(username);
		Response response08 = API.authenticate(username, password);
		assertEquals(response08.getCode(), 200, "Locked User  reset attemps denied"+username);

}


	//TODO review API.setUserTempLockAttempts(username, "0"); not working
//
//	@Test(dataProvider = "staticPasswordUsers", priority = 8)
//	@Report(name="Static password user perm lock", description="API user enters invalid credentials 10 times with user temp lock attemps in 0 ,  it is expected USR_LOCKED then resets falied attemps it is expected to be ALLOWED after ")
//	public void test7(String username, String password, String userDn) throws Exception {
//
//		if(!userDn.equals("internal")) {
//			//User setup
//			UserAPI.setupoAllowPassword(username, password, true);
//			Response res4 = API.setUserTempLockAttempts(username, "10");
//
//			System.out.println(res4.getContent()+res4.getCode());
//
//
//			//Authentication
//			for(int i = 0; i< 10;i++) {
//
//				Response response06 = API.authenticate(username, password + "+");
//				assertEquals(response06.getCode(), 401, "Locked User alloweds");
//				assertEquals(API.getTransactinsLogReason(response06.getProperty("transaction-id")), "INVALID_CREDENTIALS");
//			}
//
//			Response response07 = API.authenticate(username, password);
//			assertEquals(response07.getCode(), 401, "Locked User allowed");
//			assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "USR_LOCKED");
//			API.resetFailedAttemptsCount(username);
//			Response response08 = API.authenticate(username, password);
//			assertEquals(response08.getCode(), 200, "Locked User reset attemps denied"+username);
//		}
//	}
}



