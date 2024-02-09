package tests.tenant.superadmin;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.GatewayConfigurationPage;

public class GatewayTest extends LoginTest{

	@DataProvider(name = "gatewayDataProvider")
	public Object[][] getgtwdta() {
		return new Object[][] { 
			{Config.gatewayUrl, Config.gatewayName,  Config.gatewaySshHost},
		};
	}
	
	@Test(dataProvider = "gatewayDataProvider", priority = 1)
	public void addGateway(String gatewayHost, String gatewayName, String gatewaySshhost) throws Exception {
		
		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
			
		
		GatewayConfigurationPage gatewayConfigurationPage = new GatewayConfigurationPage(this.driver, this.wait);
		dashboardPage.getGatewayConfigurations().click();
		gatewayConfigurationPage.getAddGatewayConfigLink().click();
		gatewayConfigurationPage.getGatewayName().click();
		gatewayConfigurationPage.getGatewayName().sendKeys(gatewayName);
		gatewayConfigurationPage.getGatewayPublicHostInput().click();
		gatewayConfigurationPage.getGatewayPublicHostInput().sendKeys(gatewayHost);
		gatewayConfigurationPage.getGatewaySSHHostInput().click();
		gatewayConfigurationPage.getGatewaySSHHostInput().sendKeys(gatewaySshhost);
		gatewayConfigurationPage.getSaveButton().click();	

	}
}
