
package tests.tenant.superadmin;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.managementconsole.ManagementConsolePage;
import pages.tenant.selfserviceportal.SelfServicePortalPage;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.OrganizationIdentityPage;
import reports.Report;


public class OrganizationLogoTest extends LoginTest {

	
	@Test( priority = 3)
	@Report(name="Organization logo test", description="")
	public void organizationLogoTest() throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getOrganizationIdentityLink().click();
		
		OrganizationIdentityPage organizationIdentityPage = new OrganizationIdentityPage(this.driver, this.wait);	
		organizationIdentityPage.getLogoInput().sendKeys(Config.imagePath);
		organizationIdentityPage.getSaveButoon().click();
//		
//		//this.driver.get(Config.userPortalLoginUrl);	TODO: If SSP login is required, perform this action correctly 
//		
		SelfServicePortalPage selfServicePortalPage = new SelfServicePortalPage(this.driver, this.wait);
		this.driver.get(selfServicePortalPage.getUrl());
		assertTrue(driver.findElement(By.id("loginLogo")).isDisplayed());
		assertTrue(driver.findElement(By.id("loginLogo")).getAttribute("src").contains("swiss"));
		
		ManagementConsolePage dashboard = new ManagementConsolePage(this.driver, this.wait);
		this.driver.get(dashboard.getUrl());
		assertTrue(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div")).isDisplayed());
		assertTrue(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div")).getAttribute("style").contains("swiss"));
		
	}
	

	

}



