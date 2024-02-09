package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ImportDataFilePage extends BasePage{

	By fileInput;
	By replaceCheck;
	By yesPopup;
	By uploadButton;
	By confirmAlert;
	By submitButton;
	By confirmButton;

	public ImportDataFilePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.fileInput    		=  By.xpath("//input[@type='file']");
		this.replaceCheck 		=  By.id("id_replace");
		this.yesPopup     		=  By.xpath("//*[text() = 'Yes']");
		this.uploadButton 		=  By.cssSelector("input[value = 'Upload']");
		this.confirmAlert 		=  By.xpath("//*[text() = 'Import physical tokens confirmation']");
		this.submitButton       =  By.xpath("//input[@type='submit']");
		this.confirmButton      =  By.xpath("//input[@type='submit']");
	}

	public WebElement getFileInput() {
		return this.driver.findElement(this.fileInput);
	}

	public WebElement getReplaceCheck() {
		return this.driver.findElement(this.replaceCheck);
	}

	public WebElement getYesPopup() {
		return this.driver.findElement(this.yesPopup);
	}

	public WebElement getUploadButton() {
		return this.driver.findElement(this.uploadButton);
	}
	
	public WebElement getSubmitButton() {
		return this.driver.findElement(this.submitButton);
	}

	public WebElement getConfirmAlert() {
		return this.driver.findElement(this.confirmAlert);
	}
	
	public WebElement getConfirmButton() {
		return this.driver.findElement(this.confirmButton);
	}
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core_tools/import_physical_tokens/";
	}

}