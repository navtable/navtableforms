package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.driver.exceptions.ReloadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public class AlphanumericTableModel extends BaseTableModel {
    
    private static final Logger logger = Logger
	    .getLogger(AlphanumericTableModel.class);

    private final IEditableSource source;
    private final TableController tableController;
    private I18nResourceManager i18nManager;

    public AlphanumericTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases, I18nResourceManager i18nManager, IRowFilter filter) {
	super(colNames, colAliases, filter);
	this.i18nManager = i18nManager;
	this.source = source;
	this.tableController = new TableController(source);
	initMetadata();
    }

    public AlphanumericTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases, I18nResourceManager i18nManager) {
	super(colNames, colAliases);
	this.i18nManager = i18nManager;
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
		// If there is no translation, we show the value itself.
		value = (value.length() == 0) ?
			i18nManager.getString("table_null_value", value) :
			i18nManager.getString("table_" + value + "_value", value);
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

    public void create(HashMap<String, String> values) throws Exception {
	long pos = tableController.create(values);
	if (pos != TableController.NO_ROW) {
	    initMetadata();
	    this.fireTableDataChanged();
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
	this.fireTableDataChanged();
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

    @Override
    public void reloadUnderlyingData() {
	try {
	    source.getRecordset().reload();
	} catch (ReloadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	}
    }

}
