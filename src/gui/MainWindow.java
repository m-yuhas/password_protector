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
        if (recordMap.size() != 0) {
          Object[] options = {"Save", "Discard"};
          int returnCode = JOptionPane.showOptionDialog(
              mainFrame,
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
    passwordMenu.add(generatePasswordItem);
    Menu encryptionMenu = new Menu("Encryption");
    MenuItem encryptFileItem = new MenuItem("Encrypt File");
    MenuItem decryptFileItem = new MenuItem("Decrypt File");
    encryptionMenu.add(encryptFileItem);
    encryptionMenu.add(decryptFileItem);
    menuBar.add(fileMenu);
    menuBar.add(passwordMenu);
    menuBar.add(encryptionMenu);
    this.mainFrame.setMenuBar(menuBar);
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.pack();
  }
  
  public void show() {
    this.mainFrame.setVisible(true);
  }
  
  public void newWorkspace() {
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
    char[][] passwords = new PasswordEntryWindow().getPasswords();
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.file);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      this.recordMap = encryptedBuffer.decrypt(passwords[0], passwords[1]);
    } catch (DecryptionException e) {
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
    char[][] passwords = new PasswordEntryWindow().getPasswords();
    if (this.encryptedBuffer != null) {
      if (!this.encryptedBuffer.validatePassword(passwords)) {
        JOptionPane.showMessageDialog(this.mainFrame, "Password incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      try {
        this.encryptedBuffer.updateContents(this.recordMap, passwords);
      } catch (EncryptionException e) {
        //TODO: this
        e.printStackTrace();
      }
    } else {
      try {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, Map<String, String>>>(this.recordMap, passwords);
      } catch (EncryptionException e) {
        //TODO : this
        e.printStackTrace();
      }
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      //TODO: this
      e.printStackTrace();
    }
    this.modified = false;
  }
  
  public void changeFilePassword() {
    PasswordEntryWindow pew = new PasswordEntryWindow();
    /*
    char[][] passwords = pew.getPasswords();
    for (char[] p: passwords) {
      System.out.println(p);
    }*/
  }

}
