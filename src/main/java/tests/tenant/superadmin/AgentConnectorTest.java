package tests.tenant.superadmin;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import configs.Config;
import pages.tenant.superadmin.AgentConnectorPage;
import pages.tenant.superadmin.DashboardPage;
import reports.Report;
import utils.Terminal;


public class AgentConnectorTest extends LoginTest {

	
	@DataProvider(name = "agentConnectorDataProvider")
	public Object[][] getUserData() {
		return new Object[][] { 
			{Config.agentConnectorName, Config.ldapName}
		};
	}
	
	
	@Test( dataProvider = "agentConnectorDataProvider", priority = 2 )
	@Report(name="Agent Connector Test", description="No description")
	public void setupAgentConnector(String agentConnectorName, String ldapName) throws Exception {

	    Files.deleteIfExists(Paths.get(Config.downloadFolder+"/agent_connector_"+agentConnectorName+".zip"));

		DashboardPage dashboardPage = new DashboardPage(this.driver, this.wait);
		this.driver.get(dashboardPage.getUrl());
		dashboardPage.getAgentConnectorLink().click();
		
		this.wait = new WebDriverWait(driver, Duration.ofMinutes(5));
		this.driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(5));

		AgentConnectorPage agentConnectorPage = new AgentConnectorPage(this.driver, this.wait);
		
		if(!agentConnectorPage.existsAgentConnector(agentConnectorName)) {
			agentConnectorPage.getAddAgentConnectorLink().click();
			agentConnectorPage.getName().click();
			agentConnectorPage.getName().sendKeys(agentConnectorName);
			agentConnectorPage.getWebCertificateButton().sendKeys(Config.tnWebCertificatePath);
			agentConnectorPage.getSaveAndContinueButton().click();
			agentConnectorPage.getDownloadLink().click();


			Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(Config.agentUsername, Config.agentHost, 22);
			session.setPassword(Config.agentPassword);
			session.setConfig(config);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.put(new FileInputStream(new File(Config.downloadFolder + "/agent_connector_" + agentConnectorName + ".zip")), "/root/agent_connector_" + agentConnectorName + ".zip");
			sftpChannel.exit();

			Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername, Config.agentPassword, "unzip -o /root/agent_connector_" + agentConnectorName + ".zip -d /etc/agent");
			Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername, Config.agentPassword, "supervisorctl restart all");
			Terminal.execRemoteCommand(Config.agentHost, Config.agentPort, Config.agentUsername, Config.agentPassword, "rm -rf /root/agent_connector_" + agentConnectorName + ".zip");

			agentConnectorPage.getSavebutton().click();
		}
		
		// TODO: CHECK COONNECTION STATUS WHEN API IS AVAILABLE

		}
	
	
}
