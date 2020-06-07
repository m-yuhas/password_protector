package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
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

  /**
   * The MainWindow that created this AccountWindow.
   */
  private MainWindow parentWindow;

  /**
   * GrowableTable do display the account attributes and values.
   */
  private GrowableTable table;

  /**
   * JFrame that displays all the content of the AccountWindow.
   */
  private JFrame mainFrame;

  /**
   * Text entry field that contains this record's name.
   */
  private JTextField nameField;

  /**
   * Map of attributes for this record.  Keys are an attribute (e.g. 'password') and values are the
   * corresponding value (e.g. '12345').
   */
  private Map<String, String> attributes;

  /**
   * The original name of the record, provided when the AccountWindow is created.
   */
  private String originalName;

  /**
   * Boolean value: true if this AccountWindow is displaying a record that already exists in
   * its MainWindow's record map, or false if this is meant to be a new record with no entries.
   */
  private boolean existingRecord = false;

  /**
   * Boolean value: if true this AccountWindow is editable, if false it is not editable.
   */
  private boolean isEditable;

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
    this.attributes = attributes;
    this.isEditable = isEditable;
    this.setupMainFrame();
  }

  /**
   * Setup the main JFrame where all the contents of this window will be drawn.
   */
  private void setupMainFrame() {
    this.mainFrame = new JFrame();
    this.mainFrame.add(setupMainPanel());
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

  /**
   * Setup the main Panel in the main JFrame.
   * 
   * @return JPanel containing all the elements that should be drawn.
   */
  private JPanel setupMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    JLabel nameLabel = new JLabel(this.parentWindow.resourceBundle.getString("accountWindowLabel"));
    mainPanel.add(nameLabel);
    this.nameField = new JTextField();
    this.nameField.setText(this.originalName);
    this.nameField.setEditable(this.isEditable);
    mainPanel.add(this.nameField);
    this.table = new GrowableTable(this.setupTableModel(this.attributes, this.isEditable));
    mainPanel.add(new JScrollPane(this.table));
    if (this.isEditable) {
      mainPanel.add(this.setupSaveButton());
    }
    mainPanel.add(this.setupQuitButton());
    return mainPanel;
  }

  /**
   * Setup the save button and associated action listener.
   * 
   * @return JButton for the save button.
   */
  private JButton setupSaveButton() {
    JButton saveButton = new JButton(this.parentWindow.resourceBundle.getString("save"));
    saveButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        saveRecord();

      }
    });
    return saveButton;
  }

  /**
   * Setup the quit button and associated action listener.
   * 
   * @return JButton for the quit button.
   */
  private JButton setupQuitButton() {
    JButton quitButton;
    quitButton = new JButton(this.parentWindow.resourceBundle.getString("quit"));
    quitButton.addActionListener(new ActionListener() {
 
      public void actionPerformed(ActionEvent e) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
      }

    });
    return quitButton;
  }

  /**
   * Setup the default table model used to populate the growable table.
   * 
   * @param attributes is a map of attributes and their corresponding values to be displayed in a
   *        tabular format.
   * @param isEditable is boolean value where true means this table is editable and false means it
   *        is not.
   * @return DefaultTableModel configured to display two rows and certain number of columns with the
   *         ability to edit cells or not.
   */
  DefaultTableModel setupTableModel(Map<String, String> attributes, boolean isEditable) {
    DefaultTableModel tableModel = new DefaultTableModel() {

      private static final long serialVersionUID = 1L;

      public boolean isCellEditable(int row, int column) {
        return isEditable;
      }

    };
    tableModel.addColumn(this.parentWindow.resourceBundle.getString("attribute"));
    tableModel.addColumn(this.parentWindow.resourceBundle.getString("value"));
    if (attributes.size() == 0) {
      tableModel.addRow(new String[] {"", ""});
    } else {
      for (Entry<String, String> entry: attributes.entrySet()) {
        tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
      }
    }
    return tableModel;
  }

  /**
   * Add the entered map of attributes and their values into the parent window's recordMap.
   */
  private void saveRecord() {
    String recordName = this.nameField.getText().strip();
    if (!this.validateName(recordName)) {
      return;
    }
    try {
      this.insertRecordIntoParent(this.generateMapping(), recordName);
    } catch (AssertionError e) {
      return;
    }
    this.mainFrame.dispatchEvent(new WindowEvent(this.mainFrame, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * Validate the current record name and make sure: 1) that it is not whitespace; and 2) that it
   * does not already exist in the parent window's record map.  If the name is invalid, the
   * appropriate warning messages should be displayed to the user here.
   *
   * @param name is a String of the name to validate
   * @return boolean true if the name is valid, false if it is not.
   */
  private boolean validateName(String name) {
    if (name.length() == 0) {
      JOptionPane.showMessageDialog(
          this.mainFrame,
          this.parentWindow.resourceBundle.getString("accountWindowError1"),
          this.parentWindow.resourceBundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    } 
    if (this.parentWindow.recordMap.containsKey(name) && !this.originalName.equals(name)) {
      JOptionPane.showMessageDialog(
          this.mainFrame,
          this.parentWindow.resourceBundle.getString("accountWindowError0"),
          this.parentWindow.resourceBundle.getString("error"),
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  /**
   * Iterate through this window's table and return an map of attributes to their values.  Blank
   * attributes should be ignored, as well as duplicates.  Any appropriate error messages should be
   * displayed here.
   *
   * @return Map<String, String> where the keys correspond to attributes (column 0 in this window's
   *         table) and the values correspond to the table values (column 1 in this window's table).
   * @throws AssertionError if: 1) a value exists without a corresponding attribute; or 2) there are
   *         duplicate attributes in this window's table. 
   */
  private Map<String, String> generateMapping() throws AssertionError {
    Map<String, String> record = new HashMap<String, String>();
    for (int i = 0; i < table.getRowCount(); i++) {
      String attribute = table.getModel().getValueAt(i, 0).toString().trim();
      String value = table.getModel().getValueAt(i, 1).toString().trim();
      if (value.length() != 0 && attribute.length() == 0) {
        JOptionPane.showMessageDialog(
            this.mainFrame,
            this.parentWindow.resourceBundle.getString("accountWindowError2"),
            this.parentWindow.resourceBundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
        throw new AssertionError("No attribute for corresponding value.");
      }
      if (record.containsKey(attribute)) {
        JOptionPane.showMessageDialog(
            this.mainFrame,
            this.parentWindow.resourceBundle.getString("accountWindowError3_1") + attribute + 
            this.parentWindow.resourceBundle.getString("accountWindowError3_2"),
            this.parentWindow.resourceBundle.getString("error"),
            JOptionPane.ERROR_MESSAGE);
        throw new AssertionError("Record contains duplicate keys.");
      }
      if (attribute.length() != 0) {
        record.put(attribute, value);
      }
    }
    return record;    
  }

  /**
   * Insert a map of strings into the parent window's record map with recordName as the key.
   *
   * @param record is a Map with Strings as keys and values corresponding to entries in this
   *        window's table.
   * @param recordName is the String to use as the key when inserting this record into the parent
   *        window's record map.
   */
  private void insertRecordIntoParent(Map<String, String> record, String recordName) {
    this.parentWindow.recordMap.put(recordName, record);
    if (!this.originalName.equals(recordName) && this.existingRecord) {
      try {
        this.parentWindow.recordMap.remove(this.originalName);
      } catch (NullPointerException e) {}
    }
    this.parentWindow.listPanel.updateAccountList();
    this.parentWindow.modified = true;
  }

}
