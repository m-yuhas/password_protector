package cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import passwordio.AccountRecord;
import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

public class PasswordProtector {
  
  private boolean modified = false;
  private File file;
  private Map<String, passwordio.AccountRecord> recordMap;
  private EncryptedBuffer<Map<String, AccountRecord>> encryptedBuffer;
  
  public PasswordProtector(File file) {
    this.file = file;
    if (this.file.isFile()) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char[] password = console.readPassword("Enter the password to unlock this file:");
      try {
        encryptedBuffer = new EncryptedBuffer<Map<String, passwordio.AccountRecord>>(file);
        this.recordMap = encryptedBuffer.decrypt(password);       
      } catch (DecryptionException e) {
        System.out.println("Password incorrect.");
        System.exit(0);
      } catch (IOException e) {
        System.out.println("Error reading from file.");
        System.exit(0);
      }
    } else {
      this.recordMap = new HashMap<String, passwordio.AccountRecord>();
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
          if (parsedSelection.length > 1) {
            this.view(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter an account name (e.g. 'VIEW Facebook')");
          }
          break;
        case "add":
          if (parsedSelection.length > 1) {
            this.add(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter an account name (e.g. 'ADD Facebook')");
          }
          break;
        case "delete":
          if (parsedSelection.length > 1) {
            this.delete(parsedSelection[1].trim());
          } else {
            System.out.println("Please enter an account name (e.g. 'DELETE Facebook')");
          }
          break;
        case "modify":
          if (parsedSelection.length > 1) {
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
    Iterator<Entry<String, AccountRecord>> iterator = this.recordMap.entrySet().iterator();
    int count = 0;
    while (iterator.hasNext()) {
      count++;
      Entry<String, AccountRecord> pair = iterator.next();
      System.out.println(count + ". " + pair.getKey());
    }
    if (count == 0) {
      System.out.println("No records available.  ADD some or try a different file.");
    }
  }

  private void view(String recordName) {
    if (this.recordMap.containsKey(recordName)) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char[] password = console.readPassword("Enter the password to view this record:");
      try {
        Map<String, String> recordDetail = this.recordMap.get(recordName).getRecord(password);
        Iterator<Entry<String, String>> iterator = recordDetail.entrySet().iterator();
        while (iterator.hasNext()) {
          Entry<String, String> pair = iterator.next();
          System.out.println(pair.getKey() + ": " + pair.getValue());
          iterator.remove();
        }
      } catch (DecryptionException e) {
        System.out.println("Error occurred decrypting the record.  Your password may be incorrect.");
        e.printStackTrace();
      }
    } else {
      System.out.println("Sorry, no record with that name could be found.");
    }
  }

  private void add(String recordName) {
    String keyName = "";
    String valueName = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Map<String, String> recordData = new HashMap<String, String>();
    while (true) {
      System.out.println("Enter a key name (\"DONE\" to finalize this record):");
      try {
        System.out.print(">");
        keyName = reader.readLine();
      } catch (IOException e) {
        System.out.println("Error occurred reading the input, please try again.");
        continue;
      }
      if (keyName.strip().toLowerCase().equals("done")) {
        break;
      }
      System.out.println("Enter the corresponding value (\"GENERATE\" to generate a new password:");
      try {
        System.out.print(">");
        valueName = reader.readLine();
        if (valueName.strip().toLowerCase().contentEquals("generate")) {
          valueName = this.generatePassword();
        }
      } catch (IOException e) {
        System.out.println("Error occurred reading the input, please try again.");
        continue;
      }
      recordData.put(keyName, valueName);
    }
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] password = console.readPassword("Enter the password to encrypt this record:");
    try {
      this.recordMap.put(recordName, new AccountRecord(recordName, recordData, password));
      this.modified = true;
    } catch (EncryptionException e) {
      System.out.println("An error occurred while encrypting the record.  Please try again.");
      e.printStackTrace();
    }
  }

  private void delete(String recordName) {
    if (this.recordMap.containsKey(recordName)) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char[] password = console.readPassword("Enter the password to delete record '" + recordName +"':");
      try {
        this.recordMap.get(recordName).getRecord(password);
      } catch (DecryptionException e) {
        System.out.println("Password incorrect.");
        return;
      }
      this.recordMap.remove(recordName);
      this.modified = true;
    } else {
      System.out.println("Sorry, no record with that name could be found.");
    }
  }

  private void modify(String recordName) {
    
  }

  private void save() {
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] password = console.readPassword("Enter a password for this file:");
    if (this.encryptedBuffer == null) {
      try {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, passwordio.AccountRecord>>(this.recordMap, password);
      } catch (EncryptionException e) {
        System.out.println("An error occurred during encryption.  Please try again");
        return;
      }
    } else {
      try {
        this.encryptedBuffer.updateContents(this.recordMap, password);
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
    if (this.encryptedBuffer != null) {
      Console console = System.console();
      if (console == null) {
        System.out.println("Could not get console.  Please check system configuration.");
        System.exit(0);
      }
      char [] password = console.readPassword("Enter the old password for this file:");
      if (!this.encryptedBuffer.validatePassword(password)) {
        System.out.println("Password incorrect.");
        return;
      }
    }
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char [] password = console.readPassword("Enter the new password for this file:");
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map <String, passwordio.AccountRecord>>(this.recordMap, password);
    } catch (EncryptionException e) {
      System.out.println("Error setting new password:");
      e.printStackTrace();
    }
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
    return "";
  }

}
