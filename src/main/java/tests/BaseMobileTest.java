package tests;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

import configs.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import listeners.BaseListener;


@Listeners(BaseListener.class)
public abstract class BaseMobileTest implements BaseTest{

	protected AppiumDriverLocalService service;
    protected AppiumDriver driver;
	protected WebDriverWait wait;
    protected Platform platform;

	
	public BaseMobileTest() {
		try {	
			Config.load();
			
			URL remoteAddress = new URL(Config.getAppiumUrl());
					
			if (Config.platform.equalsIgnoreCase("android")) {
				DesiredCapabilities capabilities = new DesiredCapabilities();
		        capabilities.setCapability(CapabilityType.PLATFORM_NAME, "Android");		        
				
		        if (Config.autoStart == true) {
		        	capabilities.setCapability (MobileCapabilityType.PLATFORM_VERSION, Config.platformVersion);
		            capabilities.setCapability (MobileCapabilityType.DEVICE_NAME, Config.deviceName);
		            capabilities.setCapability (MobileCapabilityType.APP, Config.appPath);
		            capabilities.setCapability (MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
		            capabilities.setCapability(MobileCapabilityType.NO_RESET, Config.noReset);
		            capabilities.setCapability(MobileCapabilityType.FULL_RESET, Config.fullReset);
		            
		            if (Config.emulated == true) {
		            	capabilities.setCapability (AndroidMobileCapabilityType.AVD, Config.deviceName);
		            }
		            
		            HashMap<String, String> environment = new HashMap<>();
		            environment.put("ANDROID_HOME", Config.androidHome);
		            environment.put("JAVA_HOME", Config.javaHome);
		            
		            this.service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
		            		.withIPAddress(Config.appiumHost)
		            		.usingPort(Config.appiumPort)
		    				.withAppiumJS(new File(Config.appiumLibrary))
		            		.withLogFile(new File (Config.appiumLog))
		            		.withArgument(GeneralServerFlag.LOG_LEVEL, Config.appiumLogLevel)
		            		.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
		            		.withArgument(GeneralServerFlag.BASEPATH, Config.appiumPath)
		            		.withEnvironment(environment)
		            		.withCapabilities(capabilities)
		            );
		            this.service.clearOutPutStreams();
		            this.service.start();
		        }
		        
				this.driver = new AndroidDriver(remoteAddress, capabilities);
				this.platform = Platform.ANDROID;
			}

			if (Config.platform.equalsIgnoreCase("ios")) {
				DesiredCapabilities capabilities = new DesiredCapabilities();        
		        capabilities.setCapability(CapabilityType.PLATFORM_NAME, "iOS");
		        
		        if (Config.autoStart == true) {
		        	capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Config.platformVersion);
		            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Config.deviceName);
		            capabilities.setCapability(MobileCapabilityType.APP, Config.appPath);
		            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
		            capabilities.setCapability(MobileCapabilityType.NO_RESET, Config.noReset);
		            capabilities.setCapability(MobileCapabilityType.FULL_RESET, Config.fullReset);
		            
		            HashMap<String, String> environment = new HashMap<>();
		            environment.put("ANDROID_HOME", Config.androidHome);
		            environment.put("JAVA_HOME", Config.javaHome);
		            
		            this.service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
		            		.withIPAddress(Config.appiumHost)
		            		.usingPort(Config.appiumPort)
		    				.withAppiumJS(new File(Config.appiumLibrary))
		            		.withLogFile(new File (Config.appiumLog))
		            		.withArgument(GeneralServerFlag.LOG_LEVEL, Config.appiumLogLevel)
		            		.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
		            		.withArgument(GeneralServerFlag.BASEPATH, Config.appiumPath)
		            		.withEnvironment(environment)
		            		.withCapabilities(capabilities)
		            );
		            this.service.clearOutPutStreams();
		            this.service.start();
		        }
				
				this.driver = new IOSDriver(remoteAddress, capabilities);
				this.platform = Platform.IOS;
			}
						
			this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AppiumDriver getDriver() {
		return driver;
	}

	public void setDriver(AppiumDriver driver) {
		this.driver = driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public void setWait(WebDriverWait wait) {
		this.wait = wait;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	
	public WebDriver getDriverInstance() {
		return this.getDriver();
	}

	@AfterClass
	public void onClose() {
		if (this.driver != null) {
			this.driver.quit();
		}
		
		if (this.service != null && this.service.isRunning ()) {
			this.service.stop ();
	    }
	}
}
