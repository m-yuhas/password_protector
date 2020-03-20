package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import passwordio.AccountRecord;
import passwordio.DecryptionException;
import passwordio.EncryptedBuffer;
import passwordio.EncryptionException;

public class MainWindow {
  
  private ControlPanel controlPanel;
  private boolean modified = false;
  private File file;
  private JFrame mainFrame;
  public ListPanel listPanel;
  public Map<String, String> recordMap;
  private MenuItem changeFilePasswordItem;
  private EncryptedBuffer<Map<String, String>> encryptedBuffer;

  public MainWindow() {
    this.recordMap = new HashMap<String, String>();
    if (System.getProperty("os.name").contentEquals("Mac OS X")) {
      System.setProperty("apple.laf.userScreenMenuBar", "true");
    }
    this.mainFrame = new JFrame("Password Protector");
    JPanel mainPanel = new JPanel();
    this.listPanel = new ListPanel();
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        int returnCode = fileChooser.showOpenDialog(mainFrame);
        if (returnCode == JFileChooser.APPROVE_OPTION) {
          file = fileChooser.getSelectedFile();
          openFile();
        }
      }
    });
    saveItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (file == null) {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          int returnCode = fileChooser.showOpenDialog(mainFrame);
          if (returnCode == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
          }
        }
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
    this.recordMap = new HashMap<String, String>();
    this.listPanel.updateAccountList(new String[0]);
  }

  public void openFile() {
    System.out.println("HERE");
    PasswordEntryWindow pew = new PasswordEntryWindow();
    char[][] passwords = pew.getPasswords();
    for (char[] p: passwords) {
      System.out.println(p);
    }
    System.out.println("DONE");
  }
  
  public Void openFileCallback(char[] password) {
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, String>>(this.file);
      this.recordMap = encryptedBuffer.decrypt(password);
      Iterator<Entry<String, String>> iterator = recordMap.entrySet().iterator();
      String[] accountList = new String[this.recordMap.size()];
      int i = 0;
      while (iterator.hasNext()) {
        accountList[i++] = iterator.next().getKey();
      }
      this.listPanel.updateAccountList(accountList);
      this.changeFilePasswordItem.setEnabled(true);
    } catch (DecryptionException e) {
      System.out.println("ERROR");
      System.out.println(e);
    } catch (IOException e) {
      System.out.println("IO ERROR");
    }
    return null;
  }
  
  public void saveFile() {
    PasswordEntryWindow pew = new PasswordEntryWindow();
    /*
    char[][] passwords = pew.getPassword();
    for (char[] p: passwords) {
      System.out.println(p);
    }*/
  }
  
  public Void saveFileCallback(char[] password) {
    if (this.encryptedBuffer != null) {
      if (!this.encryptedBuffer.validatePassword(password)) {
        JOptionPane.showMessageDialog(this.mainFrame, "Password incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
      }
      try {
        this.encryptedBuffer.updateContents(this.recordMap, password);
      } catch (EncryptionException e) {
        e.printStackTrace();
      }
    } else {
      try {
        this.encryptedBuffer = new EncryptedBuffer<Map<String, String>>(this.recordMap, password);
      } catch (EncryptionException e) {
        e.printStackTrace();
      }
    }
    try {
      this.encryptedBuffer.writeToFile(this.file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.modified = false;
    return null;
  }
  
  public void changeFilePassword() {
    PasswordEntryWindow pew = new PasswordEntryWindow();
    /*
    char[][] passwords = pew.getPasswords();
    for (char[] p: passwords) {
      System.out.println(p);
    }*/
  }
  
  public Void changeFilePasswordCallbackA(char[] password) {
    if (!this.encryptedBuffer.validatePassword(password)) {
      JOptionPane.showMessageDialog(this.mainFrame, "Password incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
      PasswordEntryWindow pew = new PasswordEntryWindow();
      /*
      char[][] passwords = pew.getPin();
      for (char[] p: passwords) {
        System.out.println(p);
      }*/
    }
    return null;
  }
  
  public Void changeFilePasswordCallbackB(char[] password) {
    try {
      this.encryptedBuffer = new EncryptedBuffer<Map<String, String>>(this.recordMap, password);
    } catch (EncryptionException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public Void viewRecordCallback(char[] password) {
    AccountWindow accountWindow = new AccountWindow(this, this.recordMap, false);
    accountWindow.show();
    return null;
  }
  
  public Void deleteRecordCallback(char[] password) {
    this.recordMap.get(this.listPanel.accountList.getSelectedValue());
    this.recordMap.remove(this.listPanel.accountList.getSelectedValue());
    Iterator<Entry<String, String>> iterator = recordMap.entrySet().iterator();
    String[] accountList = new String[this.recordMap.size()];
    int i = 0;
    while (iterator.hasNext()) {
      accountList[i++] = iterator.next().getKey();
    }
    this.listPanel.updateAccountList(accountList);
    return null;
  }
  
  public Void modifyRecordCallback(char[] password) {
    AccountWindow accountWindow = new AccountWindow(this, recordMap, true);
    accountWindow.show();
    return null;
  }
  
  public Void addRecordCallback(String recordName, Map<String, String> recordData) {
    return null;
    
  }
  
  public Void addRecordCallbackB(char[] password) {
    this.recordMap.put("Test", "Blah");
    //this.listPanel.updateAccountList(options);
    return null;
  }

}
