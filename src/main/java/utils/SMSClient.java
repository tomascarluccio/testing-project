package utils;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import configs.Config;

public class SMSClient {
	
	public static void setMessage(String recipient, String message) throws Exception {
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.smsToken);
		headers.put("Content-Type", "application/json");
		
		JSONObject json = new JSONObject();
		json.put("recipient", recipient);
		json.put("message", message);
		
		Request request = new Request();
		request.setUrl("https://" + Config.smsHost + ":" + Config.smsPort.toString() + "/");
		request.setHeaders(headers);
		request.setData(json.toJSONString());
		request.setVerifySsl(false);
		
		Response response = request.doPost();
		if (response.getCode() != 200) {
			throw new Exception("Error to set message (" + response.getCode() + ")");
		}
	}
	
	
	public static String getMessage(String recipient) throws Exception {
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + Config.smsToken);
		headers.put("Content-Type", "application/json");
		
		JSONObject json1 = new JSONObject();
		json1.put("recipient", recipient);
		json1.put("message", "123456");
		
		
		Request request = new Request();//
		request.setUrl("https://" + Config.smsHost + "/?recipient=" + recipient);
		request.setHeaders(headers);
		request.setVerifySsl(false);
		request.setData(json1.toJSONString());
		
		Response response = request.doGet();
		if (response.getCode() == 200) {
		
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(response.getContent());	
			String message = (String) json.get("message");
			return message;
		} else {
			throw new Exception("Error to get message (" + response.getCode() + ")");
		}
	}

}
