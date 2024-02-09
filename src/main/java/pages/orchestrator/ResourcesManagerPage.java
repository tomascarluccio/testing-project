package pages.orchestrator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class ResourcesManagerPage extends BasePage{

	By addLink;
	By name;
	By maxRequestsApi;
	By maxMobileRequests;
	By maxAuthRequets;
	By maxManagementRequests;
	By numberOfBackgroundWorkers;
	By numberOfGatewaysWorkers;
	By maxPortalRequests;
	By numberOfRadiusThreads;
	By saveButton;
	

	public ResourcesManagerPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		
		this.addLink					 	= By.className("addlink");
		this.name 					     	= By.id("id_name");
		this.maxRequestsApi				 	= By.id("id_max_api_requests");
		this.maxMobileRequests 			 	= By.id("id_max_mobile_requests");
		this.maxAuthRequets 			 	= By.id("id_max_auth_requests");
		this.maxManagementRequests		 	= By.id("id_max_management_requests");
		this.numberOfBackgroundWorkers	 	= By.id("id_number_of_background_workers");
		this.numberOfGatewaysWorkers 	 	= By.id("id_number_of_gateway_workers");
		this.maxPortalRequests     		 	= By.id("id_max_portal_requests");
		this.numberOfRadiusThreads   	 	= By.id("id_number_of_radius_threads");
		this.saveButton                  	= By.cssSelector("input[value = 'Save']");
	}

	
	
	public WebElement getAddLink() {
		return this.driver.findElement(this.addLink);
	}

	public WebElement getName() {
		return this.driver.findElement(this.name);
	}

	public WebElement getMaxRequestsApi() {
		return this.driver.findElement(this.maxRequestsApi);
	}

	public WebElement getMaxMobileRequests() {
		return this.driver.findElement(this.maxMobileRequests);
	}

	public WebElement getMaxAuthRequets() {
		return this.driver.findElement(this.maxAuthRequets);
	}

	public WebElement getMaxManagementRequests() {
		return this.driver.findElement(this.maxManagementRequests);
	}

	public WebElement getNumberOfBackgroundWorkers() {
		return this.driver.findElement(this.numberOfBackgroundWorkers);
	}

	public WebElement getNumberOfGatewaysWorkers() {
		return this.driver.findElement(this.numberOfGatewaysWorkers);
	}

	public WebElement getMaxPortalRequests() {
		return this.driver.findElement(this.maxPortalRequests);
	}

	public WebElement getNumberOfRadiusThreads() {
		return this.driver.findElement(this.numberOfRadiusThreads);
	}

	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	@Override
	public String getUrl() {
		return "https://" + Config.ocHost + ":" + Config.ocSuperAdminPort.toString() + "/multitenancy/resourcesmanagerlevel/";
	}

}