package pages.tenant.managementconsole;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransactionLogPage extends UserPanel {
	
	By timeHeader;
	By localTimeHeader;
	By userHeader;
	By sentUidHeader;
	By callerHeader;
	By safewalkNodeHeader;
	By typeHeader;
	By serialNumberHeader;
	By authStatusHeader;
	By firsTransactionInTable;
	
	public TransactionLogPage (WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.timeHeader					= By.linkText("Time");
		this.localTimeHeader			= By.linkText("Local time");
		this.userHeader					= By.linkText("User");
		this.sentUidHeader				= By.linkText("Sent uid");
		this.callerHeader				= By.linkText("Caller");
		this.safewalkNodeHeader			= By.linkText("Safewalk node");
		this.typeHeader					= By.linkText("Type");
		this.authStatusHeader			= By.linkText("Auth status");
		this.serialNumberHeader			= By.linkText("Serial number");
		this.firsTransactionInTable     = By.xpath("//*[@class='flatTable']/tbody/tr[0]/td[2]");
	}
	
	public WebElement getTimeHeader() {
		return this.driver.findElement(this.timeHeader);
	}

	public WebElement getLocalTimeHeader() {
		return this.driver.findElement(this.localTimeHeader);
	}

	public WebElement getUserHeader() {
		return this.driver.findElement(this.userHeader);
	}

	public WebElement getCallerHeader() {
		return this.driver.findElement(this.callerHeader);
	}

	public WebElement getSafewalkNodeHeader() {
		return this.driver.findElement(this.safewalkNodeHeader);
	}

	public WebElement getTypeHeader() {
		return this.driver.findElement(this.typeHeader);
	}

	public WebElement getSerialNumberHeader() {
		return this.driver.findElement(this.serialNumberHeader);
	}

	public WebElement getAuthStatusHeader() {
		return this.driver.findElement(this.authStatusHeader);
	}
	
	public WebElement getFirstTransaction() {
		return this.driver.findElement(this.firsTransactionInTable);
	}
}
