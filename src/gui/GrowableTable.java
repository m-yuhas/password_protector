package gui;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

class GrowableTable extends JTable {

  private static final long serialVersionUID = 1885182474233048895L;

  public GrowableTable(DefaultTableModel m) {
    super(m);
  }
  
  @Override
  public DefaultTableModel getModel() {
    return (DefaultTableModel) super.getModel();
  }
  
  @Override
  public void editingStopped(ChangeEvent e) {
    int row = getEditingRow();
    int col = getEditingColumn();
    super.editingStopped(e);
    if (row == getRowCount() - 1 && col == getColumnCount() - 1)
    {
      getModel().addRow(new String[] {"", ""});
    }
  }
}
