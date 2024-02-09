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
import reports.Report;
import tests.tenant.API;


public class AddCustomerTest extends LoginTest {
	
	@DataProvider(name = "addCustomerDataProvider",  parallel=false)
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName, "Low"},
		};
	}
	
	
	@Test(dataProvider = "addCustomerDataProvider", priority = 2)
	@Report(name="Add a new Customer", description="Creates a tenant with testing target version")
	public void addCustomer(String tnName, String ocResourceLevel) throws Exception {
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage =  new ManageCustomersPage(driver, this.wait);		
		try {
			manageCustomersPage.getCustomerNameLink();	
		}
		catch(Exception e) {	

		manageCustomersPage.getAddLink().click();
		
		CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(driver, this.wait);
		customerDetailsPage.getName().click();
		customerDetailsPage.getName().sendKeys(Config.tnName);
		
		Select applianceVersionSelect = new Select(customerDetailsPage.getApplianceVersion());
		applianceVersionSelect.selectByVisibleText(Config.tnTargetVersion);
		
		customerDetailsPage.getSubdomain().click();
		customerDetailsPage.getSubdomain().sendKeys(Config.tnHost);
		customerDetailsPage.getAdminName().click();
		customerDetailsPage.getAdminName().sendKeys("admin");
		customerDetailsPage.getAdminPassword().click();
		customerDetailsPage.getAdminPassword().sendKeys(Config.tnPassword);
		customerDetailsPage.getAdminPasswordConfirmation().click();
		customerDetailsPage.getAdminPasswordConfirmation().sendKeys(Config.tnPassword);
		
		Select requirementsLevelSelect = new Select(customerDetailsPage.getRequirementsLevel());
		requirementsLevelSelect.selectByVisibleText(ocResourceLevel);
		
		customerDetailsPage.getGatewayDomain().click();
		customerDetailsPage.getGatewayDomain().sendKeys(Config.gtwHost);
		
		Select loginLevel = new Select(customerDetailsPage.getLoginLevel());
		loginLevel.selectByVisibleText("DEBUG");
		customerDetailsPage.getSaveButton().click();
		  
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	       .withTimeout(Duration.ofMinutes(30))
	       .pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));
		API.insertLicensesToTenant(Config.tnName, Config.deviceTypeGP, Config.initTenantLicensesCount);
		
		}

	}
		

}