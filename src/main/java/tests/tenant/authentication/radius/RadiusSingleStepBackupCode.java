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


public class RadiusSingleStepBackupCode extends BaseAPITest {

	
	@DataProvider(name = "radiusBackupCodeUsers2",  parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"radiusbackupCode1", Config.tnPassword, Config.radiusServerHost},  //LDAP Users request to Server radius
			{"radiusbackupCode2", Config.tnPassword, Config.radiusBrokerHost},   //LDAP Users request to Broker radius
			{"intradiusbackupCode5", Config.tnPassword, Config.radiusBrokerHost},   //Internal Users request to Broker radius
		};
	}
	
	@Test(dataProvider = "radiusBackupCodeUsers2", priority = 3)
	@Report(name= "Radius 1 step backup code with passsword authentication setting disabled", description = "API user enters valid backup token as first step, it is expected to be ALLOWED")
	public void test2(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		
	    //Registration
	    String backupCode = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, false);
	
	    //Authentication
	    String  status =  RadiusAuth.authenticate(username, backupCode, host);
	    assertEquals(status, "ACCESS_ALLOWED");

	}
	
	@Test(dataProvider = "radiusBackupCodeUsers2", priority = 4)
	@Report(name= "Radius 1 step backup code with passsword authentication setting enalbed", description = "API user enters valid backup token as first step, it is expected to be ALLOWED")
	public void test3(String username, String password, String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		//Registration
		String backupCode = API.getBackupCode(username);
		API.setBackupCodePasswordRequired(username, false);

		//Authentication
		String  status =  RadiusAuth.authenticate(username, backupCode, host);
		assertEquals(status, "ACCESS_ALLOWED");

	}
	
	@Test(dataProvider = "radiusBackupCodeUsers2", priority = 4)
	@Report(name= "Radius 1 step backup code with expired used  backupcode", description = "API user enters previously used backup token as first step, it is expected to be DENIED")
	public void test4(String username, String password,  String host)  throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		
	    //Registration
	    String backupCode = API.getBackupCode(username);
	    API.setBackupCodePasswordRequired(username, false);
				
	    //Authentication
		String  status =  RadiusAuth.authenticate(username, backupCode, host);
	    assertEquals(status, "ACCESS_ALLOWED");
		String backupCode2 = API.getBackupCode(username);
	    status = RadiusAuth.authenticate(username, backupCode, host);
      	assertEquals(status, "ACCESS_DENIED");	
	}
	
}



