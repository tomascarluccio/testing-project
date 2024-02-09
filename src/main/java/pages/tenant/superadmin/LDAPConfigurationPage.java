package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;
import tests.tenant.API;

public class LDAPConfigurationPage extends BasePage {
	
    By addLink;
    By name;
    By server;
    By bindDn;
    By bindPassword;
    By rootDn;
    By userSearch;
    By testConnection;
    By signOut;
    By ldapConfLink;
    By ldapType;
    By domain;
    By okAlert;
    By saveButton;
    By gateway;
    By imSureButton;
    By goButton;
    By deleteCheckbox;
    By action;
    By linkName;
	
	public LDAPConfigurationPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);                   
		
		this.addLink   				= By.linkText("Add LDAP configuration");
		this.name      				= By.id("id_name");
		this.server    				= By.id("id_server");
		this.bindDn    				= By.id("id_bind_dn");
		this.bindPassword 			= By.id("id_bind_password");
		this.rootDn       			= By.id("id_root_dn");
		this.userSearch   			= By.id("id_user_search");
		this.testConnection 		= By.id("test-conection");
		this.ldapConfLink   		= By.linkText("LDAP configurations");
		this.ldapType       		= By.id("id_ldap_type");
		this.domain         		= By.id("id_domain");
		this.okAlert        		= By.xpath("//*[text() = 'OK']");
		this.saveButton     		= By.cssSelector("input[value = 'Save']");
		this.gateway        		= By.id("id_gateway");
		this.action         		= By.name("action");
		this.deleteCheckbox 		= By.className("action-select");
		this.imSureButton   		= By.xpath("//*[@id=\"content\"]/form/div/input[4]");
		this.goButton       		= By.xpath("//*[text()='Go']");
	}


	public WebElement getAddLDAPLink() {
	    return this.driver.findElement(this.addLink);
	}

	public WebElement gettLDAPConfigurationLink() {
	    return this.driver.findElement(this.ldapConfLink);
	}
	
	public WebElement getTestConnectionButton() {
	    return this.driver.findElement(this.testConnection);
	}
	
	public WebElement getUserSearch() {
	    return this.driver.findElement(this.userSearch);
	}
	
	public WebElement getRootDn() {
	    return this.driver.findElement(this.rootDn);
	}
	
	public WebElement getBindPassword() {
	    return this.driver.findElement(this.bindPassword);
	}
	
	public WebElement getBindDn() {
	    return this.driver.findElement(this.bindDn);
	}
	
	public WebElement getServer() {
	    return this.driver.findElement(this.server);
	}
	
	public WebElement getName() {
	    return this.driver.findElement(this.name);
	}
	
	public WebElement getLDAPType() {
	    return this.driver.findElement(this.ldapType);
	}
	
	public WebElement getDomain() {
	    return this.driver.findElement(this.domain);
	}
	
	public WebElement getOkAlert() {
	    return this.driver.findElement(this.okAlert);
	}
	
	public WebElement getSavebutton() {
	    return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getGateway() {
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
	
	public WebElement getLDAPNameLink(String ldapName) {
		return this.driver.findElement(By.linkText(ldapName));
	}
	
	public WebElement getImSureButton() {
		return this.driver.findElement(this.imSureButton);
	}
	
	public boolean existsLDAPConfiguration(String ldapname) throws Exception{
		String exisits = API.listLDAPConfigurations().getContent();
		if(exisits.contains(ldapname))
			return true;
		
		return false;
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core/ldapconfiguration/";
	}
}