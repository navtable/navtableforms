/*
 * Copyright (c) 2011. iCarto
 *
 * This file is part of extNavTableForms
 *
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.icarto.gvsig.navtableforms.utils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

public class TableUtils {

    static final int NO_FEATURE = -1;

    public static long getPositionOfRowSelected(SelectableDataSource source,
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

    private static int doGetFeatureIndexOfRow(SelectableDataSource source,
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

    private static boolean isTheRow(SelectableDataSource source,
	    HashMap<String, String> row, int featIndex) {
	try {
	    boolean checkRow = false;
	    for (String key : row.keySet()) {
		int fieldIndex = source.getFieldIndexByName(key);
		/*
		 * Fields from table which are not in the source will be
		 * discarded. This is useful, for example, in the case the table
		 * loaded is not the source, but a link-table between 2 other
		 * tables. In that case, only the fields in the table which are
		 * in source will be used to compare.
		 */
		if (fieldIndex != -1) {
		    Value fieldValue = source.getFieldValue(featIndex,
			    fieldIndex);
		    if (fieldValue.toString().equals(row.get(key))) {
			checkRow = true;
		    } else {
			checkRow = false;
			break;
		    }
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
	    SelectableDataSource source, TableModel model) {
	ArrayList<Integer> rowList = new ArrayList<Integer>();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    int featIndex = getFeatureIndexOfRow(source, model, rowIndex);
	    if (featIndex != NO_FEATURE) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    private static int getFeatureIndexOfRow(SelectableDataSource source,
	    TableModel model, int rowIndex) {
	HashMap<String, String> row = getRow(model, rowIndex);
	return doGetFeatureIndexOfRow(source, row);
    }

}
