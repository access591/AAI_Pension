 /**
  * File       : AESCryptographer.java
  * Date       : 08/07/2007
  * Author     : AIMS 
  * Description: 
  * Copyright (2007) by the Navayuga Infotech, all rights reserved.
  */
package aims.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import java.util.Base64;
import aims.common.Log;
import aims.security.Cryptographer;
import aims.security.PropertyHelper;


/**
 * @author nagarjunreddyt
 *
 */
public class AESCryptographer extends Cryptographer {
	static Log log = new Log(Cryptographer.class);

	public String doDecrypt(String input) {
		String retStr = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");

			SecretKeySpec keySpec = new SecretKeySpec(
					generateKeySpc(PropertyHelper.getProperty("AES.Key")),
					"AES");

			// the below may make this less secure, hard code byte array the IV
			// in both java and .net clients
			// IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			//BASE64Decoder decoder = new BASE64Decoder();
			//Base64.getDecoder().decode(input);
			byte[] results = cipher.doFinal(Base64.getDecoder().decode(input));
			return new String(results, "UTF-8");
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		return retStr;
	}

	public String doEncrypt(String input) {
		String retStr = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");

			SecretKeySpec keySpec = new SecretKeySpec(
					generateKeySpc(PropertyHelper.getProperty("AES.Key")),
					"AES");

			// the below may make this less secure, hard code byte array the IV
			// in both java and .net clients
			// IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] results = cipher.doFinal(input.getBytes("UTF-8"));
			//BASE64Encoder encoder = new BASE64Encoder();
			//retStr = encoder.encode(results);
			
			retStr = Base64.getEncoder().encodeToString(results);  
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		return retStr;
	}

}
