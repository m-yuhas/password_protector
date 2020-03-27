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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class AccountWindow {
  
  private MainWindow parentWindow;
  private JFrame mainFrame;
  private JPanel mainPanel;
  //private JTable table;
  private JButton saveButton;
  private JButton quitButton;
  private JLabel nameLabel;
  private GrowableTable table;
  private JTextField nameField;
  private boolean existingRecord = false;
  private String originalName;

  public AccountWindow(MainWindow parentWindow, Map<String, String> attributes, String name, boolean isEditable) {
    if (name.length() != 0) {
      this.existingRecord = true;
    }
    this.originalName = name;
    this.parentWindow = parentWindow;
    this.mainFrame = new JFrame();
    this.mainPanel = new JPanel();
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
    //this.table = new JTable(m);
    this.table = new GrowableTable(m);
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
  
  class GrowableTable extends JTable {
    
    public GrowableTable(DefaultTableModel m) {
      super(m);
    }
    
    @Override
    public DefaultTableModel getModel() {
      return (DefaultTableModel) super.getModel();
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
      int row = getEditingRow();
      int col = getEditingColumn();
      super.editingStopped(e);
      if (row == getRowCount() - 1 && col == getColumnCount() - 1)
      {
        getModel().addRow(new String[] {"", ""});
      }
    }
  }

}
