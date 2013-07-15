package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.JTableUpdateContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;

public class AlphanumericNotEditableNNRelTableHandler extends
	BaseNNRelTableHandler {

    private IForm form;

    public AlphanumericNotEditableNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, dbSchema, originKey, relTable,
		destinationKey, colNames, colAliases);
    }

    protected void createTableModel() throws ReadDriverException {
	AlphanumericTableModel model = TableModelFactory
		.createFromTableWithOrFilter(sourceTableName, destinationKey,
			destinationKeyValues, colNames, colAliases);
	jtable.setModel(model);
	if (form != null) {
	    form.setModel(model);
	}
    }

    protected void createTableListener() {
	listener = new JTableUpdateContextualMenu(form);
    }

    public void reload(IForm form) {
	this.form = form;
	reload();
    }

}
