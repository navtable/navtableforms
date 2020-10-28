package es.icarto.gvsig.navtableforms.gui.tables.model;

import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.GvSIGI18nResource;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.navtableforms.utils.TOCTableManager;

/**
 * TableModelFactory
 *
 * Factory for creating table models from layers or source tables, with or
 * without filters.
 *
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */
public class TableModelFactory {
	private static final Logger logger = LoggerFactory.getLogger(TableModelFactory.class);

	/**
	 * The default i18n resource for the translations that requests them to gvSIG.
	 */
	private static final I18nResource defaultI18n = new GvSIGI18nResource();

	public static AlphanumericTableModel createFromTable(String sourceTable, String[] columnNames,
			String[] columnAliases, I18nResource[] i18nResources) {

		try {
			return createFromTableWithFilter(sourceTable, null, null, columnNames, columnAliases);
		} catch (DataException e) {
			return null;
		}
	}

	public static AlphanumericTableModel createFromTable(String sourceTable, String[] columnNames,
			String[] columnAliases) {
		return createFromTable(sourceTable, columnNames, columnAliases, new I18nResource[0]);
	}

	public static AlphanumericTableModel createFromTableWithFilter(String sourceTable, String rowFilterName,
			String rowFilterValue, String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
			throws DataException {

		TOCTableManager toc = new TOCTableManager();
		TableDocument tableDocument = toc.getTableDocumentByName(sourceTable);

		I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
		i18nManager.addResource(defaultI18n);

		FeatureStore store = tableDocument.getFeatureStore();
		try {
			FeaturePagingHelper set = getSet(store, rowFilterName, rowFilterValue);
			return new AlphanumericTableModel(tableDocument, columnNames, columnAliases, i18nManager, set);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private static FeaturePagingHelper getSet(FeatureStore store, String rowFilterName, String rowFilterValue)
			throws BaseException {
		DataManager manager = DALLocator.getDataManager();
		FeatureQuery query = null;
		if (rowFilterName != null) {
			String fkValue = "'" + rowFilterValue.replace("'", "''") + "'";
			String where = rowFilterName + " = " + fkValue;
			query = store.createFeatureQuery(where, "", false);
		} else {
			query = store.createFeatureQuery("", "", false);
		}
		FeaturePagingHelper set = manager.createFeaturePagingHelper(store, query, 10);
		return set;
	}

	public static AlphanumericTableModel createFromTableWithFilter(String sourceTable, String rowFilterName,
			String rowFilterValue, String[] columnNames, String[] columnAliases) throws DataException {
		return createFromTableWithFilter(sourceTable, rowFilterName, rowFilterValue, columnNames, columnAliases,
				new I18nResource[0]);
	}

	public static AlphanumericTableModel createFromTableWithOrFilter(String sourceTable, String rowFilterName,
			String[] rowFilterValues, String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
			throws DataException {
		throw new RuntimeException("Not implemented!");
		// TOCTableManager toc = new TOCTableManager();
		// TableDocument tableDocument =
		// toc.getTableDocumentByName(sourceTable);
		// IEditableSource model = new SelectableDataSource(
		// tableDocument.getStore());
		// int fieldIndex = model.getFieldIndexByName(rowFilterName);
		// List<IRowFilter> filters = new ArrayList<IRowFilter>();
		// for (String rowFilterValue : rowFilterValues) {
		// filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
		// }
		// I18nResourceManager i18nManager = new
		// I18nResourceManager(i18nResources);
		// i18nManager.addResource(defaultI18n);
		// return new AlphanumericTableModel(tableDocument, columnNames,
		// columnAliases, i18nManager,
		// new IRowMultipleOrFilterImplementer(filters));
	}

	public static AlphanumericTableModel createFromTableWithOrFilter(String sourceTable, String rowFilterName,
			String[] rowFilterValues, String[] columnNames, String[] columnAliases) throws DataException {
		return createFromTableWithOrFilter(sourceTable, rowFilterName, rowFilterValues, columnNames, columnAliases,
				new I18nResource[0]);
	}

	public static VectorialTableModel createFromLayer(String layerName, String[] columnNames, String[] columnAliases) {

		FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
		FeatureStore store = layer.getFeatureStore();
		try {
			DataManager manager = DALLocator.getDataManager();
			FeaturePagingHelper set = manager.createFeaturePagingHelper(store, 10);
			return new VectorialTableModel(layer, columnNames, columnAliases, set, null);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static VectorialTableModel createFromLayerWithFilter(String layerName, String rowFilterName,
			String rowFilterValue, String[] columnNames, String[] columnAliases) throws DataException {

		FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);

		try {
			DataManager manager = DALLocator.getDataManager();
			FeatureStore store = layer.getFeatureStore();
			String fkValue = "'" + rowFilterValue.replace("'", "''") + "'";
			String where = rowFilterName + " = " + fkValue;
			FeatureQuery query = store.createFeatureQuery(where, "", false);
			FeaturePagingHelper set = manager.createFeaturePagingHelper(store, query, 10);
			return new VectorialTableModel(layer, columnNames, columnAliases, set, null);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static VectorialTableModel createFromLayerWithOrFilter(String layerName, String rowFilterName,
			String[] rowFilterValues, String[] columnNames, String[] columnAliases) throws DataException {
		throw new RuntimeException("Not implemented!");
		//
		// FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
		// FeatureType type = layer.getFeatureStore().getDefaultFeatureType();
		// int fieldIndex = type.getIndex(rowFilterName);
		// List<IRowFilter> filters = new ArrayList<IRowFilter>();
		// for (String rowFilterValue : rowFilterValues) {
		// filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
		// }
		// return new VectorialTableModel(layer, columnNames, columnAliases,
		// new IRowMultipleOrFilterImplementer(filters));
	}

}
