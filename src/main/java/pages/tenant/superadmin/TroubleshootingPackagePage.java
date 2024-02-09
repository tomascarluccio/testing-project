package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;


public class TroubleshootingPackagePage extends BasePage{

	By rootPassword;
	By descriptionField;
	By countryTextField;
	By downloadButton;
	By useEncryptionCheckbox;
	
	public TroubleshootingPackagePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.rootPassword                        =  By.id("id_system_password");
		this.descriptionField                    =  By.id("id_description");	
		this.countryTextField                    =  By.id("id_country");
		this.downloadButton  	              	 =  By.id("download");
		this.useEncryptionCheckbox               =  By.name("use_encryption");
	}

	public WebElement getRootPassword() {
		return this.driver.findElement(this.rootPassword);
	}

	public WebElement getDescriptionField() {
		return this.driver.findElement(this.descriptionField);
	}

	public WebElement getCountryTextField() {
		return this.driver.findElement(this.countryTextField);
	}

	public WebElement getDownloadButton() {
		return this.driver.findElement(this.downloadButton);
	}

	public WebElement getUseEncryptionCheckBox() {
		return this.driver.findElement(this.useEncryptionCheckbox);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core_tools/prepare_logs/";
	}
	
}