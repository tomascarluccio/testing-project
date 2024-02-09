package tests.tenant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.sun.pkg.util.DerInputStream;
import com.sun.pkg.util.DerValue;

import configs.Config;
import utils.Request;
import utils.Response;

public class MobileSimulator {
	
	public  boolean initialized;
	public  String serialNumber;
	public  String accessToken;
	public  String totpKey;
	
	
	public String getPrivateKeyPath() throws URISyntaxException {
		return Config.resourcesFolder + "/fast_auth_private_key.der";
	}
	
	public String getCsrPath() throws URISyntaxException {
		return Config.resourcesFolder + "/fast_auth_csr.der";
	}
	
	public String getTsPrivateKeyPath() throws URISyntaxException {
		return Config.resourcesFolder + "/ts_fast_auth_private_key.der";
	}
	
	public void registerFastAuth(String username, String password) throws Exception{
		
		MobileSimulator mobileSimulator = new MobileSimulator();
		
		/*
		 * 01 - User authentication
		 */
		
		JSONObject jsonReq01 = new JSONObject();
		jsonReq01.put("username", username);
		jsonReq01.put("password", password);
        
		HashMap<String, String>  headers01 = new HashMap<>();
		headers01.put("Content-Type", "application/json");
		
        Request request01 = new Request();
	   request01.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/authenticate/");
        request01.setData(jsonReq01.toJSONString());
        request01.setHeaders(headers01);
        request01.setVerifySsl(false);
		
		Response response01 = request01.doPost();
		
		Assert.assertEquals(response01.getCode(), 200, response01.getContent()+"  "+"https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/authenticate/");
		
		JSONParser parser01 = new JSONParser();
		JSONObject jsonRes01 = (JSONObject) parser01.parse(response01.getContent());			
		
		String registrationOAuth2AccessToken = (String) jsonRes01.get("access_token");
		
		/*
		 * 02 - Get registration data
		 */
		
		JSONObject jsonReq02 = new JSONObject();
        
		HashMap<String, String>  headers02 = new HashMap<>();
		headers02.put("Authorization", "Bearer " + registrationOAuth2AccessToken);
		headers02.put("Content-Type", "application/json");
		
        Request request02 = new Request();
        request02.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/devices/Fast:Auth:Mobile:Asymmetric/");
        request02.setData(jsonReq02.toJSONString());
        request02.setHeaders(headers02);
        request02.setVerifySsl(false);
		
		Response response02 = request02.doPost();
		
		Assert.assertEquals(response02.getCode(), 200, response02.getContent());
		
		JSONParser parser02 = new JSONParser();
		JSONObject jsonRes02 = (JSONObject) parser01.parse(response02.getContent());			
		
		String registrationFastAuthSerialNumber = (String) jsonRes02.get("serial_number");
		String registrationFastAuthChallenge = (String) jsonRes02.get("challenge");
	
		
		/*
		 * 03 - Confirm registration
		 */
		
		// Get fast_auth_private_key.der
		String fastAuthPrivateKeyPath = mobileSimulator.getPrivateKeyPath();
		InputStream inputStream01 = new FileInputStream(fastAuthPrivateKeyPath);
		InputStreamReader inputStreamReader01 = new InputStreamReader(inputStream01);
		BufferedReader bufferReader01 = new BufferedReader(inputStreamReader01);
		StringBuilder stringBuilder01 = new StringBuilder();
		String line01;
		while ((line01 = bufferReader01.readLine()) != null) {
			stringBuilder01.append(line01);
		}
		String fastAuthPrivateKey = stringBuilder01.toString();
		
		fastAuthPrivateKey = fastAuthPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		fastAuthPrivateKey = fastAuthPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
        
        byte[] fastAuthPrivateKeyDecoded = Base64.getDecoder().decode(fastAuthPrivateKey);
        
        // Get public key from private key      
        DerInputStream derInputStreamReader = new DerInputStream(fastAuthPrivateKeyDecoded);
		DerValue[] derValueSequence = derInputStreamReader.getSequence(0);
		BigInteger modulus = derValueSequence[1].getBigInteger();
		BigInteger publicExponent = derValueSequence[2].getBigInteger();
		BigInteger privateExponent = derValueSequence[3].getBigInteger();
		BigInteger primeP = derValueSequence[4].getBigInteger();
		BigInteger primeQ = derValueSequence[5].getBigInteger();
		BigInteger primeExponentP = derValueSequence[6].getBigInteger();
		BigInteger primeExponentQ = derValueSequence[7].getBigInteger();
		BigInteger crtCoefficient = derValueSequence[8].getBigInteger();
		RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = privateKeyFactory.generatePrivate(rsaPrivateKeySpec);
                
        RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
        
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = publicKeyFactory.generatePublic(rsaPublicKeySpec);
        
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        PemObject pemObject = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        pemWriter.writeObject(pemObject);
        pemWriter.flush();
        pemWriter.close();
        String registrationFastAuthPublicKey = Base64.getEncoder().encodeToString(stringWriter.toString().getBytes());
        
        // Get csr request
		String fastAuthCsrPath = mobileSimulator.getCsrPath();
		InputStream inputStream02 = new FileInputStream(fastAuthCsrPath);
		InputStreamReader inputStreamReader02 = new InputStreamReader(inputStream02);
		BufferedReader bufferReader02 = new BufferedReader(inputStreamReader02);
		StringBuilder stringBuilder02 = new StringBuilder();
		String line02;
		while ((line02 = bufferReader02.readLine()) != null) {
			stringBuilder02.append(line02+"\n");
		}
		String registrationFastAuthCsr = stringBuilder02.toString();
        
		// Sign challenge
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(registrationFastAuthChallenge.getBytes(StandardCharsets.US_ASCII));
		byte[] unformattedSignature = signature.sign();
		String registrationFastAuthSignature = Base64.getEncoder().encodeToString(unformattedSignature);
		
		
		/*
		 *  INIT v2
		 */
		
		// Get ts_fast_auth_private_key.der
		String tsFastAuthPrivateKeyPath = mobileSimulator.getTsPrivateKeyPath();
		InputStream inputStream03 = new FileInputStream(tsFastAuthPrivateKeyPath);
		InputStreamReader inputStreamReader03 = new InputStreamReader(inputStream03);
		BufferedReader bufferReader03 = new BufferedReader(inputStreamReader03);
		StringBuilder stringBuilder03 = new StringBuilder();
		String line03;
		while ((line03 = bufferReader03.readLine()) != null) {
			stringBuilder03.append(line03);
		}
		String tsFastAuthPrivateKey = stringBuilder03.toString();
		
		tsFastAuthPrivateKey = tsFastAuthPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		tsFastAuthPrivateKey = tsFastAuthPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
        
        byte[] tsFastAuthPrivateKeyDecoded = Base64.getDecoder().decode(tsFastAuthPrivateKey);
        
        // Get public key from private key      
        DerInputStream tsDerInputStreamReader = new DerInputStream(tsFastAuthPrivateKeyDecoded);
		DerValue[] tsDerValueSequence = tsDerInputStreamReader.getSequence(0);
		BigInteger tsModulus = tsDerValueSequence[1].getBigInteger();
		BigInteger tsPublicExponent = tsDerValueSequence[2].getBigInteger();
		BigInteger tsPrivateExponent = tsDerValueSequence[3].getBigInteger();
		BigInteger tsPrimeP = tsDerValueSequence[4].getBigInteger();
		BigInteger tsPrimeQ = tsDerValueSequence[5].getBigInteger();
		BigInteger tsPrimeExponentP = tsDerValueSequence[6].getBigInteger();
		BigInteger tsPrimeExponentQ = tsDerValueSequence[7].getBigInteger();
		BigInteger tsCrtCoefficient = tsDerValueSequence[8].getBigInteger();
		RSAPrivateCrtKeySpec tsRsaPrivateKeySpec = new RSAPrivateCrtKeySpec(tsModulus, tsPublicExponent, tsPrivateExponent, tsPrimeP, tsPrimeQ, tsPrimeExponentP, tsPrimeExponentQ, tsCrtCoefficient);
        KeyFactory tsPrivateKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey tsPrivateKey = tsPrivateKeyFactory.generatePrivate(tsRsaPrivateKeySpec);
                
        RSAPrivateCrtKey tsRsaPrivateKey = (RSAPrivateCrtKey) tsPrivateKey;
        
        RSAPublicKeySpec tsRsaPublicKeySpec = new RSAPublicKeySpec(tsRsaPrivateKey.getModulus(), tsRsaPrivateKey.getPublicExponent());
        KeyFactory tsPublicKeyFactory = KeyFactory.getInstance("RSA");
        PublicKey tsPublicKey = tsPublicKeyFactory.generatePublic(tsRsaPublicKeySpec);
        
        StringWriter tsStringWriter = new StringWriter();
        PemWriter tsPemWriter = new PemWriter(tsStringWriter);
        PemObject tsPemObject = new PemObject("PUBLIC KEY", tsPublicKey.getEncoded());
        tsPemWriter.writeObject(tsPemObject);
        tsPemWriter.flush();
        tsPemWriter.close();
        
        //String tsRegistrationFastAuthPublicKey = Base64.getEncoder().encodeToString(tsStringWriter.toString().getBytes());
        String tsRegistrationFastAuthPublicKey = Base64.getEncoder().encodeToString(tsStringWriter.toString().getBytes());
		
		
		/*
		 *  END v2
		 */
		
		// Confirm
		JSONObject jsonReq03 = new JSONObject();
		jsonReq03.put("challenge", registrationFastAuthChallenge);
		jsonReq03.put("public_key", registrationFastAuthPublicKey);
		jsonReq03.put("ts_public_key", tsRegistrationFastAuthPublicKey);
		jsonReq03.put("signature", registrationFastAuthSignature);
		jsonReq03.put("csr", registrationFastAuthCsr);
				        
		HashMap<String, String>  headers03 = new HashMap<>();
		headers03.put("Authorization", "Bearer " + registrationOAuth2AccessToken);
		headers03.put("Content-Type", "application/json");
		
        Request request03 = new Request();
        request03.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v2/mobile/registration/devices/Fast:Auth:Mobile:Asymmetric/"+registrationFastAuthSerialNumber+"/confirmation/");
        request03.setData(jsonReq03.toJSONString());
        request03.setHeaders(headers03);
        request03.setVerifySsl(false);
		
		Response response03 = request03.doPost();
		
		Assert.assertEquals(response03.getCode(), 200, response03.getContent());
		
		JSONParser parser03 = new JSONParser();
		JSONObject jsonRes03 = (JSONObject) parser03.parse(response03.getContent());	
        
		String fastAuthSerialNumber = registrationFastAuthSerialNumber;
		String fastAuthAccessToken = (String) jsonRes03.get("access_token");

		// Save token and serial number in Config
		serialNumber = fastAuthSerialNumber;
		accessToken = fastAuthAccessToken;		
	}
	
	public void registerTOTP(String username, String password) throws Exception{
		
			/*
			 * 01 - User authentication
			 */
				
			JSONObject jsonReq01 = new JSONObject();
			jsonReq01.put("username", username);
			jsonReq01.put("password", password);
	        
			HashMap<String, String>  headers01 = new HashMap<>();
			headers01.put("Content-Type", "application/json");

			Request request01 = new Request();
	        request01.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/authenticate/");
	        request01.setData(jsonReq01.toJSONString());
	        request01.setHeaders(headers01);
	        request01.setVerifySsl(false);
			
			Response response01 = request01.doPost();
			
			Assert.assertEquals(response01.getCode(), 200, response01.getContent());
			
			JSONParser parser01 = new JSONParser();
			JSONObject jsonRes01 = (JSONObject) parser01.parse(response01.getContent());			
			
			String registrationOAuth2AccessToken = (String) jsonRes01.get("access_token");
			
			
			/*
			 * 02 - Get registration data
			 */
			
			JSONObject jsonReq02 = new JSONObject();
	        
			HashMap<String, String>  headers02 = new HashMap<>();
			headers02.put("Authorization", "Bearer " + registrationOAuth2AccessToken);
			headers02.put("Content-Type", "application/json");
			
	        Request request02 = new Request();
	        request02.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/devices/TOTP:Mobile/");
	        request02.setData(jsonReq02.toJSONString());
	        request02.setHeaders(headers02);
	        request02.setVerifySsl(false);
			
			Response response02 = request02.doPost();
			
			Assert.assertEquals(response02.getCode(), 200, response02.getContent());
			
			JSONParser parser02 = new JSONParser();
			JSONObject jsonRes02 = (JSONObject) parser02.parse(response02.getContent());	
			String registrationTOTPSerialNumber = (String) jsonRes02.get("serial_number");
			this.totpKey = (String) jsonRes02.get("key");
			
			
			/*
			 * 03 - Confirm registration
			 */
			String otp = getTOTPCode(Instant.now());
			
	        JSONObject jsonReq03 = new JSONObject();
            jsonReq03.put("otp_token", otp);
				     
    		HashMap<String, String>  headers03 = new HashMap<>();
		    headers03.put("Authorization", "Bearer " + registrationOAuth2AccessToken);
		    headers03.put("Content-Type", "application/json");
						
	        Request request03 = new Request();
		    request03.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/registration/devices/TOTP:Mobile/"+registrationTOTPSerialNumber+"/confirmation/");
		    request03.setData(jsonReq03.toJSONString());
	        request03.setHeaders(headers03);
		    request03.setVerifySsl(false);
						
		    Response response03 = request03.doPost();
		    Assert.assertEquals(response03.getCode(), 200, response03.getContent());
					
	   
			}
	
	public String getTOTPCode(Instant instant) throws Exception {
			int len = this.totpKey.length();
			byte[] data = new byte[len / 2];
			for (int i = 0; i < len; i += 2) {
				data[i / 2] = (byte) ((Character.digit(this.totpKey.charAt(i), 16) << 4) + Character.digit(this.totpKey.charAt(i + 1), 16));
			}
		
		    Key key = new SecretKeySpec(data, "SHA1");
		    TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
		  
		    return totp.generateOneTimePasswordString(key, instant);
	}
		
	public String getPasswordTOTPCode(Instant instant, String password, String inputMethod) throws Exception {
		int len = this.totpKey.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(this.totpKey.charAt(i), 16) << 4) + Character.digit(this.totpKey.charAt(i + 1), 16));
		}
	
	    Key key = new SecretKeySpec(data, "SHA1");
	    TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
	  
	    String otp = totp.generateOneTimePasswordString(key, instant);
	    
	    switch (inputMethod) {
			case "Suffix-OTP-6":
				return password+otp;
			case "Prefix-OTP-6":
				return otp+password;
			default:
				break;
		}
	    
	    return "Failed to genereta password + otp";
	}
	
	public void signChallenge(String challenge) throws Exception{
		this.signChallenge(challenge, false, false);
	}
	
	public void signChallenge(String challenge, boolean deny, boolean bad) throws Exception{
		MobileSimulator mobileSimulator = new MobileSimulator();
		
		String fastAuthSignature = "";
		if (!bad) {
			String fastAuthPrivateKeyPath = mobileSimulator.getPrivateKeyPath();
			InputStream inputStream01 = new FileInputStream(fastAuthPrivateKeyPath);
			InputStreamReader inputStreamReader01 = new InputStreamReader(inputStream01);
			BufferedReader bufferReader01 = new BufferedReader(inputStreamReader01);
			StringBuilder stringBuilder01 = new StringBuilder();
			String line01;
			while ((line01 = bufferReader01.readLine()) != null) {
				stringBuilder01.append(line01);
			}
			String fastAuthPrivateKey = stringBuilder01.toString();
			
			fastAuthPrivateKey = fastAuthPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
			fastAuthPrivateKey = fastAuthPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
	        
	        byte[] fastAuthPrivateKeyDecoded = Base64.getDecoder().decode(fastAuthPrivateKey);
	           
	        DerInputStream derInputStreamReader = new DerInputStream(fastAuthPrivateKeyDecoded);
			DerValue[] derValueSequence = derInputStreamReader.getSequence(0);
			BigInteger modulus = derValueSequence[1].getBigInteger();
			BigInteger publicExponent = derValueSequence[2].getBigInteger();
			BigInteger privateExponent = derValueSequence[3].getBigInteger();
			BigInteger primeP = derValueSequence[4].getBigInteger();
			BigInteger primeQ = derValueSequence[5].getBigInteger();
			BigInteger primeExponentP = derValueSequence[6].getBigInteger();
			BigInteger primeExponentQ = derValueSequence[7].getBigInteger();
			BigInteger crtCoefficient = derValueSequence[8].getBigInteger();
			RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
	        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = privateKeyFactory.generatePrivate(rsaPrivateKeySpec);
			
	        String data01 = "";
	        
	        byte[] challengeDecodedBytes = Base64.getDecoder().decode(challenge);
	        String challengeDecodedString = new String(challengeDecodedBytes);
	        
	        JSONParser parser01 = new JSONParser();  
	        JSONObject jsonObject = (JSONObject) parser01.parse(challengeDecodedString);
			JSONArray jsonArray = (JSONArray) jsonObject.get("data");
			
			Iterator iterator = jsonArray.iterator();
			while (iterator.hasNext()){
				String value = (String) iterator.next();
				data01 = value;
				break;
			}
			
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(data01.getBytes(StandardCharsets.US_ASCII));
			byte[] unformattedSignature = signature.sign();
			fastAuthSignature = Hex.encodeHexString(unformattedSignature);
		} else {
			fastAuthSignature = Hex.encodeHexString(("bad_signature").getBytes());
		}
		
		/*
		 * INIT v2
		 */

		String tsFastAuthPrivateKeyPath = mobileSimulator.getPrivateKeyPath();
		InputStream inputStream02 = new FileInputStream(tsFastAuthPrivateKeyPath);
		InputStreamReader inputStreamReader02 = new InputStreamReader(inputStream02);
		BufferedReader bufferReader02 = new BufferedReader(inputStreamReader02);
		StringBuilder stringBuilder02 = new StringBuilder();
		String line02;
		while ((line02 = bufferReader02.readLine()) != null) {
			stringBuilder02.append(line02);
		}
		String tsFastAuthPrivateKey = stringBuilder02.toString();
		
		tsFastAuthPrivateKey = tsFastAuthPrivateKey.replace("-----BEGIN RSA PRIVATE KEY-----", "");
		tsFastAuthPrivateKey = tsFastAuthPrivateKey.replace("-----END RSA PRIVATE KEY-----", "");
        
        byte[] tsFastAuthPrivateKeyDecoded = Base64.getDecoder().decode(tsFastAuthPrivateKey);
           
        DerInputStream tsDerInputStreamReader = new DerInputStream(tsFastAuthPrivateKeyDecoded);
		DerValue[] tsDerValueSequence = tsDerInputStreamReader.getSequence(0);
		BigInteger tsModulus = tsDerValueSequence[1].getBigInteger();
		BigInteger tsPublicExponent = tsDerValueSequence[2].getBigInteger();
		BigInteger tsPrivateExponent = tsDerValueSequence[3].getBigInteger();
		BigInteger tsPrimeP = tsDerValueSequence[4].getBigInteger();
		BigInteger tsPrimeQ = tsDerValueSequence[5].getBigInteger();
		BigInteger tsPrimeExponentP = tsDerValueSequence[6].getBigInteger();
		BigInteger tsPrimeExponentQ = tsDerValueSequence[7].getBigInteger();
		BigInteger tsCrtCoefficient = tsDerValueSequence[8].getBigInteger();
		RSAPrivateCrtKeySpec tsRsaPrivateKeySpec = new RSAPrivateCrtKeySpec(tsModulus, tsPublicExponent, tsPrivateExponent, tsPrimeP, tsPrimeQ, tsPrimeExponentP, tsPrimeExponentQ, tsCrtCoefficient);
        KeyFactory tsPrivateKeyFactory = KeyFactory.getInstance("RSA");
        PrivateKey tsPrivateKey = tsPrivateKeyFactory.generatePrivate(tsRsaPrivateKeySpec);
		
        String data = "";
        String fastAuthChecksum = "";
        
        byte[] challengeDecodedBytes = Base64.getDecoder().decode(challenge);
        String challengeDecodedString = new String(challengeDecodedBytes);
        
        JSONParser parser01 = new JSONParser();  
        JSONObject jsonObject = (JSONObject) parser01.parse(challengeDecodedString);
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		
		Iterator iterator = jsonArray.iterator();
		while (iterator.hasNext()){
			String value = (String) iterator.next();
			data = data + value;
		}
		
		Signature tsSignature = Signature.getInstance("SHA256withRSA");
		tsSignature.initSign(tsPrivateKey);
		tsSignature.update(data.getBytes(StandardCharsets.US_ASCII));
		byte[] unformattedSignature = tsSignature.sign();
		fastAuthChecksum = Hex.encodeHexString(unformattedSignature);
        
        /*
         * END v2
         */
        
		HashMap<String, String>  headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + accessToken);
		headers.put("Content-Type", "application/json");

		JSONObject signatureJson = new JSONObject();
		signatureJson.put("serial_number", this.serialNumber);
		signatureJson.put("signature", fastAuthSignature);		
		signatureJson.put("checksum", fastAuthChecksum);
		
		JSONArray signaturesJsonArray = new JSONArray();
		signaturesJsonArray.add(signatureJson);
		
		JSONObject json = new JSONObject();
		json.put("challenge", challenge);
		json.put("signatures", signaturesJsonArray);
		
		Request request = new Request();
		request.setHeaders(headers);
		request.setVerifySsl(false);	
		request.setData(json.toJSONString());
		
		if(deny) {
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v2/mobile/deny/");
		}
		else{
			request.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v2/mobile/sign/");		
		}
		
		Response response = request.doPost();
	
		if (response.getCode() != 200) {
			throw new IOException(response.getContent());
		}
	}
	
	
	public void signPush(int iterations, int sleep) throws Exception{
		this.signPush(iterations, sleep, false, false);
	}
	
	
	public void signPush(int iterations, int sleep, boolean deny, boolean bad) throws Exception{
		
		class SignPush implements Runnable {
			
			private int iterations;
			private int sleep;
			private boolean deny;
			private boolean bad;
			
			public SignPush(int iterations, int sleep, boolean deny, boolean bad) {
				this.iterations = iterations;
				this.sleep = sleep;
				this.deny  = deny;
				this.bad   = bad;
			}
			
			@Override
			public void run() {
				try {
					for (int i=0; i<this.iterations; i++) {
						try {
							HashMap<String, String>  headers = new HashMap<>();
							headers.put("Authorization", "Bearer " + accessToken);
							headers.put("Content-Type", "application/json");

							Request request = new Request();
							request.setUrl("https://" + Config.tnHost + ":" + Config.tnMobileApiPort + "/api/v1/mobile/Fast:Auth/pendings/");
							request.setHeaders(headers);
							request.setVerifySsl(false);		
							request.setData("{}");
						
							
							Response response = request.doGet();
							if (response.getCode() == 200) {
								JSONParser jsonArrayParser = new JSONParser();
								JSONArray jsonArray = (JSONArray) jsonArrayParser.parse(response.getContent());
					
								Iterator iterator = jsonArray.iterator();
								if (iterator.hasNext()){
									JSONParser jsonParser = new JSONParser();
									JSONObject jsonObject = (JSONObject) iterator.next();
									String challenge = (String) jsonObject.get("challenge");
									signChallenge(challenge, this.deny, this.bad);
									break;									
								}								
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
						
						Thread.sleep(this.sleep);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		SignPush signPush = new SignPush(iterations, sleep, deny, bad);
		Thread signPushThread = new Thread(signPush);
		
		signPushThread.start();
	}

	}
