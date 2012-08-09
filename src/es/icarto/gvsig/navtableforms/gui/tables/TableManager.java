package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.HashMap;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

public class TableManager {

    private HashMap<String, Table> tables;

    public TableManager() {
	tables = new HashMap<String, Table>();
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if (w instanceof Table) {
		tables.put(((Table) w).getModel().getName(), (Table) w);
	    }
	}
    }

    public Table getTableByName(String tableName) {
	return tables.get(tableName);
    }

}
