package es.icarto.gvsig.navtableforms.gui.tables.menu;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public abstract class VectorialJTableContextualMenu extends
	BaseJTableContextualMenu {

    protected String layerName;

    public VectorialJTableContextualMenu(String layerName) {
	this.layerName = layerName;
    }

    protected void openDialog() {
	AbstractForm form = FormFactory.createFormRegistered(layerName);
	form.init();
	form.setPosition(((BaseTableModel) table.getModel())
		.convertRowIndexToModel(table.getSelectedRow()));
	PluginServices.getMDIManager().addWindow(form);
    }

}
