package gui;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

/**
 * An extension of JTable that starts with a fixed number of cells, but grows as the user reaches
 * the end of the table, so that the user can continue to add to the table ad infinitum.
 */
class GrowableTable extends JTable {

  /**
   * Unique ID for serialization.
   */
  private static final long serialVersionUID = 1885182474233048895L;

  /**
   * Construct an instance of GrowableTable.
   *
   * @param tableModel is an instance of DefaultTableModel to serve as the table model for this
   *        growable table.
   */
  public GrowableTable(DefaultTableModel tableModel) {
    super(tableModel);
    this.setCellSelectionEnabled(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultTableModel getModel() {
    return (DefaultTableModel) super.getModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void editingStopped(ChangeEvent e) {
    int row = getEditingRow();
    int column = getEditingColumn();
    super.editingStopped(e);
    if (row == getRowCount() - 1 && column == getColumnCount() - 1) {
      getModel().addRow(new String[] {"", ""});
    }
  }

}
