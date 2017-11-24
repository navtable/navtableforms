package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.menu.BaseJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

/**
 * BaseTableHandler
 *
 * Core code for table handlers (handlers of tables that contain info on
 * entities related to the one displayed in the current form).
 *
 * @author Jorge López Fernández <jlopez@cartolab.es>
 * @author Francisco Puga <fpuga@icarto.es>
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
    protected final String[] destinationKey;

    /**
     * The current value of the primary key in the current form.
     */
    protected String[] originKeyValue;

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
    private final List<Integer> keyColumn = new ArrayList<Integer>();

    /**
     * The contextual menu for the table.
     */
    protected BaseJTableContextualMenu listener;

    private final Map<String, JComponent> widgets;

    public BaseTableHandler(String tableName, Map<String, JComponent> widgets,
	    String[] foreignKeyId, String[] colNames, String[] colAliases) {
	this.sourceTableName = tableName;
	getJTable(widgets);
	this.widgets = widgets;
	jtable.getTableHeader().setReorderingAllowed(false);
	this.destinationKey = foreignKeyId;
	this.originKeyValue = new String[foreignKeyId.length];
	this.colNames = colNames;
	this.colAliases = colAliases;
	if (colNames != null) {
	    List<String> colNamesList = Arrays.asList(colNames);
	    for (String s : destinationKey) {
		int index = colNamesList.indexOf(s);
		if (index != -1) {
		    keyColumn.add(index);
		} else {
		    // Probably is safer to add a -1, but previously it was
		    // initialized with 0 so this behavior is preserved
		    keyColumn.add(0);
		}
	    }
	}
    }

    protected void getJTable(Map<String, JComponent> widgets) {
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
	// Workaround for composed fk
	if (destinationKey.length > 1) {
	    for (int i = 0; i< destinationKey.length; i++) {
		JComponent c = widgets.get(destinationKey[i]);
		if (c instanceof JTextField) {
		    originKeyValue[i] = ((JTextField) c).getText();
		} else if (c instanceof JComboBox) {
		    final Object selectedItem = ((JComboBox) c).getSelectedItem();
		    if (selectedItem != null) {
			KeyValue keyvalue = (KeyValue)selectedItem;
			originKeyValue[i] = keyvalue.getKey();			
		    } else {
			originKeyValue[i] = "";
		    }
		}
	    }
	} else {
	    this.originKeyValue[0] = foreignKeyValue;
	}
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
	return destinationKey[0];
    }

    public String getOriginKeyValue() {
	return originKeyValue[0];
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
	return keyColumn.get(0);
    }

}
