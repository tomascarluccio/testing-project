package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class DefaultSettingsPage extends BasePage{

	By gralAuthSettingsLink;
	By registrationAndSelfServicePortalAccessLink;
	By allowPwdLink;
	By allowPwdForRegistrationLink;
	By allowAccessWhenPwdExpiredLink;
	By allowPwdResetWhenForgotLink;
	By registrationLinkUriField;
	By saveButton;
	By virtualSettingsLink;
	By virtualJsonBox;
		
	public DefaultSettingsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
			

		this.gralAuthSettingsLink 		 	 					= By.id("fieldsetcollapser1");
		this.virtualSettingsLink 		 						= By.id("fieldsetcollapser8");
		this.virtualJsonBox					 					= By.id("id_virtual_device_gateways");
		this.allowPwdLink			  	 	 					= By.id("id_allow_password");
		this.allowPwdForRegistrationLink 						= By.id("id_allow_password_for_registration");
		this.allowAccessWhenPwdExpiredLink	 					= By.id("id_allow_access_when_pwd_expired");
		this.allowPwdResetWhenForgotLink	 					= By.id("id_allow_password_reset_when_forgot_pwd");
		this.saveButton					 	 					= By.cssSelector("input[value = 'Save']");
		this.gralAuthSettingsLink 		 	 					= By.id("fieldsetcollapser1");
		this.registrationAndSelfServicePortalAccessLink			= By.id("fieldsetcollapser7");
		this.allowPwdLink			  	 	 					= By.id("id_allow_password");
		this.allowPwdForRegistrationLink 	 					= By.id("id_allow_password_for_registration");
		this.allowAccessWhenPwdExpiredLink	 					= By.id("id_allow_access_when_pwd_expired");
		this.allowPwdResetWhenForgotLink	 					= By.id("id_allow_password_reset_when_forgot_pwd");
		this.registrationLinkUriField							= By.id("id_registration_link_uri");	
		this.saveButton					 	 					= By.cssSelector("input[value = 'Save']");

	}
		
	public WebElement getGralAuthSettingsLink() {
		return this.driver.findElement(this.gralAuthSettingsLink);
	}
	
	public WebElement getRegistrationAndSelfServicePortalAccessLink() {
		return this.driver.findElement(this.registrationAndSelfServicePortalAccessLink);
	}

	public WebElement getAllowPwdLink() {
		return this.driver.findElement(this.allowPwdLink);
	}

	public WebElement getAllowPwdForRegistrationLink() {
		return this.driver.findElement(this.allowPwdForRegistrationLink);
	}

	public WebElement getAllowAccessWhenPwdExpiredLink() {
		return this.driver.findElement(this.allowAccessWhenPwdExpiredLink);
	}

	public WebElement getAllowPwdResetWhenForgot() {
		return this.driver.findElement(this.allowPwdResetWhenForgotLink);
	}
	
	public WebElement getRegistrationLinkUriField() {
		return this.driver.findElement(this.registrationLinkUriField);
	}
		
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getVirtualSettingsLink() {
		return this.driver.findElement(this.virtualSettingsLink);
	}
	
	public WebElement getVirtualJsonbox() {
		return this.driver.findElement(this.virtualJsonBox);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core/organizationdefaultsettings/";
	}

}
