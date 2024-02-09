package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ApplianceVersionsPage extends BasePage{

	By updateButton;
	By lastApplianceVersion;

  
	public ApplianceVersionsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
 
		this.updateButton               	= By.xpath("//*[@id=\"content-main\"]/ul/li/a");
		this.lastApplianceVersion           = By.xpath("//*[@id=\"result_list\"]/tbody/tr[1]/th/a");			
	}

	public WebElement getUpdateButton() {
	    return this.driver.findElement(this.updateButton);
	}

	public WebElement getLastApplianceVersion() {
		return this.driver.findElement(this.lastApplianceVersion);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/multitenancy/safewalkapplianceversion/";
	}

}