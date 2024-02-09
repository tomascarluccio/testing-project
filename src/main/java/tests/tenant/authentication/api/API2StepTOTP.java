package tests.tenant.authentication.api;

import static org.testng.Assert.assertEquals;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.MobileSimulator;
import utils.Response;
import utils.UserAPI;


public class API2StepTOTP extends BaseAPITest {

	@DataProvider(name = "totpDataProvider" , parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"totp2step1@"+Config.ldapDomain, Config.tnPassword }, //LDAP Users
			{"totp2step2@"+Config.ldapDomain, Config.tnPassword},
			{"totp2step5@", Config.tnPassword },  // Internal Users
			{"totp2step6@", Config.tnPassword },
		};
	}


	@Test(dataProvider="totpDataProvider", priority = 2)
	public void test1(String username, String password) throws Exception {
		API.setTOTPInputMethod("");
	}

	@Test(dataProvider = "totpDataProvider", priority = 3)
	@Report(name="2 step TOTP valid otp", description="API user enters a password and a valid OTP as second step, it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authentication
		Response response07 = API.authenticate(username,  password);
		assertEquals(response07.getCode(), 401, "Failed authenticate get otp challenge");
		
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());

		Response response08= API.authenticate(username,  otp);
		assertEquals(response08.getCode(), 200, "Failed authenticate valid otp");

	}

	@Test(dataProvider = "totpDataProvider", priority = 4)
	@Report(name="2 step TOTP invalid otp", description="API user enters a password  and a random 6 digits OTP as second step, it is expected to be DENIED")
	public void test3(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

	    //Registraton
	    MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authentication
		Response response07 = API.authenticate(username,  password);
		assertEquals(response07.getCode(), 401, "Failed authenticate get otp challenge");

		Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));

		Response response08= API.authenticate(username,  invalidOTP);
		assertEquals(response08.getCode(), 401, "Failed authenticate valid otp");
	}

	@Test(dataProvider = "totpDataProvider", priority = 5)
	@Report(name="2 step TOTP temp lock", description="API user enters a password and random 6 digits OTP as second step, repeat 6 times, then authenticates with a valid OTP, it is expected to be DENIED, then reset the failed attepms and authenticates, its expected to be ALLWED ")
	public void test4(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authentication
	 	Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));

		for(int i = 0; i< 6;i++) {
			Response response07 = API.authenticate(username,  password);
			assertEquals(response07.getCode(), 401, "Failed authenticate get otp challenge");

			Thread.sleep(40000);
			 invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));

			Response response08= API.authenticate(username,  invalidOTP);
			assertEquals(response08.getCode(), 401, "Failed authenticate valid otp");
			}

		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		Response response07 = API.authenticate(username,  password);
		assertEquals(response07.getCode(), 401, "Failed authenticate get otp challenge");
		Thread.sleep(40000);
	    invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		Response response08= API.authenticate(username,  invalidOTP);
		assertEquals(response08.getCode(), 401, "Failed authenticate valid otp");

	    Thread.sleep(40000);
	    API.resetFailedAttemptsCount(username);
		Response response09 = API.authenticate(username,  password);
		assertEquals(response09.getCode(), 401, "Failed authenticate get otp challenge");
        otp =  mobile.getTOTPCode(Instant.now());
		Response response10= API.authenticate(username,  otp);
		assertEquals(response10.getCode(), 200, "Failed authenticate valid otp");

	}


	@Test(dataProvider = "totpDataProvider", priority = 6)
	@Report(name="2 step TOTP valid OTP in acceptance tolerance window", description="API user enters a password  and a valid OTP with two minutes forward drift as second step,it is expected to be ALLOWED")
	public void test5(String username, String password) throws Exception {
		username = UserAPI.getRandomUsername(username);

		API.deleteUser(username);

		//User setup
		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

	    //Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authentication
		Thread.sleep(40000);
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
	    String otp =  mobile.getTOTPCode(instant);
		Response response09 = API.authenticate(username,  password);
		assertEquals(response09.getCode(), 401, "Failed authenticate get otp challenge");
		otp =  mobile.getTOTPCode(Instant.now());
		Response response10 = API.authenticate(username,  otp);
		assertEquals(response10.getCode(), 200, "Failed authenticate valid otp");
	}

	@Test(dataProvider = "totpDataProvider", priority = 7)
	@Report(name="2 step TOTP valid OTP device out fo sync", description="API user enters a password  and a valid OTP with five minutes backward drift as second step,it is to get a CHALLENGE, the generates a new valid otp and it is expected to be ALLOWED")
	public void test6(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		// Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    Thread.sleep(40000);

	    //Authentication
		Response response09 = API.authenticate(username,  password);
		assertEquals(response09.getCode(), 401, "Failed authenticate get otp challenge");
		Instant instant = Instant.now().minus(5, ChronoUnit.MINUTES);
	    String otp =  mobile.getTOTPCode(instant);
		Response response14= API.authenticate(username,  otp);
		assertEquals(response14.getCode(), 401, "Failed authenticate valid otp");
		otp =  mobile.getTOTPCode(Instant.now());
		Response response15= API.authenticate(username,  otp);
		assertEquals(response15.getCode(), 200, "Failed authenticate valid otp");

	}

	@Test(dataProvider = "totpDataProvider", priority = 8)
	@Report(name="2 step TOTP valid OTP with password concatenation", description="API user enters a password and a valid 6 digits OTP with suffix-6 password concatenation  as second step, it is expected to be DENIED")
	public void test7(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		// Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authentication
		Response response07 = API.authenticate(username,  password);
		assertEquals(response07.getCode(), 401, "Failed authenticate get otp challenge");
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		Response response08= API.authenticate(username,  password+otp);
		assertEquals(response08.getCode(), 401, "Failed authenticate valid otp");
 	}
	
	@Test(dataProvider = "totpDataProvider", priority = 9)
	@Report(name="2 step TOTP valid OTP as first step", description="API user entersa valid OTP as first step, it is expected to be DENIED")
	public void test8(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

	   // Registration
	 	MobileSimulator mobile = new MobileSimulator();
	 	mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	 	//Authentication
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());	
		Response response08= API.authenticate(username,  otp);
		assertEquals(response08.getCode(), 401, "Failed authenticate valid otp");
		
  }
	 

}