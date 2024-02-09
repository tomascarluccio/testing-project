package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.CustomerDetailsPage;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.ManageCustomersPage;
import pages.orchestrator.ResourcesManagerPage;
import reports.Report;


public class ResourcesManagerTest extends LoginTest {
	
	@DataProvider(name = "resourceManagerDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{ Config.resourceLevelCust},
		};
	}

	@Test(dataProvider = "resourceManagerDataProvider", priority = 2 )
	@Report(name="Add Resources level", description="Adds a custom Low-Med resource level from resources managment page")
	public void addNewResourceLevel(String ocCustomResourceLevel) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getResourcesManagerLink().click();
		
		ResourcesManagerPage resourcesManagerPage = new ResourcesManagerPage(this.driver, this.wait);	
		resourcesManagerPage.getAddLink().click();
		resourcesManagerPage.getName().sendKeys(ocCustomResourceLevel);
		resourcesManagerPage.getMaxRequestsApi().click();
		resourcesManagerPage.getMaxRequestsApi().clear();
		resourcesManagerPage.getMaxRequestsApi().sendKeys("25");
		resourcesManagerPage.getMaxMobileRequests().click();
		resourcesManagerPage.getMaxMobileRequests().clear();
		resourcesManagerPage.getMaxMobileRequests().sendKeys("25");
		resourcesManagerPage.getMaxAuthRequets().click();
		resourcesManagerPage.getMaxAuthRequets().clear();
		resourcesManagerPage.getMaxAuthRequets().sendKeys("25");
		resourcesManagerPage.getMaxManagementRequests().click();
		resourcesManagerPage.getMaxManagementRequests().clear();
		resourcesManagerPage.getMaxManagementRequests().sendKeys("17");;
		resourcesManagerPage.getNumberOfBackgroundWorkers().click();
		resourcesManagerPage.getNumberOfBackgroundWorkers().clear();
		resourcesManagerPage.getNumberOfBackgroundWorkers().sendKeys("2");
		resourcesManagerPage.getNumberOfGatewaysWorkers().click();
		resourcesManagerPage.getNumberOfGatewaysWorkers().clear();
		resourcesManagerPage.getNumberOfGatewaysWorkers().sendKeys("2");
		resourcesManagerPage.getMaxPortalRequests().click();
		resourcesManagerPage.getMaxPortalRequests().clear();
		resourcesManagerPage.getMaxPortalRequests().sendKeys("25");
		resourcesManagerPage.getNumberOfRadiusThreads().click();
		resourcesManagerPage.getNumberOfRadiusThreads().clear();
		resourcesManagerPage.getNumberOfRadiusThreads().sendKeys("7");
		resourcesManagerPage.getSaveButton().click();
	}
	
	@Test(dataProvider = "resourceManagerDataProvider", priority = 3)
	@Report(name="Change customers resources Level", description="Sets a diffenre Customer Resource Level")
	public void changeResourcesLevel(String ocResourceLevel) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage =  new ManageCustomersPage(driver, this.wait);
		manageCustomersPage.getCustomerNameLink().click();
		
		CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(this.driver, this.wait);
		Select requirementsLevelSelect = new Select(customerDetailsPage.getRequirementsLevel());
		
		requirementsLevelSelect.selectByVisibleText(ocResourceLevel);
		customerDetailsPage.getGeneralPorpuseLicense().click();
		customerDetailsPage.getGeneralPorpuseLicense().clear();
		customerDetailsPage.getGeneralPorpuseLicense().sendKeys("10");
		customerDetailsPage.getPasswordResetLicenses().click();
		customerDetailsPage.getPasswordResetLicenses().clear();
		customerDetailsPage.getPasswordResetLicenses().sendKeys("2");
		customerDetailsPage.getSaveButton().click();
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(Duration.ofMinutes(5))
			       .pollingEvery(Duration.ofSeconds(30)); 
			   
	    wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(Config.tnName), "Updating"));
	    wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(Config.tnName), "Running"));
	
	}
}