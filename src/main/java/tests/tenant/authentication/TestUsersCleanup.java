package tests.tenant.authentication;

import org.testng.annotations.Test;

import reports.Report;
import tests.BaseAPITest;
import tests.tenant.API;
import tests.tenant.authentication.api.API2StepBackupCode;
import tests.tenant.authentication.api.API2StepTOTP;
import tests.tenant.authentication.api.API2StepVirtualOTP;
import tests.tenant.authentication.api.APIPushFallbackTest;
import tests.tenant.authentication.api.APISingleStepBackupCode;
import tests.tenant.authentication.api.APISingleStepPasswordTOTP;
import tests.tenant.authentication.api.APISingleStepPush;
import tests.tenant.authentication.api.APISingleStepQR;
import tests.tenant.authentication.api.APISingleStepStaticPassword;
import tests.tenant.authentication.api.APISingleStepTOTP;
import tests.tenant.authentication.radius.Radius2StepBackupCode;
import tests.tenant.authentication.radius.Radius2StepVirtual;
import tests.tenant.authentication.radius.RadiusPushFallbackTest;
import tests.tenant.authentication.radius.RadiusSingleStepBackupCode;
import tests.tenant.authentication.radius.RadiusSingleStepPasswordTOTP;
import tests.tenant.authentication.radius.RadiusSingleStepPush;
import tests.tenant.authentication.radius.RadiusSingleStepStaticPassword;
import tests.tenant.authentication.radius.RadiusSingleStepTOTP;
import utils.Response;



public class TestUsersCleanup extends BaseAPITest {

	
	@Test(dataProvider = "backupCodeDataProvider", priority = 1,   dataProviderClass = API2StepBackupCode.class)
	@Report(name="Cleanup", description="")
	public void testTearDown(String username, String password) throws Exception {
		delUser(username);
	}
	
	@Test(dataProvider="totpDataProvider", priority = 2 , dataProviderClass=API2StepTOTP.class)
	public void test1(String username, String password) throws Exception {
		delUser(username);

	}
	
	
	@Test(dataProvider = "virtualDataProvider", priority = 3, dataProviderClass=API2StepVirtualOTP.class)
	public void test2(String username, String password) throws Exception {
		delUser(username);	
		}
	
	@Test(dataProvider = "fallbackUsers", priority = 4, dataProviderClass=APIPushFallbackTest.class)
	public void test3(String username, String password) throws Exception {
		delUser(username);
	}
	

	@Test(dataProvider = "backupCodeDataProvider2", priority = 5, dataProviderClass=APISingleStepBackupCode.class)
	public void test4(String username, String password) throws Exception {
		delUser(username);}
	
	@Test(dataProvider = "passwodTotpDataProvider", priority = 6, dataProviderClass=APISingleStepPasswordTOTP.class)
	public void test5(String username, String password, String inputMethod) throws Exception {
		delUser(username);	}
	
	@Test(dataProvider = "pushDataProvider", priority = 7, dataProviderClass=APISingleStepPush.class)
	public void test6(String username, String password) throws Exception {
		delUser(username);
		}

	@Test(dataProvider = "fastAuthDataProvider", priority = 8, dataProviderClass=APISingleStepQR.class)
	public void test7(String username, String password) throws Exception {
	delUser(username);
	}
	
	
	@Test(dataProvider = "staticPasswordUsers", priority = 9, dataProviderClass=APISingleStepStaticPassword.class)
	public void test8(String username, String password, String userDn) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider = "totpAPIDataProvider", priority = 10, dataProviderClass=APISingleStepTOTP.class)
	public void test9(String username, String password) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider = "radiusBackupCodeUsers", priority = 11, dataProviderClass=Radius2StepBackupCode.class)
	public void test10(String username, String password, String dos) throws Exception {
	delUser(username);

	}
	
	@Test(dataProvider = "radiusVirtualUsers", priority = 12, dataProviderClass=Radius2StepVirtual.class)
	public void test11(String username, String password,  String dos) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider = "radiusFallbackUsers", priority = 13, dataProviderClass=RadiusPushFallbackTest.class)
	public void test12(String username, String password,  String dos) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider = "radiusBackupCodeUsers2", priority = 14, dataProviderClass=RadiusSingleStepBackupCode.class)
	public void test13(String username, String password,  String dos) throws Exception {
	delUser(username);

	}
	
	@Test(dataProvider = "radius1totpDataProvider", priority = 15,  dataProviderClass=RadiusSingleStepPasswordTOTP.class)
	public void test14(String username, String password, String inputMethod, String radius) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider="radiusPushPasswordUsers", priority = 16, dataProviderClass=RadiusSingleStepPush.class)
	public void test15(String username, String password,  String dos) throws Exception {
		delUser(username);

	}
	
	@Test(dataProvider="radiusStaticPasswordUsers", priority =17, dataProviderClass=RadiusSingleStepStaticPassword.class)
	public void test16(String username, String password, String userDn, String host) throws Exception {
		delUser(username);
	}
	
	@Test(dataProvider = "radiusTOTPUsers2", priority = 18, dataProviderClass=RadiusSingleStepTOTP.class)
	public void test17(String username, String password, String plus) throws Exception {
		delUser(username);

	}
	
	private void delUser(String  username) throws Exception{
		Response res = API.deleteUser(username);	
		if(res.getCode() !=200 || res.getCode() !=201)
			System.out.println(res.getContent());
		Response res2 = API.createUser(username, "Safewalk1");
		if(res2.getCode() !=200 || res.getCode() !=201)
			System.out.println(res.getContent());
	}


}


