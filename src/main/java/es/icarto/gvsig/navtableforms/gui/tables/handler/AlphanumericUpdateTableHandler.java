package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericUpdateJTableContextualMenu;

/**
 * AlphanumericTableHandler
 * 
 * Handler for relationships tables that link to a subform.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class AlphanumericUpdateTableHandler extends AlphanumericTableHandler {

	public AlphanumericUpdateTableHandler(String tableName, HashMap<String, JComponent> widgets, String foreignKeyId,
			String[] colNames, String[] colAliases) {
		super(tableName, widgets, foreignKeyId, colNames, colAliases);
	}

	/**
	 * Constructor w/o the column aliases, which will be retrieved from the form's
	 * i18n resources.
	 */
	public AlphanumericUpdateTableHandler(String tableName, HashMap<String, JComponent> widgets, String foreignKeyId,
			String[] colNames) {
		super(tableName, widgets, foreignKeyId, colNames);
	}

	@Override
	protected void createTableListener() {
		listener = new AlphanumericUpdateJTableContextualMenu(form);
	}

}
