package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * Window that prompts the user for one or more passwords.
 */
public class PasswordEntryWindow extends JDialog {

  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = 5118665644770822849L;

  /**
   * Integer representing the minimum width of this window when drawn.  The current value has been
   * determined experimentally.
   */
  private final int width = 500;

  /**
   * Integer representing the minimum height of this window when drawn.  The current value has been
   * determined experimentally.
   */
  private final int height = 150;

  /**
   * Integer representing the display length in characters of a password field on this window.  The
   * current value has been determined experimentally.
   */
  private final int passwordFieldDisplayLength = 32;

  /**
   * Integer number of passwords to gather in this window.
   */
  private int numberOfPasswords;

  /**
   * Array of JPasswordFields that will contain the passwords entered by the user.
   */
  private JPasswordField[] passwordFields;

  /**
   * The parent window from which this window is created.
   */
  private transient MainWindow parentWindow;

  /**
   * Construct an instance of PasswordEntryWindow and draw it.
   *
   * @param parentWindow is the MainWindow from which this window was spawned.
   * @param message is the String to display as a prompt for the user.  This prompt should include
   *        the reason why the user is being asked to provide a password or passwords.
   * @param numberOfPasswords is the integer number of passwords for which to prompt the user.
   */
  public PasswordEntryWindow(MainWindow parentWindow, String message, int numberOfPasswords) {
    this.parentWindow = parentWindow;
    this.numberOfPasswords = numberOfPasswords;
    this.setModal(true);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setSize(new Dimension(this.width, this.height));
    this.setTitle("Password Entry");
    this.add(this.setupMainPanel(message));
    this.setVisible(true);
  }

  /**
   * Setup the JPanel that will be presented to the user with all the graphical elements required
   * for this window's operation.
   *
   * @param message is the String to display as a prompt for the user.  This prompt should include
   *        the reason why the user is being asked to provide a password or passwords. 
   * @return JPanel containing all the graphical elements to display to the user.
   */
  private JPanel setupMainPanel(String message) {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(new JPanel().add(new JLabel(message + "\n")));
    JLabel[] passwordLabels = new JLabel[this.numberOfPasswords];
    this.passwordFields = new JPasswordField[this.numberOfPasswords];
    for (int i = 0; i < this.numberOfPasswords; i++) {
      JPanel passwordPanel = new JPanel();
      passwordLabels[i] = new JLabel(
          this.parentWindow.resourceBundle.getString("password") + Integer.toString(i+1) + ":");
      passwordPanel.add(passwordLabels[i]);
      this.passwordFields[i] = new JPasswordField(this.passwordFieldDisplayLength);
      passwordPanel.add(this.passwordFields[i]);
      mainPanel.add(passwordPanel);
    }
    mainPanel.add(this.setupSubmitButton());
    return mainPanel;
  }

  /**
   * Setup the submit button.
   *
   * @return JButton with submit button's functionality.
   */
  private KeyableButton setupSubmitButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("submit");
    KeyableButton submitButton = new KeyableButton(buttonText);
    submitButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        dispose();
      }

    });
    return submitButton;
  }

  /**
   * Get the passwords that were entered by the user.
   *
   * @return array of character arrays where values in the zeroth dimension correspond to the nth
   *         password entered by the user and values in the next dimension are the characters in
   *         that password.
   */
  public char[][] getPasswords() {
    char[][] passwords = new char[this.numberOfPasswords][];
    for (int i = 0; i < this.numberOfPasswords; i++) {
      passwords[i] = this.passwordFields[i].getPassword();
    }
    return passwords;
  }

}
