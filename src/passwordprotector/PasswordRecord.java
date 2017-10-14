package passwordprotector;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PasswordRecord {
	private String accountType;
	private String userName;
	private byte[] cypherTextPassword;
	
	public PasswordRecord( String accountType, String userName, String cypherTextPassword ) {
		this.accountType = accountType;
		this.userName = userName;
		this.cypherTextPassword = cypherTextPassword.getBytes();
	}
	
	public void setAccountType( String accountType) {
		this.accountType = accountType;
	}
	
	public void setUserName( String userName ) {
		this.userName = userName;
	}
	
	public void setPassword( String plainTextPassword, String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		//TODO: Try Catch
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("twofish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("twofish");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		this.cypherTextPassword = cipher.doFinal(plainTextPassword.getBytes());
	}
	
	public String getAccountType() {
		return this.accountType;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPlainTextPasswor( String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("twofish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("twofish");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(this.cypherTextPassword)); //TODO Try Catch
	}
}
