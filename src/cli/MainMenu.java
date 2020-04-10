package cli;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

/**
 * This class serves as the main menu for the CLI.  It parses any command line arguments and starts
 * the requested subroutines.
 */
public class MainMenu {
  
  private ResourceBundle messages;

  /**
   * Create an instance of MainMenu.  This class parses the CLI arguments and either prints a help
   * message or launches the desired feature.
   * 
   * @param messages is a ResourceBundle containing the internationalization strings.
   * @param args is the command line arguments as a string array.
   */
  public MainMenu(ResourceBundle messages, String[] args) {
    this.messages = messages;
    ArgumentParser argumentParser = this.setupArgumentParser();
    Map<String, String> parsedArgs = new HashMap<String, String>();
    try {
      parsedArgs = argumentParser.parseArgs(args);
    } catch (cli.ParseException e) {
      argumentParser.printHelp();
      return;
    }
    if (parsedArgs.containsKey("p")) {
      PasswordProtector passwordProtector = new PasswordProtector(new File(parsedArgs.get("p")));
      passwordProtector.mainLoop();
    } else if (parsedArgs.containsKey("i") && parsedArgs.containsKey("o")) {
      if (parsedArgs.containsKey("e") && !parsedArgs.containsKey("d")) {
        this.encryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      } else if (parsedArgs.containsKey("d") && !parsedArgs.containsKey("d")) {
        this.decryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      }
    } else {
      argumentParser.printHelp();
      return;
    }
  }
  
  private void encryptFile(File inputFile, File outputFile) {
    if (!inputFile.isFile()) {
      System.out.println("Input file must exist and be a regular file");
      return;
    }
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] password = console.readPassword("Enter the password to encrypt this file:");
    EncryptedBuffer<Byte[]> buffer;
    try {
      byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
      Byte[] inputBytesObject = new Byte[inputBytes.length];
      int i = 0;
      for (byte b: inputBytes) {
        inputBytesObject[i++] = b;
      }
      buffer = new EncryptedBuffer<Byte[]>(inputBytesObject, password);
    } catch (IOException e) {
      System.out.println("Error occurred while attempting to open the file");
      return;
    } catch (EncryptionException e) {
      System.out.println("EncryptionException occurred while attempting to encrypt the file contents.");
      return;
    }
    try {
      buffer.writeToFile(outputFile);
    } catch (IOException e) {
      System.out.println("IOException occurred while attempting to open the file.");
      return;
    }
  }
  
  private void decryptFile(File inputFile, File outputFile) {
    if (!inputFile.isFile()) {
      System.out.println("Input file must exist and be a regular file");
      return;
    }
    Console console = System.console();
    if (console == null) {
      System.out.println("Could not get console.  Please check system configuration.");
      System.exit(0);
    }
    char[] password = console.readPassword("Enter the password to decrypt this file:");
    EncryptedBuffer<Byte[]> buffer;
    try {
      buffer = new EncryptedBuffer<Byte[]>(inputFile);
    } catch (IOException e1) {
      System.out.println("IOException occurred while attempting to open the file.");
      return;
    }
    try {
      Byte[] outputBytesObject = buffer.decrypt(password);
      byte[] outputBytes = new byte[outputBytesObject.length];
      int i = 0;
      for (Byte b: outputBytesObject) {
        outputBytes[i++] = b.byteValue();
      }
      Files.write(outputFile.toPath(), outputBytes);
    } catch (DecryptionException e) {
      System.out.println("Incorrect Password");
      return;
    } catch (IOException e) {
      System.out.println("IOException occurred while attempting to write the new file.");
      return;
    } catch (ClassNotFoundException e) {
      System.out.println("Should be impossible");
      e.printStackTrace();
    }
  }
  
  private ArgumentParser setupArgumentParser() {
    ArgumentParser argumentParser = new ArgumentParser(this.messages.getString("usage"));
    argumentParser.addArgument(
        "e",
        this.messages.getString("encrypt"),
        false,
        this.messages.getString("encryptHelp"));
    argumentParser.addArgument(
        "d",
        this.messages.getString("decrypt"),
        false,
        this.messages.getString("decryptHelp"));
    argumentParser.addArgument(
        "p", 
        this.messages.getString("passwordProtector"),
        true,
        this.messages.getString("passwordProtectorHelp"));
    argumentParser.addArgument(
        "i",
        this.messages.getString("inputFile"),
        true,
        this.messages.getString("inputFileHelp"));
    argumentParser.addArgument(
        "o",
        this.messages.getString("outputFile"),
        true,
        this.messages.getString("outputFileHelp"));
    return argumentParser;
  }
}
