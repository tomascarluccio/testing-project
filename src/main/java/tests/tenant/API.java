package tests.tenant;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import configs.Config;
import utils.Request;
import utils.Response;

public class API {
	
	
	public static Response authenticate (String username, String password) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/authenticate/");

		request.setHeaders(headers);
		request.setConnectionTimeout(100001);
		request.setReadTimeout(100001);
		request.setVerifySsl(false);
		String data = "username=" + username + "&password=" + password;
		request.setData(data);
		return request.doPost();

	}

	public static Response authenticateTimeout (String username, String password, int timeout) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/authenticate/");
		request.setHeaders(headers);
		request.setConnectionTimeout(timeout);
		request.setReadTimeout(timeout);
		request.setVerifySsl(false);
		
		String data = "username=" + username + "&password=" + password;
		request.setData(data);
		return request.doPost();
	}

	
	public static Response authenticateTransaction(String username, String password, int timeout, String hash, String data, String title, String body) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/push_signature/");		
		request.setHeaders(headers);
		request.setConnectionTimeout(timeout);
		request.setReadTimeout(timeout);
		request.setVerifySsl(false);
		String data2 = "username=" + username + "&password=" + password+"&hash="+hash+"&data="+data+"=Sign transaction&body=Push signature triggered from safewalk API";
		request.setData(data2);
		Response response = request.doPost();
		return response;
	}
	
	public static Response authenticateAsyncPush (String username, String password, int timeout) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/authenticate/?apply_push_async=True");
		
		request.setHeaders(headers);
		request.setConnectionTimeout(timeout);
		request.setReadTimeout(timeout);
		request.setVerifySsl(false);
		String data = "username=" + username + "&password=" + password;
		request.setData(data);
		Response response = request.doPost();
		
		JSONParser parser02 = new JSONParser();
		JSONObject jsonRes02 = (JSONObject) parser02.parse(response.getContent());			
		String challenge = (String) jsonRes02.get("challenge");
		
		Instant instant = Instant.now().plus(2, ChronoUnit.MINUTES);
		Response res = new Response();
		
		while(Instant.now().isBefore(instant)) {
			res = API.verifySessionKey(challenge);  
			if(res.getCode() == 200) {
					if(res.getProperty("code").equals("ACCESS_ALLOWED") || res.getProperty("code").equals("ACCESS_DENIED"))
						return res;
				}
			Thread.sleep(5000);
		}
		
		throw new Exception("Timeout");
	}
	
	public static Response authenticateSessionKey() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/json");
		headers.put(null, null);
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/session_key/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doPost();
		return response;
	}
	
	public static Response verifySessionKey(String challenge) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnAuthenticationToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/session_key/"+challenge+"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	
	
	public static Response allowAuthWithPassword (String username , boolean allow) throws IOException{
		
		HashMap<String, String>  headers = new HashMap<String, String>();	
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
	    headers.put("Content-Type", "application/x-www-form-urlencoded");
	
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username +"/settings/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		if (allow) {
			request.setData("allow_password=true&allow_password_for_registration=true");		
		}
		else {
			request.setData("allow_password=false&allow_password_for_registration=false");
		}	
		Response response = request.doPut();
			
		return response;
	}
	
	public static Response allowWithPasswordExpired (String username , boolean allow) throws IOException{
		
		HashMap<String, String>  headers = new HashMap<String, String>();	
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
	    headers.put("Content-Type", "application/x-www-form-urlencoded");
	
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username +"/settings/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		if (allow) {
			request.setData("allow_access_when_pwd_expired=true&allow_password_reset_when_forgot_pwd=true");		
		}
		else {
			request.setData("allow_access_when_pwd_expired=false&allow_password_reset_when_forgot_pwd=false");
		}	
		Response response = request.doPut();
		return response;
	}
	
	public static Response resetFailedAttemptsCount (String username ) throws IOException{
		
		HashMap<String, String>  headers = new HashMap<String, String>();	
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
	    headers.put("Content-Type", "application/x-www-form-urlencoded");
	
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username +"/access_attempt/");
		request.setHeaders(headers);
		request.setVerifySsl(false);		
		Response response = request.doDelete();
		return response;
	}
	
	public static Response createUser (String username, String password) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();		
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("username=" + username + "&password=" + password);		
		Response response = request.doPost();
		return response;
	}
	

	public static Response createUserOrchestrator (String username, String password) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.ocOauth2Token);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();		
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/admin/user/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("username=" + username + "&password=" + password);		
		Response response = request.doPost();
		return response;
	}
	
	public static Response deleteUser(String username) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();		
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		
		Request request = new Request();		
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username +"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		String payLoad = "";
		request.setData(payLoad);		
		
		Response response = request.doDelete();
		return response;
	}
	
	public static Response deleteUserOrchestrator(String username) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();		
		headers.put("Authorization", "Bearer " + Config.ocAdminApiPort);
		
		Request request = new Request();		
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/admin/user/" + username +"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		String payLoad = "";
		request.setData(payLoad);		
		
		Response response = request.doDelete();
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static Response asociateLicense(String username, String deviceType) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("device_type", deviceType);
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username + "/devices/");
		request.setHeaders(headers);
		request.setVerifySsl(false);		
		request.setData(json.toJSONString());
		
		Response response = request.doPost();
		return response;
	}
	
	public static String getBackupCode(String username) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
	
		JSONObject json = new JSONObject();
		json.put("username", username);
		json.put("device_type", "Backup");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username + "/devices/");
		request.setHeaders(headers);
		request.setVerifySsl(false);		
		request.setData(json.toJSONString());
		
		Response response = request.doPost();
		
		JSONParser parser01 = new JSONParser();
		JSONObject jsonRes01 = (JSONObject) parser01.parse(response.getContent());	

		String serialNumber = (String) jsonRes01.get("serial_number");
		String associationInstaceId = (String) jsonRes01.get("association_instance_id");
		
		JSONObject json2 = new JSONObject();
		json2.put("device_type", "Backup");
		
		Request request01  = new Request();
		request01.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/devices/Backup/"+serialNumber+"/"+associationInstaceId+"/code/");
		request01.setHeaders(headers);
		request01.setVerifySsl(false);		
		request01.setData(json2.toJSONString());

		Response response01 = request01.doGet();	
		JSONParser parser02 = new JSONParser();
		JSONObject jsonRes02 = (JSONObject) parser02.parse(response01.getContent());	
	
		if(jsonRes02 != null)
			return jsonRes02.get("otp").toString();

		return "failed to generate backupcode";
	}


	
	public static Response getLicenses (String username) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username + "/devices/");
		request.setHeaders(headers);
		request.setVerifySsl(false);		
		
		Response response = request.doGet();
		return response;
	}
	
	public static String getTransactinsLogReason (String transactionsId) throws IOException, ParseException{
		HashMap<String, String>  headers = new HashMap<>();
		System.setProperty("javax.net.ssl.debug", "all");
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/transactionlog/?q="+transactionsId);
		request.setHeaders(headers);
		request.setVerifySsl(false);
	
		Response response = request.doGet();
		JSONParser parser01 = new JSONParser();
		JSONObject jsonRes01 = (JSONObject) parser01.parse(response.getContent());	
		JSONArray jsonArr  = (JSONArray) jsonRes01.get("results");
		String reason = "";
		
		for(int n = 0; n < jsonArr.size(); n++)
		{
		    JSONObject object = (JSONObject) jsonArr.get(n);
		    reason =  (String) object.get("reason");
		}
		
		return reason;
	}
	
	
	public static Response createRadiusClient (String address, String radiusClientName, String secret, String gateway) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		
		Request request = new Request();		
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/radiusclient/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("address=" + address + "&name=" + radiusClientName + "&secret=" + secret + "&gateway=" + gateway + "&is_active=true");		
		
		Response response = request.doPost();
		return response;
	}
	
	public static Response listRadiusClient() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/radiusclient/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	
	public static Response listMessagingGateways() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/gatewayaccount/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	 public static Response getRadiusClient(String radiusClientName) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/radiusclient/"+radiusClientName+"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}

	public static Response updateRadiusClient (String address, String radiusClientName, String secret, String gateway) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		Request request = new Request(); 
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/radiusclient/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("address=" + address + "&name=" + radiusClientName + "&secret=" + secret + "&gateway=" + gateway + "&is_active=true");		
		
		Response response = request.doPut();
		return response;		
	}
	
	
	 public static Response releaseLicenses (String username) throws IOException, ParseException{
		Response license_response = API.getLicenses(username);
		Response response =null;
		try {
			JSONParser parser01 = new JSONParser();
			if(license_response.getContent().equals("[]"))
				return null;
	
			JSONArray jsonArr01 = null;
			if( parser01.parse(license_response.getContent()).getClass().equals(JSONArray.class)) {
				 jsonArr01 = (JSONArray) parser01.parse(license_response.getContent());			
			for (int i=0;i<jsonArr01.size();i++) {
				JSONObject jsonObj = (JSONObject) jsonArr01.get(i);
				
				HashMap<String, String>  headers = new HashMap<>();
				headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
				headers.put("Content-Type", "application/json");
				
				Request request = new Request();
				request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/" + username + "/devices/" + jsonObj.get("device_type") + "/" + jsonObj.get("serial_number") + "/");
				request.setHeaders(headers);
				request.setVerifySsl(false);		
				
				 response = request.doDelete();
		   		 request.doDelete();
		    	}
			}
		}catch(Exception e) {
			
		}
		return response;
	}
	 
	 public static Response getTenantStatus(String tenantId) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.ocOauth2Token);
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/multitenancy/customer/"+tenantId+"/status/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	
	public static Response createLDAPConfiguration(String name, String domain, String bindDn, String bindPassword, String rootDn, String userSearch, String priority, String ldapType, String ldapServer) throws Exception{

		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		 
		Request request = new Request(); 
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort  + "/api/v2/admin/ldapconfiguration/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("name=" + name + "&domain=" + domain + "&bind_dn=" + bindDn + "&bind_password=" + bindPassword + "&user_search=" + userSearch + "&priority=" + priority+ "&server=ldaps://" + ldapServer + "&ldap_type=" + ldapType + "&root_dn=" + rootDn +"&is_active=true"); 

		Response response = request.doPost();
		return response;
	}
	
	public static Response listLDAPConfigurations() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/ldapconfiguration/");
		
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	 
	public static Response getLDAPConfiguration(String ldapDomain) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " +Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/ldapconfiguration/" + ldapDomain + "/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	
	public static Response updateLDAPConfiguration(String name, String domain, String bindDn, String bindPassword, String rootDn, String userSearch, String priority, String ldapType, String ldapServer) throws Exception{

		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		 
		Request request = new Request(); 
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort  + "/api/v2/admin/ldapconfiguration/" + domain +"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("name=" + name + "&domain=" + domain + "&bind_dn=" + bindDn + "&bind_password=" + bindPassword + "&user_search=" + userSearch + "&priority=" + priority+ "&server=ldaps://" + ldapServer + "&ldap_type=" + ldapType + "&root_dn=" + rootDn +"&is_active=true"); 

		Response response = request.doPut();
		return response;
	}

	public static Response createAgentConnector(String gateway, String name, String maxConnections) throws Exception{

		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		 
		Request request = new Request(); 
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort  + "/api/v2/admin/agentconnector/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("gateway=" + gateway + "&name=" + name + "&max_connections=" + maxConnections); 

		Response response = request.doPost();
		return response;
	}
	
	public static Response listAgentConnectors() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/agentconnector/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	
	public static Response getAgentConnector(String agentConnectorName) throws Exception{
				HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/agentconnector/"+agentConnectorName+"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;

	}
	
	public static void getAgentConnectorPackage(String agentConnectorName) throws Exception{
		Response listResponse = listAgentConnectors();
		
		JsonObject jsonObject = JsonParser.parseString(listResponse.getContent()).getAsJsonObject();
		JsonArray results = jsonObject.getAsJsonArray("results");
		String id = null;
		for(JsonElement result:results) {			
			if(result.getAsJsonObject().get("name").toString().replaceAll("\"", "").equals(agentConnectorName)) {
				id = result.getAsJsonObject().get("id").toString();
			}
		}
		
		if(id == null) {
			return;
		}
		
		URL url = new URL("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/agentconnector/"+id+"/getpackage/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
        InputStream inputStream = conn.getInputStream();
        FileOutputStream outputStream = new FileOutputStream(Config.downloadFolder+"/agent_connector_"+agentConnectorName+".zip");
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
	}
	
	
	
	public static Response getApplianceVersion() throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.ocOauth2Token);
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/multitenancy/appliance_version/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		
		Response response = request.doGet();
		return response;
	}
	

	public static Response insertLicensesToTenant(String tenantName, String licenseType, String count) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.ocOauth2Token);
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		 
		Request request = new Request(); 
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/multitenancy/customer/"+tenantName+"/insert_licenses/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("device_type=" + licenseType + "&count=" + count ); 

		Response response = request.doPost();
		return response;
	}
	
	public static Response getTenantInfo(String tenantName) throws Exception{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.ocOauth2Token);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		Request request = new Request();
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/multitenancy/customer/"+tenantName+"/");
		request.setHeaders(headers);
		request.setVerifySsl(false);

		Response response = request.doGet();
		return response;
	}

	 public static Response setTOTPInputMethod (String inputMethod) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/json");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/organizationdefaultsettings/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
		     switch (inputMethod) {
				case "Suffix-OTP-6":
					inputMethod =  "-6";
					break;
				case "Prefix-OTP-6":
					inputMethod =  "6";
					break;
			   default:
					inputMethod= null;
					break;
			}
			JSONObject json2 = new JSONObject();
			json2.put("totp_password_required_otp_input_method", inputMethod);
			
			request.setData(json2.toJSONString());	
			Response response = request.doPatch();
			return response;
		}
	 
	 public static Response setUserTempLockAttempts(String username, String count) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/json");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/");
            request.setHeaders(headers);
			request.setVerifySsl(false);
		  	JSONObject json2 = new JSONObject();
			json2.put("dos_tmp_lockdown_max_fail_authentications",count);
			
			request.setData(json2.toJSONString());	
			Response response = request.doPut();
			return response;
		}
	 
	 public static Response setTOTPPasswordRequired (String username, boolean allow) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/x-www-form-urlencoded");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/settings/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
		
			if (allow) {
				request.setData("totp_password_required=true&totp_mobile_password_required=true");	
			}
			else {
				request.setData("totp_password_required=false&totp_mobile_password_required=false");	
			}
			
			Response response = request.doPut();
			return response;
		}
	 
	 public static Response setBackupCodePasswordRequired(String username, boolean allow) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/x-www-form-urlencoded");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/settings/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
		
			if (allow) {
				request.setData("backup_password_required=true");	
			}
			else {
				request.setData("backup_password_required=false");	
			}
		
			Response response = request.doPut();
			return response;
		}
	

	 public static Response setVirtualPasswordRequired(String username, boolean allow) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/x-www-form-urlencoded");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/settings/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
		
			if (allow) {
				request.setData("virtual_password_required=true");	
			}
			else {
				request.setData("viertual_password_required=false");	
			}
		
			Response response = request.doPut();
			return response;
		}
	 
	 public static Response setUserPersonalInformation(String username, String email, String mobile) throws IOException{
			
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/x-www-form-urlencoded");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
		
			request.setData("email="+email+"&mobile_phone="+mobile);	
		
			Response response = request.doPut();
				return response;
	}
	 
	 
	 public static Response getUserStatus(String username) throws IOException{
			HashMap<String, String>  headers = new HashMap<String, String>();	
			headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		    headers.put("Content-Type", "application/x-www-form-urlencoded");
			Request request = new Request();
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/"+username+"/");
			request.setHeaders(headers);
			request.setVerifySsl(false);
			Response response = request.doGet();
			return response;
	}
}
