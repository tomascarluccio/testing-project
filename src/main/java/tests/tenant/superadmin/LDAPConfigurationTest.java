package tests.tenant.superadmin;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.LDAPConfigurationPage;
import reports.Report;


public class LDAPConfigurationTest extends LoginTest {


	@DataProvider(name = "ldapDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.ldapName}
		};
	}
	
	
	@Test( dataProvider="ldapDataProvider", priority = 2 )
	@Report(name="LDAP Configuration Test", description="")
	public void setupLDAP(String ldapName) throws Exception {

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getIamSettingsTab().click();
		dashboardPage.getLDAPLink().click();
		
		LDAPConfigurationPage ldapConfigurationPage = new LDAPConfigurationPage(this.driver, this.wait);
		
		if(!ldapConfigurationPage.existsLDAPConfiguration(Config.ldapDomain)) {
			ldapConfigurationPage.getAddLDAPLink().click();
			Select ldapTypeSelect = new Select(ldapConfigurationPage.getLDAPType());
			ldapTypeSelect.selectByValue(Config.ldapType);
			ldapConfigurationPage.getName().click();
			ldapConfigurationPage.getName().sendKeys(ldapName);
			ldapConfigurationPage.getDomain().click();
			ldapConfigurationPage.getDomain().sendKeys(Config.ldapDomain);
			ldapConfigurationPage.getRootDn().click();
			ldapConfigurationPage.getRootDn().sendKeys(Config.ldapRootBaseDn);
			ldapConfigurationPage.getServer().click();
			ldapConfigurationPage.getServer().sendKeys(Config.ldapHost);
			ldapConfigurationPage.getBindDn().click();
			ldapConfigurationPage.getBindDn().sendKeys(Config.ldapBindDn);
			ldapConfigurationPage.getBindPassword().click();
			ldapConfigurationPage.getBindPassword().sendKeys(Config.ldapBindPassword);
			ldapConfigurationPage.getUserSearch().click();
			ldapConfigurationPage.getUserSearch().sendKeys(Config.ldapUserBaseDn);
			ldapConfigurationPage.getTestConnectionButton().click();
			
			wait.until(ExpectedConditions.visibilityOf(ldapConfigurationPage.getOkAlert()));
			ldapConfigurationPage.getOkAlert().click();
			ldapConfigurationPage.getSavebutton().click();

			
		}
	}
	
}
