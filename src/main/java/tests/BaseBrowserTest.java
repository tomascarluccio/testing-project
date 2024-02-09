package tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

import configs.Config;
import listeners.BaseListener;


@Listeners(BaseListener.class)
public abstract class BaseBrowserTest implements BaseTest  {

	protected WebDriver driver;
	protected WebDriverWait wait;	

	public BaseBrowserTest() {
		try {			
			Config.load();
			
				if (Config.browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", Config.driverPath);
			
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--remote-allow-origins=*");
				
				if (Config.docker) {
					    options.addArguments("--no-sandbox");
			                    options.addArguments("--headless");
			                    options.addArguments("--disable-gpu");
			                    options.addArguments("--disable-dev-shm-usage");
			                    options.addArguments("--remote-allow-origins=*");
			                    options.addArguments("--verbose");

				}

				DesiredCapabilities capabilities = new DesiredCapabilities();

				this.driver = new ChromeDriver(options);
			}

			if (Config.browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver", Config.driverPath);
				this.driver = new FirefoxDriver();
			}

			if (Config.browser.equalsIgnoreCase("edge")) {
				System.setProperty("webdriver.edge.driver", Config.driverPath);
				this.driver = new EdgeDriver();
			}

			if (Config.browser.equalsIgnoreCase("safari")) {
				System.setProperty("webdriver.safari.driver", Config.driverPath);
				this.driver = new SafariDriver();
			}

			this.driver.manage().window().maximize();
			this.driver.manage().deleteAllCookies();
			this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(5));
			this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(5));
			this.wait = new WebDriverWait(driver, Duration.ofMinutes(5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public void setWait(WebDriverWait wait) {
		this.wait = wait;
	}
	
	public WebDriver getDriverInstance() {
		return this.getDriver();
	}
	
	@AfterClass
	public void onClose() {
		if (this.driver != null) {
			this.driver.quit();
		}
	}
}
