package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class WordpressPage extends BasePage{

	By usernameInput;	
	By passwordInput;	
	By loginButton;
	By loginLink;
	By settingsLink;
	By identityProviderTab;
	By urlIdentifierInput;
	By singleSingOnUrlInput;
	By singleLogoutUrlInput;
	By certificateFingerprintInput;
	By updateOptionsButton;
	
	
	public WordpressPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.usernameInput                  = By.id("user_login");
		this.passwordInput                  = By.id("user_pass");
		this.loginButton               		= By.id("wp-submit");
		this.loginLink						= By.linkText("Login");
		this.settingsLink					= By.id("menu-settings");
		this.identityProviderTab            = By.linkText("Identity Provider");
		this.urlIdentifierInput             = By.id("idp_identifier");
		this.singleSingOnUrlInput           = By.id("idp_signon");
		this.singleLogoutUrlInput           = By.id("idp_logout");
		this.certificateFingerprintInput    = By.id("idp_fingerprint");
		this.updateOptionsButton           	= By.name("submit");
	}

	public WebElement getUsernameInputLink() {
		return this.driver.findElement(this.usernameInput);
	}

	public WebElement getPasswordInput() {
		return this.driver.findElement(this.passwordInput);
	}

	public WebElement getLoginButton() {
		return this.driver.findElement(this.loginButton);
	}
	
	public WebElement getLoginLink() {
		return this.driver.findElement(this.loginLink);
	}
	
	public WebElement getSettingsLink() {
		return this.driver.findElement(this.settingsLink);
	}
	
	public WebElement getIdentityProviderTab() {
		return this.driver.findElement(this.identityProviderTab);
	}
	
	public WebElement getUrlIdentifierInput() {
		return this.driver.findElement(this.urlIdentifierInput);
	}
	
	public WebElement getSingleSingOnUrlInput() {
		return this.driver.findElement(this.singleSingOnUrlInput);
	}
	
	public WebElement getSingleLogoutUrlInput() {
		return this.driver.findElement(this.singleLogoutUrlInput);
	}
	
	public WebElement getCertificateFingerprintInput() {
		return this.driver.findElement(this.certificateFingerprintInput);
	}
	
	public WebElement getUpdateOptionsButton() {
		return this.driver.findElement(this.updateOptionsButton);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/safewalk_gateway/samlwebintegration/";
	}
}