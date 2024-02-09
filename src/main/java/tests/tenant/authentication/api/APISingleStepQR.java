package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

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
import utils.UserAPI;


public class APISingleStepQR extends BaseAPITest {

	@DataProvider(name = "fastAuthDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"qruser11@"+Config.ldapDomain, "Safewalk1"},  //LDAP Users
			{"qruser22@"+Config.ldapDomain, "Safewalk1"},
			{"qruser52@", "Safewalk1"},   //Internal Users
			{"qruser62@", "Safewalk1"},
		};
	}



	@Test(dataProvider = "fastAuthDataProvider", priority = 3)
	@Report(name="Fast Auth QR Accept", description="API user enters autheticates with session key, signs the challenge and verifys session key it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {
		username = UserAPI.getRandomUsername(username);


		//User setup
		UserAPI.resetUser(username, password);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);

		//Authentication
		Response response04= API.authenticateSessionKey();
		JSONParser jsonParser01 = new JSONParser();
		JSONObject json01 = (JSONObject) jsonParser01.parse(response04.getContent());
		String challenge = (String) json01.get("challenge");

		// Sign challenge (QR)
		mobile.signChallenge(challenge, false, false);

		// Verify challenge (QR)
		Response response = API.verifySessionKey(challenge);
		assertEquals(response.getCode(), 200, "Failed to authenticate with QR");
	}

	
	@Test(dataProvider = "fastAuthDataProvider", priority = 4)
	@Report(name="Fast Auth QR Deny", description="API user enters autheticates with session key ai and verifys session key it is expected to be DENIED")
	public void test3(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);
		
		//User setup
		//User setup
		UserAPI.resetUser(username, password);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);

		//Authentication
		Response response04= API.authenticateSessionKey();
		JSONParser jsonParser01 = new JSONParser();
		JSONObject json01 = (JSONObject) jsonParser01.parse(response04.getContent());
		String challenge = (String) json01.get("challenge");
		// Sign challenge (QR)
		mobile.signChallenge(challenge, true, false);
		// Verify challenge (QR)
		Response response = API.verifySessionKey(challenge);
		//TODO: API response vacio ??
		assertEquals(response.getCode(), 200, "Failed to authenticate with QR");
		assertEquals(API.getTransactinsLogReason(response04.getProperty("transaction-id")), "USR_CANCEL");
		API.releaseLicenses(username);


	}


	@Test(dataProvider = "fastAuthDataProvider", priority = 5)
	@Report(name="Fast Auth QR Timeout", description="API user enters autheticates with session key  and verifys expired session key it is expected to be DENIED")
	public void test4(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);
		
		//User setup
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		
		//Authentication
		Response response04= API.authenticateSessionKey();
		JSONParser jsonParser01 = new JSONParser();
		JSONObject json01 = (JSONObject) jsonParser01.parse(response04.getContent());
		String challenge = (String) json01.get("challenge");
		// Avoid Sign challenge (QR)

		// Verify challenge (QR)
		Thread.sleep(40000);
		Response response = API.verifySessionKey(challenge);
		assertEquals(response.getCode(), 200, "Failed to authenticate with QR");
		API.releaseLicenses(username);
	}


}