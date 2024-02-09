package pages.mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import pages.BasePage;

public class ChangePinPage extends BasePage{
	
	protected By oneButton;
	protected By twoButton;
	protected By threeButton;
	protected By fourButton;
	protected By fiveButton;
	protected By sixButton;
	protected By sevenButton;
	protected By eightButton;
	protected By nineButton;
	protected By ceroButton;
	protected By delButton;

	
	
	
	public ChangePinPage(WebDriver driver, WebDriverWait wait, Platform platform) {
		super(driver, wait);
		
		if (platform == Platform.ANDROID) {
			this.oneButton = AppiumBy.accessibilityId("1");
			this.twoButton = AppiumBy.accessibilityId("2");
			this.threeButton = AppiumBy.accessibilityId("3");
			this.fourButton = AppiumBy.accessibilityId("4");
			this.fiveButton = AppiumBy.accessibilityId("5");
			this.sixButton = AppiumBy.accessibilityId("6");
			this.sevenButton = AppiumBy.accessibilityId("7");
			this.eightButton = AppiumBy.accessibilityId("8");
			this.nineButton = AppiumBy.accessibilityId("9");
			this.ceroButton = AppiumBy.accessibilityId("0");
			this.delButton = AppiumBy.accessibilityId("del");
			
		}
		
		if (platform == Platform.IOS) {
			this.oneButton = AppiumBy.accessibilityId("1");
			this.twoButton = AppiumBy.accessibilityId("2");
			this.threeButton = AppiumBy.accessibilityId("3");
			this.fourButton = AppiumBy.accessibilityId("4");
			this.fiveButton = AppiumBy.accessibilityId("5");
			this.sixButton = AppiumBy.accessibilityId("6");
			this.sevenButton = AppiumBy.accessibilityId("7");
			this.eightButton = AppiumBy.accessibilityId("8");
			this.nineButton = AppiumBy.accessibilityId("9");
			this.ceroButton = AppiumBy.accessibilityId("0");
			this.delButton = AppiumBy.accessibilityId("del");
		}	
	}
	
	
	public void clickOneButton() {
		this.driver.findElement(this.oneButton).click();
	}
	
	public void clickTwoButton() {
		this.driver.findElement(this.twoButton).click();
	}
	
	public void clickThreeButton() {
		this.driver.findElement(this.threeButton).click();
	}
	
	public void clickFourButton() {
		this.driver.findElement(this.fourButton).click();
	}
	
	public void clickFiveButton() {
		this.driver.findElement(this.fiveButton).click();
	}
	
	public void clickSixButton() {
		this.driver.findElement(this.sixButton).click();
	}
	
	public void clickSevenButton() {
		this.driver.findElement(this.sevenButton).click();
	}
	
	public void clickEightButton() {
		this.driver.findElement(this.eightButton).click();
	}
	
	public void clickNineButton() {
		this.driver.findElement(this.nineButton).click();
	}
	
	public void clickCeroButton() {
		this.driver.findElement(this.ceroButton).click();
	}
	
	public void clickDelButton() {
		this.driver.findElement(this.delButton).click();
	}
	
	public void waitForElements() {
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.oneButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.twoButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.threeButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.fourButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.fiveButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.sixButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.sevenButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.eightButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.nineButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.ceroButton));
		this.wait.until(ExpectedConditions.presenceOfElementLocated(this.delButton));
	}
	
	@Override
	public String getUrl() {
		return null;
	}
}
