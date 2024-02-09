package tests.orchestrator;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import pages.orchestrator.ApplianceVersionsPage;
import pages.orchestrator.DashboardPage;
import reports.Report;
import tests.tenant.API;
import utils.Response;


public class UpdateApplianceVersionTest extends LoginTest {
	
	public String getLatestVersion() throws Exception {
	    String latestVersion = null;
		Response response = API.getApplianceVersion();
	    JsonObject responseContentJson = JsonParser.parseString(response.getContent()).getAsJsonObject();
	    JsonArray resultsArray = responseContentJson.getAsJsonArray("results");
	    for (JsonElement element : resultsArray) {
	        JsonObject elementObject = element.getAsJsonObject();
	        String elementVersion = (String) elementObject.get("version").getAsString();
	        
	        if(latestVersion == null || elementVersion.compareTo(latestVersion) > 0) {
	        	latestVersion = elementVersion;
	        }        
	    }
	    
	    return latestVersion;
	}	
	
	@Test( priority = 1)
	@Report(name="Update appliance version", description="")
	public void updateApplianceVersion() throws Exception {

		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getApplianceVersionsLink().click();
		
		ApplianceVersionsPage applianceVersionsPage =  new ApplianceVersionsPage(driver, this.wait);
		applianceVersionsPage.getUpdateButton().click();
		
		  
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	    		.withTimeout(Duration.ofMinutes(30))
	    		.pollingEvery(Duration.ofSeconds(30));
		wait.until(ExpectedConditions.textToBePresentInElement(applianceVersionsPage.getLastApplianceVersion(), this.getLatestVersion()));
	}
}