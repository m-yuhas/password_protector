package gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import passwordio.PasswordGenerator;

/**
 * Window that helps the user generate pseudorandom passwords.
 */
public class GeneratePasswordWindow {

  /**
   * The parent window from which this window is created.
   */
  private MainWindow parentWindow;
  
  /**
   * The JFrame in which the contents of this window will be drawn.
   */
  private JFrame mainFrame;

  /**
   * Map of JCheckBoxes to the characters that they allow to be included in the generated password.
   */
  private Map<JCheckBox, char[]> characterChoices;

  /**
   * The JTextField where the generated password will be displayed.
   */
  private JTextField generatedPasswordField = new JTextField(16);

  /**
   * The JTextField where the user can enter the desired password size.
   */
  private JTextField passwordSizeField = new JTextField(2);

  /**
   * Construct an instance of GeneratePasswordWindow.
   */
  public GeneratePasswordWindow(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.setupCharacterChoices();
    this.mainFrame = new JFrame("Generate Password");
    this.mainFrame.add(this.setupMainPanel());
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  /**
   * Setup the main JPanel that will contain all the components for this window.  This panel should
   * include: 1) a series of checkboxes to determine the allowable characters; 2) a panel to get
   * the desired password length from the user; 3) a button to generate a password; 4) a field to
   * display the generated password; and 5) a button to copy the generated password to the clip
   * board.
   *
   * @return JPanel containing all the graphical components for the password generator.
   */
  private JPanel setupMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    for (JCheckBox checkBox: this.characterChoices.keySet()) {
      mainPanel.add(checkBox);
    }
    mainPanel.add(this.setupPasswordSizePanel());
    mainPanel.add(this.setupGenerateButton());
    this.generatedPasswordField.setEditable(false);
    mainPanel.add(generatedPasswordField);
    mainPanel.add(this.setupCopyButton());
    return mainPanel;
  }

  /**
   * Setup the map of JCheckBoxes labeled with human readable text to the characters that they
   * permit in the generated password when checked.
   */
  private void setupCharacterChoices() {
    this.characterChoices = new HashMap<JCheckBox, char[]>();
    this.characterChoices.put(
        new JCheckBox(this.parentWindow.resourceBundle.getString("numbers")),
        passwordio.PasswordGenerator.NUMBERS);
    this.characterChoices.put(
        new JCheckBox(this.parentWindow.resourceBundle.getString("lowerCase")),
        passwordio.PasswordGenerator.LOWER_CASE_LETTERS);
    this.characterChoices.put(
        new JCheckBox(this.parentWindow.resourceBundle.getString("upperCase")),
        passwordio.PasswordGenerator.UPPER_CASE_LETTERS);
    this.characterChoices.put(
        new JCheckBox(this.parentWindow.resourceBundle.getString("symbols")),
        passwordio.PasswordGenerator.SYMBOLS);
  }

  /**
   * Setup the password size panel, which contains a label and a text entry component.
   *
   * @return JPanel with the components to get password size from the user.
   */
  private JPanel setupPasswordSizePanel() {
    JPanel passwordSizePanel = new JPanel();
    passwordSizePanel.add(new JLabel(this.parentWindow.resourceBundle.getString("length")));
    passwordSizePanel.add(this.passwordSizeField);
    return passwordSizePanel;
  }

  /**
   * Setup the generate password button and add the appropriate action listener.
   *
   * @return JButton for the 'generate password' button.
   */
  private JButton setupGenerateButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("generatePassword");
    JButton generateButton = new JButton(buttonText);
    generateButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        generate();

      }
    });
    return generateButton;
  }

  /**
   * Setup the copy to clip board button and add the appropriate action listener.
   *
   * @return JButton for the 'copy to clip board' button.
   */
  private JButton setupCopyButton() {
    JButton copyButton = new JButton(this.parentWindow.resourceBundle.getString("copyToClipBoard"));
    copyButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        copyToClipboard();
      }

    });
    return copyButton;
  }

  /**
   * Generate a pseudorandom password with the allowable characters and the length specified by the
   * user.
   */
  private void generate() {
    ArrayList<char[]> acceptableCharacterSets = new ArrayList<char[]>();
    for (Entry<JCheckBox, char[]> choice: this.characterChoices.entrySet()) {
      if (choice.getKey().isSelected()) {
        acceptableCharacterSets.add(choice.getValue());
      }
    }
    char[][] acceptableCharacters = acceptableCharacterSets.toArray(new char[0][]);
    passwordio.PasswordGenerator passwordGenerator = new PasswordGenerator(acceptableCharacters);
    try {
      int passwordLength = Integer.parseInt(this.passwordSizeField.getText());
      String password = new String(passwordGenerator.generatePassword(passwordLength));
      this.generatedPasswordField.setText(password);
    } catch (NumberFormatException e) {
      this.displayGeneratePasswordError();
      return;
    }
  }

  /**
   * Copy the text in the generated password field to the system clip board.
   */
  private void copyToClipboard() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(new StringSelection(this.generatedPasswordField.getText()), null);
  }

  /**
   * Display an error dialog if an error occurs while generating a password.
   */
  private void displayGeneratePasswordError() {
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.parentWindow.resourceBundle.getString("generatePasswordError"),
        this.parentWindow.resourceBundle.getString("generatePassword"),
        JOptionPane.ERROR_MESSAGE);
  }

}