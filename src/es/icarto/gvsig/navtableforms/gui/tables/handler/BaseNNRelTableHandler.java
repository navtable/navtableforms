package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.menu.JTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class BaseNNRelTableHandler {

    protected String sourceTableName;
    protected JTable jtable;
    protected BaseTableModel model;
    protected String originKey;
    protected String destinationKey;
    protected String relTable;
    protected String dbSchema;
    protected String[] colNames;
    protected String[] colAliases;
    protected String[] destinationKeyValues;
    protected int keyColumn = 0;
    protected String originKeyValue;
    protected JTableContextualMenu listener;

    public BaseNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	this.sourceTableName = sourceTableName;
	this.originKey = originKey;
	this.dbSchema = dbSchema;
	this.relTable = relTable;
	this.destinationKey = destinationKey;
	this.colNames = colNames;
	this.colAliases = colAliases;
	if (colNames != null) {
	    for (int i = 0, columns = colNames.length; i < columns; i++) {
		if (colNames[i].equals(destinationKey)) {
		    keyColumn = i;
		    break;
		}
	    }
	}
	getJTable(widgets);
    }

    protected void getJTable(HashMap<String, JComponent> widgets) {
	jtable = (JTable) widgets.get(sourceTableName);
    }

    public void fillValues(String value) {
	try {
	    originKeyValue = value;
	    DBSession session = DBSession.getCurrentSession();
	    if (session != null) {
		destinationKeyValues = session.getDistinctValues(relTable,
			dbSchema, destinationKey, false, false, "WHERE "
				+ originKey + "='" + originKeyValue + "'");
		createTableModel();
		((DefaultTableCellRenderer) jtable.getTableHeader()
			.getDefaultRenderer())
			.setHorizontalAlignment(JLabel.CENTER);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    protected abstract void createTableModel() throws ReadDriverException;

    protected abstract void createTableListener();

    protected void reload() {
	createTableListener();
	if (listener != null) {
	    jtable.addMouseListener(listener);
	}
	reloadGUI();
    }

    public void reloadGUI() {
	// for the popUp to work on empty tables
	jtable.setFillsViewportHeight(true);
    }

    public void removeListeners() {
	if (listener != null) {
	    jtable.removeMouseListener(listener);
	}
    }

    public String getOriginKey() {
	return originKey;
    }

    public String getSourceTableName() {
	return sourceTableName;
    }

    public String getDestinationKey() {
	return destinationKey;
    }

    public String getRelationTableName() {
	return relTable;
    }

    public String getDbSchema() {
	return dbSchema;
    }

    public JTableContextualMenu getListener() {
	return listener;
    }

    public JTable getJTable() {
	return jtable;
    }

    public JTable getJtable() {
	return jtable;
    }

    public BaseTableModel getModel() {
	return model;
    }

    public String getRelTable() {
	return relTable;
    }

    public String[] getColNames() {
	return colNames;
    }

    public String[] getColAliases() {
	return colAliases;
    }

    public String[] getDestinationKeyValues() {
	return destinationKeyValues;
    }

    public int getKeyColumn() {
	return keyColumn;
    }

    public String getOriginKeyValue() {
	return originKeyValue;
    }

}
