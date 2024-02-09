package utils;

import static org.testng.Assert.assertEquals;

import com.imperva.ddc.core.Connector;
import com.imperva.ddc.core.query.ChangeRequest;
import com.imperva.ddc.core.query.ConnectionResponse;
import com.imperva.ddc.core.query.Endpoint;
import com.imperva.ddc.service.DirectoryConnectorService;

import configs.Config;

public class LDAPClient {

	public static void setUserMustChangePassword(String userDn) {

		Endpoint endpoint = new Endpoint();
		endpoint.setHost(Config.ldapHost);
		endpoint.setPort(Config.ldapPort);
		endpoint.setSecondaryPort(Config.ldapPort);
		endpoint.setUserAccountName(Config.ldapBindDn);
		endpoint.setPassword(Config.ldapBindPassword);
		endpoint.setSecuredConnection(false);
		endpoint.setSecuredConnectionSecondary(false);

		ConnectionResponse connectionResponse = DirectoryConnectorService.authenticate(endpoint);
		assertEquals(connectionResponse.isError(), false, "Failed to change change LDAP property");

		ChangeRequest changeRequest = new ChangeRequest(userDn);
		changeRequest.replace("pwdLastSet", "0");

		changeRequest.setEndpoint(endpoint);

		Connector connector = new Connector(changeRequest);
		connector.executeChangeRequest();
		connector.close();

	}

	public static void setPasswordExpired(String userDn, boolean allow) {

		Endpoint endpoint = new Endpoint();
		endpoint.setHost(Config.ldapHost);
		endpoint.setPort(Config.ldapPort);
		endpoint.setSecondaryPort(Config.ldapPort);
		endpoint.setUserAccountName(Config.ldapBindDn);
		endpoint.setPassword(Config.ldapBindPassword);
		endpoint.setSecuredConnection(false);
		endpoint.setSecuredConnectionSecondary(false);

		ConnectionResponse connectionResponse = DirectoryConnectorService.authenticate(endpoint);
		assertEquals(connectionResponse.isError(), false, "Faile to change change LDAP property");

		ChangeRequest changeRequest = new ChangeRequest(userDn);
		if(allow)
			changeRequest.replace("pwdLastSet", "1");
		else
			changeRequest.replace("pwdLastSet", "0");

		changeRequest.setEndpoint(endpoint);

		Connector connector = new Connector(changeRequest);
		connector.executeChangeRequest();
		connector.close();

	}



}