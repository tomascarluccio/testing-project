package tests.tenant.superadmin;

import org.testng.annotations.DataProvider;
import configs.Config;


public class ImportTokensTest extends LoginTest {

	@DataProvider(name = "physicalTokensDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.tokenLicensePath}
		};
	}

	
	

}