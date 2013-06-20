package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public class TableModelAlphanumeric extends AbstractTableModel {

    private static int NO_ROW = -1;

    private IEditableSource source;
    private IRowFilter filter;
    private TableController tableController;

    private String[] colNames;
    private String[] colAliases;

    private HashMap<Integer, Integer> rowIndexes;
    private int currentRow = NO_ROW;

    private int rowCount;
    private int colCount;

    public TableModelAlphanumeric(IEditableSource source, IRowFilter filter,
	    String[] colNames, String[] colAliases) {
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
	if (colNames != null) {
	    colCount = colNames.length;
	} else {
	    colCount = 0;
	}
	currentRow = NO_ROW;
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

    @Override
    public int getColumnCount() {
	return colCount;
    }

    @Override
    public int getRowCount() {
	return rowCount;
    }

    @Override
    public String getColumnName(int column) {
	return colAliases[column];
    }

    public String getColumnNameInSource(int column) {
	return colNames[column];
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
	return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
	try {
	    if (currentRow != row) {
		currentRow = row;
		tableController.read(rowIndexes.get(row));
	    }
	    String value = tableController.getValue(getColumnNameInSource(col));
	    int type = tableController.getType(getColumnNameInSource(col));
	    if ((type == java.sql.Types.BOOLEAN)
		    || (type == java.sql.Types.BIT)) {
		String translation;
		if (value.length() == 0) {
		    translation = PluginServices.getText(this,
			    "table_null_value");
		    // If there is no translation, we show the value itself.
		    value = (!translation.equals("table_null_value")) ? translation
			    : value;
		} else {
		    translation = PluginServices.getText(this, "table_" + value
			    + "_value");
		    // If there is no translation, we show the value itself.
		    value = (!translation.equals("table_" + value + "_value")) ? translation
			    : value;
		}
	    }
	    return value;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public IEditableSource getSource() {
	return source;
    }


    public void create(HashMap<String, String> values)
	    throws Exception {
	long pos = tableController.create(values);
	if (pos != TableController.NO_ROW) {
	    initMetadata();
	    this.fireTableRowsInserted(rowCount, rowCount);
	}
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
    
    public TableController getController() {
	return tableController;
    }

    public int convertRowIndexToModel(int row) {
	return rowIndexes.get(row);
    }

    public void dataChanged() {
	this.fireTableDataChanged();
	initMetadata();
    }

}
