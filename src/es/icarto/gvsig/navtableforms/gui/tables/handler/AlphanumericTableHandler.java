package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.AlphanumericCompleteJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class AlphanumericTableHandler extends BaseTableHandler {

    private IForm form;

    public AlphanumericTableHandler(String tableName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	super(tableName, widgets, foreignKeyId, colNames, colAliases);
	FormFactory.checkTableLoadedRegistered(tableName);
    }

    public void reload(AbstractSubForm form) {
	this.form = form;
	reload();
    }

    @Override
    protected void createTableModel() throws ReadDriverException {
	AlphanumericTableModel model = TableModelFactory
		.createFromTableWithFilter(sourceTableName, destinationKey,
			originKeyValue, colNames, colAliases);
	jtable.setModel(model);
	if (form != null) {
	    form.setModel(model);
	}
    }

    @Override
    protected void createTableListener() {
	listener = new AlphanumericCompleteJTableContextualMenu(form);
    }

}
