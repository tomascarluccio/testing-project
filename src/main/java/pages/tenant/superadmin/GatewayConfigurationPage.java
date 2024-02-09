package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class GatewayConfigurationPage extends BasePage {

	By gatewayName;
    By brandLogoInput;
    By gatewayConfigLink;
    By addGatewaylink;
    By nameInput;
    By gatewayPublicHostInput;
    By gatewaySSHHostInput;
    By gatewaySSHPortInput;
    By safewalkPublicHostInput;
    By addGatewayConfigLink;
    By editGatewayConfigLink;
    By rootPasswordInput;
    By registrationCheckbox;
    By userPortalCheckbox;
    By samlIdpCheckbox;
    By authenticationCheckbox;
    By identityProviderValue;
    By saveButton;
    By gatewayPassword;
    
    
    
    public GatewayConfigurationPage(WebDriver driver , WebDriverWait wait) {
    	super(driver, wait);
    	
    	this.gatewayName = By.id("id_name");
    	this.brandLogoInput = By.id("id_brand_logo");
        this.gatewayConfigLink = By.linkText("Safewalk gateway configurations");
        this.addGatewaylink = By.linkText("Add Safewalk gateway configuration");
        this.nameInput = By.id("id_name");
        this.gatewayPublicHostInput = By.id("id_gateway_public_host");
        this.gatewaySSHHostInput = By.id("id_gateway_ssh_host");
        this.gatewaySSHPortInput = By.id("id_gateway_ssh_port");
        this.safewalkPublicHostInput = By.id("id_safewalk_public_host");
        this.addGatewayConfigLink = By.linkText("Add Safewalk gateway configuration");
        this.editGatewayConfigLink = By.linkText("Safewalk gateway configurations");
        this.rootPasswordInput = By.id("id_root_password");
        this.registrationCheckbox = By.id("id_is_registration_enabled");
        this.userPortalCheckbox = By.id("id_is_userportal_enabled");
        this.samlIdpCheckbox = By.id("id_is_saml_idp_enabled");
        this.authenticationCheckbox = By.id("id_is_authentication_enabled");
        this.identityProviderValue      =  By.xpath("//*[@id=\"gatewayconfiguration_form\"]/div/fieldset[2]/div[2]/div/p");
    	this.saveButton                     =  By.cssSelector("input[value = 'Save']");
    	this.gatewayPassword             = By.id("id_root_password");
    }

    public WebElement getBrandLogoInput() {
        return driver.findElement(brandLogoInput);
    }

    public WebElement getGatewayConfigLink() {
        return  driver.findElement(gatewayConfigLink);
    }

    public WebElement getGatewayNameLink() {
        return  driver.findElement(addGatewaylink);
    }

    public WebElement getNameInput() {
        return  driver.findElement(nameInput);
    }

    public WebElement getGatewayPublicHostInput() {
        return  driver.findElement(gatewayPublicHostInput);
    }

    public WebElement getGatewaySSHHostInput() {
        return driver.findElement(gatewaySSHHostInput);
    }

    public WebElement getGatewaySSHPortInput() {
        return  driver.findElement(gatewaySSHPortInput);
    }

    public WebElement getSafewalkPublicHostInput() {
        return  driver.findElement(safewalkPublicHostInput);
    }

    public WebElement getAddGatewayConfigLink() {
        return  driver.findElement(addGatewayConfigLink);
    }

    public WebElement getEditGatewayConfigLink() {
        return  driver.findElement(editGatewayConfigLink);
    }

    public WebElement getRootPasswordInput() {
        return  driver.findElement(rootPasswordInput);
    }

    public WebElement getRegistrationCheckbox() {
        return  driver.findElement(registrationCheckbox);
    }

    public WebElement getUserPortalCheckbox() {
        return  driver.findElement(userPortalCheckbox);
    }

    public WebElement getSamlIdpCheckbox() {
        return  driver.findElement(samlIdpCheckbox);
    }

    public WebElement getAuthenticationCheckbox() {
        return  driver.findElement(authenticationCheckbox);
    }
    
    public WebElement getGatewayNameLink(String gatewayName) {
		return this.driver.findElement(By.linkText(gatewayName));
	}

	public WebElement getIdentityProviderValue() {
		return this.driver.findElement(this.identityProviderValue);
	}
	public WebElement getGatewayName() {
		return this.driver.findElement(gatewayName);
	}

	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	public WebElement getGatewayPassword() {
		return this.driver.findElement(this.gatewayPassword);
	}

	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/";
	}

  
}
