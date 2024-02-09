package tests;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Listeners;

import configs.Config;
import listeners.BaseListener;

@Listeners(BaseListener.class)
public abstract class BaseAPITest implements BaseTest  {

	public BaseAPITest() {
		try {			
			Config.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebDriver getDriverInstance() {
		return null;
	}

	@AfterClass
	public void onClose() {}
}
