package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class SSPCustomizationPage extends BasePage{

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

	public SSPCustomizationPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.colorsLink           	= By.linkText("Colors");
		this.translationsLink     	= By.linkText("Translations");
		this.gralSettingsLink     	= By.linkText("General Settings for SSP");	
		this.addTranslationLink   	= By.linkText("Add Translation");
		this.languageSelect       	= By.id("id_iso638_1");
		this.saveButton           	= By.cssSelector("input[value = 'Save']");
		this.okAlert              	= By.xpath("//*[text() = 'OK']");
		this.frLink               	= By.linkText("French");
		this.enLink              	= By.linkText("English");
		this.itLink               	= By.linkText("Italian");
		this.translationStr1     	= By.id("id_traduction_set-4-msgstr");
		this.translationStr2     	= By.id("id_traduction_set-16-msgstr");
		this.translationStr3     	= By.id("id_traduction_set-17-msgstr");
		this.translationStr4     	= By.id("id_traduction_set-0-msgstr");
		this.homeLink            	= By.linkText("Home");
		this.deleteButton        	= By.linkText("Delete");
		this.submitButton        	= By.cssSelector("input[type = 'submit']");
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
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/ssp_customization/index/";
	}
}