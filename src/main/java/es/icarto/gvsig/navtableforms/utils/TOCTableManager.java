package es.icarto.gvsig.navtableforms.utils;

import java.util.HashMap;
import java.util.List;

import org.gvsig.andami.PluginServices;
import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.app.extension.ProjectExtension;
import org.gvsig.app.project.documents.Document;
import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.app.project.documents.table.TableManager;
import org.gvsig.app.project.documents.table.gui.FeatureTableDocumentPanel;

public class TOCTableManager {

    private HashMap<String, FeatureTableDocumentPanel> tables;
    private HashMap<String, TableDocument> projectTables;

    public TOCTableManager() {
	ProjectExtension projectExt = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
	List<Document> documents = projectExt.getProject().getDocuments(TableManager.TYPENAME);
	
	projectTables = new HashMap<String, TableDocument>();
	for (Document table : documents) {
	    projectTables.put(table.getName(), ((TableDocument) table));
	}
	tables = new HashMap<String, FeatureTableDocumentPanel>();
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if (w instanceof FeatureTableDocumentPanel) {
		tables.put(((FeatureTableDocumentPanel) w).getModel().getName(), (FeatureTableDocumentPanel) w);
	    }
	}
    }

    public void closeTableByName(String tableName) {
	ProjectExtension projectExt = (ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class);
	TableDocument table = projectTables.get(tableName);
	if (table != null) {
		projectExt.getProject().removeDocument(table);
	}
    }

    public FeatureTableDocumentPanel getTableByName(String tableName) {
	return tables.get(tableName);
    }


	public TableDocument getTableDocumentByName(String sourceTable) {
		return projectTables.get(sourceTable);
	}

}
