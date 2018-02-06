package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericUpdateJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * AlphanumericNotEditableNNRelTableHandler
 * 
 * Handler for n:n relationships tables that simply link to a subform.
 * 
 * @author Jorge L�pez Fern�ndez <jlopez@cartolab.es>
 */

public class AlphanumericNotEditableNNRelTableHandler extends
	BaseNNRelTableHandler {

    private AbstractSubForm form;

    public AlphanumericNotEditableNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, dbSchema, originKey, relTable,
		destinationKey, colNames, colAliases);
	FormFactory.checkAndLoadTableRegistered(sourceTableName);
	form = FormFactory.createSubFormRegistered(sourceTableName);
    }

    /**
     * Constructor w/o the column aliases, which will be retrieved from
     * the form's i18n resources.
     */
    public AlphanumericNotEditableNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames) {
	super(sourceTableName, widgets, dbSchema, originKey, relTable,
		destinationKey, colNames, new String[colNames.length]);
	FormFactory.checkAndLoadTableRegistered(sourceTableName);
	form = FormFactory.createSubFormRegistered(sourceTableName);
	I18nResourceManager i18nManager = form.getI18nHandler().getResourceManager();
	for (int i = 0, len = colNames.length; i<len; i++) {
	    colAliases[i] = i18nManager.getString(colNames[i]);
	}
    }

    protected void createTableModel() throws ReadDriverException {
        model = TableModelFactory.createFromTableWithOrFilter(sourceTableName,
                getDestinationKey(), destinationKeyValues, colNames, colAliases,
                form.getI18nHandler().getResourceManager().getResources());

        jtable.setModel(model);
        if (form != null) {
            form.setModel((AlphanumericTableModel) model);
        }
    }

    @Deprecated
    public void reload(AbstractSubForm form) {
	this.form = form;
	reload();
    }

    protected void createTableListener() {
	listener = new AlphanumericUpdateJTableContextualMenu(form);
    }

}
