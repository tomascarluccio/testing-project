package tests.tenant.superadmin;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.RadiusClientPage;
import reports.Report;


public class RadiusClientTest extends LoginTest {

		
	@DataProvider(name = "radiusSetupData")
	public Object[][] getData() {
		return new Object[][] { 
			{Config.radiusServerClientName},
			{Config.radiusBrokerClientName}
		};
	}
		
		
	@Test( dataProvider="radiusSetupData",priority = 2)
	@Report(name="Radius Client Test", description="No description")
	public void radiusSetup(String radiusClientName) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		
		superAdminDashboardPage.getRadiusClientsLink().click();
			
		RadiusClientPage radiusPage =  new RadiusClientPage(this.driver, this.wait);

		
		if(!radiusPage.existsRadiusClient(radiusClientName)) {
			radiusPage.getAddRadiusClientLink().click();
			radiusPage.getRadiusClientName().click();
			radiusPage.getRadiusClientName().sendKeys(radiusClientName);
			radiusPage.getRadiusClientAddress().click();
			radiusPage.getRadiusClientAddress().sendKeys(Config.radiusBrokerClientAddress);
			radiusPage.getRadiusClientSharedSecret().click();
			radiusPage.getRadiusClientSharedSecret().sendKeys(Config.radiusSharedSecret);
		
		Select gatewaySelect = new Select(radiusPage.getRadiusClientGateway());
		
		gatewaySelect.selectByVisibleText(Config.gatewayName);
		radiusPage.getSavebutton().click();
		
		}
	}
}
