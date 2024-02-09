package tests.tenant.superadmin;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.TroubleshootingLogPage;
import reports.Report;
import tests.tenant.API;
import utils.Response;

public class TroubleshootingLogTest extends LoginTest{
	
	@DataProvider(name = "troubleshootingLogDataProvider")
	public Object[][] getTroubleshootingLogData() {
		return new Object[][] { 
			{"tloguser", "Safewalk1"}
		};
	}
	
	@Test( dataProvider="troubleshootingLogDataProvider",  priority =  2)
	@Report(name="Troubleshooting Log Test", description="Test authentication to users with password allowed and disabled, finally check if the troubleshooting log is generated")
	public void testEnabletroubleshoootingLog(String username, String password) throws Exception{

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getTroubleshootingLogLink().click();
		
		TroubleshootingLogPage troubleshootingLogPage = new TroubleshootingLogPage(this.driver, this.wait);
		troubleshootingLogPage.getEnableButton().click();
		troubleshootingLogPage.getOkButton().click();
		
	
		API.deleteUser(username);
		
		Response response = API.createUser(username, password);
		assertEquals(response.getCode(), 201, "Failed to create user");
		
		response = API.allowAuthWithPassword(username, true);
		Assert.assertEquals(response.getCode(), 200, "Failed then allowing authentification with password");
		
		response = API.authenticate(username, password);
		System.out.println(response.getContent());
		Assert.assertEquals(response.getCode(), 200, "Failed authentication");
			
		this.driver.get(troubleshootingLogPage.getUrl());
		Assert.assertEquals(troubleshootingLogPage.getLastLogUser().getText(),username, "Failed in generating the troubleshootingLog");
		Assert.assertEquals(troubleshootingLogPage.getLastLogApi().getText(), "/api/v2/admin/authenticate/", "Failed in generating the troubleshootingLog");

		response = API.deleteUser(username);
		Assert.assertEquals(response.getCode(), 204, "Failed deleting the user");
	
	}
	
	
}
