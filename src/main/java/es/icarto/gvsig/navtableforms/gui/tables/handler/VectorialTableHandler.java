package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import org.gvsig.fmap.dal.exception.DataException;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.menu.VectorialUpdateJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.gui.tables.model.VectorialTableModel;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * VectorialTableHandler
 * 
 * Handler for relationships tables that link to a vectorial form.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class VectorialTableHandler extends BaseTableHandler {

    public VectorialTableHandler(String layerName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	super(layerName, widgets, foreignKeyId, colNames, colAliases);
	FormFactory.checkAndLoadLayerRegistered(layerName);
    }

    @Override
    protected void createTableModel() throws DataException {
	model = TableModelFactory
		.createFromLayerWithFilter(sourceTableName, destinationKey,
			originKeyValue, colNames, colAliases);
	jtable.setModel(model);
    }

    @Deprecated
    public void reload(AbstractForm form) {
	reload();
	((VectorialUpdateJTableContextualMenu) listener).setDialog(form);
    }

    @Override
    protected void createTableListener() {
	listener = new VectorialUpdateJTableContextualMenu(sourceTableName);
    }

}
