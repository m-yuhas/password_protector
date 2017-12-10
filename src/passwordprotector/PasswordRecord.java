package passwordprotector;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class PasswordRecord {
	private String accountType;
	private String userName;
	private byte[] cypherTextPassword;
	
	public PasswordRecord( String accountType, String userName, String cypherTextPassword ) {
		this.accountType = accountType;
		this.userName = userName;
		this.cypherTextPassword = cypherTextPassword.getBytes();
	}
	
	public PasswordRecord(String accountType, String userName, String plainTextPassword, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		this.accountType = accountType;
		this.userName = userName;
		this.setPassword(plainTextPassword, key);
	}
	
	public void setAccountType( String accountType) {
		this.accountType = accountType;
	}
	
	public void setUserName( String userName ) {
		this.userName = userName;
	}
	
	public void setPassword( String plainTextPassword, String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		//TODO: Try Catch
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[8]));
		this.cypherTextPassword = cipher.doFinal(plainTextPassword.getBytes());
	}
	
	public String getAccountType() {
		return this.accountType;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPlainTextPassword( String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[8])); //TODO Prepend IV to text and make it random
		return new String(cipher.doFinal(this.cypherTextPassword)); //TODO Try Catch
	}
	
	public String getCypherTextPassword() {
		return new String(this.cypherTextPassword);
	}
}
