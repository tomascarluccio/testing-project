package tests.tenant.superadmin;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.io.Files;

import configs.Config;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.GatewayConfigurationPage;
import pages.tenant.superadmin.SAMLIntegrationsPage;
import pages.tenant.superadmin.WordpressPage;
import reports.Report;


public class SAMLIntegrationTest extends LoginTest {

		
	@DataProvider(name = "SAMLDataPRovider")
	public Object[][] getSAMLData() {
		return new Object[][] { 
			{Config.samlIntegrationName, "admin", "Safewalk1", Config.gatewayName},
		};
	}

	@Test( dataProvider = "SAMLDataPRovider", priority = 2)
	@Report(name="Setup Wordpress Integration Test", description="No description")
	public void setupSAMLIntegration(String samlIntegrationName, String wordpressAdmin, String wordpressAdminPassword, String gatewayName) throws Exception{

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getGatewayConfigurations().click();
		
		GatewayConfigurationPage gatewayConfigurationPage = new GatewayConfigurationPage(this.driver, this.wait);
		gatewayConfigurationPage.getGatewayName().click();
		String identityProvider = gatewayConfigurationPage.getIdentityProviderValue().getText();
				
		driver.get(Config.samlUrl + Config.worpressLoginSSOFalseUrl);
		WordpressPage wordpress = new WordpressPage(this.driver, this.wait);
		wordpress.getUsernameInputLink().click();
		wordpress.getUsernameInputLink().sendKeys(wordpressAdmin);
		wordpress.getPasswordInput().click();
		wordpress.getPasswordInput().sendKeys(wordpressAdminPassword);
		wordpress.getLoginButton().click();
	
		driver.get(Config.samlUrl + Config.wordpressSSOConfigUrl);
		wordpress.getIdentityProviderTab().click();
		wordpress.getUrlIdentifierInput().click();
		wordpress.getUrlIdentifierInput().clear();
		wordpress.getUrlIdentifierInput().sendKeys(Config.gatewayUrl + Config.samlMetadataUrl);
		wordpress.getSingleSingOnUrlInput().click();
		wordpress.getSingleSingOnUrlInput().clear();
		wordpress.getSingleSingOnUrlInput().sendKeys(Config.gatewayUrl + Config.samlSSOServiceUrl);
		wordpress.getSingleLogoutUrlInput().click();
		wordpress.getSingleLogoutUrlInput().clear();
		wordpress.getSingleLogoutUrlInput().sendKeys(Config.gatewayUrl + Config.samlSingleLogoutService); 
		wordpress.getCertificateFingerprintInput().click();
		wordpress.getCertificateFingerprintInput().clear();
		wordpress.getCertificateFingerprintInput().sendKeys(identityProvider); 
		wordpress.getUpdateOptionsButton().click();
		
		driver.get(Config.samlUrl + Config.samlMetadataContent);
		String pageSource = driver.findElement(By.tagName("body")).getText();
        	String xmldata = pageSource.replace(
				"This XML file does not appear to have any style information associated with it. The document tree is shown below.",
				"");
		Files.write( xmldata.getBytes(), new File(Config.wordpressMetadata+"/metadata.xml"));
           	    dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
         	dashboardPage.getSAMLIntegrationsLink().click();
		
		SAMLIntegrationsPage samlIntegration =  new SAMLIntegrationsPage(this.driver, this.wait);
		// if(!samlIntegration.existsSAMLIntegration(samlIntegrationName))
		//{
		        samlIntegration.getAddSAMLIntegrationLink().click();
			Select gatewaySelect = new Select(samlIntegration.getGatewayConfigurationSelector());
			gatewaySelect.selectByVisibleText(Config.gatewayName);
			Select integrationTypeSelect = new Select(samlIntegration.getIntegrationTypeSelector());
			integrationTypeSelect.selectByVisibleText(Config.samlIntegrationType);
			samlIntegration.getNameInput().click();
			samlIntegration.getNameInput().sendKeys(Config.samlIntegrationName+"_2");
         		samlIntegration.getNextButton().click();
			samlIntegration.getNextButton().click();
			samlIntegration.getNextButton().click();
			WebElement elem = samlIntegration.getServiceProviderXMLMetadataFile();
			elem.sendKeys(Config.wordpressMetadata+"/metadata.xml");
			driver.findElement(By.id("id_root_password")).click();
			driver.findElement(By.id("id_root_password")).sendKeys("Safewalk1");

			samlIntegration.getSaveButton().click();
	//	}
		
		driver.get(Config.samlUrl);
	        wordpress = new WordpressPage(this.driver, this.wait);
		wordpress.getLoginLink().click();
		
		SoftAssert soft = new SoftAssert();
		soft.assertTrue(driver.findElement(By.id("qrcode")).isDisplayed());
		
		this.driver.get(dashboardPage.getUrl());
	        dashboardPage.getSAMLIntegrationsLink().click();
		samlIntegration.getSamlIntegrationNameLink(samlIntegrationName).click();
		samlIntegration.getDeleteLink().click();
		samlIntegration.getConfirmButton().click();		
		   
	}
	
}
	
