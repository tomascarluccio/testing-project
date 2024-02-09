package tests.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.SystemBackupsPage;
import reports.Report;

public class SystemBackupTest extends LoginTest {

	@DataProvider(name = "troubleshootingLogDataProvider")
	public Object[][] getTroubleshootingLogData() {
		return new Object[][] { 
			{Config.ocUsername, Config.ocPassword}
		};
	}

	@Test( dataProvider="troubleshootingLogDataProvider",  priority =  2)
	@Report(name="System backup test", description="Adds a system backup")
    public void testSystemBackup(String udsername, String password) throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSystemBackupsLink().click();
		
		SystemBackupsPage systemBackupPage = new SystemBackupsPage(this.driver, this.wait);
		systemBackupPage.getAddSystemBackupLink().click();
		
		systemBackupPage.getHostField().click();
		systemBackupPage.getHostField().sendKeys(Config.systemBackupHost);
		
		systemBackupPage.getUsernameField().click();
		systemBackupPage.getUsernameField().sendKeys(Config.systemBackupUsername);
		
		systemBackupPage.getPasswordField().click();
		systemBackupPage.getPasswordField().sendKeys(Config.systemBackupPassword);
		
		Select clientTypeSelect = new Select(systemBackupPage.getClienTypeSelect());
		clientTypeSelect.selectByVisibleText("SFTP");
		
		systemBackupPage.getDestinationField().click();
		systemBackupPage.getDestinationField().sendKeys(Config.systemBackupDestination);
		
		systemBackupPage.getSaveButton().click();
		
		WebElement element = driver.findElement(By.xpath("//*[@id='container']/ul/li[1]"));
		Assert.assertEquals(element.getText(), "The system backup \"" + Config.systemBackupUsername + "@" + Config.systemBackupHost + ":22\" was added successfully.", "Failed to setup the system backup");
    
	    systemBackupPage.getDeleteCheckbox().click();
		Select actionSelect = new Select(systemBackupPage.getAction());
		actionSelect.selectByVisibleText("Delete selected system backups");
		systemBackupPage.getGoButton().click();
		systemBackupPage.getImSureButton().click();

	
	
	}


 
}
