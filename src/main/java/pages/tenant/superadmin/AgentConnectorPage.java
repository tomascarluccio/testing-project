package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;
import tests.tenant.API;

public class AgentConnectorPage extends BasePage {
	
    By addLink;
    By name;
    By webCertificateButton;
    By listItemName;
    By listItem;
    By saveButton;
    By saveAndContinueButton;
    By downloadLink;
    By statusImage;
    By statusIcon;
    By deleteCheckbox;
    By goButton;
    By imSureButton;
    By action;
    
	public AgentConnectorPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);                   
		
		this.addLink   		          	= By.linkText("Add agent connector");
		this.name      			      	= By.id("id_name");
		this.webCertificateButton     	= By.id("id_web_certificate");
		this.listItem                 	= By.xpath("//*[@id=\"result_list\"]/tbody/tr/th/a");
		this.listItemName             	= By.linkText("automation_agent");
		this.saveButton               	= By.cssSelector("input[value = 'Save']");
		this.saveAndContinueButton     	= By.cssSelector("input[value = 'Save and continue editing']");
		this.downloadLink             	= By.linkText("Download here");
		this.statusImage              	= By.xpath("//a[@href=\"/safewalk_agent/agentconnector/15/download/\"");					 
		this.statusIcon               	= By.xpath("//*[contains(@id, 'agent_connection')]/img");
		this.action         	    	= By.name("action");
		this.goButton       		    = By.xpath("//*[text()='Go']");
		this.deleteCheckbox             = By.name("_selected_action");
		
	}


	public WebElement getAddAgentConnectorLink() {
	    return this.driver.findElement(this.addLink);
	}

	public WebElement getWebCertificateButton() {
	    return this.driver.findElement(this.webCertificateButton);
	}

	public WebElement getName() {
	    return this.driver.findElement(this.name);
	}
	
	public WebElement getSaveButton() {
	    return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getDownloadLink() {
	    return this.driver.findElements(this.downloadLink).get(1);
	}
	
	public WebElement getStatusImage() {
	    return this.driver.findElement(this.statusImage);
	}
	
	public WebElement getStatusIcon() {
	    return this.driver.findElement(this.statusIcon);
	}
	
	public WebElement getDeleteCheckbox() {
		return this.driver.findElement(this.deleteCheckbox);
	}
	
	public WebElement getGoButton() {
		return this.driver.findElement(this.goButton);
	}
	
 	
	public WebElement getAction() {
		return  this.driver.findElement(action);
	}
	
	public WebElement getAgentConnectorNameLink(String name) {
		return  this.driver.findElement(By.linkText(name));
	}
	
	public WebElement getSavebutton() {
	    return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getSaveAndContinueButton() {
	    return this.driver.findElement(this.saveAndContinueButton);
	}
	
	public boolean existsAgentConnector(String connectorName) throws Exception {
		
		String con = API.listAgentConnectors().getContent();
		if(con.contains(connectorName))
			return true;
		
		return false;
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/safewalk_agent/agentconnector/";
	}
}