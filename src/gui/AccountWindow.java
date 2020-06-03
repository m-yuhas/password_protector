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
    tableModel.addColumn("Attribute");
    tableModel.addColumn("Value");
    if (attributes.size() == 0) {
      tableModel.addRow(new String[] {"", ""});
    } else {
      for (Entry<String, String> entry: attributes.entrySet()) {
        tableModel.addRow(new String[] {entry.getKey(), entry.getValue()});
      }
    }
    return tableModel;
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

}
