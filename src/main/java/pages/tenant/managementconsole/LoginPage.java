package pages.tenant.managementconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class LoginPage extends BasePage {
	
    private By usernameInput;
    private By passwordInput;
    private By loginButton;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        this.usernameInput = By.cssSelector("input[name='initial-username']");
        this.passwordInput = By.cssSelector("input[name='initial-password']");
        this.loginButton = By.id("login-btn");
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
		return "https://" + Config.tnHost + ":" + Config.tnManagementConsolePort.toString() + "/admin/login/?next=/admin/";
	}
		
}
