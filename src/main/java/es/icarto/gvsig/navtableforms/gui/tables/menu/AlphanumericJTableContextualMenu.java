package es.icarto.gvsig.navtableforms.gui.tables.menu;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;

/**
 * AlphanumericJTableContextualMenu
 * 
 * Base code for relationship table contextual menus that open subforms.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class AlphanumericJTableContextualMenu extends BaseJTableContextualMenu {

	protected IForm form;

	public AlphanumericJTableContextualMenu(IForm form) {
		this.form = form;
	}

}
