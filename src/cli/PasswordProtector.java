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

  /**
   * Boolean value to indicate if the open records file has been modified or not.
   */
  private boolean modified = false;

  /**
   * The password record file to read and write to and from.
   */
  private File file;

  /**
   * The resource bundle of internationalization strings.
   */
  private ResourceBundle messages;

  /**
   * Password records are stored in this map when they are loaded into memory.  Modifications are
   * first applied to this map and later written to an encrypted buffer and then a file.
   */
  private Map<String, Map<String, String>> recordMap;

  /**
   * The map of records needs to be encrypted before writing to file, also when opening a file, the
   * data is initially encrypted.  encryptedBuffer serves as a storage place for this data.
   */
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
          if (parsedSelection.length == 2) {
            this.view(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("viewError"));
          }
          break;
        case "add":
          if (parsedSelection.length == 2) {
            this.add(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("addError"));
          }
          break;
        case "delete":
          if (parsedSelection.length == 2) {
            this.delete(parsedSelection[1].trim());
          } else {
            System.out.println(this.messages.getString("deleteError"));
          }
          break;
        case "modify":
          if (parsedSelection.length == 2) {
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
    if (this.recordMap.containsKey(recordName)) {
      System.out.println(this.messages.getString("recordExists"));
      return;
    }
    this.recordMap.put(recordName, this.addAttributes(new HashMap<String, String>()));
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
    if (!this.recordMap.containsKey(recordName)) {
      System.out.println(this.messages.getString("recordNotFound"));
      return;
    }
    this.recordMap.put(recordName, this.addAttributes(this.recordMap.get(recordName)));
    this.modified = true;
  }

  /**
   * Write changes to disk.
   * 
   * @throws Exception if an error occurs while reading input from the console.
   */
  private void save() throws Exception {
    char[][] passwords = this.getPasswords();
    try {
      if (this.encryptedBuffer == null) {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(
            this.recordMap,
            passwords);
      } else {
        if (!this.encryptedBuffer.validatePassword(passwords)) {
          System.out.println(this.messages.getString("incorrectPasswords"));
          return;
        }
        this.encryptedBuffer.updateContents(this.recordMap, passwords);
      }
    } catch (EncryptionException | DecryptionException e) {
      System.out.println(this.messages.getString("encryptionException"));
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
      this.modified = false;
      System.out.println(this.messages.getString("saveSuccessful"));
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionWrite"));
    }
  }

  /**
   * Change the password that is used to protect this set of records.  Prompt the user first for
   * their original password and then for their new password.
   * 
   * @throws Exception if an error occurs while reading input from the console.
   */
  private void change() throws Exception {
    if (this.encryptedBuffer == null) {
      System.out.println(this.messages.getString("fileNotLoaded"));
      return;
    }
    if (this.modified) {
      System.out.println(this.messages.getString("savePrompt"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
        if (reader.readLine().strip().startsWith("Y")) {
          this.save();
        }
      } catch (IOException e) {
        System.out.println(this.messages.getString("inputError"));
        e.printStackTrace();
        return;
      }
    }
    try {
      if (!this.encryptedBuffer.validatePassword(this.getPasswords())) {
        System.out.println(this.messages.getString("incorrectPasswords"));
        return;
      }
    } catch (DecryptionException e1) {
      System.out.println(this.messages.getString("corruptFileError"));
      e1.printStackTrace();
    }
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(
          this.recordMap,
          this.getPasswords());
    } catch (EncryptionException e) {
      System.out.println(this.messages.getString("setPasswordError"));
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionWrite"));
      return;
    }
    this.modified = false;
    System.out.println(this.messages.getString("setPasswordSuccess"));
  }

  /**
   * Exit this program.  Check if the user wants to save before quitting.
   * 
   * @throws Exception if an error occurs while getting input from the user.
   */
  private void quit() throws Exception {
    if (this.modified) {
      System.out.println(this.messages.getString("savePrompt"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      try {
        if (reader.readLine().strip().startsWith("Y")) {
          this.save();
        }
      } catch (IOException e) {
        System.out.println(this.messages.getString("inputError"));
        e.printStackTrace();
        return;
      }
    }
    System.exit(0);
  }

  /**
   * Generate a pseudorandom password according the parameters entered by the user.  These include
   * the allowed characters and the length of the password.
   * 
   * @return a string containing a pseudorandom password.
   */
  private String generatePassword() {
    Map<String, char[]> characterChoices = Map.ofEntries(
        new AbstractMap.SimpleEntry<String, char[]>(
            this.messages.getString("numbers"),
            passwordio.PasswordGenerator.NUMBERS),
        new AbstractMap.SimpleEntry<String, char[]>(
            this.messages.getString("lowerCaseLetters"),
            passwordio.PasswordGenerator.LOWER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<String, char[]>(
            this.messages.getString("upperCaseLetters"),
            passwordio.PasswordGenerator.UPPER_CASE_LETTERS),
        new AbstractMap.SimpleEntry<String, char[]>(
            this.messages.getString("symbols"),
            passwordio.PasswordGenerator.SYMBOLS));
    ArrayList<Character> chosenCharactersObjects = new ArrayList<Character>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      for (String choice: characterChoices.keySet()) {
        System.out.println(
            this.messages.getString("includePart1") +
            choice + 
            this.messages.getString("includePart2"));
        if (reader.readLine().strip().startsWith("Y")) {
          for (char c: characterChoices.get(choice)) {
            chosenCharactersObjects.add(c);
          }
        }
      }
    } catch (IOException e) {
      System.out.println(this.messages.getString("inputError"));
      return "";
    }
    char[] chosenCharacters = new char[chosenCharactersObjects.size()];
    for (int i = 0; i < chosenCharacters.length; i++) {
      chosenCharacters[i] = chosenCharactersObjects.get(i);
    }
    System.out.println(this.messages.getString("getLength"));
    int length = 0;
    try {
      length = Integer.parseInt(reader.readLine());
    } catch (NumberFormatException e) {
      System.out.println(this.messages.getString("notANumber"));
      return "";
    } catch (IOException e) {
      System.out.println(this.messages.getString("inputError"));
      return "";
    }
    return new String(new passwordio.PasswordGenerator(chosenCharacters).generatePassword(length));
  }

  /**
   * Open a passwords file.
   * 
   * @throws Exception if an error occurs while reading user input
   */
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

  /**
   * Get the passwords to decrypt a file from the user.
   * 
   * @return an array of character arrays containing the passwords that were entered.
   * @throws Exception if an error occurs while getting user input.
   */
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

  /**
   * Add attributes-value pairs to a map containing the details for a particular account record.
   * 
   * @param attributes is the existing map of attributes for an account.
   * @return an updated map of attribute-value pairs for the account.
   */
  private Map<String, String> addAttributes(Map<String, String> attributes) {
    while (true) {
      System.out.println(this.messages.getString("enterAttribute"));
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String attribute;
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
      String value;
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
      attributes.put(attribute, value);
    }
    return attributes;
  }

}
