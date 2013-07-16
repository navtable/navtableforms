package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class BaseNNRelTableHandler extends BaseTableHandler {

    protected String originKey;
    protected String relTable;
    protected String dbSchema;
    protected String[] destinationKeyValues;
    protected int keyColumn = 0;

    public BaseNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, destinationKey, colNames, colAliases);
	this.originKey = originKey;
	this.dbSchema = dbSchema;
	this.relTable = relTable;
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
