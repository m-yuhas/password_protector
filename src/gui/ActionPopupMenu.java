package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import passwordio.DecryptionException;
import passwordio.DecryptionExceptionCode;

/**
 * Popup menu that appears when right clicking on an account in the accounts list.  This popup
 * menu contains a list of possible actions for any account.
 */
class ActionPopupMenu extends JPopupMenu {

  /**
   * The parent window for which this panel is created.
   */
  private transient MainWindow parentWindow;

  /**
   * Create an instance of the action popup menu and populate it with items.
   * 
   * @param parentWindow is an instance of MainWindow: the window that spawned this menu.
   */
  public ActionPopupMenu(MainWindow parentWindow) {
    super();
    this.parentWindow = parentWindow;
    this.add(this.setupViewItem());
    this.add(this.setupAddItem());
    this.add(this.setupDeleteItem());
    this.add(this.setupModifyItem());
  }

  /**
   * Prepare the menu item for viewing an account.
   * 
   * @return JMenuItem for viewing an acccount.
   */
  private JMenuItem setupViewItem() {
    JMenuItem viewItem = new JMenuItem(this.parentWindow.resourceBundle.getString("view"));
    viewItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        view();
      }

    });
    return viewItem;
  }

  /**
   * Prepare the menu item for adding a new account.
   * 
   * @return JMenuItem for creating a new account.
   */
  private JMenuItem setupAddItem() {
    JMenuItem addItem = new JMenuItem(this.parentWindow.resourceBundle.getString("add"));
    addItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        add();
      }

    });
    return addItem;
  }

  /**
   * Prepare the menu item for deleting an account.
   * 
   * @return JMenuItem for deleting an account.
   */
  private JMenuItem setupDeleteItem() {
    JMenuItem deleteItem = new JMenuItem(this.parentWindow.resourceBundle.getString("delete"));
    deleteItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        delete();
      }

    });
    return deleteItem;
  }

  /**
   * Prepare the menu item for modifying an account record.
   * 
   * @return JMenuItem for modifying an account record.
   */
  private JMenuItem setupModifyItem() {
    JMenuItem modifyItem = new JMenuItem(this.parentWindow.resourceBundle.getString("modify"));
    modifyItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        modify();
      }

    });
    return modifyItem;
  }

  /**
   * View an account record.
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
   * Add a new account record.
   */
  private void add() {
    new AccountWindow(this.parentWindow, new HashMap<String, String>(), "", true);
  }

  /**
   * Delete an account record.
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
              DecryptionExceptionCode.INCORRECT_PASSWORD
          );
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
   * Modify an account record.
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