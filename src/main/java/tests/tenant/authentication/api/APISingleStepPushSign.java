package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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


public class APISingleStepPushSign extends BaseAPITest {

	@DataProvider(name = "pushDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"pushsignUs@"+Config.ldapDomain, "Safewalk1"},  //LDAP Users
			{"pushsignUser23@"+Config.ldapDomain, "Safewalk1"},
            {"pushsignUser5@", "Safewalk1"},   //Internal Users
			{"pushsignUser6@", "Safewalk1"},
			
		};
	}

	@Test(dataProvider = "pushDataProvider", priority = 3)
	@Report(name="PUSH SYNC ACCEPTED - ALLOWED", description="No description")
	public void test2(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Sign");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(Config.signData.getBytes(StandardCharsets.UTF_8));
		Response response = API.authenticateTransaction(username, password, 40000, hash.toString(), Config.signData,Config.signTitle,Config.signBody);
		System.out.println(response.getCode() +" "+response.getContent());
		assertEquals(response.getCode(), 200, "Failed to authenticate");

	}
	
	@Test(dataProvider = "pushDataProvider", priority = 4)
	@Report(name="PUSH SYNC DENIED - TIMEOUT", description="No description")
	public void test3(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Sign");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(Config.signData.getBytes(StandardCharsets.UTF_8));
		Response response = API.authenticateTransaction(username, password, 40000, hash.toString(), Config.signData,Config.signTitle,Config.signBody);

		assertEquals(response.getCode(), 401, "Failed to authenticate");
		API.releaseLicenses(username);


	}
	
	@Test(dataProvider = "pushDataProvider", priority = 5)
	@Report(name="PUSH SYNC DENIED - DENIED", description="No description")
	public void test4(String username, String password) throws Exception {

		//User setup

		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Sign");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(Config.signData.getBytes(StandardCharsets.UTF_8));
		Response response = API.authenticateTransaction(username, password, 40000, hash.toString(), Config.signData,Config.signTitle,Config.signBody);
		assertEquals(response.getCode(), 200, "Failed to authenticate");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.getContent());
		String code = (String) json.get("code");
		assertEquals(code, "SIGN_REJECT");

	}
	
	
	@Test(dataProvider = "pushDataProvider", priority = 6)
	@Report(name="PUSH ASYNC ACCEPTED - ALLOWED", description="No description")
	public void test5(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Sign");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		String code = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(Config.signData.getBytes(StandardCharsets.UTF_8));
			Response response = API.authenticateTransaction(username, password, 40000, hash.toString(), Config.signData,Config.signTitle,Config.signBody);
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
		assertEquals(code, "SIGN_REJECT");
	}
	
	@Test(dataProvider = "pushDataProvider", priority = 7)
	@Report(name="PUSH ASYNC DENIED - TIMEOUT", description="No description")
	public void test6(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);

		//Registration
		API.asociateLicense(username, "Fast:Auth:Sign");
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerFastAuth(username, password);
		mobile.signPush(10, 3000);

		//Authentication
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(Config.signData.getBytes(StandardCharsets.UTF_8));
		Response response = API.authenticateTransaction(username, password, 40000, hash.toString(), Config.signData,Config.signTitle,Config.signBody);
		assertEquals(response.getCode(), 200, "Failed to authenticate");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.getContent());
		String code = (String) json.get("code");
		assertEquals(code, "SIGN_REJECT");

	}

}