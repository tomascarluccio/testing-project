package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class LoadSystemPage extends BasePage{

	By systemUpdate;
	By loadSystemData;
	By buttonChooseFile;
	By inputPassword;
	By inputOrganizationName;
	By buttonImport;
	By buttonLoad;
	By buttonOk;
	By successPopUp;
	
	public LoadSystemPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.systemUpdate				= By.id("ui-id-8");
		this.loadSystemData				= By.xpath("//*[@id=\"module_6_2\"]/div/ul/li[2]/a");
		this.buttonChooseFile			= By.id("id_safewalk_backup");
		this.inputPassword				= By.id("id_backup_password");
		this.inputOrganizationName		= By.id("id_organization_name");
		this.buttonImport				= By.xpath("//*[@id=\"module_\"]/div/form/p/input");
		this.buttonLoad					= By.xpath("/html/body/div[2]/div[2]/div/div[4]/div[2]/button[1]");
		this.buttonOk					= By.xpath("/html/body/div[3]/div[2]/div/div[4]/div[2]/button");
		this.successPopUp				= By.xpath("/html/body/div[3]/div[2]/div");
	}
	
	public WebElement getSystemUpdate() {
		return this.driver.findElement(this.systemUpdate);
	}
	
	public WebElement getLoadSystemData() {
		return this.driver.findElement(this.loadSystemData);
	}
	
	public WebElement getButtonChooseFile() {
		return this.driver.findElement(this.buttonChooseFile);
	}
	
	public WebElement getInputPassword() {
		return this.driver.findElement(this.inputPassword);
	}
	
	public WebElement getInputOrganizationName() {
		return this.driver.findElement(this.inputOrganizationName);
	}
	
	public WebElement getButtonImport() {
		return this.driver.findElement(this.buttonImport);
	}
	
	public WebElement getButtonLoad() {
		return this.driver.findElement(this.buttonLoad);
	}
	
	public WebElement getButtonOk() {
		return this.driver.findElement(this.buttonOk);
	}
	
	public By getSuccessPopUp() {
		return this.successPopUp;
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/backup_restore/load_system_data/";
	}
	
}
