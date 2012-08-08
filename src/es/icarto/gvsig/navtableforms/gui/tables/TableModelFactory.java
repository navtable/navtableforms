package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.ArrayList;

import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class TableModelFactory {

    public static TableModel createFromTable(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue) {

	IEditableSource sds = getSource(sourceTable);
	FieldDescription[] columnNames = sds.getFieldsDescription();
	Object[][] rows = getRowsFromSource(sds, rowFilterName, rowFilterValue);
	return new NonEditableTableModel(rows, columnNames);
    }

    public static TableModel createFromTable(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    ArrayList<String> columnNames,
	    ArrayList<String> columnAliases) {

	IEditableSource source = getSource(sourceTable);
	Object[][] rows = getRowsFromSource(source,
		rowFilterName, rowFilterValue, columnNames);
	return new NonEditableTableModel(rows,
		columnAliases.toArray(new String[1]));
    }

    public static TableModel createFromLayer(String sourceLayer,
	    String rowFilterName,
	    String rowFilterValue,
	    ArrayList<String> columnNames,
	    ArrayList<String> columnAliases) {

	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(sourceLayer);
	try {
	    Object[][] rows = getRowsFromSource(layer.getRecordset(),
		    rowFilterName,
		    rowFilterValue);
	    return new NonEditableTableModel(rows,
		    columnAliases.toArray(new String[1]));
	} catch (ReadDriverException e) {
	    return null;
	}
    }

    private static Object[][] getRowsFromSource(IEditableSource source,
	    String fieldFilterName,
	    String fieldFilterValue,
	    ArrayList<String> columnNames) {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldFilterIndex;
	try {
	    fieldFilterIndex = source.getRecordset().getFieldIndexByName(fieldFilterName);
	    ArrayList<Integer> columnIndexes = getIndexesOfColumns(
		    source.getRecordset(), columnNames);
	    ArrayList<Value> attributes = new ArrayList<Value>();
	    for (int index = 0; index < source.getRowCount(); index++) {
		IRowEdited row = source.getRow(index);
		String value = row.getAttribute(fieldFilterIndex).toString();
		if (value.equalsIgnoreCase(fieldFilterValue)) {
		    attributes.clear();
		    for(Integer idx : columnIndexes) {
			attributes.add(row.getAttribute(idx));
		    }
		    rows.add(attributes.toArray());
		}
	    }
	    return rows.toArray(new Object[1][1]);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private static ArrayList<Integer> getIndexesOfColumns(
	    SelectableDataSource sds,
	    ArrayList<String> columnNames) {

	ArrayList<Integer> indexes = new ArrayList<Integer>();
	for (int i=0; i<columnNames.size(); i++) {
	    int idx;
	    try {
		idx = sds.getFieldIndexByName(columnNames.get(i));
		indexes.add(new Integer(idx));
	    } catch (ReadDriverException e) {
		e.printStackTrace();
	    }
	}
	return indexes;
    }

    private static Object[][] getRowsFromSource(SelectableDataSource source,
	    String rowFilterName,
	    String rowFilterValue) {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldIndex;
	try {
	    fieldIndex = source.getFieldIndexByName(rowFilterName);
	    for (int index = 0; index < source.getRowCount(); index++) {
		Value[] row = source.getRow(index);
		String indexValue = row[fieldIndex].toString();
		if (indexValue.equalsIgnoreCase(rowFilterValue)) {
		    rows.add(row);
		}
	    }
	    return rows.toArray(new Object[1][1]);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private static Object[][] getRowsFromSource(IEditableSource source,
	    String fieldFilterName,
	    String fieldFilterValue) {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldFilterIndex;
	try {
	    fieldFilterIndex = source.getRecordset().getFieldIndexByName(fieldFilterName);
	    for (int index = 0; index < source.getRowCount(); index++) {
		IRowEdited row = source.getRow(index);
		String value = row.getAttribute(fieldFilterIndex).toString();
		if (value.equalsIgnoreCase(fieldFilterValue)) {
		    rows.add(row.getAttributes());
		}
	    }
	    return rows.toArray(new Object[1][1]);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private static IEditableSource getSource(String sourceTable) {
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if ((w instanceof Table)
		    && (((Table) w).getModel().getName()
			    .equalsIgnoreCase(sourceTable))) {
		return ((Table) w).getModel().getModelo();
	    }
	}
	return null;
    }

}
