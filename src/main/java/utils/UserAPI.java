package utils;

import configs.Config;
import tests.tenant.API;

import java.util.Random;

public class UserAPI {

	public static void deleteUser(String username) throws Exception
	{
		Response res1 = API.deleteUser(username);

		if( res1.getCode() != 404 &&  res1.getCode() != 201 && res1.getCode() != 200   && res1.getCode() != 204) {
			System.out.println(res1.getContent() + res1.getCode());
			throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static void deleteUserLDAP(String username) throws Exception
	{
		Response res1 = API.deleteUser(username+"@"+Config.ldapDomain);

		if( res1.getCode() != 404 &&  res1.getCode() != 201 && res1.getCode() != 200   && res1.getCode() != 204) {
			System.out.println(res1.getContent() + res1.getCode());
			throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static void createUser(String username, String password) throws Exception
	{
		Response res1 = API.createUser(username, password);

		if(res1.getCode() != 201 && res1.getCode() != 200 ) {
			System.out.println(res1.getContent() + res1.getCode());

			throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static void createUserLDAP(String username, String password) throws Exception
	{
		Response res1 = API.createUser(username, password);

		if(res1.getCode() != 201 && res1.getCode() != 200 ) {
			System.out.println(res1.getContent() + res1.getCode());

			throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static void allowPassword(String username, boolean allow )throws Exception
	{
		Response res1 =API.allowAuthWithPassword(username, allow);
		if(res1.getCode() != 201 && res1.getCode() != 200 ) {
            throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static  void allowExpiredPassword(String username, boolean allow) throws Exception
	{
		Response res1 =API.allowWithPasswordExpired(username, true);
		if(res1.getCode() != 201 && res1.getCode() != 200 ) {
				if(!String.valueOf(res1.getCode()).equals("200") || !String.valueOf(res1.getCode()).equals("201"))
			throw new Exception(res1.getCode()+"  content"+res1.getContent());
		}
	}

	public static  void resetUser(String username, String password) throws Exception
	{
		if (username.contains(Config.ldapDomain)){
			deleteUserLDAP(username);
			Thread.sleep(5000);
			createUserLDAP(username, password);
		}else{
			deleteUser(username);
			Thread.sleep(5000);
			createUser(username, password);
		}
	}


	public static String getRandomUsername(String username) throws Exception {
		Random r = new Random();
		int low = 10;
		int high = 99;
		int result = r.nextInt(high - low) + low;
		int result2 = r.nextInt(high - low) + low;

		if (username.contains(Config.ldapDomain)) {
			username = username.replace("@"+Config.ldapDomain,"");
			return result + username + result2 +Config.ldapDomain;
		}
			return result + username + result2;
	}

	public static String getUserDN(String username) throws Exception
	{
		username = username.replace("@"+Config.ldapDomain," ").trim();
		return "CN="+username+",CN=Users, DC=safewalk,DC=training";
	}
}
