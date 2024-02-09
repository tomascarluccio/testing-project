package tests.orchestrator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.orchestrator.DashboardPage;
import pages.orchestrator.InternalUsersPage;
import reports.Report;


public class AddInternalUsersTest extends LoginTest {

		
	@DataProvider(name = "internalUsersDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{"internal", "Safewalk1"},
			{"unicode", "S^7*$.,yp876%s"},
			{"management", "Safewalk1"},
			{"helpdesk", "Safewalk1"},
		};
	}

	@Test( dataProvider = "internalUsersDataProvider", priority = 2 )
	@Report(name="Add a new internal users", description="User, Unicode Password, Management, Helpdesk")
	public void addUser(String username, String password) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();
		superAdminDashboardPage.getUsersLink().click();
		
		InternalUsersPage internalUsersPage = new InternalUsersPage(this.driver, this.wait);
		internalUsersPage.getAddUserLink().click();
		internalUsersPage.getUsername().click();
		internalUsersPage.getUsername().sendKeys(username);
		internalUsersPage.getPasssword().click();
		internalUsersPage.getPasssword().sendKeys(password);
		internalUsersPage.getPassword2().click();
		internalUsersPage.getPassword2().sendKeys(password);
		internalUsersPage.getSaveButton().click();
	}
	
	@Test( dataProvider = "internalUsersDataProvider", priority = 3 )
	@Report(name="Removes internal users", description="User, Unicode Password, Management, Helpdesk")
	public void deleteUser(String username, String password) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();
		superAdminDashboardPage.getUsersLink().click();
		
		InternalUsersPage internalUsersPage = new InternalUsersPage(this.driver, this.wait);
		internalUsersPage.getUsernameLink(username).click();;
		internalUsersPage.getDeleteLink().click();
		internalUsersPage.getSubmitButton().click();
	}
	

}