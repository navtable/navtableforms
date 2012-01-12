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
	    String rowFilterName, String rowFilterValue) {

	IEditableSource sds = getSource(sourceTable);
	FieldDescription[] columnNames;
	columnNames = sds.getFieldsDescription();
	Object[][] rows = getRowsFromSource(sds, rowFilterName, rowFilterValue);
	return new NonEditableTableModel(rows, columnNames);
    }

    public static TableModel createFromLayer(String sourceLayer,
	    String filterName, String filterValue,
	    ArrayList<String> columnsToRetrieve) {

	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(sourceLayer);
	Object[] columnNames; // fielddescription[]
	Object[][] rows; // value[][]
	try {
	    columnNames = getColumnsFromSource(layer.getRecordset(),
		    columnsToRetrieve);
	    rows = getSomeRowsFromSource(layer.getRecordset(), filterName,
		    filterValue);
	    return new NonEditableTableModel(rows, columnNames);
	} catch (ReadDriverException e) {
	    return null;
	}
    }

    private static Object[] getColumnsFromSource(
	    SelectableDataSource recordset, ArrayList<String> columnNames) {

	ArrayList<FieldDescription> fds = new ArrayList<FieldDescription>();
	try {
	    for (FieldDescription fd : recordset.getFieldsDescription()) {
		if (columnNames.contains(fd.getFieldName())) {
		    fds.add(fd);
		}
	    }
	    return fds.toArray();
	} catch (ReadDriverException e) {
	    return null;
	}
    }

    private static Object[][] getSomeRowsFromSource(
	    SelectableDataSource source, String indexName, String filterValue) {
	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldIndex;
	try {
	    fieldIndex = source.getFieldIndexByName(indexName);
	    for (int index = 0; index < source.getRowCount(); index++) {
		Value[] row = source.getRow(index);
		String indexValue = row[fieldIndex].toString();
		if (indexValue.equalsIgnoreCase(filterValue)) {
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
	    String indexName, String filterValue) {
	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldIndex;
	try {
	    fieldIndex = source.getRecordset().getFieldIndexByName(indexName);
	    for (int index = 0; index < source.getRowCount(); index++) {
		IRowEdited row = source.getRow(index);
		String indexValue = row.getAttribute(fieldIndex).toString();
		if (indexValue.equalsIgnoreCase(filterValue)) {
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
