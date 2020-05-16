package gui;

import java.awt.EventQueue;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Main class for the PasswordProtector GUI.  This class creates an event Queue which is used to
 * launch the main window and all other windows.
 */
public class PasswordProtector {

  /**
   * Entry point for PasswordProtector GUI execution.  Create an event Queue and place a new
   * instance of MainWindow in it.
   * 
   * @param args is an array of strings that are the command line arguments passed to the JVM.
   */
  public static void main(String[] args) { 
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        try {
          new MainWindow(ResourceBundle.getBundle("gui.StringsBundle", Locale.getDefault()));
        } catch (MissingResourceException e) {
          new MainWindow(ResourceBundle.getBundle("gui.StringsBundle", new Locale("en", "US")));
        }
      }

    });
  }

}