package es.icarto.gvsig.navtableforms.gui.tables.menu;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
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
	    form.resetListeners();
	}
	form.setPosition(((BaseTableModel) table.getModel())
		.convertRowIndexToModel(table.getSelectedRow()));
	PluginServices.getMDIManager().addWindow(form);
    }

    @Deprecated
    public void setDialog(AbstractForm form) {
	this.form = form;
    }

}
