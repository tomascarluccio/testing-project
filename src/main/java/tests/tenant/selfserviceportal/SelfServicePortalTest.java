package tests.tenant.selfserviceportal;


import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import configs.Config;
import pages.tenant.selfserviceportal.SelfServicePortalPage;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.DefaultSettingsPage;
import reports.Report;
import tests.tenant.API;
import tests.tenant.superadmin.LoginTest;
import utils.LDAPClient;
import utils.UserAPI;

public class SelfServicePortalTest extends LoginTest {

	@DataProvider(name = "sspData")
	public Object[][] getUserData() {
		return new Object[][] { 
			{"sspuser",  "Safewalk1", "Fast:Auth:Mobile:Asymmetric","TOTP:Mobile", Config.tnHost}};
		
	}
		 
	@DataProvider(name = "passwordResetDataProvider")
	public Object[][] getPasswordResetData() {
		return new Object[][] { 
			{ "resetuser","Password:Reset", "Safewalk1","Safewalk0"}};
	}
	
	
	@Test(dataProvider = "sspData",priority = 2)
	@Report(name="SSP user login and tokens information", description="No description")
	public void tokensDisplayTest( String username, String password,String fastAuth, String totp, String tenant) throws Exception {

		API.insertLicensesToTenant(Config.tnName, Config.deviceTypeGP, "2");
		
		//Users Setup
		API.deleteUser(username);
	    API.createUser(username, password);
		API.releaseLicenses(username);
	    API.allowAuthWithPassword(username, true);	
		API.asociateLicense(username, fastAuth);
		API.asociateLicense(username, totp);
	
		
		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSettingsTab().click();;
		dashboardPage.getDefaultSettingsLink().click();
		
		DefaultSettingsPage defaultSettingsPage = new DefaultSettingsPage(driver, wait);
		defaultSettingsPage.getRegistrationAndSelfServicePortalAccessLink().click();
		defaultSettingsPage.getRegistrationLinkUriField().click();
		defaultSettingsPage.getRegistrationLinkUriField().clear();
		defaultSettingsPage.getRegistrationLinkUriField().sendKeys(Config.RegistrationLinkUri);
		defaultSettingsPage.getSaveButton().click();
			
		SelfServicePortalPage selfServicePortalPage = new SelfServicePortalPage(driver, wait);
		
		this.driver.get(selfServicePortalPage.getUrl());
		Thread.sleep(10000);
		
		selfServicePortalPage.getConsoleLogin().sendKeys(username);
		selfServicePortalPage.getConsolePassword().sendKeys(password);
		Thread.sleep(5000);
		selfServicePortalPage.getLoginButton().click();
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(selfServicePortalPage.getFastAuthLicenseStatus().getText(),"(Not Registered)");
		softAssert.assertEquals(selfServicePortalPage.getTotpLicenseStatus().getText(),"(Not Registered)");
		softAssert.assertEquals(selfServicePortalPage.getFastAuthLicenseType().getText(),fastAuth);
		softAssert.assertEquals(selfServicePortalPage.getTotpLicenseType().getText(),totp);
		
		softAssert.assertTrue(selfServicePortalPage.getFastAuthModalQrCode().isDisplayed());
		wait.until(ExpectedConditions.elementToBeClickable(selfServicePortalPage.getFastAuthModalExitButton()));
		selfServicePortalPage.getFastAuthModalExitButton().click();
		
		wait.until(ExpectedConditions.elementToBeClickable(selfServicePortalPage.getTotpModalRegisterButton()));
	
		selfServicePortalPage.getTotpModalRegisterButton().click();
		selfServicePortalPage.getTotpModalNextButton().click(); 
		softAssert.assertTrue(selfServicePortalPage.getTotpModalQrCode().isDisplayed());
		
		wait.until(ExpectedConditions.elementToBeClickable(selfServicePortalPage.getTotpModalQrButton()));
	
		selfServicePortalPage.getTotpModalQrButton().click();
		selfServicePortalPage.getLogOutButton().click();
		softAssert.assertAll();
		
	}
	
	@Test( dataProvider = "passwordResetDataProvider", priority = 3 )
	@Report(name="Password reset user must change password", description="Associate a password reeet license set user must change password in ldap login to the ssp and change the password,  logout and login with new password")
	public void passwordReset(String username, String deviceType, String password1, String password2) throws Exception {

		API.createLDAPConfiguration(Config.ldapName, Config.ldapDomain, Config.ldapBindDn, Config.ldapBindPassword, Config.ldapRootBaseDn, Config.ldapUserBaseDn, Config.ldapPriority, Config.ldapType , Config.ldapHost);
			
		API.insertLicensesToTenant(Config.tnName, "Password:Reset", "2");
		username = UserAPI.getRandomUsername(username);
		UserAPI.resetUser(username+"@"+Config.ldapDomain, password1);
		API.allowAuthWithPassword(username, true);
		API.allowWithPasswordExpired(username, true);

		API.asociateLicense(username, deviceType);
        
		//SET  LDAP MUST CHANGE PASSWORD

		LDAPClient.setUserMustChangePassword(UserAPI.getUserDN(username));
	
		//LOGIN TO SELF SERVICE PORTAL
		
		SelfServicePortalPage selfServicePortalPage = new SelfServicePortalPage(driver, wait);
		this.driver.get(selfServicePortalPage.getUrl());
		selfServicePortalPage.getConsoleLogin().sendKeys(username);
		selfServicePortalPage.getConsolePassword().sendKeys(password1);
		selfServicePortalPage.getLoginButton().click();
	
		//CHANGE THE PASSWORD
		
		selfServicePortalPage.getChangePassword1().click();
		selfServicePortalPage.getChangePassword1().clear();
		selfServicePortalPage.getChangePassword1().sendKeys(password2);
		selfServicePortalPage.getChangePassword2().click();
		selfServicePortalPage.getChangePassword2().clear();
		selfServicePortalPage.getChangePassword2().sendKeys(password2);
		selfServicePortalPage.getSendChangePasswordButton().click();
		
		assertEquals(selfServicePortalPage.getPasswordChageSuccessMessage().getText(), "Your password has been successfully changed.");
		
		selfServicePortalPage.getClickHereLoginAgain().click();
		
		//LOGIN TO SELF SERVICE PORTAL WITH NEW PASSWORD
		
		selfServicePortalPage.getConsoleLogin().sendKeys(username);
		selfServicePortalPage.getConsolePassword().sendKeys(password2);
		selfServicePortalPage.getLoginButton().click();
		Thread.sleep(5000);
		selfServicePortalPage.getLogOutButton().click();
		
		// CHECK FORGOT PASSWORD FORM ELMENTS DISPLAYED
		
		Thread.sleep(15000);
		selfServicePortalPage.getForgotPasswordLink().click();	
		selfServicePortalPage.getUsernameForgotPassword().isDisplayed();
		selfServicePortalPage.getIdCaptcha().isDisplayed();
		selfServicePortalPage.getForgotLoginBtn().isDisplayed();
		selfServicePortalPage.getClassCaptcha().isDisplayed();	
		selfServicePortalPage.getBackLogin().click();
		selfServicePortalPage.getForgotPasswordLink().isDisplayed();
		
 	}
	
}
