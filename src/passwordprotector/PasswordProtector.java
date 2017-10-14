/**
 * 
 */
package passwordprotector;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author user
 *
 */
public class PasswordProtector {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		System.out.println("Hello World");
		PasswordGenerator pg = new PasswordGenerator();
		pg.addValidCharacters(ValidCharacters.LOWERCASE);
		pg.addValidCharacters(ValidCharacters.UPPERCASE);
		System.out.println(pg.generatePassword(10));
		pg.addValidCharacters(ValidCharacters.NUMBERS);
		System.out.println(pg.generatePassword(12));
		
	}
	
	private static void parseArgs(String[] args) {
		return;
	}
	
	public ArrayList<PasswordRecord> parseSecurePasswordFile( String fileContents, String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("twofish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("twofish");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedFileContents = new String(cipher.doFinal(fileContents.getBytes()));

		
		
		return new ArrayList<PasswordRecord>(); //TODO: Implement This
	}

}
