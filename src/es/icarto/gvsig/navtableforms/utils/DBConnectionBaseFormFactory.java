package es.icarto.gvsig.navtableforms.utils;

import javax.swing.JInternalFrame;

import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.table.ProjectTable;
import com.iver.cit.gvsig.project.documents.table.gui.Table;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

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

    @Override
    public boolean checkLayerLoaded(String layerName) {
	return (new TOCLayerManager().getLayerByName(layerName) != null);
    }

    @Override
    public boolean checkTableLoaded(String tableName) {
	return (new TOCTableManager().getTableModelByName(tableName) != null);
    }

    protected void loadLayer(String layerName, String dbSchema) {
	IWindow[] windows = PluginServices.getMDIManager().getOrderedWindows();
	BaseView view = null;
	for (IWindow w : windows) {
	    if (w instanceof BaseView) {
		view = (BaseView) w;
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
				    view.getProjection()));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    protected void loadTable(String tableName, String dbSchema) {
	IWindow[] ws = PluginServices.getMDIManager().getAllWindows();
	IWindow baseView = null;
	for (IWindow w : ws) {
	    if (w instanceof BaseView) {
		baseView = w;
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

	    ProjectExtension ext = (ProjectExtension) PluginServices
		    .getExtension(ProjectExtension.class);
	    ext.getProject().addDocument(projectTable);
	    frame.toBack();
	    frame.setSelected(false);
	    JInternalFrame frameBaseView = (JInternalFrame) ((BaseView) baseView)
		    .getRootPane().getParent();
	    frameBaseView.setSelected(true);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
