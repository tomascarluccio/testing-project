package pages.tenant.managementconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserPanel extends ManagementConsolePage {
	
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
	
	public UserPanel (WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.userMailLink					= By.cssSelector("input[name = 'db_email']");
		this.submitBtnLink					= By.xpath("//button[text()='SUBMIT']");
		this.radiusTestTabLink				= By.linkText("Radius Test");
		this.radiusTestPswLink				= By.name("password");
		this.radiusTestBtnTest				= By.xpath("//button[text()='TEST']");
		this.radiusTestResponseLinkText 	= By.xpath("//*[@id=\"mCSB_15_container\"]/div/div[4]/p");
		this.userGroupTabLink				= By.linkText("Groups");
		this.userTokensTabLink				= By.linkText("Tokens");
		this.virtualTokenBtn				= By.className("btn-virtual");
		this.confirmVirtualTokenBtn			= By.id("alertify-ok");
		this.mobileTokenBtn					= By.className("btn-mobile");
		this.mobileTokensListLink			= By.className("select2-default");
		this.mobileTokenLink 				= By.className("select2-result-label");
		this.submitMobileTokenBtn			= By.xpath("/html/body/div[1]/div/section[2]/div/div/div/div/div/div[3]/div/div[1]/div/div[5]/div/div/div/div[2]/div[2]/button[2]");
		this.registrationCodeTabLink		= By.linkText("Registration code");
		this.createRegistCodeBtn			= By.className("create-registration-token");
		this.sendRegistCodeBox				= By.name("send_registration_token");
		this.confirmCreateRegCodeBtn		= By.className("btn-primary");
		this.deleteTokenBtn					= By.cssSelector("button.btn.btn-danger.btn-circle.delete");
		this.confirmDeleteTokenBtn			= By.id("alertify-ok");
		this.minimizeUserPane				= By.id("minimize-user-panel");
		this.regCodeFrame 					= By.cssSelector("div.modal.fate.create-dialog.in");
		this.refreshButton					= By.className("refresh");

	}
	
	public WebElement getUserMailLink() {
		return this.driver.findElement(this.userMailLink);
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
	
	public By getRegCodeFrame() {
		return this.regCodeFrame;
	}
	
}
