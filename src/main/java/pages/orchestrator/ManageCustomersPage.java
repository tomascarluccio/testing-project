package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ManageCustomersPage extends BasePage{

	By addLink;
	By customerInputCheck;
	By action;
	By index;
	By customerNameLink;
	By actionResultMessage;
	By restartButton;
	By runningStatus;
	By migrateButton;
	By displayedVersion;
	By rollbackButton;
	By applianceVersion;
	

	public ManageCustomersPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.addLink             			=  By.linkText("Add customer");
		this.action              			=  By.name("action");
		this.index 							=  By.name("index");
		this.customerNameLink    			=  By.linkText(Config.tnName);
		this.actionResultMessage 			=  By.xpath("//*[@id=\"container\"]/ul/li");
		this.applianceVersion    			=  By.name("version");
		this.rollbackButton      			=  By.xpath("//*[@id='customer_form']/div/fieldset[4]/table/tbody/tr[1]/td[3]/button");
		
	}

	public WebElement getAddLink() {
		return this.driver.findElement(this.addLink);
	}

	public WebElement getAction() {
		return this.driver.findElement(this.action);
	}

	public WebElement getIndex() {
		return this.driver.findElement(this.index);
	}

	public WebElement getCustomerNameLink() {
	    return this.driver.findElement(this.customerNameLink);
	}

	public WebElement getActionResultMessage() {
		return this.driver.findElement(this.actionResultMessage);
	}

	public WebElement getAppilanceVersion() {
		return this.driver.findElement(this.applianceVersion);
	}
	
	public WebElement getRollbackButton() {
		return this.driver.findElement(this.rollbackButton);
	}
	
	public WebElement getCustomerInputCheck(String tenantName) {
		return this.driver.findElement(By.xpath("//*[@id='result_list']/tbody//tr[td//text()[contains(., '"+tenantName+"')]]/td[1]/input"));
	}
	
	public WebElement getCustomerAppilanceVersion(String tenantName){
		return this.driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/table/tbody/tr[td//text()[contains(., '"+tenantName+"')]]/td[2]/span"));
	}
	
	public WebElement getCustomerStatus(String tenantName) {
		return this.driver.findElement(By.xpath("//*[@id='result_list']/tbody//tr[td//text()[contains(., '"+tenantName+"')]]/td[5]/span"));
	}
	
	public WebElement getMigrateButton(String tenantName) {
		return this.driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/table/tbody/tr[td//text()[contains(., '"+tenantName+"')]]/td[6]/a"));
	}

	
	public WebElement getStopButton(String tenantName) {
		return this.driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/table/tbody/tr[td//text()[contains(., '"+tenantName+"')]]/td[7]/a"));
	}
	
	public WebElement getStartButton(String tenantName) {
		return this.driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/table/tbody/tr[td//text()[contains(., '"+tenantName+"')]]/td[7]/a"));
	}

	public String getResourceLevel(String selectedLevel) {
		if(selectedLevel.equals(Config.resourceLevelLow))
			return Config.resourceLevelMed;
		else
			return Config.resourceLevelLow;
	}
	
	public boolean existsCustomer(String tenantName) {
		try {
		int size = driver.findElements(By.linkText(tenantName)).size();
		if(size > 0)
			return true;
		}catch(Exception e) {
			
			return false;
		}	
		
		return false;
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/multitenancy/customer/";
	}

}