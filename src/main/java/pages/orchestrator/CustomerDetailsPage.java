package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class CustomerDetailsPage extends BasePage{

	By adminPassword;
	By adminPasswordConfirmation;
	By adminName;
	By subdomain;
	By name;
    By gatewayDomain;
    By requirementsLevel;
    By applianceVersion;
    By saveButton;
    By deleteButton;
    By confirmBox;
    By passwordResetLicenses;
    By generalPorpouseLicense;
    By loginLevel;
  

	public CustomerDetailsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
 
		this.adminPassword 					=  By.id("id_admin_password_confirmation");
		this.adminPasswordConfirmation   	=  By.id("id_admin_password");	
		this.adminName                   	=  By.id("id_admin_name");
		this.subdomain       	         	=  By.id("id_subdomain");
		this.name                        	=  By.id("id_name");
		this.gatewayDomain               	=  By.id("id_gateway_domain");
		this.requirementsLevel           	=  By.id("id_requirements_level");
		this.applianceVersion            	=  By.id("id_bundle_version");
		this.saveButton                  	=  By.cssSelector("input[value = 'Save']");
		this.deleteButton                	=  By.linkText("Delete");
		this.confirmBox                  	=  By.name("confirm");
		this.passwordResetLicenses       	=  By.id("id_password_reset");
		this.generalPorpouseLicense      	=  By.id("id_generalpurpose");
		this.loginLevel					 	=  By.id("id_fk_CustomSettings_Customer-0-logging_level");
		
	}

	public WebElement getAdminPassword() {
	    return this.driver.findElement(this.adminPassword);
	}

	public WebElement getAdminPasswordConfirmation() {
		return this.driver.findElement(this.adminPasswordConfirmation);
	}

	public WebElement getAdminName() {
		return this.driver.findElement(this.adminName);
	}

	public WebElement getSubdomain() {
	    return this.driver.findElement(this.subdomain); 
	}

	public WebElement getName() {
		return this.driver.findElement(this.name);
	}

	public WebElement getGatewayDomain() {
		return this.driver.findElement(this.gatewayDomain);
	}

	public WebElement getRequirementsLevel() {
		return this.driver.findElement(this.requirementsLevel); 
	}
	
	public WebElement getApplianceVersion() {
		return this.driver.findElement(this.applianceVersion); 
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}

	public WebElement getDeleteButton() {
		return this.driver.findElement(this.deleteButton);
	}
	 
	public WebElement getConfimBox() {
		return this.driver.findElement(this.confirmBox);
	}
	
	public WebElement getPasswordResetLicenses() {
		return this.driver.findElement(this.passwordResetLicenses);
	}
	
	public WebElement getGeneralPorpuseLicense() {
		return this.driver.findElement(this.generalPorpouseLicense);
	}
	
	public WebElement getLoginLevel() {
		return this.driver.findElement(this.loginLevel);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/multitenancy/customer/";
	}
}