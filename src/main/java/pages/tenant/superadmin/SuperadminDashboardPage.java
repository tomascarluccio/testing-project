package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class SuperadminDashboardPage extends BasePage{

	By serverLicenseLink;
	By tokenLicencesLink;
	By physicalTokenDataLink;
	By settingsTab;
	By resourcesManagerLink;
	By troubleshootingPackageLink;
	By messageDeliveryGatewayLink;
	By licensesTab;
	By usersLink;
	By homeLink;
	By aboutLink;
	By settingsTabIam;
	By settingsTabLDAP;
	By sspLink;
	By ldapLink;
	By signOutLink;
	By iamSettingsTab;
	By agentConnectorLink;
	By troubleshootingLogLink;
	By defaultSettingsLink;
	By authenticationTokenLink;
	By managementTokenLink;
	By sspTokenLink;
	By SAMLIntegrations;
	By gatewayConfigurations;
	By organizationIdentityLink;
	By documentationLink;
  
	public SuperadminDashboardPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.tokenLicencesLink            =  By.linkText("Import licenses");	
		this.physicalTokenDataLink    	  =  By.linkText("Import physical tokens data");
		this.licensesTab                  =  By.id("ui-id-2");
		this.settingsTab                  =  By.id("ui-id-3");
		this.iamSettingsTab               =  By.id("ui-id-6");
		this.troubleshootingPackageLink   =  By.linkText("Create a troubleshooting package");
		this.messageDeliveryGatewayLink   =  By.linkText("Messages delivery gateways");
		this.usersLink                    =  By.linkText("Users");
		this.homeLink                     =  By.linkText("Home");
		this.aboutLink 					  =  By.linkText("About");
		this.settingsTabIam			      =  By.xpath("//*[@id=\"ui-id-6\"]");
		this.settingsTabLDAP 			  =  By.xpath("//*[@id=\"module_3_3\"]/div/ul/li/a");
		this.sspLink                      =  By.linkText("Self service customization");
		this.ldapLink                     =  By.linkText("LDAP configurations");
		this.signOutLink                  =  By.linkText("Sign Out");
		this.agentConnectorLink           =  By.linkText("Agent connectors");
		this.troubleshootingLogLink 	  =  By.linkText("Troubleshooting log");
		this.authenticationTokenLink	  =  By.xpath("//*[@id=\"module_8\"]/div/ul[1]/li[1]/ul/li");
		this.managementTokenLink		  =  By.xpath("//*[@id=\"module_8\"]/div/ul[1]/li[2]/ul/li");
		this.sspTokenLink			      =  By.xpath("//*[@id=\"module_8\"]/div/ul[1]/li[3]/ul/li");
		this.defaultSettingsLink		  =  By.linkText("Default settings");
		this.SAMLIntegrations		  	  =  By.linkText("SAML integrations");	
		this.gatewayConfigurations		  =	 By.linkText("Safewalk gateway configurations");
		this.organizationIdentityLink	  =	 By.linkText("Organization Identity");
		this.documentationLink			  =	 By.linkText("Documentation");
	}

	
	public WebElement getServerLinceseLink() {
		return this.driver.findElement(this.serverLicenseLink);
	}

	public WebElement getTokenLincencesLink() {
		return this.driver.findElement(this.tokenLicencesLink);
	}
	
	public WebElement getPhysicalTokenDataLink() {
		return this.driver.findElement(this.physicalTokenDataLink);
	}

	public WebElement getSettingsTab() {
		return this.driver.findElement(this.settingsTab);
	}

	public WebElement getResourcesManagerLink() {
		return this.driver.findElement(this.resourcesManagerLink);
	}

	public WebElement getTroubleshootingPackageLink() {
		return this.driver.findElement(this.troubleshootingPackageLink);
	}

	public WebElement getMessageDeliveryGatewayLink() {
		return this.driver.findElement(this.messageDeliveryGatewayLink);
	}

	public WebElement getLicensesTab() {
		return this.driver.findElement(this.licensesTab);
	}
	
	public WebElement getUsersLink() {
		return this.driver.findElement(this.usersLink);
	}

	public WebElement getHomeLink() {
		return this.driver.findElement(this.homeLink);
	}

	public WebElement getAboutLink() {
		return this.driver.findElement(this.aboutLink);
	}
	
	public WebElement getSettingsIam() {
		return this.driver.findElement(this.settingsTabIam);
	}
	public WebElement getSettingsTabLDAP() {
		return this.driver.findElement(this.settingsTabLDAP);
	}
  
	public WebElement getSSPLink() {
		return this.driver.findElement(this.sspLink);
	}
	
	public WebElement getLDAPLink() {
		return this.driver.findElement(this.ldapLink);
	}

	public WebElement getSignOutLink() {
		return this.driver.findElement(this.signOutLink);
	}
	
	public WebElement getIamSettingsTab() {
		return this.driver.findElement(this.iamSettingsTab);
	}
	
	public WebElement getAgentConnectorLink() {
		return this.driver.findElement(this.agentConnectorLink);
	}
	
	public WebElement getTroubleshootingLogLink() {
		return this.driver.findElement(this.troubleshootingLogLink);
	}
	
	public WebElement getDefaultSettingsLink() {
		return this.driver.findElement(this.defaultSettingsLink);
	}
	
	public WebElement getAuthTokenLink() {
		return this.driver.findElement(this.authenticationTokenLink);
	}
	
	public WebElement getManagementTokenLink() {
		return this.driver.findElement(this.managementTokenLink);
	}
	
	public WebElement getSSPTokenLink() {
		return this.driver.findElement(this.sspTokenLink);
	}
	
	public WebElement getSAMLIntegrationsLink() {
		return this.driver.findElement(this.SAMLIntegrations);
	}
	
	public WebElement getGatewayConfigurations() {
		return this.driver.findElement(this.gatewayConfigurations);
	}
	
	public WebElement getOrganizationIdentityLink() {
		return this.driver.findElement(this.organizationIdentityLink);
	}
	
	public WebElement getDocumentationLink() {
		return this.driver.findElement(this.documentationLink);
	}


	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/";
	}
}