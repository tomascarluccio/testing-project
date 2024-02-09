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


public class APISingleStepTOTP extends BaseAPITest {


	@DataProvider(name = "totpAPIDataProvider", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"totp1@"+Config.ldapDomain, Config.tnPassword }, //LDAP Users
			{"totp2@"+Config.ldapDomain, Config.tnPassword },
			{"totp3", Config.tnPassword },  //Internal Users
			{"totp5", Config.tnPassword }
			};
	}


	@Test(dataProvider = "totpAPIDataProvider", priority = 2)
	public void test1(String username, String password) throws Exception {
		API.setTOTPInputMethod("");

	}

	@Test(dataProvider = "totpAPIDataProvider", priority = 3)
	@Report(name="TOTP password single step allowed", description="API user enters valid otp as single step it is expected to be ALLOWED")
	public void test2(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
	    API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);

		API.allowAuthWithPassword(username, false);

		Instant instant = Instant.now().plus(1, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
		Response response07 = API.authenticate(username, otp);
		assertEquals(response07.getCode(), 200, "Failed authenticate valid otp");

	}

	@Test(dataProvider = "totpAPIDataProvider", priority = 5)
	@Report(name="TOTP password single step denied", description=" user enters invalid  otp as single step it is expected to be DENIED")
	public void test3 (String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
        API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		API.allowAuthWithPassword(username, false);

	    Thread.sleep(30000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		Response response09 = API.authenticate(username, invalidOTP);
		assertEquals(response09.getCode(), 401, "Failed to deny invalid otp ");
		System.out.println(response09.getProperty("transaction-id"));
		assertEquals(API.getTransactinsLogReason(response09.getProperty("transaction-id")), "INVALID_OTP");

	}

	@Test(dataProvider = "totpAPIDataProvider", priority = 6)
	@Report(name="TOTP user temp lock", description="API user enters invalid otp 6 times it is expected TMP_LOCKDOWN then resets falied attemps it is expected to be ALLOWED after")
	public void test4(String username, String password) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);
	    API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);

		API.allowAuthWithPassword(username, false);

	    //Authentication
	 	Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		for(int i = 0; i<6;i++) {
		    invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
			Response response10 = API.authenticate(username, invalidOTP );
			assertEquals(response10.getCode(), 401, "Failed deny invalid otp");
			
		}

		Instant instant = Instant.now().plus(1, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
		Response response07 = API.authenticate(username, otp);
		assertEquals(response07.getCode(), 401, "Failed authenticate valid otp");
		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "TMP_LOCKDOWN");
		API.resetFailedAttemptsCount(username);
		Instant instant2 = Instant.now().plus(2, ChronoUnit.MINUTES);

		otp =  mobile.getTOTPCode(instant2);
		Response response08 = API.authenticate(username, otp);
		assertEquals(response08.getCode(), 200, "Failed authenticate valid otp");

	}


	@Test(dataProvider = "totpAPIDataProvider", priority = 7)
	@Report(name="TOTP valid OTP in acceptance tolerance window", description="API user enters a valid OTP with two minutes forward drift,it is expected to be ALLOWED")
	public void test5(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);

		API.allowAuthWithPassword(username, false);

	    //Authentication
		Thread.sleep(40000);
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		String otp =  mobile.getTOTPCode(instant);
		Response response13 = API.authenticate(username, otp);
	    assertEquals(response13.getCode(), 200, "Failed to authenticate in acceptance tolerance");

	}

	@Test(dataProvider = "totpAPIDataProvider", priority = 8)
	@Report(name="TOTP valid OTP in acceptance tolerance window past", description="API user enters a valid OTP with two minutes backward drift,it is expected to be ALLOWED")
	public void test6(String username, String password) throws Exception {

		username = UserAPI.getRandomUsername(username);
		//User setup
				UserAPI.resetUser(username, password);
				API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		Thread.sleep(40000);

		API.allowAuthWithPassword(username, false);

		Instant instant = Instant.now().minus(2, ChronoUnit.MINUTES);

	    String otp =  mobile.getTOTPCode(instant);
		Response response12 = API.authenticate(username, otp);
		assertEquals(response12.getCode(), 401, "Failed to sync challenge");
		assertEquals(API.getTransactinsLogReason(response12.getProperty("transaction-id")), "DEVICE_OUT_OF_SYNC");
	    otp =  mobile.getTOTPCode(Instant.now());
		Response response13 = API.authenticate(username, otp);
		assertEquals(response13.getCode(), 200, "Failed to confirm challenge");

	}

	@Test(dataProvider = "totpAPIDataProvider", priority = 9)
	@Report(name="TOTP valid OTP device out fo sync", description="API user a valid OTP with five minutes backward drift,it is expected to get a CHALLENGE, the generates a new valid otp and it is expected to be ALLOWED")
	public void test7(String username, String password) throws Exception {
		username = UserAPI.getRandomUsername(username);

		//User setup
		UserAPI.resetUser(username, password);
		UserAPI.allowPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		API.allowAuthWithPassword(username, true);
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);

		API.allowAuthWithPassword(username, false);

	    //Authentication
		Thread.sleep(40000);
		Instant instant = Instant.now().plus(5, ChronoUnit.MINUTES);

	 	String otp =  mobile.getTOTPCode(instant);
		Response response07 = API.authenticate(username, otp);
		assertEquals(response07.getCode(), 401, "Failed authenticate valid otp");
		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "DEVICE_OUT_OF_SYNC");

	    otp =  mobile.getTOTPCode(Instant.now());
		Response response13 = API.authenticate(username, otp);
		assertEquals(response13.getCode(), 200, "Failed to confirm challenge");

	}

//	@Test(dataProvider = "totpAPIDataProvider", priority = 10)
//	@Report(name="TOTP perm lock", description="API userd random 6 digits OTP as second step, repeat 6 times, then authenticates with a valid OTP, it is expected to be USR_LOCKED, then reset the failed attepms and authenticates, its expected to be ALLWED ")
//	public void test8(String username, String password) throws Exception {
//		//User setup
//		API.deleteUser(username);
//	    API.createUser(username, password);
//		API.releaseLicenses(username);
//	    API.asociateLicense(username, "TOTP:Mobile");
//		API.allowAuthWithPassword(username, true);
//		API.setUserTempLockAttempts(username, "0");
//
//		//Registration
//		MobileSimulator mobile = new MobileSimulator();
//		mobile.registerTOTP(username, password);
//	    API.setTOTPPasswordRequired(username, false);
//
//	    //Authentication
//		Thread.sleep(40000);
//		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
//		for(int i = 0; i< 10;i++) {
//		    invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
//			Response response10 = API.authenticate(username, invalidOTP );
//			assertEquals(response10.getCode(), 401, "Failed deny invalid otp");
//
//		}
//
//		Thread.sleep(40000);
//		String otp =  mobile.getTOTPCode(Instant.now());
//		Response response07 = API.authenticate(username, otp);
//		assertEquals(response07.getCode(), 401, "Failed authenticate valid otp");
//		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "USR_LOCKED");
//		API.resetFailedAttemptsCount(username);
//		Thread.sleep(40000);
//		otp =  mobile.getTOTPCode(Instant.now());
//		Response response08 = API.authenticate(username, otp);
//		assertEquals(response08.getCode(), 200, "Failed authenticate valid otp");
//
//	}

	
}