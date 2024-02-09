package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.Response;
import utils.UserAPI;


public class APISingleStepPush extends BaseAPITest {

	@DataProvider(name = "pushDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] {
			{"pushUser@"+Config.ldapDomain, "Safewalk1"},  //LDAP Users
			{"pushUser23@"+Config.ldapDomain, "Safewalk1"},
			{"pushUser5@", "Safewalk1"},  //Internal Users
			{"pushUser6@", "Safewalk1"},

		};
	}

	@Test(dataProvider = "pushDataProvider", priority = 3)
	@Report(name="Push sync allowed", description="ALLOWED")
	public void test2(String username, String password) throws Exception {
		//User setup

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authenticacion
		Response response = API.authenticate(username, password);


		System.out.println(response.getContent());
		assertEquals(response.getCode(), 200, "Failed to authenticate");
	}

	@Test(dataProvider = "pushDataProvider", priority = 3)
	@Report(name="Push sync denied", description="DENIED")
	public void test3(String username, String password) throws Exception {
		//User setup

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
								
		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000, true, false);
		Response response = API.authenticate(username, password);
        assertEquals(response.getCode(), 401, "Failed to authenticate");

	}

	@Test(dataProvider = "pushDataProvider", priority = 5)
	@Report(name="Push async allowed", description="ALLOWED")
	public void test4(String username, String password) throws Exception {
		//User setup

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		Response response = API.authenticateAsyncPush(username, password, 300000);
		assertEquals(response.getCode(), 200, "Failed to authenticate");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.getContent());
		String code = (String) json.get("code");
		assertEquals(code, "ACCESS_ALLOWED");

	}


	@Test(dataProvider = "pushDataProvider", priority = 6)
	@Report(name="Push async denied", description="DENIED")
	public void test5(String username, String password) throws Exception {
		//User setup

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);

		//Authentication
		String code = "";
		try {
			Response response = API.authenticateAsyncPush(username, password, 300000);
			assertEquals(response.getCode(), 200, "Failed to authenticate");
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response.getContent());
			code = (String) json.get("code");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(code, "ACCESS_DENIED");
	}

	@Test(dataProvider = "pushDataProvider", priority = 7)
	@Report(name="Push async timeout", description="DENIED")

	public void test6(String username, String password) throws Exception {
		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Mobile:Asymmetric");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);

		//Authentication
		Response response = API.authenticateAsyncPush(username, password, 300000);
		assertEquals(response.getCode(), 200, "Failed to authenticate");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.getContent());
		String code = (String) json.get("code");

		assertEquals(code, "ACCESS_DENIED");
	}

}