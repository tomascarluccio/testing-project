package utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class Request {

	private String url;
	private HashMap<String, String> headers;
	private int connectionTimeout;
	private int readTimeout;
	private boolean allowRedirect;
	private boolean verifySsl;
	private String data;

	public Request() {
		this.connectionTimeout = 5000;
		this.readTimeout = 5000;
		this.allowRedirect = true;
		this.verifySsl = true;
	}


	public Response base(String method) throws IOException {
		
		if (!this.verifySsl) {
			HttpsTrustManager.allowAllSSL();
		}
		
		URL url = new URL(this.url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();		
		if (method.equals("PATCH")) {
			con.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			method = "POST";
		}
		
		con.setRequestMethod(method);
		con.setInstanceFollowRedirects(this.allowRedirect);
		con.setConnectTimeout(this.connectionTimeout);
		con.setReadTimeout(this.readTimeout);
		

		// Set headers
		if (this.headers != null) {
			for (Map.Entry<String, String> item : this.headers.entrySet()) {
				con.setRequestProperty(item.getKey(), item.getValue());
			}
		}
		
		// Set parameters
		if (this.data!=null) {
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(this.data.getBytes());
			os.flush();
			os.close();
		}
		
		Response response = new Response();
		
		// Get status code
		int code = con.getResponseCode();
		response.setCode(code);
		
		// Get response content
		InputStreamReader inputStreamReader;
		if (code >= 200 && code < 400) {
			inputStreamReader = new InputStreamReader(con.getInputStream());
		} else {
			inputStreamReader = new InputStreamReader(con.getErrorStream());
		}
				
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line;
		StringBuilder contentBuffer = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			contentBuffer.append(line);
		}
		bufferedReader.close();
		String content = contentBuffer.toString();
		response.setContent(content);
		
		return response;
	}
	
	public Response doGet() throws IOException {
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(this.connectionTimeout)
				.setConnectionRequestTimeout(this.readTimeout)
			    .setSocketTimeout(this.readTimeout)
			    .build();
		
		CloseableHttpClient httpClient;
		
		if (!this.verifySsl){
			httpClient = HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.build();	
		} else {
			httpClient = HttpClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.build();
		}
		
		HttpGet httpGet = new HttpGet(this.url);
		
		// Set headers		
		if (this.headers != null) {
			for (Map.Entry<String, String> item : this.headers.entrySet()) {
				httpGet.addHeader(item.getKey(), item.getValue());
			}
		}

        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);
        
        Response response = new Response();
		
		// Get status code
		response.setCode(closeableHttpResponse.getStatusLine().getStatusCode());
		
		// Get response content       
        HttpEntity entity = closeableHttpResponse.getEntity();
        if (entity != null) {
            response.setContent(EntityUtils.toString(entity));
        }
        
        return response;
	}
	
	public Response doPost() throws IOException {
		return this.base("POST");
	}
	
	public Response doPut() throws IOException {
		return this.base("PUT");
	}
	
	public Response doPatch() throws IOException {
		return this.base("PATCH");
	}
	
	public Response doDelete() throws IOException {
		return this.base("DELETE");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public boolean isAllowRedirect() {
		return allowRedirect;
	}

	public void setAllowRedirect(boolean allowRedirect) {
		this.allowRedirect = allowRedirect;
	}
	
	public boolean isVerifySsl() {
		return this.verifySsl;
	}

	public void setVerifySsl(boolean verifySsl) {
		this.verifySsl = verifySsl;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}