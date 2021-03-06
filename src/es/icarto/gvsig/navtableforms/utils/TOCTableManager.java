package es.icarto.gvsig.navtableforms.utils;

import java.util.HashMap;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.table.ProjectTable;
import com.iver.cit.gvsig.project.documents.table.ProjectTableFactory;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

public class TOCTableManager {

    private HashMap<String, Table> tables;
    private HashMap<String, ProjectTable> projectTables;

    public TOCTableManager() {
	ProjectExtension projectExt = (ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class);
	List<ProjectDocument> tableDocs = projectExt.getProject()
		.getDocumentsByType(ProjectTableFactory.registerName);
	projectTables = new HashMap<String, ProjectTable>();
	for (ProjectDocument table : tableDocs) {
	    projectTables.put(table.getName(), ((ProjectTable) table));
	}
	tables = new HashMap<String, Table>();
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if (w instanceof Table) {
		tables.put(((Table) w).getModel().getName(), (Table) w);
	    }
	}
    }

    public void closeTableByName(String tableName) {
	ProjectExtension projectExt = (ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class);
	ProjectTable table = projectTables.get(tableName);
	if (table != null) {
	    projectExt.getProject().delDocument(table);
	}
    }

    public Table getTableByName(String tableName) {
	return tables.get(tableName);
    }

    public IEditableSource getTableModelByName(String tableName) {
	ProjectTable table = projectTables.get(tableName);
	return (table != null) ? table.getModelo() : null;
    }

}
