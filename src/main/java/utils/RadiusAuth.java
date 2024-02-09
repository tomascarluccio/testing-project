package utils;

import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;

import configs.Config;

public class  RadiusAuth
 {
	

	public static String authenticate(String username, String password, String host)  throws Exception {
		return authenticateTimeout(username, password, Integer.parseInt(Config.radiusDefaultTimeout), host);
	}
	
	
	public static String authenticateTimeout(String username, String password, int timeout, String host) throws Exception{
		
		RadiusClient rc = new RadiusClient(host, Config.radiusSharedSecret);
		rc.setRetryCount(3);
		AccessRequest ar = new AccessRequest(username, password);
		ar.setAuthProtocol(AccessRequest.AUTH_PAP);
		String serverBrokerAuth = "";
		int radiusServerPort = Integer.valueOf(Config.getFsProperty("radiusServerPort"));
		if(host.equals(Config.radiusServerHost)) {
			 serverBrokerAuth = "Server";
			 System.out.println(serverBrokerAuth);
				
			 rc.setAuthPort(radiusServerPort);
			 ar.addAttribute("NAS-IP-Address", Config.radiusServerClientAddress);
		}
		else {
			serverBrokerAuth = "Broker";
			System.out.println(serverBrokerAuth);
			
			rc.setAuthPort(Integer.valueOf(Config.radiusBrokerPort));
			ar.addAttribute("NAS-IP-Address", Config.radiusBrokerClientAddress);
		}
		try {
			rc.setSocketTimeout(timeout);
			RadiusPacket response = rc.authenticate(ar);
			if(response.getPacketType() == RadiusPacket.ACCESS_ACCEPT) {
				return "ACCESS_ALLOWED";
			}
			if(response.getPacketType() == RadiusPacket.ACCESS_CHALLENGE) {
				return "ACCESS_CHALLENGE";
			}
			if(response.getPacketType() == RadiusPacket.ACCESS_REQUEST) {
				return "ACCESS_REQUEST";
			}
			if(response.getPacketType() == RadiusPacket.ACCESS_REJECT) {
				return "ACCESS_DENIED";
			}

		}catch (Exception e) {

			return "RADIUS_ERROR "+serverBrokerAuth+" user "+username + " server port "+radiusServerPort+" broker port "+Config.radiusBrokerPort+" broker client add "+Config.radiusBrokerClientAddress+" server client add "+""+Config.radiusServerClientAddress+ " exception "+e.getMessage();
		}
	
		return "RADIUS_ERROR "+serverBrokerAuth+" user "+username + " server port "+radiusServerPort+" broker port "+Config.radiusBrokerPort+" broker client add "+Config.radiusBrokerClientAddress+" server client add "+""+Config.radiusServerClientAddress;

	}

}
