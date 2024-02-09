package tests.orchestrator.postman;

import java.nio.file.Paths;

import org.testng.annotations.Test;

import configs.Config;
import tests.BaseAPITest;
import utils.Postman;


public class PostmanAPITest extends BaseAPITest{

		
	@Test(priority=2)
	public void runPostmanTenantTest() throws Exception{
		Postman postman = new Postman();
		
		String postmanOrchestratorCollectionPath = postman.getPostmanOCColectionPath();
		String postmanEnvioromentPath = postman.getPostmanEnvironmentPath();
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

	    String reportFolderPath = Paths.get(Config.reportPath).getParent().toString();

		String command = "newman run -k "+ postmanOrchestratorCollectionPath +" -e "+ postmanEnvioromentPath+ " -r htmlextra --reporter-htmlextra-export "+ reportFolderPath + " --reporter-htmlextra-displayProgressBar  --reporter-htmlextra-title 'Safewalk API Test - Orchestrator'";
		utils.Terminal.execute(command);
	}
	
}
