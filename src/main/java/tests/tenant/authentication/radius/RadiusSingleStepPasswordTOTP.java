package tests.tenant.authentication.radius;

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
import utils.RadiusAuth;
import utils.UserAPI;


public class RadiusSingleStepPasswordTOTP extends BaseAPITest {

	@DataProvider(name = "radius1totpDataProvider" ,  parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"rpTotpUser1", Config.tnPassword , "Suffix-OTP-6",  Config.radiusServerHost},  //LDAP Users request to Server radius
			{"rpTotpUser4", Config.tnPassword , "Suffix-OTP-6", Config.radiusBrokerHost},
			{"intrpTotpUser5", Config.tnPassword , "Suffix-OTP-6", Config.radiusBrokerHost},   //Internal Users request to Broker radius
		};
	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 2)
	@Report(name = "set TOTP+PASSWORD INPUT METHOD", description = "No description")
	public void test1(String username, String password, String inputMethod, String host) throws Exception {
		API.setTOTPInputMethod(inputMethod);
	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 3)
	@Report(name= "Radius Password+TOTP valid otp", description = "API user enters a password and a valid OTP Suffix-6 concatenation (eg. Safewalk1333221), it is expected to be ALLOWED")
	public void test2(String username, String password, String inputMethod,  String  host)throws Exception {

		//User setup
		API.deleteUser(username);
		API.createUser(username, password);
		API.releaseLicenses(username);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
			   	
		Thread.sleep(40000);
		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);
		String  status =  RadiusAuth.authenticate(username, otp,  host);
	    assertEquals(status, "ACCESS_ALLOWED");
			
	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 5)
	@Report(name= "Radius Password+TOTP valid otp alone", description = "API user enters a valid OTP without password concatenation, it is expected to be DENIED")
	public void test3(String username, String password, String inputMethod,  String host) throws Exception {

		//User setup
		API.deleteUser(username);
		API.createUser(username, password);
		API.releaseLicenses(username);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
		
	    Thread.sleep(40000);
		String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
		String  status =  RadiusAuth.authenticate(username, invalidOTP,  host);
	    assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		
		
	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 3)
	@Report(name= "Radius Password+TOTP valid otp alone2", description = "API user enters a valid OTP without password concatenation, it is expected to be DENIED")
	public void test4(String username, String password, String inputMethod,  String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    Thread.sleep(40000);
		String otp =  mobile.getTOTPCode(Instant.now());
		String  status =  RadiusAuth.authenticate(username, otp,  host);
	    assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		

	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 4)
	@Report(name= "Radius Password+TOTP wrong input method concatenation", description = "API user enters a valid OTP with password Preffix-6 concatenation (e.g 212443Safewalk1), it is expected to be DENIED")
	public void test5(String username, String password, String inputMethod,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    Thread.sleep(40000);
		String reversedFormat = mobile.getTOTPCode(Instant.now())+Config.tnPassword;

		String  status =  RadiusAuth.authenticate(username, reversedFormat,  host);
	    assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		
	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 5)
	@Report(name= "Radius Password+TOTP two valid OTPs concatenation", description = "API user enters a valid OTP concatenated with another valid OTP (e.g 33244417789998, it is expected to be DENIED")
	public void test6(String username, String password, String inputMethod,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
			
		Thread.sleep(40000);
	    String otp = mobile.getTOTPCode(Instant.now())+Config.tnPassword;
		Thread.sleep(40000);
		String otp2 = mobile.getTOTPCode(Instant.now())+Config.tnPassword;
			

		String  status =  RadiusAuth.authenticate(username, otp+otp2,  host);
	    assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		

	}

	@Test(dataProvider = "radius1totpDataProvider", priority = 6)
	@Report(name= "Radius Password+TOTP user temp lock", description = "API user enters valid password+otp 6 times it is expected TMP_LOCKDOWN then resets falied attemps it is expected to be ALLOWED after ")
	public void test7(String username, String password,String inputMethod,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    for(int i = 0; i< 5;i++) {
		    String invalidOTP = new DecimalFormat("000000").format(new Random().nextInt(999999));
			String  status =  RadiusAuth.authenticate(username, invalidOTP, host);
		    assertEquals(status, "ACCESS_DENIED");
			
		}
		Thread.sleep(40000);
		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);

		String  status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_DENIED");
	    API.resetFailedAttemptsCount(username);
		Thread.sleep(40000);
		
		otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);	

	    status =  RadiusAuth.authenticate(username, otp,  host);
	    assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
		
		
	}
	
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 7)
	@Report(name= "Radius Password+TOTP valid OTP in acceptance tolerance window", description = "API user enters a valid OTP with two minutes backward drift,it is expected to be ALLOWED")
	public void test8(String username, String password, String inputMethod,  String host)throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    Thread.sleep(40000);
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		
		String otp =  mobile.getPasswordTOTPCode(instant,  password, inputMethod);
	    String status =  RadiusAuth.authenticate(username, otp,  host);
	    assertEquals(status, "ACCESS_ALLOWED");
		API.releaseLicenses(username);
		
		
}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 9)
	@Report(name= "Radius Password+TOTP valid OTP device out fo sync", description = "API user a valid OTP with five minutes backward drift,it is expected to get a CHALLENGE, the generates a new valid otp and it is expected to be ALLOWED")
	public void test9(String username, String password, String inputMethod,  String host) throws Exception {

		//User setup
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username, password);
		API.asociateLicense(username, "TOTP:Mobile");
		API.allowAuthWithPassword(username, true);
				
		//Registration
		MobileSimulator mobile = new MobileSimulator();
		mobile.registerTOTP(username, password);
	    API.setTOTPPasswordRequired(username, true);
	
	    Thread.sleep(40000);
		Instant instant = Instant.now().minus(5, ChronoUnit.MINUTES);
		
		String otp =  mobile.getPasswordTOTPCode(instant, password, inputMethod);
	    String status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_CHALLENGE");
	    Thread.sleep(40000);
		
	    otp =  mobile.getTOTPCode(Instant.now());
	     status =  RadiusAuth.authenticate(username, otp, host);
	    assertEquals(status, "ACCESS_DENIED");
		API.releaseLicenses(username);
		
		
	   
	}
	
//	@Test(dataProvider = "totpDataProvider", priority = 10)
//	@Report(name="RADIUS PASSWORD+TOTP VALID OTP - PASSWORD NOT REQUIRED -  DENIED", description="No description")
//	public void test10(String username, String password, String inputMethod) throws Exception {
//
//	
//		API.deleteUser(username);
//		Response response01 = API.createUser(username, password);
//		assertEquals(response01.getCode(), 201, "Failed to create user");
//		Response response02 = API.allowAuthWithPassword(username, true);	
//		assertEquals(response02.getCode(), 200, "Failed to allow auth with password");
//		Response response03 = API.asociateLicense(username, "TOTP:Mobile");
//	    assertEquals(response03.getCode(), 200, "Failed to assign license");	
//		 
//		MobileSimulator mobile = new MobileSimulator();
//		mobile.registerTOTP(username, password);
//	    Response res = API.setTOTPPasswordRequired(username, false);
//		assertEquals(res.getCode(), 200, "Failed to assign license");	
//		
//		Thread.sleep(40000);
//		String otp =  mobile.getPasswordTOTPCode(Instant.now(), password, inputMethod);
//	     String status =  RadiusAuth.authenticate(username, otp);
//	    assertEquals(status, "ACCESS_DENIED");
//		   
//	}
//			
}



