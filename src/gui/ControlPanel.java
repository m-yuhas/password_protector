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

  public ControlPanel() {
    JButton viewButton = new JButton("View");
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JButton modifyButton = new JButton("Modify");
    viewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AccountWindow accountWindow = new AccountWindow(accounts, false);
        accountWindow.show();
      }
    });
    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AccountWindow accountWindow = new AccountWindow();
      }
    });
    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PasswordEntryWindow passwordEntryWindow = new PasswordEntryWindow(context, callback);
        passwordEntryWindow.show();
      }
    });
    modifyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AccountWindow accountWindow = new AccountWindow(accounts, true);
        accountWindow.show();
      }
    });
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(viewButton);
    this.add(addButton);
    this.add(deleteButton);
    this.add(modifyButton);
  }

}
