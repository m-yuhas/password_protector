package gui;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 * ListPanel extends JPanel and is used to display the list of all accounts in a passwords file.
 */
public class ListPanel extends JPanel {

  /**
   * JList to display the list of of accounts loaded from the parent window's record map.
   */
  public JList<String> accountList;

  /**
   * JTextField to search for an account by name.
   */
  private JTextField searchField;

  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = -1294042084540205908L;

  /**
   * Minimum width for this JPanel.  Current value was determined experimentally.
   */
  private final int width = 250;

  /**
   * Minimum height for this JPanel.  Current value was determined experimentally.
   */
  private final int height = 500;

  /**
   * The parent window for which this panel is created.
   */
  private transient MainWindow parentWindow;

  /**
   * Construct an instance of ListPanel.
   *
   * @param parentWindow is the MainFrame where this ListPanel will be displayed.
   */
  public ListPanel(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    JLabel searchLabel = new JLabel("Search:");
    this.add(searchLabel);
    this.searchField = new JTextField();
    this.searchField.setEditable(true);
    this.searchField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(KeyEvent e) {
        filterAccountList();
      }

    });
    this.add(this.searchField);
    this.accountList = new JList<String>();
    this.accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.accountList.setLayoutOrientation(JList.VERTICAL);
    this.accountList.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(this.accountList);
    listScroller.setPreferredSize(new Dimension(this.width, this.height));
    this.accountList.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          String recordName = ListPanel.this.accountList.getSelectedValue();
          new AccountWindow(
              ListPanel.this.parentWindow,
              ListPanel.this.parentWindow.recordMap.get(recordName),
              recordName,
              false
          );
        }
        if (SwingUtilities.isRightMouseButton(e)) {
          ActionPopupMenu popupMenu = new ActionPopupMenu(ListPanel.this.parentWindow);
          popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
      }

    });
    this.accountList.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == 0) {
          String recordName = ListPanel.this.accountList.getSelectedValue();
          new AccountWindow(
              ListPanel.this.parentWindow,
              ListPanel.this.parentWindow.recordMap.get(recordName),
              recordName,
              false
          );
        }
      }

    });
    this.add(listScroller);
    this.updateAccountList();
  }

  /**
   * Update the accounts displayed in this ListPanel with the current contents of the parent
   * window's record map.
   */
  public void updateAccountList() {
    String[] accounts = this.parentWindow.recordMap.keySet().toArray(new String[0]);
    Arrays.sort(accounts);
    this.accountList.setListData(accounts);
    this.revalidate();
  }

  /**
   * Filter the accounts displayed in this ListPanel by the string in searchField.
   */
  public void filterAccountList() {
    HashSet<String> accounts = new HashSet<String>(this.parentWindow.recordMap.keySet());
    String searchString = this.searchField.getText();
    accounts.removeIf(s -> !s.toLowerCase().contains(searchString.toLowerCase()));
    this.accountList.setListData(accounts.toArray(new String[0]));
    this.revalidate();
  }

}
