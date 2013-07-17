package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowMultipleOrFilterImplementer;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.navtableforms.utils.TOCTableManager;

public class TableModelFactory {

    public static AlphanumericTableModel createFromTable(String sourceTable,
	    String[] columnNames, String[] columnAliases) {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableByName(sourceTable).getModel()
		.getModelo();
	return new AlphanumericTableModel(model, columnNames, columnAliases);
    }

    public static AlphanumericTableModel createFromTableWithFilter(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    String[] columnNames,
	    String[] columnAliases)
		    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableByName(sourceTable).getModel()
		.getModelo();
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(
		fieldIndex, rowFilterValue);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		filter);
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableByName(sourceTable).getModel()
		.getModelo();
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		new IRowMultipleOrFilterImplementer(filters));
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
