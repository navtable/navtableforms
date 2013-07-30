package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.VectorialEditableNNRelJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;
import es.udc.cartolab.gvsig.users.utils.DBSession;

/**
 * VectorialEditableNNRelTableHandler
 * 
 * Handler for n:n relationships tables that link to a vectorial form and in
 * which we can add/delete relations.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class VectorialEditableNNRelTableHandler extends
	EditableNNRelTableHandler {

    public VectorialEditableNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, dbSchema, originKey, relTable,
		destinationKey, colNames, colAliases);
	FormFactory.checkAndLoadLayerRegistered(sourceTableName);
    }

    protected void createTableModel() throws ReadDriverException {
	TableModel model = TableModelFactory.createFromLayerWithOrFilter(
		sourceTableName, destinationKey, destinationKeyValues,
		colNames, colAliases);
	jtable.setModel(model);
    }

    protected void createTableListener() {
	listener = new VectorialEditableNNRelJTableContextualMenu(this);
    }

    @Deprecated
    public void reload(AbstractForm form) {
	reload();
	((VectorialEditableNNRelJTableContextualMenu) listener).setDialog(form);
    }

    public List<String> getUnlinkedSecondaryValues() {
	try {
	    String where = "";
	    if (destinationKeyValues.length > 0) {
		where = " WHERE " + destinationKey + " NOT IN (";
		for (String val : destinationKeyValues) {
		    where += "'" + val + "', ";
		}
		where = where.substring(0, where.length() - 2) + ")";
	    }
	    String[] values = DBSession.getCurrentSession().getDistinctValues(
		    sourceTableName, dbSchema, destinationKey, false, false,
		    where);
	    return Arrays.asList(values);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public void insertRow(String secondaryPKValue) {
	try {
	    String[] columns = { originKey, destinationKey };
	    String[] values = { originKeyValue, secondaryPKValue };
	    DBSession.getCurrentSession().insertRow(dbSchema, relTable,
		    columns, values);
	    fillValues(originKeyValue);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void deleteRow(String secondaryPKValue) {
	try {
	    String where = "WHERE " + originKey + " = '" + originKeyValue
		    + "' AND " + destinationKey + " = '" + secondaryPKValue
		    + "'";
	    DBSession.getCurrentSession().deleteRows(dbSchema, relTable, where);
	    fillValues(originKeyValue);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
