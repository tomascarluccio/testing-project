package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.LDAPClient;
import utils.RadiusAuth;
import utils.UserAPI;


public class RadiusSingleStepStaticPassword extends BaseAPITest {

	
	@DataProvider(name = "radiusStaticPasswordUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radiusstatpass1",  "Safewalk1", "CN=radiusstatpass1,CN=Users, DC=safewalk ,DC=training", Config.radiusBrokerHost},   //LDAP Users request to Broker radius
			{"radiusstatpass2",  "Safewalk1", "CN=radiusstatpass2,CN=Users, DC=safewalk ,DC=training", Config.radiusServerHost},
			{"intradiusstatpass3@",  "Safewalk1", "internal", Config.radiusBrokerHost},   //Internal Users request to Broker radius
			{"intradiusstatpass4@",  "Safewalk1", "internal", Config.radiusServerHost},   //Internal Users request Server radius
			
		};
	}
	
	
	@Test(dataProvider="radiusStaticPasswordUsers", priority =4)
	@Report(name= "Radius Static password default", description = "API user enters valid credentials with password not allowed setting  it is expected to be DENIED")
	public void test2(String username, String password, String userDn, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);

		//Authentication
		String status = RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_DENIED");
		
	}	
	
	@Test(dataProvider="radiusStaticPasswordUsers", priority = 5)
	@Report(name= "Radius Static password password allowed", description = "API user enters valid credentials with password not allowed setting  it is expected to be DENIED")
	public void test3(String username, String password, String userDn, String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		  
		//Authentication
		String status = RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_ALLOWED");
	
	}	
	
	@Test(dataProvider="radiusStaticPasswordUsers", priority = 6)
	@Report(name= "Radius Static password- passoword expired allowed", description = "API user enters expired credentials with password expired setting disabled it is expected to be ALLOWED")
	public void test4(String username, String password, String userDn,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.allowWithPasswordExpired(username, true);	
			
	    if(!userDn.equals("internal")) {	
			LDAPClient.setUserMustChangePassword(userDn);	
		    API.allowWithPasswordExpired(username, true);
		    API.allowAuthWithPassword(username, true);
			String status = RadiusAuth.authenticate(username, password, host);
			assertEquals(status, "ACCESS_ALLOWED");
	    }
	}	

	@Test(dataProvider="radiusStaticPasswordUsers", priority = 7)
	@Report(name= "Radius Static password- passoword expired not allowed", description = "API user enters expired credentials with password expired setting disabled it is expected to be LDAP_USER_PASSWORD_EXPIRED")
	public void test5(String username, String password, String userDn,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		if(username.endsWith(Config.ldapDomain)) {
		    API.allowWithPasswordExpired(username, false);	
	    	LDAPClient.setUserMustChangePassword(userDn);
			String status = RadiusAuth.authenticate(username, password, host);
			assertEquals(status, "ACCESS_DENIED");
		}
			
	}	

	@Test(dataProvider = "radiusStaticPasswordUsers", priority = 8)
	@Report(name="Radius Static password user temp lock", description = "API user enters invalid credentials 6 times it is expected TMP_LOCKDOWN then resets failed attemps it is expected to be ALLOWED after ")
	public void test6(String username, String password, String userDn,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);	
	    API.allowWithPasswordExpired(username, true);	
		   
		for(int i = 0; i< 5;i++) {
			String status = RadiusAuth.authenticate(username, password+"*", host);
		    assertEquals(status, "ACCESS_DENIED");
		}
		
		String status = RadiusAuth.authenticate(username, password, host);
		assertEquals(status, "ACCESS_DENIED");
		API.resetFailedAttemptsCount(username);
		status = RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_ALLOWED");
	}
	
	@Test(dataProvider = "radiusStaticPasswordUsers", priority = 8)
	@Report(name="Radius Static password user perm lock", description = "API user enters invalid credentials 10 times with user perm lock attemps in 0 ,  it is expected USR_LOCKED then resets falied attemps it is expected to be ALLOWED after ")
	public void test7(String username, String password, String userDn,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);	
	    API.allowWithPasswordExpired(username, true);	
		API.setUserTempLockAttempts(username, "0"); 
		
		for(int i = 0; i< 10;i++) {
			String status = RadiusAuth.authenticate(username, password+"*", host);
		    assertEquals(status, "ACCESS_DENIED");
		}
		
		String status = RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_DENIED");
		API.resetFailedAttemptsCount(username);
	    status = RadiusAuth.authenticate(username, password, host);
	    assertEquals(status, "ACCESS_ALLOWED");
	}
}



