package tests.tenant.superadmin;

import configs.Config;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.MessageDeliveryGatewayPage;
import reports.Report;


public class MessageDeliveryGatewayTest extends LoginTest {

	@DataProvider(name = "smtpDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{"smtpTesting"}
		};
	}
	
	
	@Test( dataProvider="smtpDataProvider", priority = 2 )
	@Report(name="Tenant SMTP Gateway Test", description="Configure and delete and SMTP gateway")
	public void setupSMTPGateway(String smtpName) throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSettingsTab().click();;
		dashboardPage.getMessageDeliveryGatewayLink().click();
		
		MessageDeliveryGatewayPage messageDeliveryGatewayPage = new MessageDeliveryGatewayPage(this.driver, this.wait);
		
		if(!messageDeliveryGatewayPage.existsSMTPGateway("SMTP")) {
			messageDeliveryGatewayPage.getAddButton().click();
			messageDeliveryGatewayPage.getName().click();
			messageDeliveryGatewayPage.getName().sendKeys(smtpName);
			messageDeliveryGatewayPage.getType().sendKeys(Config.smtpType);
			messageDeliveryGatewayPage.getNextButton().click();
			messageDeliveryGatewayPage.getSMTPHost().sendKeys(Config.smtpHost);
			messageDeliveryGatewayPage.getSMTPPort().sendKeys(Config.smtpPort);
			messageDeliveryGatewayPage.getSMTPUser().sendKeys(Config.smtpUser);
			messageDeliveryGatewayPage.getSMTPPassword().sendKeys(Config.smtpPassword);
			messageDeliveryGatewayPage.getUseTSL().click();
			messageDeliveryGatewayPage.getSaveButton().click();	
		}
		
		messageDeliveryGatewayPage.getDeleteCheckbox().click();
		Select actionSelect = new Select(messageDeliveryGatewayPage.getAction());
		actionSelect.selectByVisibleText("Delete selected Messages delivery gateways");
		messageDeliveryGatewayPage.getGoButton().click();
		messageDeliveryGatewayPage.getImSureButton().click();
	
	}
	
}