package es.icarto.gvsig.navtableforms.utils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

public class TableUtils {

    static final int NO_FEATURE = -1;

    public static long getPositionOfRowSelected(IEditableSource source,
	    TableModel model, int rowIndex) {
	HashMap<String, String> rowSelected = getRow(model, rowIndex);
	// see setPosition() at abstractnavtable
	return doGetFeatureIndexOfRow(source, rowSelected) + 1;
    }

    private static HashMap<String, String> getRow(TableModel model, int rowIndex) {
	String key, value;
	HashMap<String, String> rowSelected = new HashMap<String, String>();
	for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
	    value = model.getValueAt(rowIndex, colIndex).toString();
	    key = model.getColumnName(colIndex);
	    rowSelected.put(key, value);
	}
	return rowSelected;
    }

    private static int doGetFeatureIndexOfRow(IEditableSource source,
	    HashMap<String, String> row) {
	try {
	    int featIndex;
	    for (featIndex = 0; featIndex < source.getRowCount(); featIndex++) {
		if (isTheRow(source, row, featIndex)) {
		    return featIndex;
		}
	    }
	    return NO_FEATURE;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return NO_FEATURE;
	}
    }

    private static boolean isTheRow(IEditableSource source,
	    HashMap<String, String> row, int featIndex) {
	try {
	    boolean checkRow = false;
	    for (String key : row.keySet()) {
		int fieldIndex = source.getRecordset().getFieldIndexByName(key);
		Value fieldValue = source.getRecordset().getFieldValue(
			featIndex, fieldIndex);
		if (fieldValue.toString().equals(row.get(key))) {
		    checkRow = true;
		} else {
		    checkRow = false;
		    break;
		}
	    }
	    if (checkRow) {
		return true;
	    }
	    return false;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public static ArrayList<Integer> getIndexesOfRowsInTable(
	    IEditableSource source, TableModel model) {
	ArrayList<Integer> rowList = new ArrayList<Integer>();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    int featIndex = getFeatureIndexOfRow(source, model, rowIndex);
	    if (featIndex != NO_FEATURE) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    private static int getFeatureIndexOfRow(IEditableSource source,
	    TableModel model, int rowIndex) {
	HashMap<String, String> row = getRow(model, rowIndex);
	return doGetFeatureIndexOfRow(source, row);
    }

}
