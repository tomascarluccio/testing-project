package tests.tenant.superadmin;

import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.InternalUsersDetailPage;
import pages.tenant.superadmin.InternalUsersPage;
import pages.tenant.superadmin.LoginPage;
import tests.BaseBrowserTest;


public class LoginTest extends BaseBrowserTest {

	@DataProvider(name = "loginDataProvider", parallel=true)
	public Object[][] getLoginData() {
		return new Object[][] { 
			{Config.tnApiUsername, Config.tnApiPassword, Config.tnUsername, Config.tnPassword}};

	}

	@Test(dataProvider = "loginDataProvider", priority = 1)
	public void login(String  username, String password, String  adminUser, String adminPassword) throws Exception {
		LoginPage loginPage = new LoginPage(this.driver, this.wait);
		this.driver.get(loginPage.getUrl());

		loginPage.getUsernameInput().sendKeys(adminUser);
		loginPage.getPasswordInput().sendKeys(adminPassword);
		loginPage.getLoginButton().click();

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());

		superAdminDashboardPage.getUsersLink().click();
		InternalUsersPage internalUsersPage = new InternalUsersPage(this.driver, this.wait);

		if (internalUsersPage.existsUsernameLink(username)) {
			internalUsersPage.getUsernameLink(username).click();
			InternalUsersDetailPage internalUsersDetailPage = new InternalUsersDetailPage(this.driver, this.wait);

			Config.tnAuthenticationToken = internalUsersDetailPage.getAuthTokenInput().getAttribute("value");
			Config.tnManagementToken = internalUsersDetailPage.getManageTokenInput().getAttribute("value");
			Config.tnSSPToken = internalUsersDetailPage.getUserAccessTokenInput().getAttribute("value");

			internalUsersDetailPage.getSaveButton().click();
		} else {
			internalUsersPage.getAddUserLink().click();
			internalUsersPage.getUsername().sendKeys(username);
			internalUsersPage.getPassword().sendKeys(Config.tnApiPassword);
			internalUsersPage.getPassword2().sendKeys(Config.tnApiPassword);
			internalUsersPage.getSaveAndContinue().click();

			InternalUsersDetailPage internalUsersDetailPage = new InternalUsersDetailPage(this.driver, this.wait);
			Select select = new Select(internalUsersDetailPage.getGroupFrom());
			select.selectByVisibleText("Users");
			internalUsersDetailPage.getRemoveGroupLink().click();
			select = new Select(internalUsersDetailPage.getGroupto());
			select.selectByVisibleText("Management");
			internalUsersDetailPage.getAddGroupLink().click();
			internalUsersDetailPage.getInputCheckSuperUser().click();
			internalUsersDetailPage.getAuthenticationTokenLink().click();
			internalUsersDetailPage.getManagementTokenLink().click();
			internalUsersDetailPage.getUserAccessTokenLink().click();

			Config.tnAuthenticationToken = internalUsersDetailPage.getAuthTokenInput().getAttribute("value");
			Config.tnManagementToken = internalUsersDetailPage.getManageTokenInput().getAttribute("value");
			Config.tnSSPToken = internalUsersDetailPage.getUserAccessTokenInput().getAttribute("value");

			internalUsersDetailPage.getSaveButton().click();
		}

		Config.setFsProperty("tnManagementToken", Config.tnManagementToken);
		Config.setFsProperty("tnAuthenticationToken", Config.tnAuthenticationToken);
		Config.setFsProperty("tnSSPToken", Config.tnSSPToken);
	}
}
