package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;


public abstract class EditableNNRelTableHandler extends BaseNNRelTableHandler {

    public EditableNNRelTableHandler(String sourceTableName,
	    HashMap<String, JComponent> widgets, String dbSchema,
	    String originKey, String relTable, String destinationKey,
	    String[] colNames, String[] colAliases) {
	super(sourceTableName, widgets, dbSchema, originKey, relTable,
		destinationKey, colNames, colAliases);
    }

    public abstract List<String> getUnlinkedSecondaryValues();

    public abstract void insertRow(String secondaryPKValue);

    public abstract void deleteRow(String secondaryPKValue);
}