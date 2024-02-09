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


public class APISingleStepPasswordTOTP extends BaseAPITest {

	@DataProvider(name = "passwodTotpDataProvider",  parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"passwordtotp1", Config.tnPassword , "Suffix-OTP-6"},  //LDAP Users
			{"passwordtotp2", Config.tnPassword , "Suffix-OTP-6"},
			{"intpasswordtotpint1", Config.tnPassword , "Suffix-OTP-6"},   //Internal Users
			{"intpasswordtotpint2", Config.tnPassword , "Suffix-OTP-6"}
		};
	}

	@DataProvider(name = "passwodTotpDataProvider2",  parallel=false)
	public Object[][] getUserData2() {
		return new Object[][] {
				{"passwordtotp1", Config.tnPassword , "Suffix-OTP-6"},  //LDAP Users
		};
	}

	@Test(dataProvider = "passwodTotpDataProvider2", priority = 2)
	public void test1(String username, String password, String inputMethod) throws Exception {
		API.setTOTPInputMethod(inputMethod);
	}

	@Test(dataProvider = "passwodTotpDataProvider", priority = 3)
	@Report(name="Password+TOTP valid otp", description="API user enters a password and a valid OTP Suffix-6 concatenation (eg. Safewalk1333221), it is expected to be ALLOWED")
	public void test2(String username, String password, String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		Response r4 = API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
			   
	    //Authenticacion
		Thread.sleep(30000);
		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);
		Response response07 = API.authenticate(username,  otp);
		assertEquals(response07.getCode(), 200, "Failed authenticate valid otp");

	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 4)
	@Report(name="Password+TOTP invalid otp", description="API user enters a password and a random invalid OTP Suffix-6 concatenation, it is expected to be DENIED")
	public void test3(String username, String password, String inputMethod) throws Exception {
		//User setup

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authenticacion
	    Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		Response response09 = API.authenticate(username, password + invalidOTP);
		assertEquals(response09.getCode(), 401, "Failed to deniy invalid otp ");
		assertEquals(API.getTransactinsLogReason(response09.getProperty("transaction-id")), "INVALID_OTP");
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 5)
	@Report(name="Password+TOTP only otp without password suffix", description="API user enters a password and without OTP Suffix-6 concatenation (eg.1333221), it is expected to be DENIED")
	public void test4(String username, String password, String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		Response r4 = API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authenticacion
		Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		Response response07 = API.authenticate(username,  otp);
		assertEquals(response07.getCode(), 401, "Failed deny authentication with only otp");
		API.releaseLicenses(username);

	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 6)
	@Report(name="Password+TOTP wrong input method concatenation", description="API user enters a valid OTP with password Preffix-6 concatenation (e.g 212443Safewalk1), it is expected to be DENIED")
	public void test5(String username, String password, String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);

		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authenticacion
	    Thread.sleep(40000);
		String reversedFormat = mobile.getTOTPCode(Instant.now())+Config.tnPassword;

		Response response13 = API.authenticate(username, reversedFormat);
		assertEquals(response13.getCode(), 401, "Failed to deniy  otp ");
		API.releaseLicenses(username);
		

	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 7)
	@Report(name="Password+TOTP two valid OTPs concatenation", description="API user enters a valid OTP concatenated with another valid OTP (e.g 33244417789998, it is expected to be DENIED")
	public void test6(String username, String password, String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");
								
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authenticacion
	    Thread.sleep(40000);
	    String otp = mobile.getTOTPCode(Instant.now())+Config.tnPassword;
		Thread.sleep(40000);
		String otp2 = mobile.getTOTPCode(Instant.now())+Config.tnPassword;
			
		Response response14 = API.authenticate(username, otp+otp2);
		assertEquals(response14.getCode(), 401, "Failed to deniy only otp ");
		API.releaseLicenses(username);
		

	}

	@Test(dataProvider = "passwodTotpDataProvider", priority = 8)
	@Report(name="Password+TOTP user temp lock", description="API user enters valid password+otp 6 times it is expected TMP_LOCKDOWN then resets falied attemps it is expected to be ALLOWED after ")
	public void test7(String username, String password,String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authenticacion
	    for(int i = 0; i< 6;i++) {
		    String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
			Response response10 = API.authenticate(username, invalidOTP );
			assertEquals(response10.getCode(), 401, "Failed deny invalid otp");
		}
		
		Thread.sleep(40000);
		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);
		Response response07 = API.authenticate(username, otp);
		assertEquals(response07.getCode(), 401, "Failed authenticate otp tmp locakdown");
		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "TMP_LOCKDOWN");// o USR_LOCKED
		API.resetFailedAttemptsCount(username);
		Thread.sleep(40000);
		
		otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);	
		Response response08 = API.authenticate(username, otp);
		assertEquals(response08.getCode(), 200, "Failed authenticate valid otp");
		API.releaseLicenses(username);
		
	}
	
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 9)
	@Report(name="Password+TOTP  in acceptance tolerance window future", description="API user enters a valid OTP with two minutes backward drift,it is expected to be ALLOWED")
	public void test8(String username, String password, String inputMethod) throws Exception {

		username = UserAPI.getRandomUsername(username);

		UserAPI.resetUser(username, password);
		Response response = API.allowAuthWithPassword(username, true);
		Response r4 = API.asociateLicense(username, "TOTP:Mobile");
		
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);

	    //Authenticacion
		Thread.sleep(40000);
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		
		String otp =  mobile.getPasswordTOTPCode(instant, password, inputMethod);
		Response response07 = API.authenticate(username,  otp);
		assertEquals(response07.getCode(), 200, "Failed to authenticate in acceptance tolerance");

		
	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 10)
	@Report(name="Password+TOTP OUT SYNC OTP ONLY - CHALLENGE/ DENIED", description="after challenge of out of sync  the user enters a valid OTP without suffix")
	public void test9(String username, String password, String inputMethod) throws Exception {

		username = UserAPI.getRandomUsername(username);

		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    //Authenticacion
	    Thread.sleep(40000);
		Instant instant = Instant.now().minus(5, ChronoUnit.MINUTES);
		
		String otp =  mobile.getPasswordTOTPCode(instant, password, inputMethod);
		Response response07 = API.authenticate(username, otp);
		assertEquals(response07.getCode(), 401, "Failed authenticate valid otp");
		assertEquals(API.getTransactinsLogReason(response07.getProperty("transaction-id")), "DEVICE_OUT_OF_SYNC");
		Thread.sleep(40000);
		
	    otp =  mobile.getTOTPCode(Instant.now());
		Response response13 = API.authenticate(username, otp);
		assertEquals(response13.getCode(), 401, "Failed to confirm challenge");


	}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 11)
	@Report(name="Password+TOTP VALID OTP - PASSWORD NOT REQUIRED -  DENIED", description="API user enters a valid OTP with two minutes backward drift,it is expected to be ALLOWED")
	public void test10(String username, String password, String inputMethod) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.allowAuthWithPassword(username, true);
		API.asociateLicense(username, "TOTP:Mobile");

		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, false);
		
	    //Authenticacion
	    Thread.sleep(40000);
		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);
		Response response07 = API.authenticate(username,  otp);
		assertEquals(response07.getCode(), 401, "Failed authenticate valid otp");

	}
}