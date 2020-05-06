package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Window containing a viewable list of attributes for an account.
 */
public class AccountWindow {
  
  private MainWindow parentWindow;
  private GrowableTable table;
  private JButton saveButton;
  private JButton quitButton;
  private JFrame mainFrame;
  private JLabel nameLabel;
  private JPanel mainPanel;
  private JTextField nameField;
  private String originalName;
  private boolean existingRecord = false;

  /**
   * Construct an instance of AccountWindow and display it.
   * 
   * @param parentWindow is a MainWindow object: the object that spawned this window.
   * @param attributes is a map of strings containing the attributes and values for this account.
   * @param name is a string representing the account name.  If the name is an empty string, this
   *        window represents an new, empty account.
   * @param isEditable is a boolean flag that determines if this account window is read only (only
   *        displays data) or is editable (user is able to add and modify attributes and their
   *        values).
   */
  public AccountWindow(
      MainWindow parentWindow,
      Map<String, String> attributes,
      String name,
      boolean isEditable) {
    if (name.length() != 0) {
      this.existingRecord = true;
    }
    this.originalName = name;
    this.parentWindow = parentWindow;
    this.mainFrame = new JFrame();
    this.mainPanel = new JPanel();
    this.table = new GrowableTable(this.setupTableModel(attributes, isEditable));
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.nameLabel = new JLabel("Account Name:");
    this.mainPanel.add(nameLabel);
    this.nameField = new JTextField();
    this.nameField.setText(name);
    this.nameField.setEditable(isEditable);
    this.mainPanel.add(nameField);
    this.mainPanel.add(new JScrollPane(this.table));
    if (isEditable) {
      this.saveButton = new JButton("Save");
      this.saveButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          saveRecord();
        }
      });
      this.mainPanel.add(this.saveButton);
    }
    this.quitButton = new JButton("Quit");
    this.quitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
      }
    });
    this.mainPanel.add(this.quitButton);
    this.mainFrame.add(this.mainPanel);
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }
  
  public void saveRecord() {
    String recordName = this.nameField.getText().strip();
    if (recordName.length() == 0) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "Please enter a name for this account.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (this.parentWindow.recordMap.containsKey(recordName) && !this.existingRecord) {
      JOptionPane.showMessageDialog(this.mainFrame,
          "An account with this name already exists.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    Map<String, String> record = new HashMap<String, String>();
    for (int i = 0; i < table.getRowCount(); i++) {
      String attribute = table.getModel().getValueAt(i, 0).toString().trim();
      String value = table.getModel().getValueAt(i, 1).toString().trim();
      if (value.length() != 0 && attribute.length() == 0) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "Please make sure every value has a corresponding attribute.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (record.containsKey(attribute)) {
        JOptionPane.showMessageDialog(this.mainFrame,
            "Duplicate attribute \"" + attribute + "\" detected.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (attribute.length() != 0) {
        record.put(attribute, value);
      }
    }
    this.parentWindow.recordMap.put(recordName, record);
    if (!this.originalName.equals(recordName) && this.existingRecord) {
      try {
        this.parentWindow.recordMap.remove(this.originalName);
      } catch (NullPointerException e) {}
    }
    this.parentWindow.listPanel.updateAccountList();
    this.parentWindow.modified = true;
    mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
  }

  DefaultTableModel setupTableModel(Map<String, String> attributes, boolean isEditable) {
    DefaultTableModel m = new DefaultTableModel() {
      public boolean isCellEditable(int row, int column) {
        return isEditable;
      }
    };
    m.addColumn("Attribute");
    m.addColumn("Value");
    if (attributes.size() == 0) {
      m.addRow(new String[] {"", ""});
    } else {
      Iterator<Entry<String, String>> iterator = attributes.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<String, String> entry = iterator.next();
        Vector<String> v = new Vector<String>();
        v.add(entry.getKey());
        v.add(entry.getValue());
        m.addRow(v);
      }
    }
    return m;
  }
}
