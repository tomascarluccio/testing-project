package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;


public class OrganizationIdentityPage extends BasePage {
	
	By nameInput;
	By logoInput;
	By faviconInput;
	By saveButoon;

	
	public OrganizationIdentityPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.nameInput 			= By.id("id_name");
		this.logoInput 			= By.id("id_logo");
		this.faviconInput 		= By.id("id_favicon");
		this.saveButoon 		= By.className("default");
	}
	
	public WebElement getNameInput() {
		return this.driver.findElement(this.nameInput);
	}
	
	public WebElement getLogoInput() {
		return this.driver.findElement(this.logoInput);
	}
	
	public WebElement getFaviconInput() {
		return this.driver.findElement(this.faviconInput);
	}
	
	public WebElement getSaveButoon() {
		return this.driver.findElement(this.saveButoon);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core/organizationidentity/";
	}
}
