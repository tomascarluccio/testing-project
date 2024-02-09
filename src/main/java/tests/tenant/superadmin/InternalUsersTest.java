package tests.tenant.superadmin;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.InternalUsersPage;
import reports.Report;


public class InternalUsersTest extends LoginTest {

		
	@DataProvider(name = "internalUsersDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{"internal", "Safewalk1"},
			{"unicode", "S^7*$.,yp876%s"},
		};
	}

	@Test( dataProvider = "internalUsersDataProvider", priority = 2 )
	@Report(name="Internal Users Test", description="No description")
	public void addUser(String username, String password) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();
		superAdminDashboardPage.getUsersLink().click();
		
		InternalUsersPage internalUsersPage = new InternalUsersPage(this.driver, this.wait);
		
		if(!internalUsersPage.existsUsernameLink(username)) {
			internalUsersPage.getAddUserLink().click();
			internalUsersPage.getUsername().click();
			internalUsersPage.getUsername().sendKeys(username);
			internalUsersPage.getPassword().click();
			internalUsersPage.getPassword().sendKeys(password);
			internalUsersPage.getPassword2().click();
			internalUsersPage.getPassword2().sendKeys(password);
			internalUsersPage.getSaveButton().click();
			
			this.driver.get(superAdminDashboardPage.getUrl());
			superAdminDashboardPage.getSettingsTab().click();
			superAdminDashboardPage.getUsersLink().click();
			
			internalUsersPage.getUsernameLink(username).click();;
			internalUsersPage.getDeleteLink().click();
			internalUsersPage.getSubmitButton().click();
		}
	}
	
}