package pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import pages.BasePage;

public class SettingsPage extends BasePage{
	
	protected By changePinButton;
	protected By faqButton;
	protected By aboutButton;
	
	
	
	public SettingsPage(WebDriver driver, WebDriverWait wait, Platform platform) {
		super(driver, wait);
		
		if (platform == Platform.ANDROID) {
			this.changePinButton = AppiumBy.xpath("//android.widget.Button[@content-desc=\"settings-pin-button\"]");
			this.faqButton = AppiumBy.xpath("//android.widget.Button[@content-desc=\"settings-faq-button\"]");
			this.aboutButton = AppiumBy.xpath("//android.widget.Button[@content-desc=\"settings-about-button\"]");
		}
		
		if (platform == Platform.IOS) {
			this.changePinButton = AppiumBy.accessibilityId("settings-pin-button");
			this.faqButton = AppiumBy.accessibilityId("settings-faq-button");
			this.aboutButton = AppiumBy.accessibilityId("settings-about-button");
		}	
	}
	
	
	public void clickChangePinButton() {
		this.driver.findElement(this.changePinButton).click();
	}
	
	public void clickFaqButton() {
		this.driver.findElement(this.faqButton).click();
	}
	
	public void clickAboutButton() {
		this.driver.findElement(this.aboutButton).click();
	}
	
	public void waitForElements() {
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.changePinButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.faqButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.aboutButton));
	}
	
	@Override
	public String getUrl() {
		return null;
	}
	
}
