package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 8932199718088447354L;
  private MainWindow parentWindow;

  public ControlPanel(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    JButton viewButton = new JButton("View");
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton modifyButton = new JButton("Modify");
    viewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        view();
      }
    });
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        add();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        delete();
      }
    });
    modifyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        modify();
      }
    });
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(viewButton);
    this.add(addButton);
    this.add(deleteButton);
    this.add(modifyButton);
  }
  
  private void view() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      String recordName = this.parentWindow.listPanel.accountList.getSelectedValue();
      new AccountWindow(this.parentWindow, this.parentWindow.recordMap.get(recordName), recordName, false);
    }
  }
  
  private void add() {
    new AccountWindow(this.parentWindow, new HashMap<String, String>(), "", true);
  }
  
  private void delete() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      char[][] passwords = new PasswordEntryWindow("Enter the file passwords to confirm deletion.", 2).getPasswords();
    }
  }
  
  private void modify() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      String recordName = this.parentWindow.listPanel.accountList.getSelectedValue();
      new AccountWindow(this.parentWindow, this.parentWindow.recordMap.get(recordName), recordName, true);
    }
  }

}
