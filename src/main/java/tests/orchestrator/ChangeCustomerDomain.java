package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.CustomerDetailsPage;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.ManageCustomersPage;
import reports.Report;


public class ChangeCustomerDomain extends LoginTest {

	@DataProvider(name = "changeCustomerSettingsDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName, Config.resourceLevelMed},
		};
	}
	
	
	
	@Test(dataProvider = "changeCustomerSettingsDataProvider", priority = 5)
	@Report(name="Restore Customer domain", description="No description")
	public void changeCustomerDomain(String tnName, String ocResourceLevel) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage =  new ManageCustomersPage(driver, this.wait);
		manageCustomersPage.getCustomerNameLink().click();
		
		CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(this.driver, this.wait);
		customerDetailsPage.getSubdomain().click();
		customerDetailsPage.getSubdomain().clear();
		customerDetailsPage.getSubdomain().sendKeys("cd"+Config.tnHost);
		customerDetailsPage.getGatewayDomain().click();
		customerDetailsPage.getGatewayDomain().clear();
		customerDetailsPage.getGatewayDomain().sendKeys("cd"+Config.tnHost);
		customerDetailsPage.getSaveButton().click();
	
		Thread.sleep(5000);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	       .withTimeout(Duration.ofMinutes(20))
	       .pollingEvery(Duration.ofMinutes(1)); 
	
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Updating"));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));

		manageCustomersPage.getCustomerNameLink().click();
		
		customerDetailsPage.getSubdomain().click();
		customerDetailsPage.getSubdomain().clear();
		customerDetailsPage.getSubdomain().sendKeys(Config.tnHost);
		customerDetailsPage.getGatewayDomain().click();
		customerDetailsPage.getGatewayDomain().clear();
		customerDetailsPage.getGatewayDomain().sendKeys(Config.tnHost);
		customerDetailsPage.getSaveButton().click();
	
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Updating"));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));

	}
	
}