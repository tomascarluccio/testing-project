package pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import pages.BasePage;

public class MainPage extends BasePage{
	
	protected By showButtom;
	protected By widgetToast;
	
	
	public MainPage(WebDriver driver, WebDriverWait wait, Platform platform) {
		super(driver, wait);
		
		if (platform == Platform.ANDROID) {
			this.showButtom = AppiumBy.id("com.example.myapplication:id/showButton");
			this.widgetToast = AppiumBy.xpath("/hierarchy/android.widget.Toast");
		}
		
		if (platform == Platform.IOS) {
			this.showButtom = AppiumBy.accessibilityId("notification");
		}	
	}
	
	
	public void clickShowButton() {
		this.driver.findElement(this.showButtom).click();
	}
	
	public void waitForWidgetToast() {
		this.wait.until(ExpectedConditions.presenceOfElementLocated(widgetToast));
	}
	
	public String getWidgetToastMessage() {
		return this.driver.findElement(this.widgetToast).getText();
	}
	
	@Override
	public String getUrl() {
		return null;
	}
	
}
