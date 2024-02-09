package tests.tenant.superadmin;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.SSPCustomizationPage;
import reports.Report;

public class SSPTranslationsSetupTest extends LoginTest {

	@DataProvider(name = "sspUSers", parallel=true)
	public Object[][] getUserData() {
		return new Object[][] { 
			{"sspuser",  "Safewalk1", "Fast:Auth:Mobile:Asymmetric","TOTP:Mobile", Config.tnHost}};
		
		}
		
		
	@Test(priority = 2 )
	@Report(name="Add SSP Italian Translation", description="No description")
	public void addTranslation() throws Exception {
		
		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSettingsTab().click();
		dashboardPage.getSSPLink().click();
		
		SSPCustomizationPage sspCustomizationPage = new SSPCustomizationPage(driver, wait);
		sspCustomizationPage.getTranslationsLink().click();
		sspCustomizationPage.getAddTranslationLink().click();
		Select loginType = new Select(sspCustomizationPage.getLanguageSelect());
		loginType.selectByVisibleText("Italian");
		sspCustomizationPage.getSaveButton().click();
		wait.until(ExpectedConditions.elementToBeClickable(sspCustomizationPage.getOKAlert()));
		sspCustomizationPage.getOKAlert().click();
		
		assertTrue(sspCustomizationPage.getTranslationStr1().isDisplayed());
		assertTrue(sspCustomizationPage.getTranslationStr2().isDisplayed());
		assertTrue(sspCustomizationPage.getTranslationStr3().isDisplayed());
		
		sspCustomizationPage.getSaveButton().click();
		wait.until(ExpectedConditions.elementToBeClickable(sspCustomizationPage.getOKAlert()));
		sspCustomizationPage.getOKAlert().click();		
 	}


	@Test( priority = 3)
	@Report(name="update SSP Italian Translation", description="No description")
	public void updateTranslation() throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSettingsTab().click();
		dashboardPage.getSSPLink().click();
		
		SSPCustomizationPage sspCustomizationPage = new SSPCustomizationPage(driver, wait);
		sspCustomizationPage.getTranslationsLink().click();
		sspCustomizationPage.getENLink().click();
		sspCustomizationPage.getTranslationStr4().clear();
		sspCustomizationPage.getTranslationStr4().sendKeys("Updated Token Str");
		sspCustomizationPage.getHomeLink().click();
	}

	@Test( priority = 4)
	@Report(name="delete SSP Italian Translation", description="No description")
	public void deleteTranslation() throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getSettingsTab().click();
		dashboardPage.getSSPLink().click();
		
		SSPCustomizationPage sspCustomizationPage = new SSPCustomizationPage(driver, wait);
		sspCustomizationPage.getTranslationsLink().click();
		sspCustomizationPage.getITLink().click();
		sspCustomizationPage.getDeleteButton().click();
		sspCustomizationPage.getSubmitButton().click();
			
	}	
	
}