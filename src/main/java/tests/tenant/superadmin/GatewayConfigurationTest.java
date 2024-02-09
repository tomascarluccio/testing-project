package tests.tenant.superadmin;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.GatewayConfigurationPage;
import reports.Report;


public class GatewayConfigurationTest extends LoginTest {

		

	@DataProvider(name = "gatewayDataProvider")
	public Object[][] getgtwdta() {
		return new Object[][] { 
			{Config.gatewayUrl, Config.gatewayName,  Config.gatewaySshHost},
		};
	}

	@Test( dataProvider = "gatewayDataProvider", priority = 2)
	@Report(name="Setup Wordpress Integration Test", description = "No description")
	public void swtupGatewayConf(String gtwUrl, String gtwName, String gtwSSH) throws Exception{
		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getGatewayConfigurations().click();

		if(Config.safewalkMode.equals("v5")) {
			GatewayConfigurationPage gatewayConfigurationPage = new GatewayConfigurationPage(this.driver, this.wait);
			gatewayConfigurationPage.getAddGatewayConfigLink().click();
			gatewayConfigurationPage.getGatewayName().click();
			gatewayConfigurationPage.getGatewayName().sendKeys(gtwName);
			gatewayConfigurationPage.getGatewayPublicHostInput().click();
			gatewayConfigurationPage.getGatewayPublicHostInput().sendKeys(gtwUrl);
			gatewayConfigurationPage.getGatewaySSHHostInput().click();
			gatewayConfigurationPage.getGatewaySSHHostInput().sendKeys(gtwSSH);
			gatewayConfigurationPage.getGatewayPassword().sendKeys(Config.tnPassword);
			gatewayConfigurationPage.getSaveButton().click();
		}
		
	}
	
}
	