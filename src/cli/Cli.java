/**
 * 
 */
package cli;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author yuhasmj
 *
 */
public class Cli {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Options options = new Options();
    options.addOption("e", "encrypt", false, "Encrypt a file.");
    options.addOption("d", "decrypt", false, "Decrypt a file.");
    options.addOption("p", "password_protector", true, "Open a password storage file.");
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
        System.out.println("Encrypt");
      } else if (parsedArgs.hasOption("d") && !parsedArgs.hasOption("d")) {
        System.out.println("Decrypt");
      }
    } else {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("password_protector", options);
    }
  }

}
