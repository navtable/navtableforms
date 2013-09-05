package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.menu.BaseJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;

/**
 * BaseTableHandler
 * 
 * Core code for table handlers (handlers of tables that contain info on
 * entities related to the one displayed in the current form).
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class BaseTableHandler {

    /**
     * The name of the table, which should also provide us the related form.
     */
    protected String sourceTableName;

    /**
     * The table widget.
     */
    protected JTable jtable;

    /**
     * The table model.
     */
    protected BaseTableModel model;

    /**
     * The name of the foreign key in the related entity.
     */
    protected String destinationKey;

    /**
     * The current value of the primary key in the current form.
     */
    protected String originKeyValue;

    /**
     * The columns we want to show of those related entities.
     */
    protected String[] colNames;

    /**
     * The titles for those columns.
     */
    protected String[] colAliases;

    /**
     * The column which contains the foreign key.
     */
    protected int keyColumn = 0;

    /**
     * The contextual menu for the table.
     */
    protected BaseJTableContextualMenu listener;

    public BaseTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	this.sourceTableName = tableName;
	getJTable(widgets);
	jtable.getTableHeader().setReorderingAllowed(false);
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

    public void reload() {
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
