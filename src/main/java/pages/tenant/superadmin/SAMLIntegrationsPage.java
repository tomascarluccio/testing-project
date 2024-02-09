package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class SAMLIntegrationsPage extends BasePage{

	By addSAMLIntegrationLink;	
	By gatewayConfigurationSelector;
	By integrationTypeSelector;
	By nameInput;
	By isEnabledCheckbox;
	By nextButton;
	By saveButton;
	By safewalkUserSelector;
	By authenticationTimeoutInput;
	By serviceProviderXMLMetadataFile;
	By deleteLink;
	By confirmButton;
	
	public SAMLIntegrationsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);

		this.addSAMLIntegrationLink                 =  By.linkText("Add SAML integration");
		this.gatewayConfigurationSelector           =  By.id("id_SELECTION-gateway_configuration");
		this.integrationTypeSelector                =  By.id("id_SELECTION-integration_type");
		this.nameInput                  			=  By.id("id_SELECTION-name");
		this.isEnabledCheckbox                  	=  By.id("id_SELECTION-is_enabled");
		this.nextButton                  			=  By.xpath("//*[@id=\"module_\"]/div/form/p/input");	
		this.saveButton                  			=  By.xpath("//*[@id=\"module_\"]/div/form/p/input");	
		this.safewalkUserSelector                  	=  By.id("id_SAFE_CONF-safewalk_user");
		this.authenticationTimeoutInput             =  By.id("id_SAFE_CONF-safewalk_auth_timeout");
		this.serviceProviderXMLMetadataFile         =  By.id("id_SP_CONF-sp_xml_metadata");
		this.deleteLink				       		 	=  By.linkText("Delete");	
		this.confirmButton				       		=  By.xpath("//*[@id=\"content\"]/form/div[1]/input[2]");	
	}

	public WebElement getAddSAMLIntegrationLink() {
		return this.driver.findElement(this.addSAMLIntegrationLink);
	}
	
	public WebElement getGatewayConfigurationSelector() {
		return this.driver.findElement(this.gatewayConfigurationSelector);
	}
	
	public WebElement getIntegrationTypeSelector() {
		return this.driver.findElement(this.integrationTypeSelector);
	}
	
	public WebElement getNameInput() {
		return this.driver.findElement(this.nameInput);
	}
	
	public WebElement getIsEnabledCheckbox() {
		return this.driver.findElement(this.isEnabledCheckbox);
	}

	public WebElement getNextButton() {
		return this.driver.findElement(this.nextButton);
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getSafewalkUserSelector() {
		return this.driver.findElement(this.safewalkUserSelector);
	}
	
	public WebElement getAuthenticationTimeoutInput() {
		return this.driver.findElement(this.authenticationTimeoutInput);
	}
	
	public WebElement getServiceProviderXMLMetadataFile() {
		return this.driver.findElement(this.serviceProviderXMLMetadataFile);
	}
	
	public WebElement getSamlIntegrationNameLink(String integrationName) {
		return this.driver.findElement(By.linkText(integrationName));
	}
	
	public WebElement getDeleteLink() {
		return this.driver.findElement(this.deleteLink);
	}
	
	public WebElement getConfirmButton() {
		return this.driver.findElement(this.confirmButton);
	}
	
	public boolean existsSAMLIntegration(String samlName) {
		try {
			// TODO: Look for more efficient alternatives
			int size = driver.findElements(By.linkText(samlName)).size();
			if(size > 0)
				return true;
			
		} catch (Exception e) {	
		}
			return false;
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/safewalk_gateway/samlwebintegration/";
	}
	
}