package tests.mobile;

import org.testng.annotations.Test;

import pages.mobile.MainPage;
import tests.BaseMobileTest;
import reports.Report;


public class MainTest extends BaseMobileTest {


	@Test(priority = 1)
	@Report(name = "Main", description = "No description")
	public void main() throws Exception {

		MainPage mainPage = new MainPage(this.driver, this.wait, this.platform);
		mainPage.clickShowButton();
		mainPage.waitForWidgetToast();
		mainPage.getWidgetToastMessage();
	}
}



