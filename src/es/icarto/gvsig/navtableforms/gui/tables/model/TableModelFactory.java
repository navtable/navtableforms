package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

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
	IEditableSource model = toc.getTableModelByName(sourceTable);
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
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
		    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(
		fieldIndex, rowFilterValue);
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		i18nManager, filter);
    }

    public static AlphanumericTableModel createFromTableWithFilter(String sourceTable,
	    String rowFilterName, String rowFilterValue, String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {
	return createFromTableWithFilter(sourceTable, rowFilterName, rowFilterValue,
		columnNames, columnAliases, new I18nResource[0]);
    }

    public static AlphanumericTableModel createFromTableWithNotFilter(
	    String sourceTable, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		i18nManager, filter);
    }

    public static AlphanumericTableModel createFromTableWithNotFilter(
	    String sourceTable, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {
	return createFromTableWithNotFilter(sourceTable, rowFilterName, rowFilterValue,
		columnNames, columnAliases, new I18nResource[0]);
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases, I18nResource[] i18nResources)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	I18nResourceManager i18nManager = new I18nResourceManager(i18nResources);
	i18nManager.addResource(defaultI18n);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		i18nManager, new IRowMultipleOrFilterImplementer(filters));
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {
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
	    throws ReadDriverException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	int fieldIndex = layer.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(fieldIndex,
		rowFilterValue);
	return new VectorialTableModel(layer, columnNames, columnAliases,
		filter);
    }

    public static VectorialTableModel createFromLayerWithNotFilter(
	    String layerName, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	int fieldIndex = layer.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	return new VectorialTableModel(layer, columnNames, columnAliases,
		filter);
    }

    public static VectorialTableModel createFromLayerWithOrFilter(
	    String layerName, String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	int fieldIndex = layer.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new VectorialTableModel(layer, columnNames, columnAliases,
		new IRowMultipleOrFilterImplementer(filters));
    }

}
