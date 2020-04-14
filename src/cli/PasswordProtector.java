package cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

/**
 * This class implements the password protector functionality for the CLI.
 */
public class PasswordProtector {

  private boolean modified = false;
  private File file;
  private ResourceBundle messages;
  private Map<String, Map<String, String>> recordMap;
  private EncryptedBuffer<Map<String, Map<String, String>>> encryptedBuffer;

  /**
   * Construct an instance of PasswordProtector.
   * 
   * @param file is the file object from which to read and write passwords.  If it doesn't exist,
   *        a new file will be created.
   * @throws Exception  if an error occurs while trying to open the password file.
   */
  public PasswordProtector(File file, ResourceBundle messages) throws Exception {
    this.file = file;
    this.messages = messages;
    if (this.file.isFile()) {
      this.open();
    } else {
      this.recordMap = new HashMap<String, Map<String, String>>();
    }
  }

  /**
   * Run the password protector main loop.  This method will run until the user selects the exit
   * option or the JVM is terminated by the OS.
   * 
   * @throws Exception if any error occurs getting input from the user.
   */
  public void mainLoop() throws Exception {
    while (true) {
      System.out.println(this.messages.getString("menu"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String selection;
      try {
        System.out.print(">>>");
        selection = reader.readLine();
      } catch (IOException e) {
        System.out.println(this.messages.getString("consoleReadError"));
        continue;
      }
      String[] parsedSelection = selection.trim().split(" ", 2);
      switch(parsedSelection[0].toLowerCase()) {
        case "list":
          this.list();
          break;
        case "view":
          if (parsedSelection.length == 1) {
            this.view(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("viewError"));
          }
          break;
        case "add":
          if (parsedSelection.length == 1) {
            this.add(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("addError"));
          }
          break;
        case "delete":
          if (parsedSelection.length == 1) {
            this.delete(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("deleteError"));
          }
          break;
        case "modify":
          if (parsedSelection.length == 1) {
            this.modify(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("modifyError"));
          }
          break;
        case "save":
          this.save();
          break;
        case "change":
          this.change();
          break;
        case "quit":
          this.quit();
          break;
        default:
          System.out.println(this.messages.getString("invalidInput"));
          break;
      }
    }
  }

  /**
   * List the stored accounts.
   */
  private void list() {
    int count = 0;
    for (String account: this.recordMap.keySet()) {
      System.out.println(Integer.toString(++count) + ". " + account);
    }
    if (count == 0) {
      System.out.println(this.messages.getString("noRecords"));
    }
  }

  /**
   * View a specific account.
   * 
   * @param recordName is a string of the name of the account to view.
   */
  private void view(String recordName) {
    if (this.recordMap.containsKey(recordName)) {
      Map<String, String> record = this.recordMap.get(recordName);
      for (String attribute: record.keySet()) {
        System.out.println(attribute + ": " + record.get(attribute));
      }
    } else {
      System.out.println(this.messages.getString("recordNotFound"));
    }
  }

  /**
   * Add a new account.  If the account already exists it will be overwritten.
   * 
   * @param recordName is a string of the name of the account to add.
   */
  private void add(String recordName) {
    String attribute = "";
    String value = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Map<String, String> recordData = new HashMap<String, String>();
    while (true) {
      System.out.println(this.messages.getString("enterAttribute"));
      try {
        System.out.print(">");
        attribute = reader.readLine();
      } catch (IOException e) {
        System.out.println(this.messages.getString("consoleReadError"));
        continue;
      }
      if (attribute.strip().toLowerCase().equals("done")) {
        break;
      }
      System.out.println(this.messages.getString("enterValue"));
      try {
        System.out.print(">");
        value = reader.readLine();
        if (value.strip().toLowerCase().contentEquals("generate")) {
          value = this.generatePassword();
        }
      } catch (IOException e) {
        System.out.println(this.messages.getString("consoleReadError"));
        continue;
      }
      recordData.put(attribute, value);
    }
    this.recordMap.put(recordName, recordData);
    this.modified = true;
  }

  /**
   * Delete an account.
   * 
   * @param recordName is a string of the name of the account to delete.
   * @throws Exception if any error occurs getting input from the user.
   */
  private void delete(String recordName) throws Exception {
    if (!this.recordMap.containsKey(recordName)) {
      System.out.println(this.messages.getString("recordNotFound"));
      return;
    }
    if (this.encryptedBuffer != null) {
      char[][] passwords = this.getPasswords();
      try {
        if (!this.encryptedBuffer.validatePassword(passwords)) {
          System.out.println(this.messages.getString("incorrectPasswords"));
          return;
        }
      } catch (DecryptionException e) {
        System.out.println(this.messages.getString("corruptFileError"));
        return;
      }
    }
    this.recordMap.remove(recordName);
    this.modified = true;
  }

  /**
   * Edit an account.
   * 
   * @param recordName is a string of the name of the account to edit.
   */
  private void modify(String recordName) {
    
  }

  private void save() {
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] passwordOne = console.readPassword("Enter password 1:");
    char[] passwordTwo = console.readPassword("Enter password 2:");
    if (this.encryptedBuffer == null) {
      try {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.recordMap, passwordOne, passwordTwo);
      } catch (EncryptionException e) {
        System.out.println("An error occurred during encryption.  Please try again");
        return;
      }
    } else {
      try {
        if (!this.encryptedBuffer.validatePassword(passwordOne, passwordTwo)) {
          System.out.println("One or more of the passwords is incorrect.");
          return;
        }
        this.encryptedBuffer.updateContents(this.recordMap, passwordOne, passwordTwo);
      } catch (EncryptionException | DecryptionException e) {
        System.out.println("An error occurred during encryption.  Please try again");
        return;
      }
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
      this.modified = false;
      System.out.println("File written succesffully.");
    } catch (IOException e) {
      System.out.println("An error occurred while writing to file.");
    }
  }

  private void change() {    
    if (this.encryptedBuffer == null) {
      System.out.println("No passwords file is currently loaded.");
      return;
    }
    if (this.modified) {
      System.out.println("This list of records has been modified, would you like to save first? (Y/n):");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
        if (reader.readLine().strip().startsWith("Y")) {
          this.save();
        }
      } catch (IOException e) {
        System.out.println("Error occurred during reading input");
        e.printStackTrace();
        return;
      }
    }
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] oldPasswordOne = console.readPassword("Enter current password 1:");
    char[] oldPasswordTwo = console.readPassword("Enter current password 2:");
    try {
      if (!this.encryptedBuffer.validatePassword(oldPasswordOne, oldPasswordTwo)) {
        System.out.println("One or more of the passwords is incorrect.");
        return;
      }
    } catch (DecryptionException e1) {
      System.out.println("Corrupt File?");
      e1.printStackTrace();
    }
    char[] newPasswordOne = console.readPassword("Enter new password 1:");
    char[] newPasswordTwo = console.readPassword("Enter new password 2:");
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.recordMap, newPasswordOne, newPasswordTwo);
    } catch (EncryptionException e) {
      System.out.println("Error setting new password:");
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      System.out.println("Error occurred while writing to disk.");
      return;
    }
    this.modified = false;
    System.out.println("File passwords modified successfully");
  }

  private void quit() {
    if (this.modified) {
      System.out.println("This list of records has been modified, would you like to save first? (Y/n):");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
        if (reader.readLine().strip().startsWith("Y")) {
          this.save();
        }
      } catch (IOException e) {
        System.out.println("Error occurred during reading input");
        e.printStackTrace();
        return;
      }
    }
    System.exit(0);
  }
  
  private String generatePassword() {
    Map<String, char[]> characterChoices = Map.ofEntries(
        new AbstractMap.SimpleEntry<String, char[]>("numbers", passwordio.PasswordGenerator.NUMBERS),
        new AbstractMap.SimpleEntry<String, char[]>("lower case letters", passwordio.PasswordGenerator.LOWER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<String, char[]>("upper case letters", passwordio.PasswordGenerator.UPPER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<String, char[]>("symbols", passwordio.PasswordGenerator.SYMBOLS));
    ArrayList<Character> chosenCharactersObjects = new ArrayList<Character>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      for (String choice: characterChoices.keySet()) {
        System.out.println("Include " + choice + "? (Y/n):");
        if (reader.readLine().strip().startsWith("Y")) {
          for (char c: characterChoices.get(choice)) {
            chosenCharactersObjects.add(c);
          }
        }
      }
    } catch (IOException e) {
      System.out.println("Error occurred during reading input");
      e.printStackTrace();
      return "";
    }
    char[] chosenCharacters = new char[chosenCharactersObjects.size()];
    for (int i = 0; i < chosenCharacters.length; i++) {
      chosenCharacters[i] = chosenCharactersObjects.get(i);
    }
    System.out.println("Enter the length of the password in characters:");
    int length = 0;
    try {
      length = Integer.parseInt(reader.readLine());
    } catch (NumberFormatException e) {
      System.out.println("Entry was not a number.");
      return "";
    } catch (IOException e) {
      System.out.println("Error occurred during reading input");
      e.printStackTrace();
    }
    return new String(new passwordio.PasswordGenerator(chosenCharacters).generatePassword(length));
  }

  private void open() throws Exception {
    char[][] passwords = this.getPasswords();
    try {
      encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(file);
      this.recordMap = encryptedBuffer.decrypt(passwords);       
    } catch (DecryptionException e) {
      System.out.println(this.messages.getString("incorrectPasswords"));
      throw new Exception("One or more passwords was incorrect.");
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionRead"));
      throw new Exception("Error occurred while reading the file");
    } catch (ClassNotFoundException e) {
      System.out.println(this.messages.getString("classNotFoundException"));
      e.printStackTrace();
      throw new Exception("Invalid state.");
    }
  }

  private char[][] getPasswords() throws Exception {
    Console console = System.console();
    if (console == null) {
      System.out.println(this.messages.getString("consoleError"));
      throw new Exception("Could not get console.");
    }
    char[][] passwords = new char[2][];
    passwords[0] = console.readPassword(this.messages.getString("readPassword1"));
    passwords[1] = console.readPassword(this.messages.getString("readPassword2"));
    return passwords;
  }

}
