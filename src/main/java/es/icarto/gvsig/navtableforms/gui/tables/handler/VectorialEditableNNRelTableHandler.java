package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

import org.gvsig.fmap.dal.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author Jorge L�pez Fern�ndez <jlopez@cartolab.es>
 */

public class VectorialEditableNNRelTableHandler extends
		EditableNNRelTableHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(VectorialEditableNNRelTableHandler.class);

	public VectorialEditableNNRelTableHandler(String sourceTableName,
			HashMap<String, JComponent> widgets, String dbSchema,
			String originKey, String relTable, String destinationKey,
			String[] colNames, String[] colAliases) {
		super(sourceTableName, widgets, dbSchema, originKey, relTable,
				destinationKey, colNames, colAliases);
		FormFactory.checkAndLoadLayerRegistered(sourceTableName);
	}

	@Override
	protected void createTableModel() throws DataException {
		model = TableModelFactory.createFromLayerWithOrFilter(sourceTableName,
				destinationKey, destinationKeyValues, colNames, colAliases);
		jtable.setModel(model);
	}

	@Override
	protected void createTableListener() {
		listener = new VectorialEditableNNRelJTableContextualMenu(this);
	}

	@Deprecated
	public void reload(AbstractForm form) {
		reload();
		((VectorialEditableNNRelJTableContextualMenu) listener).setDialog(form);
	}

	@Override
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
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void insertRow(String secondaryPKValue) {
		String[] columns = { originKey, destinationKey };
		String[] values = { originKeyValue, secondaryPKValue };
		DBSession.getCurrentSession().insertRow(dbSchema, relTable, columns,
				values);
		fillValues(originKeyValue);
	}

	@Override
	public void deleteRow(String secondaryPKValue) {
		String where = "WHERE " + originKey + " = '" + originKeyValue
				+ "' AND " + destinationKey + " = '" + secondaryPKValue + "'";
		DBSession.getCurrentSession().deleteRows(dbSchema, relTable, where);
		fillValues(originKeyValue);
	}

}
