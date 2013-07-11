package es.icarto.gvsig.navtableforms.gui.tables.model;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public final class NotEditableTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
	return false;
    }
}