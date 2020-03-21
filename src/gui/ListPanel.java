package gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import passwordio.EncryptedBuffer;

public class ListPanel extends JPanel {
  
  /**
   * 
   */
  private static final long serialVersionUID = -1294042084540205908L;
  private MainWindow parentWindow;
  public JList<String> accountList;

  public ListPanel(MainWindow parentWindow) {
    this.parentWindow = parentWindow;
    this.accountList = new JList<String>();
    this.accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.accountList.setLayoutOrientation(JList.VERTICAL);
    this.accountList.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(this.accountList);
    listScroller.setPreferredSize(new Dimension(250, 500));
    this.add(listScroller);
  }

  public void updateAccountList() {
    Iterator<Entry<String, Map<String, String>>> iterator = this.parentWindow.recordMap.entrySet().iterator();
    String[] options = new String[this.parentWindow.recordMap.size()];
    int i = 0;
    while (iterator.hasNext()) {
      options[i++] = iterator.next().getKey();
    }
    this.accountList.setListData(options);
    this.revalidate();
  }
}
