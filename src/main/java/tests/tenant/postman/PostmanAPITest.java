package tests.tenant.postman;

import java.nio.file.Paths;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import configs.Config;
import tests.BaseAPITest;
import tests.tenant.API;
import utils.Postman;
import utils.Response;


public class PostmanAPITest extends BaseAPITest{
	
	@Test(priority=2)
	public void setTenant() throws Exception {

		Postman postman = new Postman();
		String postmanEnvioromentPath = postman.getPostmanEnvironmentPath();
		String postmanSetTenantPath = System.getProperty("user.dir") + "/src/main/resources/setTenant.postman_collection.json";
		String stableBundleVersion = (Config.tnStableVersion).split(" ")[0];
		String targetBundleVersion = (Config.tnTargetVersion).split(" ")[0];

		postman.changeJSONKey("tenant-id", Config.tnName, postmanEnvioromentPath);
		postman.changeJSONKey("tenant-lan-domain", Config.tnHost, postmanEnvioromentPath);
		postman.changeJSONKey("tenant-dmz-domain", Config.tnHost, postmanEnvioromentPath);
		postman.changeJSONKey("oc-access-token", Config.ocOauth2Token, postmanEnvioromentPath);
		postman.changeJSONKey("stable-appliance-version", stableBundleVersion, postmanEnvioromentPath);
		postman.changeJSONKey("target-appliance-version", targetBundleVersion, postmanEnvioromentPath);
		postman.changeJSONKey("tenant-management-token", Config.tnManagementToken, postmanEnvioromentPath);
		postman.changeJSONKey("tenant-authentication-token", Config.tnAuthenticationToken, postmanEnvioromentPath);

	    
		String command = "newman run "+ postmanSetTenantPath +" -e "+ postmanEnvioromentPath;	
		utils.Terminal.execute(command);		
		
		Response response = API.getTenantStatus(Config.tnName);
		JsonObject jsonObject = JsonParser.parseString(response.getContent()).getAsJsonObject();

		String authenticationApi = jsonObject.getAsJsonObject("access-tokens").get("authentication-api").getAsString();
        String userApi = jsonObject.getAsJsonObject("access-tokens").get("user-api").getAsString();
        String adminApi = jsonObject.getAsJsonObject("access-tokens").get("admin-api").getAsString();

        postman.changeJSONKey("tenant-management-token", adminApi, postmanEnvioromentPath);
        postman.changeJSONKey("tenant-authentication-token", authenticationApi, postmanEnvioromentPath);
	}

		
	@Test(priority=3)
	public void runPostmanTenantTest() throws Exception{
		Postman postman = new Postman();
		String postmanTenantCollectionPath = postman.getPostmanTenantCollectionPath();
		String postmanEnvioromentPath = postman.getPostmanEnvironmentPath();
	  String reportFolderPath = Paths.get(Config.reportPath).getParent().toString();
		
		String command = "newman run -k "+ postmanTenantCollectionPath +" -e "+ postmanEnvioromentPath+ " -r htmlextra --reporter-htmlextra-export "+ reportFolderPath + " --reporter-htmlextra-displayProgressBar  --reporter-htmlextra-title 'Safewalk API Test - Tenant'";	
		utils.Terminal.execute(command);	
	}
	
}
