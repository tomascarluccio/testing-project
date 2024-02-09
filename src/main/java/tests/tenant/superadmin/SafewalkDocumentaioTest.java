package tests.tenant.superadmin;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import pages.tenant.superadmin.DashboardPage;
import reports.Report;

public class SafewalkDocumentaioTest extends LoginTest{
	
	
	
	@Test( priority = 4)
	@Report(name="Documentation link test", description="")
	public void documentationLinkTest() throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getDocumentationLink().click();
		
		String oldTab = driver.getWindowHandle();
	    ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles());
	    newTab.remove(oldTab);
	    driver.switchTo().window(newTab.get(0));
	    
	    assertTrue(driver.findElement(By.linkText("Table of Contents")).isDisplayed());
		assertTrue(driver.findElement(By.className("search")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("Management console")).isDisplayed());
	}
}
