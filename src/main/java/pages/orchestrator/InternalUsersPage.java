package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class InternalUsersPage extends BasePage {
	
	By addLink;
	By username;
	By passsword;
	By password2;
	By saveButton;
	By deleteLink;
	By submitButton;


	public InternalUsersPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.addLink   					= By.linkText("Add user");	
		this.username  					= By.id("id_username");
		this.passsword 					= By.id("id_password1");
		this.password2 					= By.id("id_password2");
		this.saveButton 				= By.name("_save");
		this.deleteLink 				= By.linkText("Delete");
		this.submitButton 				= By.xpath("//*[@type='submit']");
		
	}

	public WebElement getUsernameLink(String username) {
		 return this.driver.findElement(By.linkText(username));
	}

	public WebElement getAddUserLink() {
	    return this.driver.findElement(this.addLink);
	}

	public WebElement getUsername() {
		return this.driver.findElement(this.username);
	}

	public WebElement getPasssword() {
		return this.driver.findElement(this.passsword);
	}

	public WebElement getPassword2() {
		return this.driver.findElement(this.password2);
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getDeleteLink() {
		return this.driver.findElement(this.deleteLink);
	}

	public WebElement getSubmitButton() {
		return this.driver.findElement(this.submitButton);
	}
	
	public boolean exists(String username) {
		// TODO: Look for more efficient alternatives
		int size = driver.findElements(By.linkText(username)).size();
		if(size > 0)
			return true;
		return false;
	}
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/accounts/appuser/";
	}

}
