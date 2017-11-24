package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

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

    public AlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	super(tableName, widgets, new String[] {foreignKeyId}, colNames, colAliases);
	FormFactory.checkAndLoadTableRegistered(tableName);
	form = FormFactory.createSubFormRegistered(tableName);
    }

    /**
     * Constructor w/o the column aliases, which will be retrieved from
     * the form's i18n resources.
     */
    public AlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames) {
	super(tableName, widgets, new String[] {foreignKeyId}, colNames, new String[colNames.length]);
	FormFactory.checkAndLoadTableRegistered(tableName);
	form = FormFactory.createSubFormRegistered(tableName);
	I18nResourceManager i18nManager = form.getI18nHandler().getResourceManager();
	for (int i = 0, len = colNames.length; i<len; i++) {
	    colAliases[i] = i18nManager.getString(colNames[i]);
	}
    }

    @Override
    protected void createTableModel() throws ReadDriverException {
	AlphanumericTableModel model = TableModelFactory
		.createFromTableWithFilter(sourceTableName, getDestinationKey(),
			getOriginKeyValue(), colNames, colAliases);
	jtable.setModel(model);
	if (form != null) {
	    form.setModel(model);
	}
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
	foreignKey.put(getDestinationKey(), getOriginKeyValue());
	if (form != null) {
	    form.setForeingKey(foreignKey);
	}
    }

    @Override
    protected void createTableListener() {
	listener = new AlphanumericCompleteJTableContextualMenu(form);
    }

}
