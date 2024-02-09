package tests.orchestrator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.TroubleshootingLogPage;
import reports.Report;


public class TroubleshootingLogTest extends LoginTest {
    
	@DataProvider(name = "troubleshootingLogDataProvider")
	public Object[][] getTroubleshootingLogData() {
		return new Object[][] { 
			{Config.tnUsername, Config.tnPassword}
		};
	}

	@Test( dataProvider="troubleshootingLogDataProvider",  priority =  2)
	@Report(name="Orchestrator Enable troubleshooting log", description="")
	public void enableDisblleTroubleshootingLog(String username, String password) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());

		superAdminDashboardPage.getTroubleshootingLogLink().click();
		Thread.sleep(5000);
		TroubleshootingLogPage troubleshootingLogPage = new TroubleshootingLogPage(this.driver, this.wait);
		troubleshootingLogPage.getEnableButton().click();
		troubleshootingLogPage.getOkButton().click();
		Thread.sleep(5000);
		superAdminDashboardPage.getHomeLink().click();

		superAdminDashboardPage.getTroubleshootingLogLink().click();
		troubleshootingLogPage.getDisableButton().click();
		troubleshootingLogPage.getOkButton().click();
		Assert.assertEquals(troubleshootingLogPage.getWarningMessage().getText(), "Troubleshooting output is currently inactive - Select an option from the action menu to activate it", "Failed in generating the troubleshootingLog");
		
		}


}