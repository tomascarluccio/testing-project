package pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import pages.BasePage;

public class HomePage extends BasePage{
	
	protected By menuButton;
	
	
	
	public HomePage(WebDriver driver, WebDriverWait wait, Platform platform) {
		super(driver, wait);
		
		if (platform == Platform.ANDROID) {
			this.menuButton = AppiumBy.xpath("//android.widget.TextView[@content-desc=\"Menu\"]");
		}
		
		if (platform == Platform.IOS) {
			this.menuButton = AppiumBy.accessibilityId("");
		}	
	}
	
	
	public void clickMenuButton() {
		this.driver.findElement(this.menuButton).click();
	}
	
	@Override
	public String getUrl() {
		return null;
	}
	
}
