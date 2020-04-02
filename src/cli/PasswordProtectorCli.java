/**
 * 
 */
package cli;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

/**
 * @author yuhasmj
 *
 */
public class PasswordProtectorCli {

  /**
   * @param args
   */
  public static void main(String[] args) {
    ArgumentParser argumentParser = new ArgumentParser("usage: java -jar PasswordProtectorCli.jar");
    argumentParser.addArgument("e", "encrypt", false, "Encrypt a file.");
    argumentParser.addArgument("d", "decrypt", false, "Decrypt a file.");
    argumentParser.addArgument("p", "password_protector", true, "Open a password storage file or create a new one.");
    argumentParser.addArgument("i", "input_file", true, "Input file for encrypt and decrypt modes.");
    argumentParser.addArgument("o", "output_file", true, "Output file for encrypt and decrypt modes.");
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
        encryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      } else if (parsedArgs.containsKey("d") && !parsedArgs.containsKey("d")) {
        decryptFile(new File(parsedArgs.get("i")), new File(parsedArgs.get("o")));
      }
    } else {
      argumentParser.printHelp();
    }
  }
  
  private static void encryptFile(File inputFile, File outputFile) {
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
  
  private static void decryptFile(File inputFile, File outputFile) {
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
    }
  }

}
