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

import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

public class PasswordProtector {
  
  private boolean modified = false;
  private File file;
  private Map<String, Map<String, String>> recordMap;
  private EncryptedBuffer<Map<String, Map<String, String>>> encryptedBuffer;
  
  public PasswordProtector(File file) {
    this.file = file;
    if (this.file.isFile()) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char[] passwordOne = console.readPassword("Enter password 1:");
      char[] passwordTwo = console.readPassword("Enter password 2:");
      try {
        encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(file);
        this.recordMap = encryptedBuffer.decrypt(passwordOne, passwordTwo);       
      } catch (DecryptionException e) {
        System.out.println("One or more of the passwords was incorrect.");
        System.exit(0);
      } catch (IOException e) {
        System.out.println("An error occurred while reading from file.");
        System.exit(0);
      }
    } else {
      this.recordMap = new HashMap<String, Map<String, String>>();
    }
  }
  
  public void mainLoop() {
    while (true) {
      System.out.println("Options: \n1. LIST accounts\n2. VIEW account\n3. ADD account\n4. DELETE account\n5. MODIFY account\n6. SAVE file\n7. CHANGE file password\n8. QUIT");
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String selection;
      try {
        System.out.print(">>>");
        selection = reader.readLine();
      } catch (IOException e) {
        System.out.println("Error occurred reading the input, please try again.");
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
            System.out.println("Please enter an account name (e.g. 'VIEW Facebook')");
          }
          break;
        case "add":
          if (parsedSelection.length == 1) {
            this.add(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter an account name (e.g. 'ADD Facebook')");
          }
          break;
        case "delete":
          if (parsedSelection.length == 1) {
            this.delete(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter an account name (e.g. 'DELETE Facebook')");
          }
          break;
        case "modify":
          if (parsedSelection.length == 1) {
            this.modify(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter account name (e.g. 'MODIFY' Facebook')");
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
          System.out.println("Sorry, I didn't understand.");
          break;
      }
    }
  }

  private void list() {
    int count = 0;
    for (String account: this.recordMap.keySet()) {
      System.out.println(Integer.toString(++count) + ". " + account);
    }
    if (count == 0) {
      System.out.println("No records available.  ADD some or try a different file.");
    }
  }

  private void view(String recordName) {
    if (this.recordMap.containsKey(recordName)) {
      Map<String, String> record = this.recordMap.get(recordName);
      for (String attribute: record.keySet()) {
        System.out.println(attribute + ": " + record.get(attribute));
      }
    } else {
      System.out.println("Sorry, no record with that name could be found.");
    }
  }

  private void add(String recordName) {
    String attribute = "";
    String value = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Map<String, String> recordData = new HashMap<String, String>();
    while (true) {
      System.out.println("Enter an account attribute (\"DONE\" to finalize this record):");
      try {
        System.out.print(">");
        attribute = reader.readLine();
      } catch (IOException e) {
        System.out.println("Error occurred reading the input, please try again.");
        continue;
      }
      if (attribute.strip().toLowerCase().equals("done")) {
        break;
      }
      System.out.println("Enter the corresponding value (\"GENERATE\" to generate a new password:");
      try {
        System.out.print(">");
        value = reader.readLine();
        if (value.strip().toLowerCase().contentEquals("generate")) {
          value = this.generatePassword();
        }
      } catch (IOException e) {
        System.out.println("Error occurred reading the input, please try again.");
        continue;
      }
      recordData.put(attribute, value);
    }
    this.recordMap.put(recordName, recordData);
    this.modified = true;
  }

  private void delete(String recordName) {
    if (!this.recordMap.containsKey(recordName)) {
      System.out.println("Sorry, no record with that name could be found.");
      return;
    }
    if (this.encryptedBuffer != null) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char[] passwordOne = console.readPassword("Enter password 1:");
      char[] passwordTwo = console.readPassword("Enter password 2:");
      if (!this.encryptedBuffer.validatePassword(passwordOne, passwordTwo)) {
        System.out.println("One or more of the passwords is incorrect.");
        return;
      }
    }
    this.recordMap.remove(recordName);
    this.modified = true;
  }

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
      } catch (EncryptionException e) {
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
    if (!this.encryptedBuffer.validatePassword(oldPasswordOne, oldPasswordTwo)) {
      System.out.println("One or more of the passwords is incorrect.");
      return;
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

}
