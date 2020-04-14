package cli;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Main class in the CLI package.  This class serves as the entry point for the CLI.
 */
public class PasswordProtectorCli {

  /**
   * Entry point for the PasswordProtectorCli.  Its only responsibility is to load the
   * internationalization resource bundle and start the main menu.
   */
  public static void main(String[] args) {
    try {
      new MainMenu(ResourceBundle.getBundle("cli.StringsBundle", Locale.getDefault()), args);
    } catch (MissingResourceException e) {
      new MainMenu(ResourceBundle.getBundle("cli.StringsBundle", new Locale("en", "US")), args);
    }
  }

}