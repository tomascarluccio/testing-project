package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.Keys;
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


public class RestartCustomerTest extends LoginTest {

	@DataProvider(name = "restartCustomerDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName},
		};
	}
	
	@Test(dataProvider = "restartCustomerDataProvider", priority = 2)
	@Report(name="Stops a customer", description="Stops and Starts a customer from customers list")
	public void stopCustomer(String tnName) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getManageCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage = new ManageCustomersPage(this.driver, this.wait);
		manageCustomersPage.getStopButton(tnName).click();
		
		CustomerDetailsPage customerDetalisPage = new CustomerDetailsPage(this.driver, this.wait);
		customerDetalisPage.getConfimBox().click();
		customerDetalisPage.getConfimBox().sendKeys(Config.tnName);
		customerDetalisPage.getConfimBox().sendKeys(Keys.ENTER);

	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	    		.withTimeout(Duration.ofMinutes(15))
	    		.pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Stopped"));
   }
	
	@Test(dataProvider = "restartCustomerDataProvider", priority = 3 )
	@Report(name="Starts a customer", description="No description")
	public void startCustomer(String tnName) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getManageCustomersLink().click();;	
		
		ManageCustomersPage manageCustomersPage = new ManageCustomersPage(this.driver, this.wait);
		manageCustomersPage.getStartButton(tnName).click();
	
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	    		.withTimeout(Duration.ofMinutes(15))
	    		.pollingEvery(Duration.ofSeconds(15));
	    wait.until(ExpectedConditions.textToBePresentInElement(manageCustomersPage.getCustomerStatus(tnName), "Running"));
   }
}
