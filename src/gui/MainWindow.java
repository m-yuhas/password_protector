package gui;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import passwordio.DecryptionException;
import passwordio.DecryptionExceptionCode;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

/**
 * The main window of the password protector GUI.
 */
public class MainWindow {

  /**
   * ResourceBundle that includes language specific strings.
   */
  protected ResourceBundle resourceBundle;

  /**
   * The JFrame that contains everything drawn as part of the main window.
   */
  protected JFrame mainFrame;

  /**
   * The portion of the window where the accounts are listed.
   */
  protected ListPanel listPanel;

  /**
   * The data structure containing the attributes and values for each protected account.  The keys
   * are the account names as displayed in the ListPanel, and the values are a map of strings where
   * the keys are an attribute (e.g. 'password'), and the values are the corresponding value
   * (e.g. 'abc123').
   */
  protected Map<String, Map<String, String>> recordMap;

  /**
   * The encrypted buffer where encrypted data read from a password file is stored.  When loading a
   * file recordMap is created by decrypting this buffer, and when saving a file, this buffer is
   * created using the contents of recordMap.
   */
  protected EncryptedBuffer<Map<String, Map<String, String>>> encryptedBuffer;

  /**
   * If true, the currently data from the loaded file has been modified.  If false, no file has
   * been loaded or the data from the file has not been modified.
   */
  protected boolean modified = false;

  /**
   * Supported languages that can be loaded on the fly from the "languages" menu.  The key is the
   * language name as it should appear on the UI, and the value is the Locale to use when reloading
   * the resource bundle.
   */
  private final static Map<String, Locale> supportedLanguages;
  static {
      Map<String, Locale> map = new HashMap<String, Locale>();
      map.put("English", new Locale("en", "US"));
      map.put("español", new Locale("es", "MX"));
      map.put("français", new Locale("fr", "CA"));
      map.put("中文", new Locale("zh", "CN"));
      supportedLanguages = Collections.unmodifiableMap(map);
  }

  /**
   * The portion of the window where the control action buttons are located.
   */
  private ControlPanel controlPanel;

  /**
   * The currently loaded passwords file.
   */
  private File file;

  /**
   * This menu item allows the user to change the file password for the currently loaded password
   * file.  Unlike the other menu items, changeFilePasswordItem becomes a class attribute, because
   * the state of this menu item can toggle between active an inactive depending on if a file has
   * been loaded or not.
   */
  private MenuItem changeFilePasswordItem;

  /**
   * Construct an instance of MainWindow.
   * 
   * @param resourceBundle is the resource bundle containing the strings to use when initially
   *        drawing window.
   */
  public MainWindow(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
    this.recordMap = new HashMap<String, Map<String,String>>();
    if (System.getProperty("os.name").contentEquals("Mac OS X")) {
      System.setProperty("apple.laf.userScreenMenuBar", "true");
    }
    this.setupMainFrame();
  }

  /**
   * Initialize the main JFrame for main window.
   */
  private void setupMainFrame() {
    this.mainFrame = new JFrame(this.resourceBundle.getString("passwordProtector"));
    this.mainFrame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent windowEvent) {
        promptSaveChanges();
      }

    });
    this.mainFrame.add(this.setupMainPanel());
    this.mainFrame.setMenuBar(this.setupMenuBar());
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  /**
   * Initialize and return the main JPanel for the main frame.  This JPanel contains the control
   * panel and the list panel.
   *
   * @return a JPanel containing the control panel and the list panel.
   */
  private JPanel setupMainPanel() {
    this.controlPanel = new ControlPanel(this);
    this.listPanel = new ListPanel(this);
    JPanel mainPanel = new JPanel();
    mainPanel.add(this.listPanel);
    mainPanel.add(this.controlPanel);
    return mainPanel;
  }

  /**
   * Initialize the menu bar for the main window.
   *
   * @return a MenuBar containing all the menus for the main window.
   */
  private MenuBar setupMenuBar() {
    MenuBar menuBar = new MenuBar();
    menuBar.add(this.setupFileMenu());
    menuBar.add(this.setupPasswordMenu());
    menuBar.add(this.setupEncryptionMenu());
    menuBar.add(this.setupLanguageMenu());
    return menuBar;
  }

  /**
   * Initialize the file menu.
   * 
   * @return a Menu containing all the entries for the file menu.
   */
  private Menu setupFileMenu() {
    Menu fileMenu = new Menu(this.resourceBundle.getString("file"));
    fileMenu.add(this.setupNewMenuItem());
    fileMenu.add(this.setupOpenMenuItem());
    fileMenu.add(this.setupSaveMenuItem());
    this.setupChangePasswordMenuItem();
    fileMenu.add(this.changeFilePasswordItem);
    return fileMenu;
  }

  /**
   * Initialize the 'new' menu item.
   *
   * @return a MenuItem for the 'new' action.
   */
  private MenuItem setupNewMenuItem() {
    MenuItem newItem = new MenuItem(
        this.resourceBundle.getString("new"),
        new MenuShortcut(KeyEvent.VK_N));
    newItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        newWorkspace();
      }

    });
    return newItem;
  }

  /**
   * Initialize the 'open' menu item.
   *
   * @return a MenuItem for the 'open' action.
   */
  private MenuItem setupOpenMenuItem() {
    MenuItem openItem = new MenuItem(
        this.resourceBundle.getString("open"),
        new MenuShortcut(KeyEvent.VK_O));
    openItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        openFile();
      }

    });
    return openItem;
  }

  /**
   * Initialize the 'save' menu item.
   *
   * @return a MenuItem for the 'save' action.
   */
  private MenuItem setupSaveMenuItem() {
    MenuItem saveItem = new MenuItem(
        this.resourceBundle.getString("save"),
        new MenuShortcut(KeyEvent.VK_S));
    saveItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveFile();
      }

    });
    return saveItem;
  }

  /**
   * Initialize the 'change password' menu item.  This method returns void since
   * changeFilePasswordItem is a class attribute.
   */
  private void setupChangePasswordMenuItem() {
    this.changeFilePasswordItem = new MenuItem("Change File Password");
    this.changeFilePasswordItem.setEnabled(false);
    this.changeFilePasswordItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        changeFilePassword();
      }

    });
  }

  /**
   * Initialize the password menu.
   *
   * @return a Menu containing all the entries for the password menu.
   */
  private Menu setupPasswordMenu() {
    Menu passwordMenu = new Menu(this.resourceBundle.getString("password"));
    passwordMenu.add(this.setupGeneratePasswordMenuItem());
    return passwordMenu;
  }

  /**
   * Initialize the 'generate password' menu item.
   *
   * @return a MenuItem for the 'generate password' action.
   */
  private MenuItem setupGeneratePasswordMenuItem() {
    MenuItem generatePasswordItem = new MenuItem(this.resourceBundle.getString("generatePassword"));
    generatePasswordItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        generatePassword();
      }

    });
    return generatePasswordItem;
  }

  /**
   * Initialize the encryption menu.
   *
   * @return a Menu containing all the entries for the encryption menu.
   */
  private Menu setupEncryptionMenu() {
    Menu encryptionMenu = new Menu(this.resourceBundle.getString("encryption"));
    encryptionMenu.add(this.setupEncryptFileMenuItem());
    encryptionMenu.add(this.setupDecryptFileMenuItem());
    return encryptionMenu;
  }

  /**
   * Initialize the 'encrypt file' menu item.
   *
   * @return a MenuItem for the 'encrypt file' action.
   */
  private MenuItem setupEncryptFileMenuItem() {
    MenuItem encryptFileItem = new MenuItem(this.resourceBundle.getString("encryptFile"));
    encryptFileItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        encryptFile();
      }

    });
    return encryptFileItem;
  }

  /**
   * Initialize the 'decrypt file' menu item.
   *
   * @return a MenuItem for the 'decrypt file' action.
   */
  private MenuItem setupDecryptFileMenuItem() {
    MenuItem decryptFileItem = new MenuItem(this.resourceBundle.getString("decryptFile"));
    decryptFileItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        decryptFile();
      }

    });
    return decryptFileItem;
  }

  /**
   * Initialize the language menu.
   *
   * @return a Menu containing all the entries for the language menu.
   */
  private Menu setupLanguageMenu() {
    Menu languageMenu = new Menu(this.resourceBundle.getString("language"));
    for (Entry<String, Locale> entry: MainWindow.supportedLanguages.entrySet()) {
      MenuItem menuItem = new MenuItem(entry.getKey());
      menuItem.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          setLanguage(entry.getValue());
        }

      });
      languageMenu.add(menuItem);
    }
    return languageMenu;
  }

  /**
   * Set the language for the UI.  This method will redraw the entire main window.
   *
   * @param locale is the locale to use when loading the resource bundle with localized strings.
   */
  private void setLanguage(Locale locale) {
    try {
      this.resourceBundle = ResourceBundle.getBundle("gui.StringsBundle", locale);
    } catch (MissingResourceException e) {
      JOptionPane.showMessageDialog(
          this.mainFrame,
          this.resourceBundle.getString("loadLanguageError"),
          this.resourceBundle.getString("loadLanguageErrorTitle"),
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.mainFrame.dispose();
    this.setupMainFrame();
  }

  /**
   * Create a new workspace.  Check if the user wants to save before creating a new list of password
   * records and deleting the old one.
   */
  public void newWorkspace() {
    this.promptSaveChanges();
    this.file = null;
    this.recordMap = new HashMap<String, Map<String, String>>();
    this.listPanel.updateAccountList();
  }

  /**
   * Open a password file and load the data into the current workspace.  The behavior should be as
   * follows: 1) check if the current workspace has been modified and needs to be saved first;  2)
   * present a file dialog to select the file to open;  3) present a password prompt to fetch the
   * passwords to decrypt the file; 4) open and decrypt the file; 5) redraw the main window to
   * reflect the contents of the file that was just opened.  Error messages should be displayed to
   * the user as dialog boxes and should be intelligible to the lay user.
   */
  public void openFile() {
    this.promptSaveChanges();
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    if (fileChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    this.file = fileChooser.getSelectedFile();
    char[][] passwords = new PasswordEntryWindow(this.resourceBundle.getString("openFile"), 2)
        .getPasswords();
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.file);
    } catch (IOException e) {
      this.displayOpenFileError();
      return;
    }
    try {
      this.recordMap = encryptedBuffer.decrypt(passwords);
    } catch (DecryptionException | ClassNotFoundException e) {
      this.displayPasswordIncorrectError();
      return;
    }
    this.listPanel.updateAccountList();
    this.changeFilePasswordItem.setEnabled(true);
  }

  /**
   * Save the currently loaded workspace to a file.  The behavior should be as follows: 1) If no
   * file is currently loaded, find out what the file name should be; 2) get the passwords required
   * to save; 3) encrypt the current workspace; 4) write the resulting encrypted buffer to a file.
   * Error messages should be displayed to the user as dialog boxes and should be intelligible to
   * the lay user.
   */
  public void saveFile() {
    if (this.file == null) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setMultiSelectionEnabled(false);
      if (fileChooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
        return;
      }
      this.file = fileChooser.getSelectedFile();
    }
    char[][] passwords = new PasswordEntryWindow(this.resourceBundle.getString("saveFile"), 2)
        .getPasswords();
    try {
      if (this.encryptedBuffer != null) {
          if (!this.encryptedBuffer.validatePassword(passwords)) {
            throw new DecryptionException(
                "Incorrect Password",
                DecryptionExceptionCode.INCORRECT_PASSWORD);
          }
          this.encryptedBuffer.updateContents(this.recordMap, passwords);
      } else {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(
            this.recordMap,
            passwords);
      } 
    } catch (EncryptionException e) {
      this.displayEncryptionError();
      return;
    } catch (DecryptionException e) {
      this.displayPasswordIncorrectError();
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      this.displaySaveFileError();
      return;
    }
    this.modified = false;
  }

  /**
   * Change the passwords protecting the current password file.  The behavior should be as follows:
   * 1) verify that a file is loaded; 2) save the workspace if changes have been made since the
   * last save; 3) get the old passwords and verify them; 4) get the new passwords and use them to
   * write to the file.  Error messages should be displayed to the user as dialog boxes and should
   * be intelligible to the lay user.
   */
  public void changeFilePassword() {
    if (this.encryptedBuffer == null) {
      JOptionPane.showMessageDialog(
          this.mainFrame,
          this.resourceBundle.getString("changePasswordError"),
          this.resourceBundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (this.modified) {
      Object[] options = {
          this.resourceBundle.getString("save"),
          this.resourceBundle.getString("discard")};
      int returnCode = JOptionPane.showOptionDialog(
          this.mainFrame,
          this.resourceBundle.getString("unsavedChangesNewPassword"),
          this.resourceBundle.getString("unsavedChangesTitle"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);
      if (returnCode == JOptionPane.NO_OPTION) {
        return;
      }
    }
    try {
      char[][] oldPasswords = new PasswordEntryWindow(
          this.resourceBundle.getString("changePasswordOldPassword"), 2).getPasswords();
      if (!this.encryptedBuffer.validatePassword(oldPasswords)) {
        throw new DecryptionException(
            "Incorrect Password",
            DecryptionExceptionCode.INCORRECT_PASSWORD);
      }
      char[][] newPasswords = new PasswordEntryWindow(
          this.resourceBundle.getString("changePasswordNewPassword"), 2).getPasswords();
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(
          this.recordMap,
          newPasswords);
    } catch (EncryptionException e) {
      this.displayEncryptionError();
      return;
    } catch (DecryptionException e) {
      this.displayPasswordIncorrectError();
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      this.displaySaveFileError();
      return;
    }
    this.modified = false;
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.resourceBundle.getString("changePasswordSuccess"),
        this.resourceBundle.getString("success"),
        JOptionPane.INFORMATION_MESSAGE);
    return;
  }

  /**
   * Check if the workspace has been modified since the last save and prompt the user if they want
   * to save the changes.
   */
  private void promptSaveChanges() {
    if (this.modified) {
      String[] options = {
          this.resourceBundle.getString("save"),
          this.resourceBundle.getString("discard")};
      int returnCode = JOptionPane.showOptionDialog(
          this.mainFrame,
          this.resourceBundle.getString("unsavedChanges"),
          this.resourceBundle.getString("unsavedChangesTitle"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);
      if (returnCode == JOptionPane.YES_OPTION) {
        this.saveFile();
      }
    }
  }

  /**
   * Open a window that can help the user generate a pseudorandom password.
   */
  private void generatePassword() {
    new GeneratePasswordWindow(this);
  }

  /**
   * Encrypt a file (not a password file) with a single password.  The behavior should be as
   * follows: 1) prompt the user to select the file they want to encrypt; 2) gather the password to
   * use for encryption; 3) prompt the user to select the destination file; 4) encrypt the contents
   * of the input file and write it to the output file.  Any error messages should be understandable
   * to the lay user.
   */
  private void encryptFile() {
    JFileChooser inputChooser = new JFileChooser();
    inputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    inputChooser.setMultiSelectionEnabled(false);
    if (inputChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    char[][] passwords = new PasswordEntryWindow(
        this.resourceBundle.getString("encryptGetPassword"), 1).getPasswords();
    EncryptedBuffer<byte[]> buffer;
    try {
      byte[] inputBytes = Files.readAllBytes(inputChooser.getSelectedFile().toPath());
      buffer = new EncryptedBuffer<byte[]>(inputBytes, passwords);
    } catch (IOException e) {
      this.displayOpenFileError();
      return;
    } catch (EncryptionException e) {
      this.displayEncryptionError();
      return;
    }
    JFileChooser outputChooser = new JFileChooser();
    outputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    outputChooser.setMultiSelectionEnabled(false);
    if (outputChooser.showSaveDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    try {
      buffer.writeToFile(outputChooser.getSelectedFile());
    } catch (IOException e) {
      this.displaySaveFileError();
      return;
    }

  }

  /**
   * Decrypt a file (no a password file) with a single password.  The behavior should be as follows:
   * 1) prompt the user for the encrypted file they want to decrypt; 2) prompt the user for the
   * password to decrypt that file; 3) prompt the user for the destination path for the decrypted
   * data; 4) decrypt the input file with the provided password and write the output to the output
   * file.  Any error messages should be understandable to the lay user.
   */
  private void decryptFile() {
    JFileChooser inputChooser = new JFileChooser();
    inputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    inputChooser.setMultiSelectionEnabled(false);
    if (inputChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    char[][] passwords = new PasswordEntryWindow(
        this.resourceBundle.getString("decryptGetPassword"), 1).getPasswords();
    EncryptedBuffer<byte[]> buffer;
    try {
      buffer = new EncryptedBuffer<byte[]>(inputChooser.getSelectedFile());
    } catch (IOException e) {
      this.displayOpenFileError();
      return;
    }
    JFileChooser outputChooser = new JFileChooser();
    outputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    outputChooser.setMultiSelectionEnabled(false);
    if (outputChooser.showSaveDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    try {
      Files.write(outputChooser.getSelectedFile().toPath(), buffer.decrypt(passwords));
    } catch (DecryptionException | ClassNotFoundException e) {
      this.displayPasswordIncorrectError();
      return;
    } catch (IOException e) {
      this.displaySaveFileError();
      return;
    }
  }

  /**
   * Display the error dialog for an error that occurs while saving a file.
   */
  void displaySaveFileError() {
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.resourceBundle.getString("saveFileError"),
        this.resourceBundle.getString("ioError"),
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Display the error dialog for when one or more of the user-entered passwords is incorrect.
   */
  void displayPasswordIncorrectError() {
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.resourceBundle.getString("passwordError"),
        this.resourceBundle.getString("passwordIncorrect"),
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Display the error dialog for an error that occurs while opening a file.
   */
  void displayOpenFileError() {
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.resourceBundle.getString("openFileError"),
        this.resourceBundle.getString("ioError"),
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Display the error dialog for an error that occurs during encryption of data.
   */
  void displayEncryptionError() {
    JOptionPane.showMessageDialog(
        this.mainFrame,
        this.resourceBundle.getString("encryptionErrorText"),
        this.resourceBundle.getString("encryptionErrorTitle"),
        JOptionPane.ERROR_MESSAGE);
  }

}