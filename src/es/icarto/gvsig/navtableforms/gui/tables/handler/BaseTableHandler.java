package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.menu.BaseJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;

public abstract class BaseTableHandler {

    protected String sourceTableName;
    protected JTable jtable;
    protected BaseTableModel model;
    protected String destinationKey;
    protected String originKeyValue;
    protected String[] colNames;
    protected String[] colAliases;
    protected int keyColumn = 0;
    protected BaseJTableContextualMenu listener;

    public BaseTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	this.sourceTableName = tableName;
	getJTable(widgets);
	this.destinationKey = foreignKeyId;
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

    public void fillValues(String foreignKeyValue) {
	this.originKeyValue = foreignKeyValue;
	try {
	    createTableModel();
	    ((DefaultTableCellRenderer) jtable.getTableHeader()
		    .getDefaultRenderer())
		    .setHorizontalAlignment(JLabel.CENTER);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}

    }

    public void removeListeners() {
	jtable.removeMouseListener(listener);
    }

    public String getSourceTableName() {
	return sourceTableName;
    }

    public String getDestinationKey() {
	return destinationKey;
    }

    public String getOriginKeyValue() {
	return originKeyValue;
    }

    public BaseJTableContextualMenu getListener() {
	return listener;
    }

    public JTable getJTable() {
	return jtable;
    }

    public BaseTableModel getModel() {
	return model;
    }

    public String[] getColNames() {
	return colNames;
    }

    public String[] getColAliases() {
	return colAliases;
    }

    public int getKeyColumn() {
	return keyColumn;
    }

}
