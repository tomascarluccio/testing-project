package tests.mobile;

import org.testng.annotations.Test;

import pages.mobile.ChangePinPage;
import pages.mobile.ConfirmationChangePinPage;
import pages.mobile.HomePage;
import pages.mobile.SettingsPage;
import reports.Report;
import tests.BaseMobileTest;


public class ChangePinTest extends BaseMobileTest {


	@Test(priority = 1)
	@Report(name = "Change pin", description = "Change pin")
	public void main() throws Exception {

		HomePage homePage = new HomePage(this.driver, this.wait, this.platform);
		SettingsPage settingsPage = new SettingsPage(this.driver, this.wait, this.platform);
		ChangePinPage changePinPage = new ChangePinPage(this.driver, this.wait, this.platform);
		ConfirmationChangePinPage confirmationChangePinPage = new ConfirmationChangePinPage(this.driver, this.wait, this.platform);
		
		homePage.clickMenuButton();
		
		settingsPage.clickChangePinButton();
		
		changePinPage.waitForElements();
		changePinPage.clickOneButton();
		changePinPage.clickCeroButton();
		changePinPage.clickOneButton();
		changePinPage.clickFiveButton();
	
		confirmationChangePinPage.clickOneButton();
		confirmationChangePinPage.clickCeroButton();
		confirmationChangePinPage.clickOneButton();
		confirmationChangePinPage.clickFiveButton();
		
		confirmationChangePinPage.waitForOkButton();
		confirmationChangePinPage.clickOkButton();
		
	}
}
