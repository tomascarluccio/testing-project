package tests.tenant.authentication.radius;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.RadiusAuth;
import utils.UserAPI;


public class RadiusSingleStepPush extends BaseAPITest {

	
	@DataProvider(name = "radiusPushPasswordUsers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"pushRadiusUser1", "Safewalk1", Config.radiusBrokerHost}, //LDAP Users request to Broker radius
			{"pushRadiusUser4", "Safewalk1", Config.radiusServerHost},
			{"intpushRadiusUser5@", "Safewalk1", Config.radiusBrokerHost}, // Internal Users request to Broker radius
			
		};
	}

	@Test(dataProvider="radiusPushPasswordUsers", priority = 3)
	@Report(name="Radius Fast Auth Push accepted", description = "API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void pushRadiusAccept(String username, String password, String host) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
						
		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);
		Thread.sleep(5000);
			
		//Authentication
	    String status =  RadiusAuth.authenticateTimeout(username, password, 600000, host);
		assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider="radiusPushPasswordUsers", priority = 3)
	@Report(name="Radius Fast Auth Push denied", description = "API user enters credentials and wait fot timeout, then enters an OTP, it is expected to be ALLOWED")
	public void pushRadiusDeny(String username, String password, String host) throws Exception {

		//User setup
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
						
		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);
		Thread.sleep(5000);
			
		//Authentication
		String status =  RadiusAuth.authenticateTimeout(username, password, 600000, host);
		assertEquals(status, "ACCESS_DENIED");
		
	}
	
	
}



