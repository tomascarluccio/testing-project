package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.ImportLicensesPage;
import reports.Report;


public class ImportLicensesTest extends LoginTest {

	@DataProvider(name = "licensesText")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tokenLicensePath, Config.pswResetLicensePath},
		};
	}
	
	@Test(dataProvider = "licensesText", priority = 2 )
	@Report(name="Import General Porpouse token licenses", description="Imports a file with Demo GP licenses")
	public void importTokenLicenses(String pathGP, String pathPwdREset) throws Exception {
		
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(40));

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getLicensesTab().click();
		superAdminDashboardPage.getTokenLincencesLink().click();
		
		ImportLicensesPage importLicensesPage = new ImportLicensesPage(this.driver, this.wait);	
		importLicensesPage.getFileInput().sendKeys(pathGP); 
		importLicensesPage.getReplaceCheck().click();
		importLicensesPage.getUploadButton().click();
		
		this.wait.until(ExpectedConditions.visibilityOf(importLicensesPage.getConfirmAlert()));
		
		importLicensesPage.getYesPopup().click();
	}

	@Test(dataProvider = "licensesText", priority = 3 )
	@Report(name="Import Password Reset token licenses", description="Imports a file with Password Reset licenses")
		public void importPasswordResetTokens(String pathGP, String pathPwdREset)  throws Exception {
		
		this.wait = new WebDriverWait(driver, Duration.ofMinutes(40));
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(10));
		this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(10));

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getLicensesTab().click();
		superAdminDashboardPage.getTokenLincencesLink().click();
		wait.withTimeout( Duration.ofMinutes(40));
		
		ImportLicensesPage importLicensesPage = new ImportLicensesPage(this.driver, this.wait);	
		importLicensesPage.getFileInput().sendKeys(pathPwdREset);
		importLicensesPage.getReplaceCheck().click();
		importLicensesPage.getUploadButton().click();
		
		this.wait.until(ExpectedConditions.visibilityOf(importLicensesPage.getConfirmAlert()));
		
		importLicensesPage.getYesPopup().click();
	}
}