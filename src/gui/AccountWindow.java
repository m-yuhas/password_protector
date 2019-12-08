package gui;

import java.awt.ScrollPane;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class AccountWindow {
  
  private JFrame mainFrame;
  private JPanel mainPanel;
  private JTable table;
  
  public AccountWindow() {
    this.mainFrame = new JFrame();
    this.mainPanel = new JPanel();
    //DefaultTableModel m = new DefaultTableModel();
    DefaultTableModel m = new DefaultTableModel()
    {
      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      public boolean isCellEditable(int row, int column)
      {
        return false;//This causes all cells to be not editable
      }
    };
    m.addColumn("Key");
    m.addColumn("Value");
    for (int i = 0; i < 12; i++) {
      m.addRow(new String[] {"", ""});
    }
    this.table = new JTable(m);
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
    this.mainPanel.add(new JScrollPane(this.table));
    this.mainFrame.add(this.mainPanel);
  }

  public AccountWindow(Map<String, passwordio.AccountRecord> accounts, boolean isEditable) {
    
  }
  
  public void show() {
    this.mainFrame.pack();
    this.mainFrame.setVisible(true);
  }

}
