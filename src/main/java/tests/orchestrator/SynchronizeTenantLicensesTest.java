package tests.orchestrator;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.ManageCustomersPage;
import reports.Report;


public class SynchronizeTenantLicensesTest extends LoginTest {

	@DataProvider(name = "synchronizeTenantLicensesDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName},
		};
	}

	@Test(dataProvider = "synchronizeTenantLicensesDataProvider", priority = 2 )
	@Report(name="Synchronize the customers licenses", description="Sync a customer licenses from the customers list page")
	public void syncLicenses(String tnName) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getManageCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage = new ManageCustomersPage(this.driver, this.wait);
		manageCustomersPage.getCustomerInputCheck(tnName).click();
	    Select actionSelect = new Select(manageCustomersPage.getAction());
	    actionSelect.selectByVisibleText("Sync Licenses");
	    manageCustomersPage.getIndex().click();
	    
	    assertEquals( manageCustomersPage.getActionResultMessage().getText(), "Customer "+ tnName +" licenses were successfully synced.", "Failed to synchronize.");
    }
	
}
