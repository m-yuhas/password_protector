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

  /**
   * The resource bundle of internationalization strings.
   */
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
      File passwordFile = new File(parsedArgs.get("p"));
      try {
        PasswordProtector passwordProtector = new PasswordProtector(passwordFile, messages);
        passwordProtector.mainLoop();
      } catch (Exception e) {
        return;
      }
    } else if (parsedArgs.containsKey("i") && parsedArgs.containsKey("o")) {
      if (parsedArgs.containsKey("e") && !parsedArgs.containsKey("d")) {
        this.encryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      } else if (parsedArgs.containsKey("d") && !parsedArgs.containsKey("e")) {
        this.decryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      }
    } else {
      argumentParser.printHelp();
      return;
    }
  }

  /**
   * Encrypt a file.
   * 
   * @param inputFile is the unencrypted input file to this process.
   * @param outputFile is the encrypted output file generated by this process.
   */
  private void encryptFile(File inputFile, File outputFile) {
    if (!inputFile.isFile()) {
      System.out.println(this.messages.getString("badFileError"));
      return;
    }
    Console console = System.console();
    if (console == null) {
      System.out.println(this.messages.getString("consoleError"));
      return;
    }
    char[] password = console.readPassword(this.messages.getString("readPasswordEncrypt"));
    EncryptedBuffer<byte[]> buffer;
    try {
      buffer = new EncryptedBuffer<byte[]>(Files.readAllBytes(inputFile.toPath()), password);
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionRead"));
      return;
    } catch (EncryptionException e) {
      System.out.println(this.messages.getString("encryptionException"));
      return;
    }
    try {
      buffer.writeToFile(outputFile);
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionWrite"));
      return;
    }
  }

  /**
   * Decrypt a file.
   * 
   * @param inputFile is the encrypted file serving as input to this process.
   * @param outputFile is the unencrypted output file that is the output of this process.
   */
  private void decryptFile(File inputFile, File outputFile) {
    if (!inputFile.isFile()) {
      System.out.println(this.messages.getString("badFileError"));
      return;
    }
    Console console = System.console();
    if (console == null) {
      System.out.println(this.messages.getString("consoleError"));
      System.exit(0);
    }
    char[] password = console.readPassword(this.messages.getString("readPasswordDecrypt"));
    EncryptedBuffer<byte[]> buffer;
    try {
      buffer = new EncryptedBuffer<byte[]>(inputFile);
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionRead"));
      return;
    }
    try {
      Files.write(outputFile.toPath(), buffer.decrypt(password));
    } catch (DecryptionException e) {
      System.out.println(this.messages.getString("incorrectPassword"));
      return;
    } catch (IOException e) {
      System.out.println(this.messages.getString("ioExceptionWrite"));
      return;
    } catch (ClassNotFoundException e) {
      System.out.println(this.messages.getString("classNotFoundException"));
      return;
    }
  }

  /**
   * Setup the argument parser.
   * 
   * @return an ArgumentParser object ready to parse the command-line arguments for this program.
   */
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
