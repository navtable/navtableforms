package es.icarto.gvsig.navtableforms.gui.tables.menu;

import org.gvsig.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * VectorialJTableContextualMenu
 *
 * Base code for relationship table contextual menus that open vectorial forms.
 *
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class VectorialJTableContextualMenu extends
		BaseJTableContextualMenu {

	protected AbstractForm form;
	protected boolean formInitialized = false;

	public VectorialJTableContextualMenu(String layerName) {
		this.form = FormFactory.createFormRegistered(layerName);
	}

	protected void openDialog() {
		if (!formInitialized) {
			formInitialized = true;
			form.init();
		} else {
			form.reinit();
		}
		int position = table.convertRowIndexToModel(table.getSelectedRow());
		form.setPosition(position);
		PluginServices.getMDIManager().addWindow(form);
	}

	@Deprecated
	public void setDialog(AbstractForm form) {
		this.form = form;
	}

}
