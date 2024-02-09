package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class SystemBackupsPage extends BasePage{

	By addSystemBackupLink;
	By hostField;
	By usernameField;
	By passwordField;
	By clienTypeSelect;
	By destinationField;
	By saveButton;
	By deleteCheckbox;
    By goButton;
    By action;
    By imSureButton;

	public SystemBackupsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
 
		this.addSystemBackupLink 			=  By.linkText("Add system backup");
		this.hostField						=  By.id("id_host");
		this.usernameField					=  By.id("id_username");
		this.passwordField					=  By.id("id_password");
		this.clienTypeSelect				=  By.id("id_client_type");
		this.destinationField				=  By.id("id_destination");
		this.saveButton                  	=  By.cssSelector("input[value = 'Save']");
		this.deleteCheckbox  		= By.className("action-select");
		this.action          		= By.name("action");
		this.goButton				= By.xpath("//*[text()='Go']");
		this.imSureButton			= By.xpath("//*[@id=\"content\"]/form/div/input[4]");
		
		

	}

	public WebElement getAddSystemBackupLink() {
	    return this.driver.findElement(this.addSystemBackupLink);
	}
	
	public WebElement getHostField() {
	    return this.driver.findElement(this.hostField);
	}
	
	public WebElement getUsernameField() {
	    return this.driver.findElement(this.usernameField);
	}
	
	public WebElement getPasswordField() {
	    return this.driver.findElement(this.passwordField);
	}
	
	public WebElement getClienTypeSelect() {
	    return this.driver.findElement(this.clienTypeSelect);
	}
	
	public WebElement getDestinationField() {
	    return this.driver.findElement(this.destinationField);
	}
	
	public WebElement getSaveButton() {
	    return this.driver.findElement(this.saveButton);
	}

	public WebElement getGoButton() {
		return this.driver.findElement(this.goButton);
	}
	
	public WebElement getDeleteCheckbox() {
		return this.driver.findElement(deleteCheckbox);
	}

	public WebElement getImSureButton() {
		return this.driver.findElement(this.imSureButton);
	}

	public WebElement getAction() {
		return  this.driver.findElement(action);
	}

	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/multitenancy/systembackup/";
	}
}