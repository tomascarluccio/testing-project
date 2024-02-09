package tests.orchestrator;

import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.orchestrator.DashboardPage;
import pages.orchestrator.MessageDeliveryGatewayPage;
import reports.Report;


public class MessageDeliveryGatewayTest extends LoginTest {

	@DataProvider(name = "messageDeliveryGatewayTestDataProvider")
	public Object[][] getSMTPData() {
		return new Object[][] { 
			{Config.smtpHost, Config.smtpPort, Config.smtpUser, Config.smtpPassword},
		};
	}
	
	
	@Test(dataProvider = "messageDeliveryGatewayTestDataProvider", priority = 2 )
	@Report(name="Orchestrator Setup an SMTP gateway", description="No description")
	public void setupSMTPGateway(String smtpHost, String smtpPort, String smtpUser, String smtpPassword) throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();;
		superAdminDashboardPage.getMessageDeliveryGatewayLink().click();;	
		
		MessageDeliveryGatewayPage messageDeliveryGatewayPage = new MessageDeliveryGatewayPage(this.driver, this.wait);
		messageDeliveryGatewayPage.getAddButton().click();
		messageDeliveryGatewayPage.getName().click();
		messageDeliveryGatewayPage.getName().sendKeys("SMTP Gateway Test");
		messageDeliveryGatewayPage.getType().sendKeys("SMTP");
		messageDeliveryGatewayPage.getNextButton().click();
		messageDeliveryGatewayPage.getSMTPHost().sendKeys(smtpHost);
		messageDeliveryGatewayPage.getSMTPPort().sendKeys(smtpPort);
		messageDeliveryGatewayPage.getSMTPUser().sendKeys(smtpUser);
		messageDeliveryGatewayPage.getSMTPPassword().sendKeys(smtpPassword);
		messageDeliveryGatewayPage.getUseTSL().click();
		messageDeliveryGatewayPage.getSaveButton().click();	

	}
	
	@Test( priority = 3 )
	@Report(name="Orchestrator Delete an SMTP gateway", description="No description")
	public void deleteSMTPGateway() throws Exception {
		
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();;
		superAdminDashboardPage.getMessageDeliveryGatewayLink().click();
		
		MessageDeliveryGatewayPage messageDeliveryGatewayPage = new MessageDeliveryGatewayPage(this.driver, this.wait);	
		messageDeliveryGatewayPage.getDeleteCheckbox().click();
		Select actionSelect = new Select(messageDeliveryGatewayPage.getAction());
		actionSelect.selectByVisibleText("Delete selected Messages delivery gateways");
		messageDeliveryGatewayPage.getGoButton().click();
		messageDeliveryGatewayPage.getImSureButton().click();
	}

}
