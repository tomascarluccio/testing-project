package tests.tenant.managementconsole;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.managementconsole.LoginPage;
import tests.BaseBrowserTest;



public class LoginTest extends BaseBrowserTest {

	@DataProvider(name = "loginDataProvider")
	public Object[][] getLoginData() {
		return new Object[][] { 
			{Config.tnUsername, Config.tnPassword},
		};
	}
	
	@Test(dataProvider = "loginDataProvider", priority = 1)
	public void login(String username, String password) throws Exception {
		LoginPage loginPage = new LoginPage(this.driver, this.wait);
		this.driver.get(loginPage.getUrl());
		loginPage.getUsernameInput().sendKeys(username);
		loginPage.getPasswordInput().sendKeys(password);
		loginPage.getLoginButton().click();
	}
}
