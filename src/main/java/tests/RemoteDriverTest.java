package tests;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import pages.tenant.superadmin.DashboardPage;


public class RemoteDriverTest extends BaseBrowserTest {
	
	@Test( priority = 1)
	public void mltest() throws Exception {
		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get("https://www.mercadolibre.com.ar");
		Thread.sleep(25000);
		driver.findElement(By.id("nav-cart")).click();
	}
}
