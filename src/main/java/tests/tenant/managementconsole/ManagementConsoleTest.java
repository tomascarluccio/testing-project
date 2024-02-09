package tests.tenant.managementconsole;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.managementconsole.ManagementConsolePage;
import pages.tenant.managementconsole.TransactionLogPage;
import reports.Report;
import tests.tenant.API;
import tests.tenant.superadmin.LoginTest;
import utils.Response;
import utils.UserAPI;

import java.util.Random;

public class ManagementConsoleTest extends LoginTest {
	
		@DataProvider(name = "mngDataProvider")
		public Object[][] getUserData() {
			return new Object[][] { 
			{"mgmuser", "testing@altipeak.com", Config.tnPassword, Config.tnUsername, Config.tnPassword, Config.ldapName },
			};
		}
		
	@Test (dataProvider = "mngDataProvider", priority = 2)
	@Report(name="MANAGEMENT CONSOLE TEST USER DETAILS", description="")
	public void testUserDetails( String mngUserUser, String mngUserMail, String mngUserPsw, String adminUser, String adminPassword, String ldapName) throws Exception {

		API.createLDAPConfiguration(Config.ldapName, Config.ldapDomain, Config.ldapBindDn, Config.ldapBindPassword, Config.ldapRootBaseDn, Config.ldapUserBaseDn, Config.ldapPriority, Config.ldapType , Config.ldapHost);
		API.insertLicensesToTenant(Config.tnName, "GeneralPorpuse", "2");

		mngUserUser = UserAPI.getRandomUsername(mngUserUser);
		UserAPI.resetUser(mngUserUser+"@"+Config.ldapDomain, mngUserPsw);

		ManagementConsolePage managementconsolePage = new ManagementConsolePage(this.driver, this.wait);	
		this.driver.get(managementconsolePage.getUrl());
		API.allowAuthWithPassword(mngUserUser, true);
		
		this.driver.get(managementconsolePage.getUrl());
		managementconsolePage.getUsernameInput().sendKeys(adminUser);
		managementconsolePage.getPasswordInput().sendKeys(adminPassword);
		managementconsolePage.getLoginButton().click();
		managementconsolePage.getUsersTabLink().click();

   	
		managementconsolePage.getLDAPUsersLink(ldapName).click();
		managementconsolePage.getSearchBarLink().click();
		managementconsolePage.getSearchBarLink().sendKeys(mngUserUser);
		managementconsolePage.getSearchBarLink().sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.stalenessOf(managementconsolePage.getUsernameLink(mngUserUser)));
		managementconsolePage.getUsernameLink(mngUserUser).click();
		managementconsolePage.getUserMailLink().click();
		managementconsolePage.getUserMailLink().clear();
		managementconsolePage.getUserMailLink().sendKeys(mngUserMail);	
		
		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getUserMobileLink()));
		managementconsolePage.getUserMobileLink().click();
		managementconsolePage.getUserMobileLink().clear();
		managementconsolePage.getUserMobileLink().sendKeys("5491151114444");
		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getSubmitBtnLink()));
		managementconsolePage.getSubmitBtnLink().click();
		API.allowAuthWithPassword(mngUserUser+"ldap@"+Config.ldapDomain, true);

		wait.until(ExpectedConditions.visibilityOf(managementconsolePage.getPersonalInformationAlertMessage()));

		driver.navigate().refresh();
		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getInternalUsersLink()));

		mngUserUser = UserAPI.getRandomUsername(mngUserUser);
		UserAPI.resetUser(mngUserUser, mngUserPsw);

		managementconsolePage.getInternalUsersLink().click();
		managementconsolePage.getSearchBarLink().click();
		managementconsolePage.getSearchBarLink().clear();
		managementconsolePage.getSearchBarLink().sendKeys(mngUserUser);
		managementconsolePage.getSearchBarLink().sendKeys(Keys.ENTER);
		wait.until(ExpectedConditions.stalenessOf(managementconsolePage.getUsernameLinkInternal(mngUserUser)));
		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getUsernameLinkInternal(mngUserUser)));

		managementconsolePage.getUsernameLinkInternal(mngUserUser).click();
		managementconsolePage.getRadiusTestTabLink().click();

		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getRadiusTestPswLink()));
		managementconsolePage.getRadiusTestPswLink().click();
		managementconsolePage.getRadiusTestPswLink().clear();
		managementconsolePage.getRadiusTestPswLink().sendKeys(mngUserPsw);
		wait.until(ExpectedConditions.elementToBeClickable(managementconsolePage.getRadiusTestBtnTest()));

		managementconsolePage.getRadiusTestBtnTest().click();
		managementconsolePage.getUserTokensTabLink().click();
		managementconsolePage.getVirtualTokenBtn().click();
		managementconsolePage.getAlertifyOk().sendKeys(Keys.ENTER);
		managementconsolePage.getDeleteTokenBtn().click();
		managementconsolePage.getAlertifyOk().sendKeys(Keys.ENTER);

	}

	@Test (dataProvider = "mngDataProvider", priority = 3)
	@Report(name="MANAGEMENT CONSOLE TEST AUTHENTICATION METHODS", description="")
	public void testTransactionsLog(String mngUserUser, String mngUserMail, String mngUserPsw, String adminUser, String adminPassword, String ldapName)  throws Exception {

		mngUserUser = UserAPI.getRandomUsername(mngUserUser);
		UserAPI.resetUser(mngUserUser+"@"+Config.ldapDomain, mngUserPsw);
		API.allowAuthWithPassword(mngUserUser, true);

		ManagementConsolePage managementconsolePage = new ManagementConsolePage(this.driver, this.wait);
		this.driver.get(managementconsolePage.getUrl());

		TransactionLogPage mngTransactionConsole = new TransactionLogPage(this.driver, this.wait);
		wait.until(ExpectedConditions.elementToBeClickable(	mngTransactionConsole.getTransLogTab()));
		mngTransactionConsole.getTransLogTab().click();

		API.allowAuthWithPassword(mngUserUser, true);
		API.authenticate(mngUserUser, mngUserPsw);
		API.authenticate(mngUserUser+"ldap@"+Config.ldapDomain, "xxxxxx");
		API.authenticate(mngUserUser, mngUserPsw);
		API.authenticate(mngUserUser+"ldap@"+Config.ldapDomain, "xxxxxx");

		//TODO: Check above  transactions details in the ui

	    mngTransactionConsole.getTimeHeader().click();
		mngTransactionConsole.getLocalTimeHeader().click();
		mngTransactionConsole.getUserHeader().click();
		mngTransactionConsole.getCallerHeader().click();
		mngTransactionConsole.getSafewalkNodeHeader().click();
		mngTransactionConsole.getTypeHeader().click();
		mngTransactionConsole.getSerialNumberHeader().click();
		mngTransactionConsole.getAuthStatusHeader().click();

	}

	@Test (dataProvider = "mngDataProvider", priority = 4)
	@Report(name="MANAGEMENT CONSOLE TEST REPORTS TAB", description="")
	public void testReportsTab(String mngUserUser, String mngUserMail, String mngUserPsw, String adminUser, String adminPassword, String ldapName)  throws Exception {
		ManagementConsolePage managementconsolePage = new ManagementConsolePage(this.driver, this.wait);
		this.driver.get(managementconsolePage.getUrl());

		wait.until(ExpectedConditions.elementToBeClickable(	managementconsolePage.getReportsTab()));
		//TODO: check all the tabs are displaying correctly
		//TODO: check filter data on each tab/report
		//TODO: check export is working for all reports

	}

}

