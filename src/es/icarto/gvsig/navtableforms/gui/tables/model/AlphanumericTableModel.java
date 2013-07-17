package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.HashMap;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public class AlphanumericTableModel extends BaseTableModel {

    private IEditableSource source;
    private TableController tableController;

    public AlphanumericTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases, IRowFilter filter) {
	super(colNames, colAliases, filter);
	this.source = source;
	this.tableController = new TableController(source);
	initMetadata();
    }

    public AlphanumericTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases) {
	super(colNames, colAliases);
	this.source = source;
	this.tableController = new TableController(source);
	initMetadata();
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

    @Override
    protected int getModelRowCount() {
	try {
	    return source.getRowCount();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    @Override
    protected IRow getSourceRow(int row) {
	try {
	    return source.getRow(row);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

}
