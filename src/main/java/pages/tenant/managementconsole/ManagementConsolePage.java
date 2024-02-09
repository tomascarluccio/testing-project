package pages.tenant.managementconsole;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ManagementConsolePage extends BasePage{
	
	By usersTabLink;
	By reportsTabLink;
	By appVersion;
	By serverTime;
	By time;
	By transLogTab;
	By usernameInput;
	By passwordInput;
	By loginButton;
	By internalUsersLink;
	By searchBarLink;
	By usersList;
	By categoriesUsersList;
	By lockedUsersBox;
	By userLoggedName;
	By logoutBtn;
	By alertyfiOk;
	By userMailLink;
	By submitBtnLink;
	By radiusTestTabLink;
	By radiusTestPswLink;
	By radiusTestBtnTest;
	By radiusTestResponseLinkText;
	By userGroupTabLink;
	By userTokensTabLink;
	By virtualTokenBtn;
	By confirmVirtualTokenBtn;
	By mobileTokenBtn;
	By mobileTokensListLink;
	By mobileTokenLink;
	By submitMobileTokenBtn;
	By registrationCodeTabLink;
	By createRegistCodeBtn;
	By sendRegistCodeBox;
	By confirmCreateRegCodeBtn;
	By deleteTokenBtn;
	By confirmDeleteTokenBtn;
	By minimizeUserPane;
	By regCodeFrame;
	By refreshButton;
	By mobileNumber;
	By personalInformationAlertMessage;
	
	
	public ManagementConsolePage (WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.usersTabLink				= By.linkText("USERS");
		this.reportsTabLink				= By.linkText("REPORTS");
		this.appVersion					= By.className("app_version");
		this.serverTime					= By.className("server_time");
		this.time						= By.className("time");	
		this.transLogTab				= By.linkText("TRANSACTION LOG");
		this.usernameInput 				= By.name("initial-username");	
		this.passwordInput 				= By.name("initial-password");	
		this.loginButton   				= By.cssSelector("input[value = 'LOGIN']");
		this.internalUsersLink			= By.partialLinkText("Internal Users");
		this.searchBarLink				= By.cssSelector("input[name = 'q']");
		this.usersList					= By.cssSelector("ul#tables-tab-headers li");
		this.categoriesUsersList		= By.xpath("/html/body/div/div/div/ul");
		this.lockedUsersBox				= By.xpath("/html/body/div/div/section[1]/div[1]/div[1]/div[1]/form[2]/label/span");
		this.userLoggedName				= By.className("name_usuario");
		this.logoutBtn					= By.xpath("/html/body/header/div[2]/div/ul/li/a");
		this.alertyfiOk					= By.xpath("//*[text()='OK']");
		this.userMailLink					= By.name("ldap_email");
		this.mobileNumber					= By.name("ldap_mobile_phone");
		this.submitBtnLink					= By.xpath("//button[text()='SUBMIT']");
		this.radiusTestTabLink				= By.linkText("Radius Test");
		this.radiusTestPswLink				= By.name("password");
		this.radiusTestBtnTest				= By.xpath("//button[text()='TEST']");
		this.radiusTestResponseLinkText 	= By.xpath("//*[contains(text(), 'code\": \"ACCESS_ALLOWED\"')]");
		this.userGroupTabLink				= By.linkText("Groups");
		this.userTokensTabLink				= By.linkText("Tokens");
		this.virtualTokenBtn				= By.className("btn-virtual");
		this.mobileTokenBtn					= By.className("btn-mobile");
		this.mobileTokensListLink			= By.className("select2-default");
		this.mobileTokenLink 				= By.className("select2-result-label");
		this.submitMobileTokenBtn			= By.xpath("/html/body/div[1]/div/section[2]/div/div/div/div/div/div[3]/div/div[1]/div/div[5]/div/div/div/div[2]/div[2]/button[2]");
		this.registrationCodeTabLink		= By.linkText("Registration code");
		this.createRegistCodeBtn			= By.className("create-registration-token");
		this.sendRegistCodeBox				= By.name("send_registration_token");
		this.confirmCreateRegCodeBtn		= By.className("btn-primary");
		this.deleteTokenBtn					= By.cssSelector("button.btn.btn-danger.btn-circle.delete");
		this.minimizeUserPane				= By.id("minimize-user-panel");
		this.regCodeFrame 					= By.cssSelector("div.modal.fate.create-dialog.in");
		this.refreshButton					= By.className("refresh");
		this.personalInformationAlertMessage = By.xpath("//*[@id=\"alertify-logs\"]/article");
	

	}
	
	public WebElement getUsersTabLink () {
		return this.driver.findElement(this.usersTabLink);
	}
	
	public WebElement getAppVersion() {
		return this.driver.findElement(this.appVersion);
	}

	public WebElement getServerTime() {
		return this.driver.findElement(this.serverTime);
	}
	

	public WebElement getTime() {
		return this.driver.findElement(this.time);
	}
	
	public WebElement getTransLogTab () {
		return this.driver.findElement(this.transLogTab);
	}
	

	public WebElement getUsernameInput() {
		return this.driver.findElement(this.usernameInput);
	}

	public WebElement getPasswordInput() {
		return this.driver.findElement(this.passwordInput);
	}

	public WebElement getLoginButton() {
		return this.driver.findElement(this.loginButton);
	}
	
	public WebElement getInternalUsersLink () {
		return this.driver.findElement(this.internalUsersLink);
	}

	public WebElement getLDAPUsersLink (String ldapName) {
		return this.driver.findElement(By.partialLinkText(ldapName));
	}
	
	public WebElement getLdapUsersLink () {
		return this.driver.findElement(this.internalUsersLink);
	}
	
	public WebElement getSearchBarLink() {
		return this.driver.findElement(this.searchBarLink);
	}
	
	public WebElement getUsernameLink(String username) {
		return this.driver.findElement(By.xpath("//tr[@class='row-table']/td[1]"));
	}

	public WebElement getUsernameLinkInternal(String username) {
		return this.driver.findElement(By.xpath("//tr[@class='row-table']/td[1]"));
	}

	public List<WebElement> getUsersList() {
		return this.driver.findElements(this.usersList);
	}
		
	public List<WebElement> getCategoriesUsersList() {
		return this.driver.findElements(this.categoriesUsersList);
	}
	
	public WebElement getLockedUsersBox() {
		return this.driver.findElement(this.lockedUsersBox);
	}
	
	public WebElement getLogoutBtn() {
		return this.driver.findElement(this.logoutBtn);
	}
	
	public WebElement getUserLoggedName() {
		return this.driver.findElement(this.userLoggedName);
	}
	
	public WebElement getAlertifyOk() {
		return this.driver.findElement(this.alertyfiOk);
	}
	

	public WebElement getUserMailLink() {
		return this.driver.findElement(this.userMailLink);
	}
	
	public WebElement getUserMobileLink() {
		return this.driver.findElement(this.mobileNumber);
	}
	
	
	public WebElement getSubmitBtnLink() {
		return this.driver.findElement(this.submitBtnLink);
	}
	
	public WebElement getRadiusTestTabLink() {
		return this.driver.findElement(this.radiusTestTabLink);
	}
	
	public WebElement getRadiusTestPswLink() {
		return this.driver.findElement(this.radiusTestPswLink);
	}
	
	public WebElement getRadiusTestBtnTest() {
		return this.driver.findElement(this.radiusTestBtnTest);
	}
	
	public WebElement getRadiusResponseLinkText() {
		return this.driver.findElement(this.radiusTestResponseLinkText);
	}
	
	public WebElement getUserGroupTabLink() {
		return this.driver.findElement(this.userGroupTabLink);
	}
	
	public WebElement getUserTokensTabLink() {
		return this.driver.findElement(this.userTokensTabLink);
	}
	
	public WebElement getVirtualTokenBtn() {
		return this.driver.findElement(this.virtualTokenBtn);
	}
	
	public WebElement getConfirmVirtualTokenBtn() {
		return this.driver.findElement(this.confirmVirtualTokenBtn);
	}
	
	public WebElement getMobileTokenBtn() {
		return this.driver.findElement(this.mobileTokenBtn);
	}
	
	public WebElement getMobileTokensListLink() {
		return this.driver.findElement(this.mobileTokensListLink);
	}
	
	public WebElement getMobileTokenLink (int tokenType) {
		return this.driver.findElement(this.mobileTokenLink);
	}
	
	public WebElement submitMobileTokenBtn() {
		return this.driver.findElement(this.submitMobileTokenBtn);
	}
	
	public WebElement getRegistrationCodeTabLink() {
		return this.driver.findElement(this.registrationCodeTabLink);
	}
	
	public WebElement getCreateRegistCodeBtn() {
		return this.driver.findElement(this.createRegistCodeBtn);
	}
	
	public WebElement getSendRegistCodeBox() {
		return this.driver.findElement(this.sendRegistCodeBox);
	}
	
	public WebElement getConfirmCreateRegCodeBtn() {
		return this.driver.findElement(this.confirmCreateRegCodeBtn);
	}
	
	public WebElement getDeleteTokenBtn() {
		return this.driver.findElement(this.deleteTokenBtn);
	}
	
	public WebElement getConfirmDeleteTokenBtn() {
		return this.driver.findElement(this.confirmDeleteTokenBtn);
	}
	
	public WebElement getMinimizeUserPane() {
		return this.driver.findElement(this.minimizeUserPane);
	}
	
	public WebElement getRefreshButton() {
		return this.driver.findElement(this.refreshButton);
	}
	
	public WebElement getReportsTab() {
		return this.driver.findElement(this.reportsTabLink);
	}
	public By getRegCodeFrame() {
		return this.regCodeFrame;
	}

	public WebElement getPersonalInformationAlertMessage() {
		return this.driver.findElement(this.personalInformationAlertMessage);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnManagementConsolePort.toString() + "/";
	}
	
}