package es.icarto.gvsig.navtableforms.gui.tables.menu;

import org.gvsig.andami.ui.mdiManager.MDIManagerFactory;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * VectorialJTableContextualMenu
 *
 * Base code for relationship table contextual menus that open vectorial forms.
 *
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class VectorialJTableContextualMenu extends BaseJTableContextualMenu {

	protected AbstractForm form;
	protected boolean formInitialized = false;

	public VectorialJTableContextualMenu(String layerName) {
		this.form = FormFactory.createFormRegistered(layerName);
	}

	protected void openDialog() {
		if (!this.formInitialized) {
			this.formInitialized = true;
			this.form.init();
		} else {
			this.form.reinit();
		}
		final int position = this.table.convertRowIndexToModel(this.table.getSelectedRow());
		this.form.setPosition(position);
		MDIManagerFactory.getManager().addWindow(this.form);
	}

	@Deprecated
	public void setDialog(AbstractForm form) {
		this.form = form;
	}

}
