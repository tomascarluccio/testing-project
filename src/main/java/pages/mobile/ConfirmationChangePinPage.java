package pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;

public class ConfirmationChangePinPage extends ChangePinPage{
	
	protected By okButton;
	
	public ConfirmationChangePinPage(WebDriver driver, WebDriverWait wait, Platform platform) {
		super(driver, wait, platform);
		
		if (platform == Platform.ANDROID) {
			this.okButton = AppiumBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.appcompat.widget.LinearLayoutCompat/android.widget.ScrollView/android.widget.LinearLayout/android.widget.Button");		
		}
		
		if (platform == Platform.IOS) {
			this.okButton = AppiumBy.accessibilityId("");
		}	
	}
	
	
	public void clickOkButton() {
		this.driver.findElement(this.okButton).click();
	}
	
	public void waitForOkButton() {
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.okButton));
		
	}
	
	
	
	
}
