package pages.tenant.selfserviceportal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class SelfServicePortalPage extends BasePage{

	By colorsLink;
	By translationsLink;
	By gralSettingsLink;
	By addTranslationLink;
	By languageSelect;
	By saveButton;
	By okAlert;
	By frLink;
	By enLink;
	By itLink;
	By english;
	By translationStr1;
	By translationStr2;
	By translationStr3;
	By translationStr4;
	By homeLink;
	By deleteButton;
	By submitButton;
	By fastAuthLicenseStatus;
	By totpLicenseStatus;
	By fastAuthLicenseType;
	By totpLicenseType;
	By consoleLogin;
	By consolePassword;
	By loginButton;
	By totpModalRegisterButton;
	By fastAuthModalRegisterButton;
	By fastAuthModalExitButton;
	By fastAuthModalQrCode;
	By totpModalQrButton;
	By totpModalNextButton;
	By totpModalQrCode;
	By logOutButton;
	By pushWaitText;
	By clickHereLink;
	By changePassword1;
	By changePassword2;
	By sendChangePasswordButton;
	By clickHereLoginAgain;
	By forgotPasswordLink;
	By usernameForgotPassword;
	By idCaptcha;
	By forgotLoginBtn;
	By classCaptcha;
	By backLogin;
    By passwordChangeSuccessMessage;
    
    
    
	public SelfServicePortalPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.colorsLink           			= By.linkText("Colors");
		this.translationsLink     			= By.linkText("Translations");
		this.gralSettingsLink     			= By.linkText("General Settings for SSP");	
		this.addTranslationLink   			= By.linkText("Add Translation");
		this.languageSelect       			= By.id("id_iso638_1");
		this.saveButton           			= By.cssSelector("input[value = 'Save']");
		this.okAlert              			= By.xpath("//*[text() = 'OK']");
		this.pushWaitText         			= By.xpath("//*[text() = 'Please accept the notification on your mobile device']");
		this.frLink               			= By.linkText("French");
		this.enLink               			= By.linkText("English");
		this.itLink              			= By.linkText("Italian");
		this.translationStr1     			= By.id("id_traduction_set-4-msgstr");
		this.translationStr2     			= By.id("id_traduction_set-16-msgstr");
		this.translationStr3     			= By.id("id_traduction_set-17-msgstr");
		this.translationStr4     			= By.id("id_traduction_set-0-msgstr");
		this.homeLink            			= By.linkText("Home");
		this.deleteButton        			= By.linkText("Delete");
		this.submitButton        			= By.cssSelector("input[type = 'submit']");
		this.fastAuthLicenseStatus 			= By.xpath("//*[@id=\"dashboard\"]/div/div/div[2]/div/div[2]/div/div/div/div/div[1]/p/strong");//).getText(),"(Not Registered)");
		this.totpLicenseStatus     			= By.xpath("//*[@id=\"dashboard\"]/div/div/div[2]/div/div[1]/div/div/div/div/div/p/strong");//).getText(),"(Not Registered)");
		this.fastAuthLicenseType   			= By.xpath("//*[@id=\"dashboard\"]/div/div/div[2]/div/div[1]/div/div/h5/span");//).getText(),"Fast:Auth:Mobile:Asymmetric");
		this.totpLicenseType       			= By.xpath("//*[@id=\"dashboard\"]/div/div/div[2]/div/div[2]/div/div/h5/span");//).getText(),"TOTP:Mobile");
		this.consoleLogin          			= By.name("initial-username");
		this.consolePassword       			= By.name("initial-password");
		this.loginButton           			= By.id("login-btn");
		this.fastAuthModalRegisterButton   	= By.xpath("/html/body/ngb-modal-window/div/div/app-register-fastauth/div[1]/button/i");
		this.fastAuthModalQrCode		   	= By.xpath("/html/body/ngb-modal-window/div/div/app-register-fastauth/div[2]/div/div[2]");
		this.fastAuthModalExitButton		= By.xpath("/html/body/ngb-modal-window/div/div/app-register-fastauth/div[1]/button/i");
  	    this.totpModalRegisterButton       	= By.xpath("//*[@id=\"dashboard\"]/div/div/div[2]/div/div[2]/div/div/div/div/div[2]/button");
  	    this.totpModalNextButton           	= By.id("NextLink");
  	    this.totpModalQrCode               	= By.xpath(" /html/body/ngb-modal-window/div/div/app-qr/div[2]/div[2]/qrcode");
		this.totpModalQrButton             	= By.xpath("/html/body/ngb-modal-window/div/div/app-qr/div[1]/button/i");
		this.logOutButton                  	= By.id("LogOut");
		this.clickHereLink				   	= By.linkText("click here");
		this.changePassword1			   	= By.id("pin");
		this.changePassword2			   	= By.id("newpassword2");
		this.sendChangePasswordButton	   	= By.id("sendPans");
		this.clickHereLoginAgain		   	= By.linkText("Click here to login again");
		this.forgotPasswordLink		   		= By.id("forgot_password");
		this.usernameForgotPassword        	= By.name("username");
		this.idCaptcha					   	= By.id("id_captcha_1");
		this.forgotLoginBtn				   	= By.id("login-btn");
		this.classCaptcha				   	= By.className("captcha");
		this.backLogin					   	= By.linkText("Back to login");
		this.passwordChangeSuccessMessage   = By.xpath("//*[text() = 'Your password has been successfully changed.']");
	}
	
	public WebElement getColorsLink() {
		return  this.driver.findElement(colorsLink);
	}

	public WebElement getTranslationsLink() {
		return this.driver.findElement(translationsLink);
	}

	public WebElement getGralSettingsLink() {
		return this.driver.findElement(gralSettingsLink);
	}
	
	public WebElement getAddTranslationLink() {
		return this.driver.findElement(addTranslationLink);
	}

	public WebElement getLanguageSelect() {
		return this.driver.findElement(languageSelect);
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(saveButton);
	}
	
	public WebElement getOKAlert() {
		return this.driver.findElement(okAlert);
	}
	
	public WebElement getFRLink() {
		return this.driver.findElement(frLink);
	}
	
	public WebElement getENLink() {
		return this.driver.findElement(enLink);
	}
	
	public WebElement getITLink() {
		return this.driver.findElement(itLink);
	}
	
	public WebElement getTranslationStr1() {
		return this.driver.findElement(translationStr1);
	}
	
	public WebElement getTranslationStr2() {
		return this.driver.findElement(translationStr2);
	}
	
	public WebElement getTranslationStr3() {
		return this.driver.findElement(translationStr3);
	}
	
	public WebElement getTranslationStr4() {
		return this.driver.findElement(translationStr4);
	}
	
	public WebElement getHomeLink() {
		return this.driver.findElement(homeLink);
	}
	
	public WebElement getSubmitButton() {
		return this.driver.findElement(submitButton);
	}
	
	public WebElement getDeleteButton() {
		return this.driver.findElement(deleteButton);
	}
	
	public WebElement getFastAuthLicenseStatus() {
		 return this.driver.findElement(fastAuthLicenseStatus);
	}

	public WebElement getTotpLicenseStatus() {
	   return this.driver.findElement(totpLicenseStatus);
	}

	public WebElement getFastAuthLicenseType() {
		 return this.driver.findElement(fastAuthLicenseType);
	}

	public WebElement getTotpLicenseType() {
		 return this.driver.findElement(totpLicenseType);
	}
	
	public WebElement getConsolePassword() {
		return this.driver.findElement(consolePassword);
	}

	public WebElement getConsoleLogin() {
		return this.driver.findElement(consoleLogin);
	}
	
	public WebElement getLoginButton() {
		return this.driver.findElement(loginButton);
	}
	
	public WebElement getTotpModalRegisterButton() {
		return this.driver.findElement(totpModalRegisterButton);
	}

	public WebElement getTotpModalQrButton() {
		return this.driver.findElement(totpModalQrButton);
	}

	public WebElement getTotpModalNextButton() {
		return this.driver.findElement(totpModalNextButton);
	}

	public WebElement getTotpModalQrCode() {
		return this.driver.findElement(totpModalQrCode);
	}
	
	public WebElement getFastAuthModalRegisterButton() {
		return this.driver.findElement(fastAuthModalRegisterButton);
	}
	
	public WebElement getFastAuthModalQrCode() {
		return this.driver.findElement(fastAuthModalQrCode);
	}
	
	public WebElement getFastAuthModalExitButton() {
		return this.driver.findElement(fastAuthModalExitButton);
	}
	
	public WebElement getLogOutButton() {
		return this.driver.findElement(logOutButton);
	}

	public WebElement getPushWaitText() {
		return this.driver.findElement(pushWaitText);
	}
	
	public WebElement getClickHereLink() {
		return this.driver.findElement(clickHereLink);
	}
	
	public WebElement getChangePassword1() {
		return this.driver.findElement(changePassword1);
	}
	
	public WebElement getChangePassword2() {
		return this.driver.findElement(changePassword2);
	}
	
	public WebElement getSendChangePasswordButton() {
		return this.driver.findElement(sendChangePasswordButton);
	}
	
	public WebElement getClickHereLoginAgain() {
		return this.driver.findElement(clickHereLoginAgain);
	}
	
	public WebElement getForgotPasswordLink() {
		return this.driver.findElement(forgotPasswordLink);
	}
	
	public WebElement getUsernameForgotPassword() {
		return this.driver.findElement(usernameForgotPassword);
	}
	
	public WebElement getIdCaptcha() {
		return this.driver.findElement(idCaptcha);
	}
	
	public WebElement getForgotLoginBtn() {
		return this.driver.findElement(forgotLoginBtn);
	}
	
	public WebElement getClassCaptcha() {
		return this.driver.findElement(classCaptcha);
	}
	
	public WebElement getBackLogin() {
		return this.driver.findElement(backLogin);
	}
	
	public WebElement getPasswordChageSuccessMessage() {
		return this.driver.findElement(passwordChangeSuccessMessage);
	}
	
	public String getRegistrationLinkURI() {
		return Config.RegistrationLinkUri;
	}	
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSelfServicePortalPort + "/";
	}
}