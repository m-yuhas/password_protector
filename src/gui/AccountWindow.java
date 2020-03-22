package gui;

import java.awt.ScrollPane;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class AccountWindow {
  
  private MainWindow parentWindow;
  private JFrame mainFrame;
  private JPanel mainPanel;
  private JTable table;
  private JButton saveButton;
  private JButton quitButton;
  private JLabel nameLabel;
  private JTextField nameField;

  public AccountWindow(MainWindow parentWindow, Map<String, String> attributes, String name, boolean isEditable) {
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
      for (int i = 0; i < 12; i++) {
        m.addRow(new String[] {"", ""});
      }
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
    this.table = new JTable(m);
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
    Map<String, String> record = new HashMap<String, String>();
    for (int i = 0; i < table.getColumnCount(); i++) {
      if (table.getModel().getValueAt(i, 0).toString().trim().length() != 0 && table.getModel().getValueAt(i, 1).toString().trim().length() != 0) {
        record.put(table.getModel().getValueAt(i, 0).toString().trim(), table.getModel().getValueAt(i, 1).toString().trim());
      }
    }
    this.parentWindow.recordMap.put(this.nameField.getText().strip(), record);
    this.parentWindow.listPanel.updateAccountList();
    this.parentWindow.modified = true;
    mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
  }

}
