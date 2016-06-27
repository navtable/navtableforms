package es.icarto.gvsig.navtableforms.utils;

import javax.swing.JInternalFrame;

import org.gvsig.andami.PluginServices;
import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.app.ApplicationLocator;
import org.gvsig.app.extension.ProjectExtension;
import org.gvsig.app.project.ProjectManager;
import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.app.project.documents.table.TableManager;
import org.gvsig.app.project.documents.table.gui.FeatureTableDocumentPanel;
import org.gvsig.app.project.documents.view.gui.AbstractViewPanel;
import org.gvsig.app.project.documents.view.gui.IView;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.cartolab.gvsig.users.utils.DBSession;

/**
 * DBConnectionBaseFormFactory
 * 
 * This abstract class provides some code that can be used by other factories
 * which load layers and tables through extDBConnection.
 * 
 * @author Jorge L�pez Fern�ndez <jlopez@cartolab.es>
 */

public abstract class DBConnectionBaseFormFactory extends FormFactory {

	
	private static final Logger logger = LoggerFactory
			.getLogger(DBConnectionBaseFormFactory.class);
    @Override
    public boolean checkLayerLoaded(String layerName) {
	return (new TOCLayerManager().getLayerByName(layerName) != null);
    }

    @Override
    public boolean checkTableLoaded(String tableName) {
	return (new TOCTableManager().getTableDocumentByName(tableName) != null);
    }

    protected void loadLayer(String layerName, String dbSchema) {
	IWindow[] windows = PluginServices.getMDIManager().getOrderedWindows();
	IView view = null;
	for (IWindow w : windows) {
	    if (w instanceof IView) {
		view = (IView) w;
		break;
	    }
	}
	try {
	    view.getMapControl()
		    .getMapContext()
		    .getLayers()
		    .addLayer(
			    DBSession.getCurrentSession().getLayer(layerName,
				    layerName, dbSchema, null,
				    view.getMapControl().getProjection()));
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	}
    }

    protected void loadTable(String tableName, String dbSchema) {
	IWindow[] ws = PluginServices.getMDIManager().getAllWindows();
	AbstractViewPanel baseView = null;
	for (IWindow w : ws) {
	    if (w instanceof AbstractViewPanel) {
		baseView = (AbstractViewPanel) w;
	    }
	}
	DBSession session = DBSession.getCurrentSession();

	String completeTableName = session.getCompleteTableName(tableName,
		dbSchema);

	LayerFactory.getDataSourceFactory().addDBDataSourceByTable(tableName,
		session.getServer(), session.getPort(), session.getUserName(),
		session.getPassword(), session.getDatabase(),
		completeTableName, session.getAlphanumericDriverName());

	try {
	    DataSource dataSource;
	    dataSource = LayerFactory.getDataSourceFactory()
		    .createRandomDataSource(tableName,
			    DataSourceFactory.AUTOMATIC_OPENING);
	    SelectableDataSource sds = new SelectableDataSource(dataSource);
	    EditableAdapter auxea = new EditableAdapter();

	    auxea.setOriginalDataSource(sds);

	    ProjectTable projectTable = ProjectFactory.createTable(tableName,
		    auxea);
	    Table t = new Table();
	    t.setModel(projectTable);

	    PluginServices.getMDIManager().addWindow(t);
	    JInternalFrame frame = (JInternalFrame) t.getRootPane().getParent();

	    ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
	    
	    ext.getProject().addDocument(projectTable);
	    frame.toBack();
	    frame.setSelected(false);
	    if (baseView != null) {
		JInternalFrame frameBaseView = (JInternalFrame) baseView.getRootPane().getParent();
		frameBaseView.setSelected(true);
	    }
	} catch (Exception e) {
	   logger.error(e.getMessage(), e);
	}
    }

    public void creartabla() {
    	DataManager dataManager = DALLocator.getDataManager();
    	ProjectManager projectManager = ProjectManager.getInstance();
    	FeatureStore fs = (FeatureStore) dataManager.openStore(dataStoreParameters.getDataStoreName(), dataStoreParameters);
        TableDocument tableDocument = (TableDocument) projectManager.createDocument(TableManager.TYPENAME, "NOMBRE_DE_LA_TABLA");
        tableDocument.setStore(fs);
        projectManager.getCurrentProject().addDocument(tableDocument);
    }
    
    public void creartabla2() {
    		ProjectManager projectManager = ProjectManager.getInstance();
			TableManager tableManager = getTableManager();
			FLyrVect co = null;
			TableDocument tableDocument = tableManager.getTableDocument(co);
			
			FeatureStore fs = co.getFeatureStore();

			if (tableDocument == null) {
				tableDocument = (TableDocument) projectManager.createDocument( TableManager.TYPENAME, "NOMBRE_DE_LA_TABLA");
				tableDocument.setStore(fs);
				tableDocument.setAssociatedLayer(co);
				// co.getParentLayer().addLayerCollectionListener(this);
				projectManager.getCurrentProject().addDocument(tableDocument);
			}

			FeatureTableDocumentPanel featureTableDocumentPanel = (FeatureTableDocumentPanel) tableDocument
					.getFactory().getMainWindow(tableDocument);

			featureTableDocumentPanel.getModel().setModified(true);
			PluginServices.getMDIManager().addWindow(
					featureTableDocumentPanel);
		}
	}
}
}



private TableManager getTableManager() {
TableManager tableManager =
    (TableManager) getProjectManager().getDocumentManager(
        TableManager.TYPENAME);
return tableManager;
}
}
