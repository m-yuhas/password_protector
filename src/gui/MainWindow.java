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
  
  private void setupMainFrame() {
    this.mainFrame = new JFrame("Password Protector");
    this.mainFrame.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent windowEvent) {
        quit();
      }

    });
    this.mainFrame.add(this.setupMainPanel());
    this.mainFrame.setMenuBar(this.setupMenuBar());
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }
  
  private JPanel setupMainPanel() {
    this.controlPanel = new ControlPanel(this);
    this.listPanel = new ListPanel(this);
    JPanel mainPanel = new JPanel();
    mainPanel.add(this.listPanel);
    mainPanel.add(this.controlPanel);
    return mainPanel;
  }

  private MenuBar setupMenuBar() {
    MenuBar menuBar = new MenuBar();
    menuBar.add(this.setupFileMenu());
    menuBar.add(this.setupPasswordMenu());
    menuBar.add(this.setupEncryptionMenu());
    menuBar.add(this.setupLanguageMenu());
    return menuBar;
  }

  private Menu setupFileMenu() {
    Menu fileMenu = new Menu("File");
    fileMenu.add(this.setupNewMenuItem());
    fileMenu.add(this.setupOpenMenuItem());
    fileMenu.add(this.setupSaveMenuItem());
    this.setupChangePasswordMenuItem();
    fileMenu.add(this.changeFilePasswordItem);
    return fileMenu;
  }
  
  private MenuItem setupNewMenuItem() {
    MenuItem newItem = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
    newItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        newWorkspace();
      }

    });
    return newItem;
  }
  
  private MenuItem setupOpenMenuItem() {
    MenuItem openItem = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
    openItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        openFile();
      }

    });
    return openItem;
  }

  private MenuItem setupSaveMenuItem() {
    MenuItem saveItem = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
    saveItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveFile();
      }

    });
    return saveItem;
  }

  private void setupChangePasswordMenuItem() {
    this.changeFilePasswordItem = new MenuItem("Change File Password");
    this.changeFilePasswordItem.setEnabled(false);
    this.changeFilePasswordItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        changeFilePassword();
      }

    });
  }

  private Menu setupPasswordMenu() {
    Menu passwordMenu = new Menu("Password");
    passwordMenu.add(this.setupGeneratePasswordMenuItem());
    return passwordMenu;
  }

  private MenuItem setupGeneratePasswordMenuItem() {
    MenuItem generatePasswordItem = new MenuItem("Generate Password");
    generatePasswordItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        generatePassword();
      }

    });
    return generatePasswordItem;
  }

  private Menu setupEncryptionMenu() {
    Menu encryptionMenu = new Menu("Encryption");
    encryptionMenu.add(this.setupEncryptFileMenuItem());
    encryptionMenu.add(this.setupDecryptFileMenuItem());
    return encryptionMenu;
  }

  private MenuItem setupEncryptFileMenuItem() {
    MenuItem encryptFileItem = new MenuItem("Encrypt File");
    encryptFileItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        encryptFile();
      }

    });
    return encryptFileItem;
  }

  private MenuItem setupDecryptFileMenuItem() {
    MenuItem decryptFileItem = new MenuItem("Decrypt File");
    decryptFileItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        decryptFile();
      }

    });
    return decryptFileItem;
  }

  private Menu setupLanguageMenu() {
    Menu languageMenu = new Menu("Language");
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
  
  private void setLanguage(Locale locale) {
    try {
      this.resourceBundle = ResourceBundle.getBundle("gui.StringsBundle", locale);
    } catch (MissingResourceException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "Unable to load language resource file.",
          "MissingResourceException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.mainFrame.dispose();
    this.setupMainFrame();
  }

  public void newWorkspace() {
    if (this.recordMap.size() != 0) {
      Object[] options = {"Save", "Discard"};
      int returnCode = JOptionPane.showOptionDialog(
          this.mainFrame,
          "There are unsaved changes in the current workspace.  Would you like to save before creating a new one?",
          "Save or Discard Changes?",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);
      if (returnCode == JOptionPane.YES_OPTION) {
        saveFile();
      }
    }
    this.file = null;
    this.recordMap = new HashMap<String, Map<String, String>>();
    this.listPanel.updateAccountList();
  }

  public void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    if (fileChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    this.file = fileChooser.getSelectedFile();
    char[][] passwords = new PasswordEntryWindow("Enter the passwords needed to unlock this file:", 2).getPasswords();
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.file);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      this.recordMap = encryptedBuffer.decrypt(passwords[0], passwords[1]);
    } catch (DecryptionException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "One or more of the passwords was incorrect.",
          "Incorrect Password",
          JOptionPane.ERROR_MESSAGE);
      return;
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.listPanel.updateAccountList();
    this.changeFilePasswordItem.setEnabled(true);
  }
  
  public void saveFile() {
    if (this.file == null) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setMultiSelectionEnabled(false);
      int returnCode = fileChooser.showSaveDialog(mainFrame);
      if (returnCode != JFileChooser.APPROVE_OPTION) {
        return;
      }
      this.file = fileChooser.getSelectedFile();
    }
    char[][] passwords = new PasswordEntryWindow("Enter the passwords needed save this file:", 2).getPasswords();
    if (this.encryptedBuffer != null) {
      try {
        if (!this.encryptedBuffer.validatePassword(passwords)) {
          JOptionPane.showMessageDialog(this.mainFrame,
              "One or more of the passwords was incorrect.",
              "Incorrect Password",
              JOptionPane.ERROR_MESSAGE);
          return;
        }
      } catch (DecryptionException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      try {
        this.encryptedBuffer.updateContents(this.recordMap, passwords);
      } catch (EncryptionException e) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "EncryptionException occurred while encrypting the data.",
            "EncryptionException",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    } else {
      try {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.recordMap, passwords[0], passwords[1]);
      } catch (EncryptionException e) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "EncryptionException occurred while encrypting the data.",
            "EncryptionException",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.modified = false;
  }
  
  public void changeFilePassword() {
    if (this.encryptedBuffer == null) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "No passwords file is currently loaded.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (this.modified) {
    Object[] options = {"Save", "Discard"};
      int returnCode = JOptionPane.showOptionDialog(
          this.mainFrame,
          "There are unsaved changes in the current workspace.  These will be automatically saved and written to the disk.  Is it okay to procede?",
          "Save or Discard Changes?",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);
      if (returnCode == JOptionPane.NO_OPTION) {
        return;
      }
    }
    char[][] oldPasswords = new PasswordEntryWindow("Enter the current passwords for this file:", 2).getPasswords();
    try {
      if (!this.encryptedBuffer.validatePassword(oldPasswords[0], oldPasswords[1])) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "One or more of the passwords was incorrect.",
            "Incorrect Password",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
    } catch (DecryptionException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    char[][] newPasswords = new PasswordEntryWindow("Enter the new passwords for this file:", 2).getPasswords();
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.recordMap, newPasswords[0], newPasswords[1]);
    } catch (EncryptionException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "EncryptionException occurred while encrypting the data.",
          "EncryptionException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.modified = false;
    JOptionPane.showMessageDialog(this.mainFrame,
        "File passwords updated successfully.",
        "Success",
        JOptionPane.INFORMATION_MESSAGE);
    return;
  }
  
  private void quit() {
    if (modified) {
      if (JOptionPane.showConfirmDialog(mainFrame, 
          "There are unsaved changes in this workspace, do you want to save your changes?",
          "Unsaved Changes", 
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
        this.saveFile();
      }
      System.out.println("No Option");
    }
  }

  private void generatePassword() {
    new GeneratePasswordWindow();
  }
  
  private void encryptFile() {
    JFileChooser inputChooser = new JFileChooser();
    inputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    inputChooser.setMultiSelectionEnabled(false);
    if (inputChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    char[][] passwords = new PasswordEntryWindow("Enter the password you want to use to protect this file:", 1).getPasswords();
    EncryptedBuffer<Byte[]> buffer;
    try {
      byte[] inputBytes = Files.readAllBytes(inputChooser.getSelectedFile().toPath());
      Byte[] inputBytesObject = new Byte[inputBytes.length];
      int i = 0;
      for (byte b: inputBytes) {
        inputBytesObject[i++] = b;
      }
      buffer = new EncryptedBuffer<Byte[]>(inputBytesObject, passwords);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    } catch (EncryptionException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "EncryptionException occurred while attempting to encrypt the file contents.",
          "EncryptionException",
          JOptionPane.ERROR_MESSAGE);
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
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

  }
  
  private void decryptFile() {
    JFileChooser inputChooser = new JFileChooser();
    inputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    inputChooser.setMultiSelectionEnabled(false);
    if (inputChooser.showOpenDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    char[][] passwords = new PasswordEntryWindow("Enter the password to decrypt this file:", 1).getPasswords();
    EncryptedBuffer<Byte[]> buffer;
    try {
      buffer = new EncryptedBuffer<Byte[]>(inputChooser.getSelectedFile());
    } catch (IOException e1) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to open the file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    JFileChooser outputChooser = new JFileChooser();
    outputChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    outputChooser.setMultiSelectionEnabled(false);
    if (outputChooser.showSaveDialog(this.mainFrame) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    try {
      Byte[] outputBytesObject = buffer.decrypt(passwords);
      byte[] outputBytes = new byte[outputBytesObject.length];
      int i = 0;
      for (Byte b: outputBytesObject) {
        outputBytes[i++] = b.byteValue();
      }
      Files.write(outputChooser.getSelectedFile().toPath(), outputBytes);
    } catch (DecryptionException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "Incorrect Password.",
          "Incorrect Password",
          JOptionPane.ERROR_MESSAGE);
      return;
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "IOException occurred while attempting to write the new file.",
          "IOException",
          JOptionPane.ERROR_MESSAGE);
      return;
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
