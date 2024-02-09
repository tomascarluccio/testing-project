package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;


public class AboutPage extends BasePage {
	
	By applianceVersion;
	By serverVersion;
	By managementVersion;
	By radiusVersion;
	By rabbitmqVersion;
	By memcachedVersion;
	By safewalkGatewayVersion;
	
	public AboutPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	
		this.applianceVersion 				= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[3]/td[2]");
		this.serverVersion 					= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[4]/td[2]");
		this.managementVersion 				= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[5]/td[2]");
		this.radiusVersion 					= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[6]/td[2]");
		this.rabbitmqVersion 				= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[7]/td[2]");
		this.memcachedVersion 				= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[8]/td[2]");
		this.safewalkGatewayVersion 		= By.xpath("//*[@id=\"module_\"]/div/table/tbody/tr[9]/td[2]");
	}
	
	public WebElement getApplianceVersion() {
		return this.driver.findElement(this.applianceVersion);
	}
	
	public WebElement getServerVersion() {
		return this.driver.findElement(this.serverVersion);
	}
	
	public WebElement getManagementVersion() {
		return this.driver.findElement(this.managementVersion);
	}
	
	public WebElement getRadiusVersion() {
		return this.driver.findElement(this.radiusVersion);
	}
	
	public WebElement getRabbitmqVersion() {
		return this.driver.findElement(this.rabbitmqVersion);
	}
	
	public WebElement getMemcachedVersion() {
		return this.driver.findElement(this.memcachedVersion);
	}
	
	public WebElement getSafewalkGatewayVersion() {
		return this.driver.findElement(this.safewalkGatewayVersion);
	}

	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core_tools/about/";
	}

}
