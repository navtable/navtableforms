package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.gvsig.fmap.dal.exception.DataException;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericCompleteJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * AlphanumericTableHandler
 * 
 * Handler for relationships tables that link to a subform.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class AlphanumericTableHandler extends BaseTableHandler {

	protected AbstractSubForm form;

	public AlphanumericTableHandler(String tableName, HashMap<String, JComponent> widgets, String foreignKeyId,
			String[] colNames, String[] colAliases) {
		super(tableName, widgets, foreignKeyId, colNames, colAliases);
		FormFactory.checkAndLoadTableRegistered(tableName);
		form = FormFactory.createSubFormRegistered(tableName);
	}

	/**
	 * Constructor w/o the column aliases, which will be retrieved from the form's
	 * i18n resources.
	 */
	public AlphanumericTableHandler(String tableName, HashMap<String, JComponent> widgets, String foreignKeyId,
			String[] colNames) {
		super(tableName, widgets, foreignKeyId, colNames, new String[colNames.length]);
		FormFactory.checkAndLoadTableRegistered(tableName);
		form = FormFactory.createSubFormRegistered(tableName);
		I18nResourceManager i18nManager = form.getI18nHandler().getResourceManager();
		for (int i = 0, len = colNames.length; i < len; i++) {
			colAliases[i] = i18nManager.getString(colNames[i]);
		}
	}

	@Override
	protected void createTableModel() throws DataException {
		if (form != null) {
			model = TableModelFactory.createFromTableWithFilter(sourceTableName, destinationKey, originKeyValue,
					colNames, colAliases, form.getI18nHandler().getResourceManager().getResources());
			form.setModel((AlphanumericTableModel) model);
		} else {
			model = TableModelFactory.createFromTableWithFilter(sourceTableName, destinationKey, originKeyValue,
					colNames, colAliases);
		}
		jtable.setModel(model);
	}

	@Deprecated
	public void reload(AbstractSubForm form) {
		this.form = form;
		reload();
	}

	@Override
	public void fillValues(String foreignKeyValue) {
		super.fillValues(foreignKeyValue);
		Map<String, String> foreignKey = new HashMap<String, String>(1);
		foreignKey.put(destinationKey, originKeyValue);
		if (form != null) {
			form.setForeingKey(foreignKey);
		}
	}

	@Override
	protected void createTableListener() {
		listener = new AlphanumericCompleteJTableContextualMenu(form);
	}

}
