package es.icarto.gvsig.navtableforms.launcher;

import javax.swing.table.DefaultTableModel;

public class NonEditableTableModel extends DefaultTableModel {

    public NonEditableTableModel(Object[][] rows, Object[] columns) {
	super(rows, columns);
    }

    public boolean isCellEditable(int row, int column) {
	return false;
    }

}
