package es.icarto.gvsig.navtableforms.utils;


import org.cresques.cts.IProjection;
import org.gvsig.andami.PluginServices;
import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.app.project.ProjectManager;
import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.app.project.documents.table.TableManager;
import org.gvsig.app.project.documents.table.gui.FeatureTableDocumentPanel;
import org.gvsig.app.project.documents.view.gui.IView;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.commons.gvsig2.ConnectionWithParams;
import es.udc.cartolab.gvsig.users.utils.DBSession;

/**
 * DBConnectionBaseFormFactory
 * 
 * This abstract class provides some code that can be used by other factories
 * which load layers and tables through extDBConnection.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
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
    	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
    	if (iWindow instanceof IView) {
    		MapContext mc = ((IView) iWindow).getMapControl().getMapContext();
    		IProjection projection = mc.getProjection();
			try {
				FLayer layer = DBSession.getCurrentSession().getLayer(layerName, layerName, dbSchema, null, projection);
				mc.getLayers().addLayer(layer);
			} catch (BaseException e) {
				logger.error(e.getMessage(), e);
			}
    	}
    }

    protected void loadTable(String tableName, String dbSchema) {
    	try {
			loadTableDocument(tableName, dbSchema);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
    }

    public TableDocument loadTableDocument(String tableName, String dbSchema) throws BaseException {
    	ConnectionWithParams cwp = DBSession.getCurrentSession().getConnectionWithParams();
    	JDBCStoreParameters storeParams = cwp.getStoreParams();
    	storeParams.setTable(tableName);
    	storeParams.setSchema(dbSchema);
    	
    	DataManager dataManager = DALLocator.getDataManager();
    	
    	ProjectManager projectManager = ProjectManager.getInstance();
    	FeatureStore fs = (FeatureStore) dataManager.openStore(storeParams.getDataStoreName(), storeParams);
        TableDocument tableDocument = (TableDocument) projectManager.createDocument(TableManager.TYPENAME, tableName);
        tableDocument.setStore(fs);
        projectManager.getCurrentProject().addDocument(tableDocument);
        return tableDocument;
    }
    


public TableDocument addAssociatedTable(FLyrVect lyr) {
	TableDocument tableDocument = getAssociatedTable(lyr);
	if (tableDocument != null) {
		return tableDocument;
	}
	FeatureStore fs = lyr.getFeatureStore();
	ProjectManager projectManager = ProjectManager.getInstance();
	tableDocument = (TableDocument) projectManager.createDocument( TableManager.TYPENAME, "table:" + lyr.getName());
	tableDocument.setStore(fs);
	tableDocument.setAssociatedLayer(lyr);
	projectManager.getCurrentProject().addDocument(tableDocument);
	
	FeatureTableDocumentPanel featureTableDocumentPanel = (FeatureTableDocumentPanel) tableDocument.getFactory().getMainWindow(tableDocument);
	PluginServices.getMDIManager().addWindow(featureTableDocumentPanel);
	
	
	return tableDocument; 
}

public TableDocument getAssociatedTable(FLyrVect lyr) {
	ProjectManager projectManager = ProjectManager.getInstance();
	TableManager tableManager = (TableManager) projectManager.getDocumentManager(TableManager.TYPENAME);
	return tableManager.getTableDocument(lyr);
}

}
