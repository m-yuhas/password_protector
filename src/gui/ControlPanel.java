package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    if (this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      return;
    }
    PasswordEntryWindow passwordEntryWindow = new PasswordEntryWindow(parentWindow, this.parentWindow::viewRecordCallback);
    passwordEntryWindow.show();
  }
  
  private void add() {
    AccountWindow accountWindow = new AccountWindow(this.parentWindow);
    accountWindow.show();
  }
  
  private void delete() {
    PasswordEntryWindow passwordEntryWindow = new PasswordEntryWindow(parentWindow, this.parentWindow::deleteRecordCallback);
    passwordEntryWindow.show();
  }
  
  private void modify() {
    PasswordEntryWindow passwordEntryWindow = new PasswordEntryWindow(parentWindow, this.parentWindow::modifyRecordCallback);
  }

}
