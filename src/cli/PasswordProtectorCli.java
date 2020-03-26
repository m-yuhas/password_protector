/**
 * 
 */
package cli;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
    Options options = new Options();
    options.addOption("e", "encrypt", false, "Encrypt a file.");
    options.addOption("d", "decrypt", false, "Decrypt a file.");
    options.addOption("p", "password_protector", true, "Open a password storage file or create a new one.");
    options.addOption("i", "input_file", true, "Input file for encrypt and decrypt modes.");
    options.addOption("o", "output_file", true, "Output file for encrypt and decrypt modes.");
    CommandLineParser parser = new DefaultParser();
    CommandLine parsedArgs;
    try {
      parsedArgs = parser.parse(options, args);
    } catch (ParseException e) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("password_protector", options);
      return;
    }
    if (parsedArgs.hasOption("p")) {
      PasswordProtector passwordProtector = new PasswordProtector(new File(parsedArgs.getOptionValue("p")));
      passwordProtector.mainLoop();
    } else if (parsedArgs.hasOption("i") && parsedArgs.hasOption("o")) {
      if (parsedArgs.hasOption("e") && !parsedArgs.hasOption("d")) {
        encryptFile(new File(parsedArgs.getOptionValue("i")), new File(parsedArgs.getOptionValue("o")));
      } else if (parsedArgs.hasOption("d") && !parsedArgs.hasOption("d")) {
        decryptFile(new File(parsedArgs.getOptionValue("i")), new File(parsedArgs.getOptionValue("o")));
      }
    } else {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("password_protector", options);
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
      Byte[] outputBytesObject = (Byte[]) buffer.decrypt(password);
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
