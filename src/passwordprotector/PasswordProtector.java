/**
 * 
 */
package passwordprotector;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static ArrayList<PasswordRecord> parseSecurePasswordFile(String fileName, String key) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		char[] fileContents = null;
		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		fileReader.read(fileContents);
		fileReader.close();
		return parseSecurePasswordString(new String(fileContents), key);
	}
	
	public static ArrayList<PasswordRecord> parseSecurePasswordString( String fileContents, String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("twofish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("twofish");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		String decryptedFileContents = new String(cipher.doFinal(fileContents.getBytes()));
		String[] records = decryptedFileContents.split(";");
		ArrayList<PasswordRecord> passwordRecordsArrayList = new ArrayList<PasswordRecord>();
		for ( String record : records ) {
			String[] recordDetails = record.split(",");
			PasswordRecord passwordRecord = new PasswordRecord(recordDetails[0],recordDetails[1],recordDetails[2]);
			passwordRecordsArrayList.add(passwordRecord);
		}
		return passwordRecordsArrayList;
	}
	
	public static void writeSecurePasswordFile( ArrayList<PasswordRecord> passwordRecordList, String filename, String key ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		String fileString = new String("");
		for ( PasswordRecord passwordRecord : passwordRecordList ) {
			fileString = fileString + passwordRecord.getAccountType() + "," + passwordRecord.getUserName() + "," + passwordRecord.getCypherTextPassword() + ";";
		}
		SecureRandom secureRandom = new SecureRandom(key.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("twofish");
		keyGenerator.init(secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = Cipher.getInstance("twofish");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		String encryptedFileString = new String(cipher.doFinal(fileString.getBytes()));
		File file = new File(filename);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(encryptedFileString);
		fileWriter.flush();
		fileWriter.close();
		return;
	}

}
