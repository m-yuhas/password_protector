package gui;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * ListPanel extends JPanel and is used to display the list of all accounts in a passwords file.
 */
public class ListPanel extends JPanel {

  /**
   * JList to display the list of of accounts loaded from the parent window's record map.
   */
  public JList<String> accountList;

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
  private MainWindow parentWindow;

  /**
   * Construct an instance of ListPanel.
   *
   * @param parentWindow is the MainFrame where this ListPanel will be displayed.
   */
  public ListPanel(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.accountList = new JList<String>();
    this.accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.accountList.setLayoutOrientation(JList.VERTICAL);
    this.accountList.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(this.accountList);
    listScroller.setPreferredSize(new Dimension(this.width, this.height));
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

}
