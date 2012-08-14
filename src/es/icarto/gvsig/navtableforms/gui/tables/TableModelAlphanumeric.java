package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.dataacces.TableController;

public class TableModelAlphanumeric extends AbstractTableModel {

    private static int NO_ROW = -1;

    private IEditableSource source;
    private IRowFilter filter;
    private TableController tableController;

    private List<String> colNames;
    private List<String> colAliases;

    private HashMap<Integer, Integer> rowIndexes;
    private int currentRow = NO_ROW;

    private int rowCount;
    private int colCount;

    public TableModelAlphanumeric(IEditableSource source,
	    IRowFilter filter,
	    List<String> colNames,
	    List<String> colAliases) {
	this.source = source;
	this.filter = filter;
	this.colNames = colNames;
	this.colAliases = colAliases;
	this.tableController = new TableController(source);
	initMetadata();
    }

    private void initMetadata() {
	rowIndexes = getRowIndexes();
	rowCount = rowIndexes.size();
	colCount = colNames.size();
    }

    private HashMap<Integer, Integer> getRowIndexes() {
	int indexInJTable = 0;
	this.rowIndexes = new HashMap<Integer, Integer>();
	try {
	    for (int indexInSource = 0; indexInSource < source.getRowCount(); indexInSource++) {
		if (filter.evaluate(source.getRow(indexInSource))) {
		    rowIndexes.put(indexInJTable, indexInSource);
		    indexInJTable++;
		}
	    }
	    return rowIndexes;
	} catch (Exception e) {
	    e.printStackTrace();
	    rowIndexes.clear();
	    currentRow = NO_ROW;
	    return rowIndexes;
	}
    }

    public int getColumnCount() {
	return colCount;
    }

    public int getRowCount() {
	return rowCount;
    }

    public String getColumnName(int column) {
	return colAliases.get(column);
    }

    public String getColumnNameInSource(int column) {
	return colNames.get(column);
    }

    public boolean isCellEditable(int arg0, int arg1) {
	return false;
    }

    public Object getValueAt(int row, int col) {
	try {
	    if (currentRow != row) {
		currentRow = row;
		tableController.read(rowIndexes.get(row));
	    }
	    return tableController.getValue(colNames.get(col));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public IEditableSource getSource() {
	return source;
    }

    public void create(int position) {
	// TODO: implement
	System.out.println("Create");
    }

    public HashMap<String, String> read(int row) throws ReadDriverException {
	if (currentRow != row) {
	    currentRow = row;
	    tableController.read(rowIndexes.get(row));
	}
	return tableController.getValuesOriginal();
    }

    public void updateValue(String fieldName, String fieldValue) {
	tableController.setValue(fieldName, fieldValue);
    }

    public void update(int row) throws ReadDriverException {
	tableController.update(rowIndexes.get(row));
	this.fireTableDataChanged();
    }

    public void delete(int row) throws StopWriterVisitorException,
    StartWriterVisitorException, InitializeWriterException,
    ReadDriverException {
	tableController.delete(rowIndexes.get(row));
	this.fireTableRowsDeleted(row, row);
	initMetadata();
    }

}
