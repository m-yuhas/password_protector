package gui;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

public class MainWindow {
  
  private ControlPanel controlPanel;
  public boolean modified = false;
  private File file;
  private JFrame mainFrame;
  public ListPanel listPanel;
  public Map<String, Map<String, String>> recordMap;
  private MenuItem changeFilePasswordItem;
  private EncryptedBuffer<Map<String, Map<String, String>>> encryptedBuffer;

  public MainWindow() {
    this.recordMap = new HashMap<String, Map<String,String>>();
    if (System.getProperty("os.name").contentEquals("Mac OS X")) {
      System.setProperty("apple.laf.userScreenMenuBar", "true");
    }
    this.mainFrame = new JFrame("Password Protector");
    this.mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        quit();
      }
    });
    JPanel mainPanel = new JPanel();
    this.listPanel = new ListPanel(this);
    mainPanel.add(this.listPanel);
    this.controlPanel = new ControlPanel(this);
    mainPanel.add(this.controlPanel);
    this.mainFrame.add(mainPanel);
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem newItem = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
    MenuItem openItem = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
    MenuItem saveItem = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
    this.changeFilePasswordItem = new MenuItem("Change File Password");
    newItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        newWorkspace();
      }
    });
    openItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openFile();
      }
    });
    saveItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveFile();
      }
    });
    this.changeFilePasswordItem.setEnabled(false);
    this.changeFilePasswordItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changeFilePassword();
      }
    });
    fileMenu.add(newItem);
    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.add(changeFilePasswordItem);
    Menu passwordMenu = new Menu("Password");
    MenuItem generatePasswordItem = new MenuItem("Generate Password");
    generatePasswordItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        generatePassword();
      }
    });
    passwordMenu.add(generatePasswordItem);
    Menu encryptionMenu = new Menu("Encryption");
    MenuItem encryptFileItem = new MenuItem("Encrypt File");
    encryptFileItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        encryptFile();
      }
    });
    MenuItem decryptFileItem = new MenuItem("Decrypt File");
    decryptFileItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        decryptFile();
      }
    });
    encryptionMenu.add(encryptFileItem);
    encryptionMenu.add(decryptFileItem);
    menuBar.add(fileMenu);
    menuBar.add(passwordMenu);
    menuBar.add(encryptionMenu);
    this.mainFrame.setMenuBar(menuBar);
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
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
      if (!this.encryptedBuffer.validatePassword(passwords)) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "One or more of the passwords was incorrect.",
            "Incorrect Password",
            JOptionPane.ERROR_MESSAGE);
        return;
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
    if (!this.encryptedBuffer.validatePassword(oldPasswords[0], oldPasswords[1])) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "One or more of the passwords was incorrect.",
          "Incorrect Password",
          JOptionPane.ERROR_MESSAGE);
      return;
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
    } catch (IOException e1) {
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
      Byte[] outputBytesObject = (Byte[]) buffer.decrypt(passwords);
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
    }
  }
}
