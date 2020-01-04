package gui;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ListPanel extends JPanel {
  
  /**
   * 
   */
  private static final long serialVersionUID = -1294042084540205908L;
  public JList<String> accountList;

  public ListPanel() {
    this.accountList = new JList<String>();
    this.accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.accountList.setLayoutOrientation(JList.VERTICAL);
    this.accountList.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(this.accountList);
    listScroller.setPreferredSize(new Dimension(250, 500));
    this.add(listScroller);
  }

  public void updateAccountList(String[] options) {
    this.accountList.setListData(options);
    this.revalidate();
  }
}
