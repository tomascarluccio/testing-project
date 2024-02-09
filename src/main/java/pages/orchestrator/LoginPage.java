package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class LoginPage extends BasePage{

	By usernameInput;	
	By passwordInput;	
	By loginButton;
	
	public LoginPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.usernameInput 		= By.id("id_username");	
		this.passwordInput 		= By.id("id_password");	
		this.loginButton 		= By.cssSelector("input[value = 'Log in']");
	}
	
	public WebElement getUsernameInput() {
		return this.driver.findElement(this.usernameInput);
	}

	public WebElement getPasswordInput() {
		return this.driver.findElement(this.passwordInput);
	}

	public WebElement getLoginButton() {
		return this.driver.findElement(this.loginButton);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/";
	}
}
