package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class InternalUsersDetailPage extends BasePage{

	By saveButton;
	By homeLink;
	By userIam;
	By inputCheckSuperUser;
	By authenticationTokenLink;
	By managementTokenLink;
	By userAccessTokenLink;
	By authenticationTokenInput;
	By managementTokenInput;
	By userAccessTokenInput;
	By groupsFrom;
	By groupsTo;
	By removeGroupLink;
	By addGroupLink;
	By removeAllGroupsLink;

	
	public InternalUsersDetailPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.saveButton					= By.name("_save");
		this.homeLink					= By.linkText("Home");
		this.userIam					= By.linkText("Users");
		this.inputCheckSuperUser		= By.id("id_is_superuser");
		//there are several "create access token" because for each call, the interface takes one at a time, since there is no ID or Value to which we can hold on.
		this.authenticationTokenLink	= By.linkText("Create Access Token");
		this.managementTokenLink		= By.linkText("Create Access Token");
		this.userAccessTokenLink		= By.linkText("Create Access Token");
		this.authenticationTokenInput	= By.id("id_accesstoken_set-0-token");
		this.managementTokenInput		= By.id("id_accesstoken_set-2-0-token");
		this.userAccessTokenInput		= By.id("id_accesstoken_set-3-0-token");
		this.groupsFrom                 = By.id("id_groups_from");
		this.groupsTo                   = By.id("id_groups_to");
		this.removeGroupLink            = By.id("id_groups_remove_link");
		this.addGroupLink               = By.id("id_groups_add_link");
		this.removeAllGroupsLink		= By.id("id_groups_remove_all_link");
		
	}
	
	public WebElement getUser(String userName){
	    By selectUser = By.linkText(userName);
	    return this.driver.findElement(selectUser);
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getHomeLink() {
		return this.driver.findElement(this.homeLink);
	}
	
	public WebElement getUserIam() {
		return this.driver.findElement(this.userIam);
	}
	
	public WebElement getInputCheckSuperUser() {
		return this.driver.findElement(this.inputCheckSuperUser);
	}
	
	public WebElement getAuthenticationTokenLink() {
		return this.driver.findElement(this.authenticationTokenLink);
	}
	
	public WebElement getManagementTokenLink() {
		return this.driver.findElement(this.managementTokenLink);
	}
	
	public WebElement getUserAccessTokenLink() {
		return this.driver.findElement(this.userAccessTokenLink);
	}
	
	public WebElement getAuthTokenInput() {
		return this.driver.findElement(this.authenticationTokenInput);
	}
	
	public WebElement getManageTokenInput() {
		return this.driver.findElement(this.managementTokenInput);
	}
	
	public WebElement getUserAccessTokenInput() {
		return this.driver.findElement(this.userAccessTokenInput);
	}
	
	public WebElement getRemoveGroupLink() {
		return this.driver.findElement(this.removeGroupLink);
	}
	
	public WebElement getGroupFrom() {
		return this.driver.findElement(this.groupsTo);
	}
	
	public WebElement getGroupto() {
		return this.driver.findElement(this.groupsFrom);
	}
	
	public WebElement getAddGroupLink() {
		return this.driver.findElement(this.addGroupLink);
	}
	
	public WebElement getRemoveAllGroupsLink() {
		return this.driver.findElement(this.removeAllGroupsLink);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/accounts/appuser/";
	}
}