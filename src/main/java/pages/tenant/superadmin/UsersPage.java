package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class UsersPage extends BasePage {
	By addUserLink;
	By usernameInput;
	By passwordInput;
	By passwordConfirmationInput;
	By saveButton;

	
	public UsersPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	
		this.addUserLink            		= By.linkText("Add user");
		this.usernameInput                 	= By.id("id_username");	
		this.passwordInput                 	= By.id("id_password1");	
		this.passwordConfirmationInput      = By.id("id_password2");	
		this.saveButton                 	= By.name("_save");	
		}
	
	public WebElement getAddUserLink() {
		return this.driver.findElement(this.addUserLink);
	}

	public WebElement getUsernameInput() {
		return this.driver.findElement(this.usernameInput);
	}
	
	public WebElement getPasswordInput() {
		return this.driver.findElement(this.passwordInput);
	}
	
	public WebElement getPasswordConfirmationInput() {
		return this.driver.findElement(this.passwordConfirmationInput);
	}	
	
	public WebElement getAuthAccessToken(String username) {
		return this.driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/table/tbody/tr[td//text()[contains(., '"+username+"')]]/td[8]"));
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}	
	

	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/accounts/appuser/";
	}
}
