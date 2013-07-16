package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;

import javax.swing.JComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;

import es.icarto.gvsig.navtableforms.gui.tables.menu.VectorialJTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.gui.tables.model.VectorialTableModel;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class VectorialTableHandler extends BaseTableHandler {

    public VectorialTableHandler(String layerName,
	    HashMap<String, JComponent> widgets, String foreignKeyId,
	    String[] colNames, String[] colAliases) {
	super(layerName, widgets, foreignKeyId, colNames, colAliases);
	FormFactory.checkLayerLoadedRegistered(layerName);
    }

    public void reload(String layerName) {
	this.sourceTableName = layerName;
	reload();
    }

    @Override
    protected void createTableModel() throws ReadDriverException {
	VectorialTableModel model = TableModelFactory
		.createFromLayerWithFilter(sourceTableName, destinationKey,
			originKeyValue, colNames, colAliases);
	jtable.setModel(model);
    }

    @Override
    protected void createTableListener() {
	listener = new VectorialJTableContextualMenu(sourceTableName);
    }

}
