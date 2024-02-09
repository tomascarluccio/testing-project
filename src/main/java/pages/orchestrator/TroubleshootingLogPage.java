package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class TroubleshootingLogPage extends BasePage{

	
	By enableButton;
	By disableButton;
	By okButton;
	By paginator;
	By lastLogUser;
	By lastLogApi;
	By warningMessage;
	
	public TroubleshootingLogPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.enableButton				= By.className("enable_link");
		this.disableButton				= By.className("disable_link");
		this.okButton					= By.className("ajs-ok");
		this.paginator   				= By.className("paginator");
		this.lastLogUser                = By.xpath("//*[@id='result_list']/tbody/tr[1]/td[7]");
		this.lastLogApi                 = By.xpath("//*[@id=\"result_list\"]/tbody/tr[1]/td[5]");
		this.warningMessage             = By.className("success");
	}
	
	public WebElement getEnableButton() {
		return this.driver.findElement(this.enableButton);
	}

	public WebElement getDisableButton() {
		return this.driver.findElement(this.disableButton);
	}

	public WebElement getOkButton() {
		return this.driver.findElement(this.okButton);
	}
	
	public WebElement getNumberOfLogs() {
		return this.driver.findElement(this.paginator);
	}
	
	public WebElement getLastLogUser() {
		return this.driver.findElement(this.lastLogUser);
	}
	
	public WebElement getLastLogApi() {
		return this.driver.findElement(this.lastLogApi);
	}
		
	public WebElement getWarningMessage() {
		return this.driver.findElement(this.warningMessage);
	}
	
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/dblogging/logentry/";
	}
}