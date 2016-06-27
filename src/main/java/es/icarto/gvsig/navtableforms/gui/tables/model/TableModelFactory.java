package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import es.icarto.gvsig.commons.gvsig2.IEditableSource;
import es.icarto.gvsig.commons.gvsig2.SelectableDataSource;
import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.GvSIGI18nResource;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowMultipleOrFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowNotFilterImplementer;
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


    /**
     * The default i18n resource for the translations that requests them
     * to gvSIG.
     */
    private static final I18nResource defaultI18n = new GvSIGI18nResource();

    public static AlphanumericTableModel createFromTable(String sourceTable,
	    String[] columnNames, String[] columnAliases, I18nResource[] i18nResources) {

	TOCTableManager toc = new TOCTableManager();
	TableDocument tableDocument = toc.getTableDocumentByName(sourceTable);
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(tableDocument, columnNames, columnAliases,
		i18nManager);
    }

    public static AlphanumericTableModel createFromTable(String sourceTable,
	    String[] columnNames, String[] columnAliases) {
	return createFromTable(sourceTable, columnNames, columnAliases,
		new I18nResource[0]);
    }

    public static AlphanumericTableModel createFromTableWithFilter(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    String[] columnNames,
	    String[] columnAliases, I18nResource[] i18nResources)
		    throws DataException {

	TOCTableManager toc = new TOCTableManager();
	TableDocument tableDocument = toc.getTableDocumentByName(sourceTable);
	IEditableSource model = new SelectableDataSource(tableDocument.getStore());
	int fieldIndex = model.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(
		fieldIndex, rowFilterValue);
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	
	return new AlphanumericTableModel(tableDocument, columnNames, columnAliases,
		i18nManager, filter);
    }

    public static AlphanumericTableModel createFromTableWithFilter(String sourceTable,
	    String rowFilterName, String rowFilterValue, String[] columnNames, String[] columnAliases)
	    throws DataException {
	return createFromTableWithFilter(sourceTable, rowFilterName, rowFilterValue,
		columnNames, columnAliases, new I18nResource[0]);
    }

    public static AlphanumericTableModel createFromTableWithNotFilter(
	    String sourceTable, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
	    throws DataException {

	TOCTableManager toc = new TOCTableManager();
	TableDocument tableDocument = toc.getTableDocumentByName(sourceTable);
	IEditableSource model = new SelectableDataSource(tableDocument.getStore());
	int fieldIndex = model.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(tableDocument, columnNames, columnAliases,
		i18nManager, filter);
    }

    public static AlphanumericTableModel createFromTableWithNotFilter(
	    String sourceTable, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws DataException {
	return createFromTableWithNotFilter(sourceTable, rowFilterName, rowFilterValue,
		columnNames, columnAliases, new I18nResource[0]);
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
	    throws DataException {

	TOCTableManager toc = new TOCTableManager();
	TableDocument tableDocument = toc.getTableDocumentByName(sourceTable);
	IEditableSource model = new SelectableDataSource(tableDocument.getStore());
	int fieldIndex = model.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(tableDocument, columnNames, columnAliases,
		i18nManager, new IRowMultipleOrFilterImplementer(filters));
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws DataException {
	return createFromTableWithOrFilter(sourceTable, rowFilterName, rowFilterValues,
		columnNames, columnAliases, new I18nResource[0]);
    }

    public static VectorialTableModel createFromLayer(String layerName,
	    String[] columnNames, String[] columnAliases) {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	return new VectorialTableModel(layer, columnNames, columnAliases);
    }

    public static VectorialTableModel createFromLayerWithFilter(
	    String layerName, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws DataException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	FeatureType type = layer.getFeatureStore().getDefaultFeatureType();
	int fieldIndex = type.getIndex(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(fieldIndex,
		rowFilterValue);
	return new VectorialTableModel(layer, columnNames, columnAliases,
		filter);
    }

    public static VectorialTableModel createFromLayerWithNotFilter(
	    String layerName, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws DataException {
    	
	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	FeatureType type = layer.getFeatureStore().getDefaultFeatureType();
	int fieldIndex = type.getIndex(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	return new VectorialTableModel(layer, columnNames, columnAliases,
		filter);
    }

    public static VectorialTableModel createFromLayerWithOrFilter(
	    String layerName, String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws DataException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	FeatureType type = layer.getFeatureStore().getDefaultFeatureType();
	int fieldIndex = type.getIndex(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new VectorialTableModel(layer, columnNames, columnAliases,
		new IRowMultipleOrFilterImplementer(filters));
    }

}
