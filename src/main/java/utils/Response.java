package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Response {

	private int code;
	private String content;

	public Response() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProperty(String property) throws Exception {
		JSONParser parser02 = new JSONParser();
		JSONObject jsonRes02 = (JSONObject) parser02.parse(getContent());	
		return jsonRes02.get(property).toString();
	}
}
