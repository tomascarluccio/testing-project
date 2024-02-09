package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;
import tests.tenant.API;
import utils.Response;

public class RadiusClientPage extends BasePage {
	By addRadiusClientLink;
	By sharedSecret;
	By name;
	By address;
	By gateway;
	By deleteCheckbox;
	By imSureButton;
	By goButton;
	By action;
	By saveButton;
	
	public RadiusClientPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	
		this.addRadiusClientLink            = By.linkText("Add radius client");
		this.sharedSecret                   = By.id("id_secret");
		this.gateway                        = By.id("id_gateway");
		this.address                        = By.id("id_address");
		this.name                           = By.id("id_name");
		this.deleteCheckbox 	      	    = By.name("_selected_action");
		this.action                         = By.name("action");
		this.imSureButton   	         	= By.xpath("//*[@id=\"content\"]/form/div/input[4]");
		this.goButton       		        = By.xpath("//*[text()='Go']");
		this.saveButton     		        = By.cssSelector("input[value = 'Save']");
		
		}
	
	public WebElement getRadiusClientAddress() {
		return this.driver.findElement(this.address);
	}
	
	public WebElement getAddRadiusClientLink() {
		return this.driver.findElement(this.addRadiusClientLink);
	}
	
	public WebElement getRadiusClientName() {
		return this.driver.findElement(this.name);
	}
	
	public WebElement getRadiusClientSharedSecret() {
		return this.driver.findElement(this.sharedSecret);
	}
	
	public WebElement getRadiusClientGateway() {
		return this.driver.findElement(this.gateway);
	}
	
	public WebElement getAction() {
		return  this.driver.findElement(action);
	}
	
	public WebElement getDeleteCheckbox() {
		return this.driver.findElement(deleteCheckbox);
	}
	
	public WebElement getGoButton() {
		return this.driver.findElement(this.goButton);
	}
	
	public WebElement getImSureButton() {
		return this.driver.findElement(this.imSureButton);
	}
	
	public WebElement getSavebutton() {
	    return this.driver.findElement(this.saveButton);
	}

	
	public boolean existsRadiusClient(String radiusName) throws Exception{
		String exists = API.listRadiusClient().getContent();
		if(exists.contains(radiusName))
			return true;
		
		return false;
	}

	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core_tools/about/";
	}
}
