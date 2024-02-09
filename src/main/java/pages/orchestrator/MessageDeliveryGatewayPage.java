package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class MessageDeliveryGatewayPage extends BasePage{

	By type;
	By name;
	By smtpUser;
	By smtpPassword;
	By smtpHost;
	By smtpPort;
	By goButton;
	By download;
	By msgSent;
	By useTSL;
	By timeout;
	By addButton;
	By imSureButton;
	By yesPopup;
	By sentPopup;
	By testButton;
	By nextButton;
	By saveButton;
	By gtwInstanceName;
	By deleteCheckbox;
	By action;

	public MessageDeliveryGatewayPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.type					= By.id("id_gateway");
		this.smtpUser				= By.id("id_username");
		this.name					= By.id("id_name");
		this.goButton				= By.xpath("//*[text()='Go']");
		this.download				= By.id("download");
		this.msgSent				= By.name("use_encryption");
		this.smtpHost				= By.id("id_host");
		this.smtpPort 				= By.id("id_port");
		this.timeout				= By.name("id_timeout");
		this.useTSL					= By.id("id_use_tls");
		this.addButton				= By.linkText("Add Messages delivery gateway");
		this.imSureButton			= By.xpath("//*[@id=\"content\"]/form/div/input[4]");
		this.yesPopup    			= By.xpath("//*[text()='Yes']");
		this.sentPopup    			= By.xpath("//*[text()='Message sent. Please check your account.']");
		this.testButton   			= By.cssSelector("input[value = 'TEST']");
		this.saveButton   			= By.cssSelector("input[value = 'Save']");
		this.nextButton   			= By.cssSelector("input[value = 'Next']");
		this.smtpPassword  			= By.id("id_password");
		this.gtwInstanceName 		= By.linkText("SMTP");
		this.deleteCheckbox  		= By.className("action-select");
		this.action          		= By.name("action");
	}
	
	public WebElement getAction() {
		return  this.driver.findElement(action);
	}

	public WebElement getDeleteCheckbox() {
		return this.driver.findElement(deleteCheckbox);
	}

	public WebElement getType() {
		return this.driver.findElement(this.type);
	}

	public WebElement getName() {
		return this.driver.findElement(this.name);
	}

	public WebElement getSMTPPassword() {
		return this.driver.findElement(this.smtpPassword);
	}

	public WebElement getGoButton() {
		return this.driver.findElement(this.goButton);
	}

	public WebElement getDownload() {
		return this.driver.findElement(this.download);
	}

	public WebElement getMsgSent() {
		return this.driver.findElement(this.msgSent);
	}

	public WebElement getSMTPHost() {
		return this.driver.findElement(this.smtpHost);
	}

	public WebElement getSMTPPort() {
		return this.driver.findElement(this.smtpPort);
	}

	public WebElement getUseTSL() {
		return this.driver.findElement(this.useTSL);
	}

	public WebElement getTimeout() {
		return this.driver.findElement(this.timeout);
	}

	public WebElement getAddButton() {
		return this.driver.findElement(this.addButton);
	}

	public WebElement getImSureButton() {
		return this.driver.findElement(this.imSureButton);
	}

	public WebElement getYesPopup() {
		return this.driver.findElement(this.yesPopup);
	}

	public WebElement getSentPopup() {
		return this.driver.findElement(this.sentPopup);
	}

	public WebElement getTestButton() {
		return this.driver.findElement(this.testButton);
	}

	public WebElement getNextButton() {
		return this.driver.findElement(this.nextButton);
	}

	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}

	public WebElement getSMTPUser() {
		return this.driver.findElement(this.smtpUser);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/gateway_core/gatewayaccount/";
	}

}
	
