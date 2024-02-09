package tests.tenant.superadmin;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.LoadSystemPage;
import reports.Report;

public class LoadSystemTest extends LoginTest {

	@Test( priority = 1)
	@Report(name="Load System data", description="")
	public void loadBackup() throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSystemUpdateTab().click();
		superAdminDashboardPage.getLoadSystemDataLink().click();
		
		LoadSystemPage loadBackup = new LoadSystemPage(this.driver,this.wait);
		loadBackup.getSystemUpdate().click();
		loadBackup.getLoadSystemData().click();
		
		String filePath = Config.downloadFolder + "/backup.zip";
		loadBackup.getButtonChooseFile().sendKeys(filePath);
		loadBackup.getInputOrganizationName().sendKeys("migration");
		loadBackup.getButtonImport().click();
		loadBackup.getButtonLoad().click();
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(Duration.ofMinutes(15))
			       .pollingEvery(Duration.ofSeconds(15));
		wait.until(ExpectedConditions.presenceOfElementLocated(loadBackup.getSuccessPopUp()));
		
		loadBackup.getButtonOk().click();
	}

}
