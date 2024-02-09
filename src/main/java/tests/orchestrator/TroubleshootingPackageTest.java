package tests.orchestrator;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.TroubleshootingPackagePage;
import reports.Report;

public class TroubleshootingPackageTest extends LoginTest {
	
	@Test(priority = 2)
	@Report(name="Downloads a troubleshooting package fro OC", description="No description")
	public void downloadTroubleshootingPackage() throws Exception {


		this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(5));
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(5));
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getTroubleshootingPackageLink().click();
		int lenghtPreDownload = new File(Config.downloadFolder).list().length;

		TroubleshootingPackagePage troubleshootingPackageTest = new TroubleshootingPackagePage(this.driver, this.wait);	
		troubleshootingPackageTest.getDescriptionField().sendKeys("Troubleshooting package automation test");
		troubleshootingPackageTest.getCountryTextField().sendKeys("Argentina");
		troubleshootingPackageTest.getUseEncryptionCheckBox().click();
		troubleshootingPackageTest.getDownloadButton().click();

		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMinutes(5)).pollingEvery(Duration.ofSeconds(5));
		wait.until(x ->(new File(Config.downloadFolder).list().length == (lenghtPreDownload + 1)));

		Thread.sleep(200000);

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
