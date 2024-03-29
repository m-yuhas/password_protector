package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
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
  private transient MainWindow parentWindow;

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
   * @return KeyableButton for the 'view' button.
   */
  private KeyableButton setupViewButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("view");
    KeyableButton viewButton = new KeyableButton(buttonText);
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
   * @return KeyableButton for the 'add' button.
   */
  private KeyableButton setupAddButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("add");
    KeyableButton addButton = new KeyableButton(buttonText);
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
   * @return KeyableButton for the 'delete' button.
   */
  private KeyableButton setupDeleteButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("delete");
    KeyableButton deleteButton = new KeyableButton(buttonText);
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
   * @return KeyableButton for the 'modify' account.
   */
  private KeyableButton setupModifyButton() {
    String buttonText = this.parentWindow.resourceBundle.getString("modify");
    KeyableButton modifyButton = new KeyableButton(buttonText);
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
          false
      );
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
          this.parentWindow,
          this.parentWindow.resourceBundle.getString("deletePrompt"),
          2
      ).getPasswords();
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
   * Modify an account.
   */
  private void modify() {
    if (!this.parentWindow.listPanel.accountList.isSelectionEmpty()) {
      String recordName = this.parentWindow.listPanel.accountList.getSelectedValue();
      new AccountWindow(
          this.parentWindow,
          this.parentWindow.recordMap.get(recordName),
          recordName,
          true
      );
    }
  }

}
