/**
 * The passwordprotectorcli package can be bundled as a jar to run the passwordprotector application on any system with JRE1.8 or higher installed.
 * 
 * @author	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
package passwordprotectorcli;

import java.io.Console;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import passwordprotector.PasswordRecord;
import passwordprotector.ValidCharacters;
import passwordprotector.PasswordGenerator;
import passwordprotector.PasswordProtector;

/**
 * The PasswordProtectorCLI class implements a command line application to encrypt and decrypt password records.
 * 
 * @author 	Michael Yuhas
 * @since	0.1
 * @version	0.1
 */
public class PasswordProtectorCLI {
	
	static ArrayList<PasswordRecord> passwordRecordArray;

	/**
	 * The main method is the entry point for this command line application
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 * @param	args	array of command line arguments
	 */
	public static void main(String[] args) {
		try {
			parseArgs(args);
			mainMenu();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("resource")
	public static void parseArgs(String[] args) throws Exception {
		if (args.length == 0) {
			passwordRecordArray = new ArrayList<PasswordRecord>();
		} else if (args.length != 1) {
			System.out.println("Usage:\njava -jar passwordprotector.jar <encrypted password file>");
			throw new Exception();
		} else {
			Scanner reader = new Scanner(System.in); //TODO make password invisible
			String key = reader.nextLine();
			passwordRecordArray = PasswordProtector.parseSecurePasswordFile(args[0], key);
		}
	}
	
	@SuppressWarnings("resource")
	public static void mainMenu() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		String selection = "";
		while (selection.toLowerCase() != "exit") {
			System.out.println("'VIEW <number>' to view record, 'NEW' to create a new Record, 'DELETE <number>' to delete a record, 'SAVE' to save the file, and 'EXIT' to exit.");
			Scanner reader = new Scanner(System.in);
			selection = reader.nextLine();
			String choiceArray[] = selection.split(" ");
			switch (choiceArray[0].toLowerCase()) {
				case "view": 
					viewRecord(passwordRecordArray, Integer.parseInt(choiceArray[1]));
					break;
				case "new":
					passwordRecordArray.add(createNewRecord());
					break;
				case "delete":
					passwordRecordArray.remove(Integer.parseInt(choiceArray[1]));
					break;
				case "save":
					saveFile(passwordRecordArray, choiceArray[1]);
					break;
				case "exit":
					cleanUp(passwordRecordArray);
					return;
				default:
					System.out.println("Invalid Entry");
					break;
			}
		}
		cleanUp(passwordRecordArray);
		return;
		
		
	}
	
	public static void viewRecord(ArrayList<PasswordRecord> recordArray, int n) {
		Console console = System.console();
		if (console == null) {
			System.out.println("Couldn't get Console instance");
			return;
		}
		char passwordBytes[] = console.readPassword("Enter password: ");
		try {
			System.out.printf("Password: %s",recordArray.get(n).getPlainTextPassword(new String(passwordBytes)));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Finish This
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Press ENTER to continue...");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		return;
	}
	
	public static PasswordRecord createNewRecord() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		System.out.println("Enter the account type:");
		@SuppressWarnings("resource") //TODO: Find out what this is
		Scanner reader = new Scanner(System.in);
		String accountType = reader.nextLine();
		System.out.println("Enter the username for this account:");
		String userName = reader.nextLine();
		System.out.println("Enter your own password or 'GENERATE' to randomly generate one");
		String plainTextPassword = reader.nextLine();
		if (plainTextPassword.toLowerCase().equals("generate")) {
			System.out.println("How long should the password be (in characters)?");
			int passwordLength = Integer.parseInt(reader.nextLine());
			ArrayList<ValidCharacters> validCharacters = new ArrayList<ValidCharacters>();
			System.out.println("Are lowercase letters allowed? (Yes or No)");
			if (reader.nextLine().toLowerCase().equals("yes")) {
				validCharacters.add(ValidCharacters.LOWERCASE);
			}
			System.out.println("Are uppercase letters allowed? (Yes or No)");
			if (reader.nextLine().toLowerCase().equals("yes")) {
				validCharacters.add(ValidCharacters.UPPERCASE);
			}
			System.out.println("Are numbers allowed? (Yes or No)");
			if (reader.nextLine().toLowerCase().equals("yes")) {
				validCharacters.add(ValidCharacters.NUMBERS);
			}
			System.out.println("Are symbols allowed? (Yes or No)");
			if (reader.nextLine().toLowerCase().equals("yes")) {
				validCharacters.add(ValidCharacters.SYMBOLS);
			}
			SecureRandom sr = new SecureRandom();
			byte seedBytes[] = sr.generateSeed(8);
			long seed = 0;
			for (int i = 0; i < seedBytes.length; i++) {
				seed = (seed << 8) + (long) seedBytes[i]; //TODO: Test this
			}
			PasswordGenerator passwordGenerator = new PasswordGenerator(seed,validCharacters);
			plainTextPassword = passwordGenerator.generatePassword(passwordLength);
			System.out.printf("Plain text password is: %s\n", plainTextPassword);
		}
		System.out.println("Enter your password to encrypt this record: ");
		String key = reader.nextLine();
		return new PasswordRecord(accountType, userName, plainTextPassword, key);
		
	}
	
	public static void saveFile(ArrayList<PasswordRecord> passwordRecordList, String filename) {
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		if (filename == null) {
			System.out.println("Enter the filename to which to save: ");
			filename = reader.nextLine();
		}
		System.out.println("Enter your password to encrypt the file: ");
		String key = reader.nextLine();
		try {
			PasswordProtector.writeSecurePasswordFile(passwordRecordList, filename, key);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Do this correctly
			e.printStackTrace();
		}
		return;
	}
	
	public static void cleanUp(ArrayList<PasswordRecord> passwordRecordList) {
		passwordRecordList = null;
		return;
	}
	
	public static void createNewFile() {
		
	}

}
