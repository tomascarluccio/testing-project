package tests.tenant.superadmin;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import reports.Report;


import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.TroubleshootingPackagePage;

public class TroubleshootingPackageTest extends LoginTest {

	@DataProvider(name = "troubleshootingPackageDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{"Troubleshooting Package Testing", "Argentina"}
		};
	}

	@Test( dataProvider="troubleshootingPackageDataProvider" , priority = 2 )
	@Report(name="Troubleshooting Package generation", description="No description")
	public void downloadTroubleshootingPackage(String packageName, String country) throws Exception {

		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(3));

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getTroubleshootingPackageLink().click();
		
		int lenghtPreDownload = new File(Config.downloadFolder).list().length;
		
		TroubleshootingPackagePage troubleshootingPackageTest = new TroubleshootingPackagePage(this.driver, this.wait);	
		troubleshootingPackageTest.getDescriptionField().sendKeys(packageName);
		troubleshootingPackageTest.getCountryTextField().sendKeys(country);
		troubleshootingPackageTest.getUseEncryptionCheckBox().click();
		troubleshootingPackageTest.getDownloadButton().click();
		Thread.sleep(500000);


		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMinutes(5)).pollingEvery(Duration.ofSeconds(5));
		wait.until(x ->(new File(Config.downloadFolder).list().length == (lenghtPreDownload + 1)));

		File[] files = new File(Config.downloadFolder).listFiles();
		if (files != null && files.length > 0) {
			Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
			File latestFile = files[0];
			String latestFileName = latestFile.getName();

			Assert.assertTrue(latestFileName.endsWith(".zip"), "Last downloaded file is not a zip file: " + latestFileName);
		} else {
			Assert.fail("Could not find any downloaded file");
		}
 	}
}

