package tests.tenant.superadmin;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.ImportDataFilePage;
import reports.Report;


public class ImportPhysicalTokensTest extends LoginTest {

	@DataProvider(name = "physicalTokensDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.physicalLicensePath}
		};
	}
	
	
	
	@Test( dataProvider="physicalTokensDataProvider" , priority = 2 )
	@Report(name="Import Physical Tokens Test", description="Imports a file with 4 physical token licenses")
	public void importPhysicalTokenData(String licensesFilePath) throws Exception {

		this.wait = new WebDriverWait(driver, Duration.ofMinutes(40));
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(40));
		this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(40));
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getLicensesTab().click();
		superAdminDashboardPage.getPhysicalTokenDataLink().click();
		
		ImportDataFilePage importLicensesPage = new ImportDataFilePage(this.driver, this.wait);	
		importLicensesPage.getFileInput().sendKeys(licensesFilePath);
		importLicensesPage.getReplaceCheck().click();
		importLicensesPage.getUploadButton().click();
		
		this.wait.until(ExpectedConditions.visibilityOf(importLicensesPage.getConfirmAlert()));
		
		importLicensesPage.getYesPopup().click();
	
	}

}