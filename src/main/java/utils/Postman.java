package utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import configs.Config;

public class Postman {
	
	public void changeJSONKey(String key, String value, String filename) {
		
	       try (Reader reader = new FileReader(filename)) {     
	    	   System.out.println(filename);
	        	JSONParser postman_environment_parser = new JSONParser();
	            JSONObject postman_environment = (JSONObject) postman_environment_parser.parse(reader);     
	            
	            JSONArray old_values = (JSONArray) postman_environment.get("values");     
	            JSONArray new_values = new JSONArray();
	            Iterator<JSONObject> iterator = old_values.iterator();
	            while (iterator.hasNext()) {
	                JSONObject item = iterator.next();     
	                if (item.get("key").equals(key)) {
	                	item.replace("value", value);
	                }                
	                new_values.add(item);
	            }          
	            
	            postman_environment.replace("values", new_values);
	            
	            try (FileWriter file = new FileWriter(filename)) {
	                file.write(postman_environment.toJSONString());
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }          

	        } catch (Exception e) {
	        	e.printStackTrace();
	        }   
		
	}
	
	public String getPostmanOCColectionPath() {
		if (Config.postmanOcCollectionPath.isEmpty()) {			
			Config.postmanOcCollectionPath = System.getProperty("user.dir") + "/src/main/resources/API_Orchestrator.postman_collection.json";
		}
		return Config.postmanOcCollectionPath;
	}
	
	public String getPostmanTenantCollectionPath() {
		if (Config.postmanTenantCollectionPath.isEmpty()) {			
			Config.postmanTenantCollectionPath = System.getProperty("user.dir") + "/src/main/resources/API_V5.postman_collection.json";
		}
		return Config.postmanTenantCollectionPath;
	}

	public String getPostmanEnvironmentPath() {
		if (Config.postmanEnvironmentPath.isEmpty()) {			
			Config.postmanEnvironmentPath = System.getProperty("user.dir") + "/src/main/resources/qa-sfs-st-v5-01.postman_environment.json";
		}
		return Config.postmanEnvironmentPath;
	}
	

}
