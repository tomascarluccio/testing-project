package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.Keys;
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


public class MigrateCustomerTest extends LoginTest {
	
	@DataProvider(name = "migrateCustomerDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName, "Low-Med"},
		};
	}

	@Test(dataProvider = "migrateCustomerDataProvider", priority = 2 )
	@Report(name="Add a stable version Customer", description="No description")
	public void addCustomer(String tnName, String ocCustomResourceLevel) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage =  new ManageCustomersPage(driver, this.wait);
		manageCustomersPage.getAddLink().click();
		
		CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(driver, this.wait);
		customerDetailsPage.getName().click();
		customerDetailsPage.getName().sendKeys(Config.tnName);
		Select applianceVersionSelect = new Select(customerDetailsPage.getApplianceVersion());
		applianceVersionSelect.selectByVisibleText(Config.tnStableVersion);
		customerDetailsPage.getSubdomain().click();
		customerDetailsPage.getSubdomain().sendKeys(Config.tnHost);
		customerDetailsPage.getAdminName().click();
		customerDetailsPage.getAdminName().sendKeys("admin");
		customerDetailsPage.getAdminPassword().click();
		customerDetailsPage.getAdminPassword().sendKeys("Safewalk1");
		customerDetailsPage.getAdminPasswordConfirmation().click();
		customerDetailsPage.getAdminPasswordConfirmation().sendKeys(Config.tnPassword);
		Select requirementsLevelSelect = new Select(customerDetailsPage.getRequirementsLevel());
		requirementsLevelSelect.selectByVisibleText(ocCustomResourceLevel);
		customerDetailsPage.getGatewayDomain().click();
		customerDetailsPage.getGatewayDomain().sendKeys(Config.tnHost);
		customerDetailsPage.getSaveButton().click();
		  
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	       .withTimeout(Duration.ofMinutes(15))
	       .pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));

		}
	
	@Test(dataProvider = "migrateCustomerDataProvider", priority = 3 )
	@Report(name="Migrate a customer to the target version", description="No description")
	public void migrateCustomer(String tnName) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getManageCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage = new ManageCustomersPage(this.driver, this.wait);
		manageCustomersPage.getMigrateButton(tnName).click();
		Select applianceVersionSelect = new Select(manageCustomersPage.getAppilanceVersion());
		applianceVersionSelect.selectByVisibleText(Config.tnTargetVersionSimplified);
		CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(this.driver, this.wait);
		customerDetailsPage.getConfimBox().click();
		customerDetailsPage.getConfimBox().sendKeys(Config.tnName);
		customerDetailsPage.getConfimBox().sendKeys(Keys.ENTER);
	
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofMinutes(15))
				.pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));
   }
	
	@Test(dataProvider = "migrateCustomerDataProvider", priority = 4 )
	@Report(name="Rollback a customer to the stable version", description="No description")
	public void rollbackCustomer(String tnName) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getManageCustomersLink().click();;	
		
		ManageCustomersPage manageCustomersPage = new ManageCustomersPage(this.driver, this.wait);
    	manageCustomersPage.getCustomerNameLink().click();
		manageCustomersPage.getRollbackButton().click();
		
    	CustomerDetailsPage customerDetailsPage = new CustomerDetailsPage(this.driver, this.wait);
    	customerDetailsPage.getConfimBox().click();
		customerDetailsPage.getConfimBox().sendKeys(Config.tnName);
		customerDetailsPage.getConfimBox().sendKeys(Keys.ENTER);
	
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofMinutes(15))
				.pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));
   }
	
}
