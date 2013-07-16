package es.icarto.gvsig.navtableforms.gui.tables.menu;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;

public abstract class AlphanumericJTableContextualMenu extends
	BaseJTableContextualMenu {

    protected IForm form;

    public AlphanumericJTableContextualMenu(IForm form) {
	this.form = form;
    }

}
