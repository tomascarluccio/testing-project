package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class DashboardPage extends BasePage{

	By serverLicenseLink;
	By tokenLicencesLink;
	By settingsTab;
	By resourcesManagerLink;
	By troubleshootingPackageLink;
	By messageDeliveryGatewayLink;
	By customersLink;
	By manageCustomersLink;
	By licensesTab;
	By usersLink;
	By homeLink;
	By agentConnectorLink;
	By troubleshootingLogLink;
	By applianceVersionsLink;
	By systemBackupsLink;
	By logOutLink;
	

	public DashboardPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.tokenLicencesLink            =  By.linkText("Import licenses");	
		this.licensesTab                  =  By.id("ui-id-2");
		this.settingsTab                  =  By.id("ui-id-3");
		this.resourcesManagerLink         =  By.linkText("Resources Manager");
		this.messageDeliveryGatewayLink   =  By.linkText("Messages delivery gateways");
		this.customersLink                =  By.linkText("Customers");
		this.manageCustomersLink          =  By.linkText("Manage customers");
		this.usersLink                    =  By.linkText("Users");
		this.homeLink                     =  By.linkText("Home");
		this.agentConnectorLink           =  By.linkText("Agent connectors");
		this.troubleshootingLogLink       =  By.linkText("View");
		this.applianceVersionsLink        =  By.linkText("Safewalk appliance versions");
		this.systemBackupsLink			  =  By.linkText("System backups");
		this.logOutLink                   =  By.linkText("Log out");
		this.troubleshootingPackageLink   =  By.linkText("Create a troubleshooting package");
	
	}

	public WebElement getServerLinceseLink() {
		return this.driver.findElement(this.serverLicenseLink);
	}

	public WebElement getTokenLincencesLink() {
		return this.driver.findElement(this.tokenLicencesLink);
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
	
	public WebElement getTroubleshootingLogLink() {
		return this.driver.findElements(this.troubleshootingLogLink).get(0);
	}

	public WebElement getMessageDeliveryGatewayLink() {
		return this.driver.findElement(this.messageDeliveryGatewayLink);
	}

	public WebElement getLicensesTab() {
		return this.driver.findElement(this.licensesTab);
	}

	public WebElement getCustomersLink() {
		return this.driver.findElement(this.customersLink);
	}

	public WebElement getManageCustomersLink() {
		return this.driver.findElement(this.manageCustomersLink);
	}
	
	public WebElement getUsersLink() {
		return this.driver.findElement(this.usersLink);
	}

	public WebElement getHomeLink() {
		return this.driver.findElement(this.homeLink);
	}

	public WebElement getAgentConnectorLink(){
		return this.driver.findElement(this.agentConnectorLink);
	}
	
	public WebElement getApplianceVersionsLink(){
		return this.driver.findElement(this.applianceVersionsLink);
	}
	
	public WebElement getSystemBackupsLink(){
		return this.driver.findElement(this.systemBackupsLink);
	}

	public WebElement getLogOutLink(){
		return this.driver.findElement(this.logOutLink);
	}

	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/";
	}
}