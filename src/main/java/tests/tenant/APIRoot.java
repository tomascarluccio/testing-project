package tests.tenant;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class APIRoot {
	
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
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAuthApiPort + "/api/v1/auth/");
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
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/user/");
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
	

	public static Response getLicenses (String username) throws IOException{
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/json");
		
		Request request = new Request();
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin/");
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
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v1/admin");
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
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("address=" + address + "&name=" + radiusClientName + "&secret=" + secret + "&gateway=" + gateway + "&is_active=true");		
		
		Response response = request.doPost();
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
		Response license_response = APIRoot.getLicenses(username);
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
	 

	public static Response createLDAPConfiguration(String name, String domain, String bindDn, String bindPassword, String rootDn, String userSearch, String priority, String ldapType, String ldapServer) throws Exception{

		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.getFsProperty("tnManagementToken"));
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		 
		Request request = new Request(); 
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort  + "/api/v2/admin/");
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData("name=" + name + "&domain=" + domain + "&bind_dn=" + bindDn + "&bind_password=" + bindPassword + "&user_search=" + userSearch + "&priority=" + priority+ "&server=ldaps://" + ldapServer + "&ldap_type=" + ldapType + "&root_dn=" + rootDn +"&is_active=true"); 

		Response response = request.doPost();
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
		request.setUrl("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/agentconnector");;
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
		
		URL url = new URL("https://" + Config.tnHost + ":" + Config.tnAdminApiPort + "/api/v2/admin/agentconnector/");
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
		request.setUrl("https://" + Config.ocHost + ":" + Config.ocAdminApiPort + "/api/v1/multitenancy/");
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
			System.out.println(response.getContent() +" "+response.getCode());
			
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
