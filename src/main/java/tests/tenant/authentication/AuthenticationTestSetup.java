package tests.tenant.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import configs.Config;
import pages.orchestrator.ImportLicensesPage;
import pages.tenant.superadmin.AgentConnectorPage;
import pages.tenant.superadmin.DashboardPage;
import pages.tenant.superadmin.DefaultSettingsPage;
import pages.tenant.superadmin.ImportDataFilePage;
import pages.tenant.superadmin.LDAPConfigurationPage;
import pages.tenant.superadmin.MessageDeliveryGatewayPage;
import pages.tenant.superadmin.RadiusClientPage;
import reports.Report;
import tests.tenant.API;
import tests.tenant.superadmin.LoginTest;
import utils.Response;
import utils.Terminal;



public class AuthenticationTestSetup extends LoginTest {

	@DataProvider(name = "testSetup" , parallel=false)
	public Object[][] getData() {
		return new Object[][] { 
			{Config.agentConnectorName, Config.radiusBrokerClientName, Config.radiusServerClientName,  Config.ldapName},
		};
	}
	
	@Test( dataProvider="testSetup",priority = 2)
	public void saveAPITokens(String agentConnectorName,  String radiusBrokerClientName ,String radiusServerClientName,  String ldapName) throws Exception {

		Config.setFsProperty("tnManagementToken", Config.tnManagementToken);
 		Config.setFsProperty("tnAuthenticationToken", Config.tnAuthenticationToken);
		Config.setFsProperty("tnSSPToken", Config.tnSSPToken);
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver,this.wait);
		superAdminDashboardPage.getHomeLink().click();
		//Config.radiusServerPort = API.getTenantInfo(Config.tnName).getProperty("radius_auth_port");
		Config.setFsProperty("radiusServerPort", Config.radiusServerPort);
		System.out.println(Config.getFsProperty("radiusServerPort"));
		Response res = API.insertLicensesToTenant(Config.tnName, Config.deviceTypeGP, Config.initTenantLicensesCount);
		if(res.getCode() != 200 && res.getCode() != 201) {
			throw new Exception(res.getCode()+"  content"+res.getContent());
		}
	}

	@Test( dataProvider="testSetup",priority = 3)
	public void setupRadiusclient(String agentConnectorName, String radiusBrokerClientName ,String radiusServerClientName,  String ldapName) throws Exception {
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getRadiusClientsLink().click();

		RadiusClientPage radiusPage =  new RadiusClientPage(this.driver, this.wait);
		boolean radiusBrokerClientNameExists  =radiusPage.existsRadiusClient(radiusBrokerClientName);
		if(!radiusBrokerClientNameExists) {
			radiusPage.getAddRadiusClientLink().click();
			radiusPage.getRadiusClientName().click();
			radiusPage.getRadiusClientName().sendKeys(radiusBrokerClientName);
			radiusPage.getRadiusClientAddress().click();
			radiusPage.getRadiusClientAddress().sendKeys(Config.radiusBrokerClientAddress);
			radiusPage.getRadiusClientSharedSecret().click();
			radiusPage.getRadiusClientSharedSecret().sendKeys(Config.radiusSharedSecret);
			Select gatewaySelect = new Select(radiusPage.getRadiusClientGateway());
			gatewaySelect.selectByVisibleText(Config.gatewayName);

		    radiusPage.getSavebutton().click();
		}

		boolean radiusServerClientNameExists  =radiusPage.existsRadiusClient(radiusServerClientName);

		if(!radiusServerClientNameExists) {
			radiusPage.getAddRadiusClientLink().click();
			radiusPage.getRadiusClientName().click();
			radiusPage.getRadiusClientName().sendKeys(radiusServerClientName);
			radiusPage.getRadiusClientAddress().click();
			radiusPage.getRadiusClientAddress().sendKeys(Config.radiusServerClientAddress);
			radiusPage.getRadiusClientSharedSecret().click();
			radiusPage.getRadiusClientSharedSecret().sendKeys(Config.radiusSharedSecret);
		    radiusPage.getSavebutton().click();
		}
		superAdminDashboardPage.getHomeLink().click();

	}


	@Test( dataProvider="testSetup",priority = 4)
	public void setupAgentConnector(String agentConnectorName,String radiusBrokerClientName ,String radiusServerClientName, String ldapName) throws Exception {
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		superAdminDashboardPage.getAgentConnectorLink().click();
		AgentConnectorPage agentConnectorPage = new AgentConnectorPage(this.driver, this.wait);

		boolean agentConnectorExits = agentConnectorPage.existsAgentConnector(agentConnectorName);
		if(!agentConnectorExits) {
			Thread.sleep(10000);
		    Files.deleteIfExists(Paths.get(Config.downloadFolder+"/agent_connector_"+agentConnectorName+".zip"));

			agentConnectorPage.getAddAgentConnectorLink().click();
			agentConnectorPage.getName().click();
			agentConnectorPage.getName().sendKeys(agentConnectorName);
			agentConnectorPage.getWebCertificateButton().sendKeys(Config.tnWebCertificatePath);
			agentConnectorPage.getSaveAndContinueButton().click();
			agentConnectorPage.getDownloadLink().click();


		    Properties config = new java.util.Properties();
		    config.put("StrictHostKeyChecking", "no");
		    JSch jsch = new JSch();
		    Session session=jsch.getSession(Config.agentUsername, Config.agentHost, 22);
		    session.setPassword(Config.agentPassword);
		    session.setConfig(config);
		    session.connect();

		    Channel channel = session.openChannel("sftp");
		    channel.connect();
		    ChannelSftp sftpChannel = (ChannelSftp) channel;
		    sftpChannel.put(new FileInputStream(new File(Config.downloadFolder+"/agent_connector_"+agentConnectorName+".zip")), "/root/agent_connector_"+agentConnectorName+".zip");
		    sftpChannel.exit();

		    Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername, Config.agentPassword, "unzip -o /root/agent_connector_"+agentConnectorName+".zip -d /etc/agent");
		    Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername,  Config.agentPassword, "supervisorctl restart all");
		    Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername, Config.agentPassword, "rm -rf /root/agent_connector_"+agentConnectorName+".zip");

		    agentConnectorPage.getSavebutton().click();
//			Thread.sleep(15000);
//		    Files.deleteIfExists(Paths.get(Config.downloadFolder+"/agent_connector_"+agentConnectorName+".zip"));

		}

		superAdminDashboardPage.getHomeLink().click();
		superAdminDashboardPage.getIamSettingsTab().click();
	    superAdminDashboardPage.getLDAPLink().click();

	}

	@Test( dataProvider="testSetup",priority = 5)
	public void setupLDAPConfiguration(String agentConnectorName,String radiusBrokerClientName ,String radiusServerClientName,  String ldapName) throws Exception {
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
		else {
			ldapConfigurationPage.getLDAPNameLink(ldapName).click();
			Select select = new Select(ldapConfigurationPage.getGateway());
			select.selectByVisibleText("---------");
			ldapConfigurationPage.getSavebutton().click();
//
//			ldapConfigurationPage.getLDAPNameLink(ldapName).click();
//			Select select = new Select(ldapConfigurationPage.getGateway());
//			select.selectByVisibleText("__mt__");
//			ldapConfigurationPage.getTestConnectionButton().click();


		}

//		wait.until(ExpectedConditions.visibilityOf(ldapConfigurationPage.getOkAlert()));
//		ldapConfigurationPage.getOkAlert().click();
//		ldapConfigurationPage.getSavebutton().click();
	}


	@Test( dataProvider="testSetup",priority = 6)
	public void setupMessagingGateway(String agentConnectorName,String radiusBrokerClientName ,String radiusServerClientName,  String ldapName) throws Exception {
		DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(superAdminDashboardPage.getUrl());
		superAdminDashboardPage.getSettingsTab().click();;
		superAdminDashboardPage.getMessageDeliveryGatewayLink().click();

		MessageDeliveryGatewayPage messageDeliveryGatewayPage = new MessageDeliveryGatewayPage(this.driver, this.wait);

		if(!messageDeliveryGatewayPage.existsSMTPGateway("Generic HTTP(S)")) {
			messageDeliveryGatewayPage.getImportLink().click();
			ImportDataFilePage importLicensesPage = new ImportDataFilePage(this.driver, this.wait);
			importLicensesPage.getFileInput().sendKeys(Config.resourcesFolder + "/GenericGatewayAccount.csv");
			importLicensesPage.getSubmitButton().click();
			importLicensesPage.getConfirmButton().click();

			superAdminDashboardPage.getHomeLink().click();
			superAdminDashboardPage.getSettingsTab().click();
			superAdminDashboardPage.getDefaultSettingsLink().click();

			DefaultSettingsPage settingsPage = new DefaultSettingsPage(this.driver, this.wait);
			settingsPage.getVirtualSettingsLink().click();
			String json = "{ \"gateways\":[ { \"priority\":1, \"text\":\"%(otp)s\", \"gateway\":\"SMS\", \"subject\":\"Your OTP code\" } ] }";
			settingsPage.getVirtualJsonbox().clear();
			settingsPage.getVirtualJsonbox().sendKeys(json);
			settingsPage.getSaveButton().click();
		}

//		}

	}

	@Test( dataProvider="testSetup" , priority = 7)
	@Report(name = "Tokens Import Test", description="Imports a file with 4 physical token licenses")
	public void importTokens( String agentConnectorName,String radiusBrokerClientName ,String radiusServerClientName,  String ldapName) throws Exception {
		if(Config.safewalkMode.equals("v5")) {
			this.wait = new WebDriverWait(driver, Duration.ofMinutes(4));
			this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(4));
			this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(4));
			DashboardPage superAdminDashboardPage = new DashboardPage(this.driver, this.wait);
			this.driver.get(superAdminDashboardPage.getUrl());
			superAdminDashboardPage.getLicensesTab().click();
			superAdminDashboardPage.getTokenLincencesLink().click();
			this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			this.driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(40));
			ImportLicensesPage importLicensesPage = new ImportLicensesPage(this.driver, this.wait);
			importLicensesPage.getFileInput().sendKeys(Config.tokenLicensePath);
			importLicensesPage.getReplaceCheck().click();
			importLicensesPage.getUploadButton().click();
			this.wait.until(ExpectedConditions.visibilityOf(importLicensesPage.getConfirmAlert()));
			importLicensesPage.getYesPopup().click();
		}
	}
}

