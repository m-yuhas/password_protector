package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import passwordio.DecryptionException;
import passwordio.DecryptionExceptionCode;

/**
 * ControlPanel extends JPanel and includes all the controls for actions on specific accounts.
 */
public class ControlPanel extends JPanel {

  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = 8932199718088447354L;

  /**
   * The parent window for which this panel is created.
   */
  private MainWindow parentWindow;

  /**
   * Construct an instance of ControlPanel.
   *
   * @param parentWindow is the MainFrame where this ControlPanel will be displayed.
   */
  public ControlPanel(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(this.setupViewButton());
    this.add(this.setupAddButton());
    this.add(this.setupDeleteButton());
    this.add(this.setupModifyButton());
  }

  /**
   * Setup the button used to view an account.
   *
   * @return JButton for the 'view' button.
   */
  private JButton setupViewButton() {
    JButton viewButton = new JButton(this.parentWindow.resourceBundle.getString("view"));
    viewButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        view();
      }

    });
    return viewButton;
  }

  /**
   * Setup the button used to add an account.
   *
   * @return JButton for the 'add' button.
   */
  private JButton setupAddButton() {
    JButton addButton = new JButton(this.parentWindow.resourceBundle.getString("add"));
    addButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        add();
      }

    });
    return addButton;
  }

  /**
   * Setup the button used to delete an account.
   *
   * @return JButton for the 'delete' button.
   */
  private JButton setupDeleteButton() {
    JButton deleteButton = new JButton(this.parentWindow.resourceBundle.getString("delete"));
    deleteButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        delete();
      }

    });
    return deleteButton;

  }

  /**
   * Setup the button used to modify an account.
   *
   * @return JButton for the 'modify' account.
   */
  private JButton setupModifyButton() {
    JButton modifyButton = new JButton(this.parentWindow.resourceBundle.getString("modify"));
    modifyButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        modify();
      }

    });
    return modifyButton;
  }

  /**
   * View the selected account.
   */
  private void view() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      String recordName = this.parentWindow.listPanel.accountList.getSelectedValue();
      new AccountWindow(
          this.parentWindow,
          this.parentWindow.recordMap.get(recordName),
          recordName,
          false);
    }
  }

  /**
   * Add a new account.
   */
  private void add() {
    new AccountWindow(this.parentWindow, new HashMap<String, String>(), "", true);
  }

  /**
   * Delete an account.
   */
  private void delete() {
    if (this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      return;
    }
    if (this.parentWindow.encryptedBuffer != null) {
      char[][] passwords = new PasswordEntryWindow(
          this.parentWindow.resourceBundle.getString("deletePrompt"), 2).getPasswords();
      try {
        if (!this.parentWindow.encryptedBuffer.validatePassword(passwords)) {
          throw new DecryptionException(
              "Incorrect Password",
              DecryptionExceptionCode.INCORRECT_PASSWORD);
        }
      } catch (DecryptionException e) {
        this.parentWindow.displayPasswordIncorrectError();
        return;
      }
    }
    this.parentWindow.recordMap.remove(this.parentWindow.listPanel.accountList.getSelectedValue());
    this.parentWindow.listPanel.updateAccountList();
  }

  /**
   * Modify and account.
   */
  private void modify() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      String recordName = this.parentWindow.listPanel.accountList.getSelectedValue();
      new AccountWindow(
          this.parentWindow,
          this.parentWindow.recordMap.get(recordName),
          recordName,
          true);
    }
  }

}