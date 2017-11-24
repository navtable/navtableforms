package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

import es.udc.cartolab.gvsig.users.utils.DBSession;

/**
 * BaseNNRelTableHandler
 * 
 * Base handler for n:n relationships tables.
 * 
 * IMPORTANT: the destinationKey is in this case treated as the primary key of
 * the related table, and the n:n table is assumed to have the field which links
 * to it with the same name.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class BaseNNRelTableHandler extends BaseTableHandler {

    /**
     * The name of the primary key in the current form (assumed to have the same
     * name in the n:n table).
     */
    protected String originKey;

    /**
     * The name of the n:n table.
     */
    protected String relTable;

    /**
     * The name of the n:n table's schema.
     */
    protected String dbSchema;

    /**
     * The related entities' primary key values.
     */
    protected String[] destinationKeyValues;

    public BaseNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, new String[] {destinationKey}, colNames, colAliases);
	this.originKey = originKey;
	this.dbSchema = dbSchema;
	this.relTable = relTable;
    }

    @Override
    public void fillValues(String value) {
	try {
	    originKeyValue[0] = value;
	    DBSession session = DBSession.getCurrentSession();
	    if (session != null) {
		destinationKeyValues = session.getDistinctValues(relTable,
			dbSchema, getDestinationKey(), false, false, "WHERE "
				+ originKey + "='" + getOriginKeyValue() + "'");
		createTableModel();
		((DefaultTableCellRenderer) jtable.getTableHeader()
			.getDefaultRenderer())
			.setHorizontalAlignment(JLabel.CENTER);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public String getOriginKey() {
	return originKey;
    }

    public String getDbSchema() {
	return dbSchema;
    }

    public String getRelTable() {
	return relTable;
    }

    public String[] getDestinationKeyValues() {
	return destinationKeyValues;
    }

}
