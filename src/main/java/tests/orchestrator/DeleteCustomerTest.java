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


public class DeleteCustomerTest extends LoginTest {
	
	@DataProvider(name = "deleteCustomerDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tnName},
		};
	}
	
	@Test(dataProvider = "deleteCustomerDataProvider", priority = 2 )
	@Report(name="Delete Customer", description="Deletes tenant")
	public void deleteCustomer(String tnName) throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getCustomersLink().click();
		
		ManageCustomersPage manageCustomersPage =  new ManageCustomersPage(driver, this.wait);
		
		
		if(manageCustomersPage.existsCustomer(tnName)) {
			manageCustomersPage.getCustomerNameLink().click();
			CustomerDetailsPage customerDetalisPage = new CustomerDetailsPage(this.driver, this.wait);
			customerDetalisPage.getDeleteButton().click();
			customerDetalisPage.getConfimBox().click();
			customerDetalisPage.getConfimBox().sendKeys(Config.tnName);
			customerDetalisPage.getConfimBox().sendKeys(Keys.ENTER);
			
		    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
		       .withTimeout(Duration.ofMinutes(15))
		       .pollingEvery(Duration.ofSeconds(15));
			wait.until(ExpectedConditions.invisibilityOf(manageCustomersPage.getCustomerStatus(tnName)));
		}
	}

}