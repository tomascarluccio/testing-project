package tests.tenant.superadmin;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.DefaultSettingsPage;
import reports.Report;

public class DefaultSettingTest extends LoginTest{
	
	
	@Test( priority = 2)
	@Report(name="Default Settings Test", description="enable password authentication, forgot password, and password expired in defautl settings")
	public void enableSettings() throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();
		superAdminDashboardPage.getDefaultSettingsLink().click();
		
		DefaultSettingsPage defaultSettingTest = new DefaultSettingsPage(this.driver, this.wait);
		defaultSettingTest.getGralAuthSettingsLink().click();
		Select select = new Select(defaultSettingTest.getAllowPwdLink());
		select.selectByVisibleText("Yes");
		Select select1 = new Select(defaultSettingTest.getAllowPwdForRegistrationLink());
		select1.selectByVisibleText("Yes");
		Select select2 = new Select(defaultSettingTest.getAllowAccessWhenPwdExpiredLink());
		select2.selectByVisibleText("Yes");
		Select select3 = new Select(defaultSettingTest.getAllowPwdResetWhenForgot());
		select3.selectByVisibleText("Yes");
		defaultSettingTest.getSaveButton().click();
	}
	
	
}
