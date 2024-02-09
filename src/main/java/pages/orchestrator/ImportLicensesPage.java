package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ImportLicensesPage extends BasePage{

	By fileInput;
	By replaceCheck;
	By yesPopup;
	By uploadButton;
	By confirmAlert;

	public ImportLicensesPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.fileInput    				=  By.xpath("//input[@type='file']");
		this.replaceCheck 				=  By.id("id_replace");
		this.yesPopup     				=  By.xpath("//*[text() = 'Yes']");
		this.uploadButton 				=  By.cssSelector("input[value = 'Upload']");
		this.confirmAlert 				=  By.xpath("//*[text() = 'Import licenses confirmation']");

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

	public WebElement getConfirmAlert() {
		return this.driver.findElement(this.confirmAlert);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/core_tools/import_licenses/";
	}

}