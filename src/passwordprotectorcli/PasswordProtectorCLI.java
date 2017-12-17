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
	
	static String passwordRecordFile;
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
		} catch (InvalidSelectionException e) {
			System.out.println("Usage:\njava -jar passwordprotector.jar <encrypted password file>");
		} catch (CouldNotInstantiateConsoleException e) {
			System.out.println("Unable to instatiate a console instance in this runtime environment.  Please start this program in Bash or Windows Command Shell.");
		} catch (Exception e) {
			System.out.println("Error: an exception occurred.\nPlease grab the following stack trace and open an issue at https://github.com/m-yuhas/password_protector");
			e.printStackTrace();
		}
	}
	
	/**
	 * The parseArgs method takes the argument array passed by the command line and uses the results to set the static variables needed by this class.
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 * @param	args	array of command line arguments
	 * @throws	Exception
	 */
	public static void parseArgs(String[] args) throws Exception {
		if (args.length == 0) {
			passwordRecordArray = new ArrayList<PasswordRecord>();
			passwordRecordFile = null;
		} else if (args.length != 1) {
			throw new InvalidSelectionException(new String(args.length + " args provided"));
		} else {
			Console console = System.console();
			if (console == null) {
				throw new Exception("Unable to instatiate Console instance");
			}
			char passwordBytes[] = console.readPassword("Enter password to unlock file: ");
			passwordRecordArray = PasswordProtector.parseSecurePasswordFile(args[0], new String(passwordBytes));
			passwordRecordFile = args[0];
		}
	}
	
	/**
	 * The mainMenu method displays the mainMenu and processes the user's input.
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 * @throws	InvalidKeyException					An error occurred while encrypting of decrypting a record: key was invalid
	 * @throws	NoSuchAlgorithmException			An error occurred while encrypting or decrypting a record: invalid encryption algorithm selected
	 * @throws	NoSuchPaddingException				An error occurred while encrypting or decrypting a record: invalid padding used
	 * @throws	IllegalBlockSizeException			An error occurred while encrypting or decrypting a record: invalid block size selected
	 * @throws	BadPaddingException					An error occurred while encrypting or decrypting a record: invalid padding used
	 * @throws	InvalidAlgorithmParameterException	An error occurred while encrypting or decrypting a record: invalid encryption algorithm selected
	 * @throws	NumberFormatException				An error occurred while encrypting or decrypting a record: invalid number format used
	 * @throws 	CouldNotInstantiateConsoleException	An error occurred while trying to gather user input: could not instantiate a console in this runtime environment	
	 */
	public static void mainMenu() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NumberFormatException, CouldNotInstantiateConsoleException {
		String selection = "";
		while (selection.toLowerCase() != "exit") {
			printRecordList();
			System.out.println("'VIEW <number>' to view record, 'NEW' to create a new Record, 'DELETE <number>' to delete a record, 'SAVE' to save the file, and 'EXIT' to exit.");
			Console console = System.console();
			if (console == null) {
				throw new CouldNotInstantiateConsoleException("Could not instantiate console in this context.");
			}
			String choiceArray[] = console.readLine().split(" ");
			switch (choiceArray[0].toLowerCase()) {
				case "view": 
					if (choiceArray.length < 2) {
						System.out.println("Please select a record number using the syntax 'VIEW <number>'");
						break;
					} else if (Integer.parseInt(choiceArray[1]) < 1) {
						System.out.println("Please select a valid record number");
						break;
					} else {
						viewRecord(passwordRecordArray, Integer.parseInt(choiceArray[1]) - 1);
						break;
					}
				case "new":
					passwordRecordArray.add(createNewRecord());
					break;
				case "delete":
					if (choiceArray.length < 2) {
						System.out.println("Please select a record number using the syntax 'DELETE <number>'");
						break;
					} else if (Integer.parseInt(choiceArray[1]) < 1) {
						System.out.println("Please select a valid record number");
						break;
					} else {
						passwordRecordArray.remove(Integer.parseInt(choiceArray[1]));
						break;
					}
				case "save":
					saveFile(passwordRecordArray, choiceArray[1]);
					break;
				case "exit":
					cleanUp();
					return;
				default:
					System.out.println("Invalid Entry");
					break;
			}
		}
		cleanUp();
		return;	
	}
	
	/**
	 * The viewRecord function decrypts and prints the password associated with a particular account's record.
	 * 
	 * @author	Michael Yuhas
	 * @since	0.1
	 * @version	0.1
	 * @param	recordArray								Array of password records to look through
	 * @param 	n										Index of the record to view
	 * @throws 	CouldNotInstantiateConsoleException 	An error occurred while trying to gather user input: could not instantiate a console in this runtime environment
	 * @throws 	InvalidKeyException						An error occurred while encrypting of decrypting a record: key was invalid
	 * @throws	NoSuchAlgorithmException				An error occurred while encrypting or decrypting a record: invalid encryption algorithm selected
	 * @throws	NoSuchPaddingException					An error occurred while encrypting or decrypting a record: invalid padding used
	 * @throws	IllegalBlockSizeException				An error occurred while encrypting or decrypting a record: invalid block size selected
	 * @throws	BadPaddingException						An error occurred while encrypting or decrypting a record: invalid padding used
	 * @throws	InvalidAlgorithmParameterException		An error occurred while encrypting or decrypting a record: invalid encryption algorithm selected
	 */
	public static void viewRecord(ArrayList<PasswordRecord> recordArray, int n) throws CouldNotInstantiateConsoleException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException {
		Console console = System.console();
		if (console == null) {
			throw new CouldNotInstantiateConsoleException("Could not instantiate console in this context.");
		}
		char passwordBytes[] = console.readPassword("Enter password to unlock record: ");
		System.out.printf("Account Type: %s\nUser Name: %s\nPassword: %s",recordArray.get(n).getAccountType(),recordArray.get(n).getUserName(),recordArray.get(n).getPlainTextPassword(new String(passwordBytes)));
		System.out.println("Press ENTER to continue...");
		console.readLine();
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
	
	public static void cleanUp() {
		passwordRecordArray = null;
		return;
	}
	
	public static void createNewFile() {
		
	}
	
	public static void printRecordList() {
		if (passwordRecordArray.size() == 0) {
			System.out.println("No records to display.");
			return;
		}
		for (int i = 0; i < passwordRecordArray.size(); i++) {
			System.out.println(String.valueOf(i+1) + ".) " + passwordRecordArray.get(i).getAccountType());
		}
		return;
	}

}
