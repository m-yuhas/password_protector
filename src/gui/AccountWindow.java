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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import passwordio.AccountRecord;

public class AccountWindow {
  
  private MainWindow parentWindow;
  private JFrame mainFrame;
  private JPanel mainPanel;
  private JTable table;
  private JButton saveButton;
  private JButton quitButton;
  
  public AccountWindow(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.mainFrame = new JFrame();
    this.mainPanel = new JPanel();
    DefaultTableModel m = new DefaultTableModel();
    m.addColumn("Key");
    m.addColumn("Value");
    for (int i = 0; i < 12; i++) {
      m.addRow(new String[] {"", ""});
    }
    this.table = new JTable(m);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.mainPanel.add(new JScrollPane(this.table));
    this.saveButton = new JButton("Save");
    this.quitButton = new JButton("Quit");
    
    this.saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Map<String, String> recordMap = new HashMap<String, String>();
        for (int i = 0; i < table.getColumnCount(); i++) {
          if (table.getModel().getValueAt(i, 0).toString().trim().length() != 0 && table.getModel().getValueAt(i, 1).toString().trim().length() != 0) {
            recordMap.put(table.getModel().getValueAt(i, 0).toString().trim(), table.getModel().getValueAt(i, 1).toString().trim());
          }
        }
        parentWindow.addRecordCallback("Test", recordMap);
      }
    });
    
    this.quitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
      }
    });
    
    this.mainPanel.add(this.saveButton);
    this.mainPanel.add(this.quitButton);
    this.mainFrame.add(this.mainPanel);
  }

  public AccountWindow(MainWindow parentWindow, Map<String, String> accounts, boolean isEditable) {
    this.parentWindow = parentWindow;
    this.mainFrame = new JFrame();
    this.mainPanel = new JPanel();
    DefaultTableModel m = new DefaultTableModel() {
      public boolean isCellEditable(int row, int column) {
        return isEditable;
      }
    };
    m.addColumn("Key");
    m.addColumn("Value");
    Iterator<Entry<String, String>> iterator = accounts.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, String> entry = iterator.next();
      Vector<String> v = new Vector<String>();
      v.add(entry.getKey());
      v.add(entry.getValue());
      m.addRow(v);
    };
    this.table = new JTable(m);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.saveButton = new JButton("Save");
    this.quitButton = new JButton("Quit");
    this.mainPanel.add(this.saveButton);
    this.mainPanel.add(this.quitButton);
    this.mainPanel.add(new JScrollPane(this.table)); 
  }
  
  public void show() {
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }
  
  public void saveRecord() {
    
  }

}
