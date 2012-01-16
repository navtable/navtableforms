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

package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class TableUtils {

    private static final int NO_FEATURE = -1;
    private static final int NO_FIELD = -1;

    public static long getIndexOfRowSelected(SelectableDataSource source,
	    TableModel model, int rowIndex) {
	HashMap<String, String> rowSelected = getRow(model, rowIndex);
	return doGetIndexOfRow(source, rowSelected);
    }

    public static long getIndexOfRowSelected(SelectableDataSource source,
	    TableModel model, 
	    int rowIndex, 
	    int foreignKey) {
	HashMap<String, String> rowSelected = getRow(model, rowIndex, foreignKey);
	return doGetIndexOfRow(source, rowSelected);
    }

    public static ArrayList<Long> getIndexesOfAllRowsInTable(SelectableDataSource source, 
	    TableModel model) {
	ArrayList<Long> rowList = new ArrayList<Long>();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    long featIndex = getIndexOfRowSelected(source, model, rowIndex);
	    if (featIndex != NO_FEATURE) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    public static ArrayList<Long> getIndexesOfAllRowsInTable(SelectableDataSource source, 
	    TableModel model,
	    int foreignKey) {
	ArrayList<Long> rowList = new ArrayList<Long>();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    long featIndex = getIndexOfRowSelected(source, model, rowIndex, foreignKey);
	    if (featIndex != NO_FEATURE) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    public static int getColumnIndex(TableModel model, String colName) {
	for(int i=0; i<model.getColumnCount(); i++) {
	    if(colName.equals(model.getColumnName(i))) {
		return i;
	    }
	}
	return NO_FIELD;
    }

    private static HashMap<String, String> getRow(TableModel model,
	    int rowIndex,
	    int colIndex) {
	String key, value;
	HashMap<String, String> rowSelected = new HashMap<String, String>();
	value = ((Value) model.getValueAt(rowIndex, colIndex)).getStringValue(
		new ValueFormatNT());
	key = model.getColumnName(colIndex);
	rowSelected.put(key, value);
	return rowSelected;
    }

    private static HashMap<String, String> getRow(TableModel model, 
	    int rowIndex) {
	String key, value;
	HashMap<String, String> rowSelected = new HashMap<String, String>();
	for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
	    value = ((Value) model.getValueAt(rowIndex, colIndex)).getStringValue(
		    new ValueFormatNT());
	    key = model.getColumnName(colIndex);
	    rowSelected.put(key, value);
	}
	return rowSelected;
    }

    private static int doGetIndexOfRow(SelectableDataSource source,
	    HashMap<String, String> row) {
	try {
	    int rowIndex;
	    for (rowIndex = 0; rowIndex < source.getRowCount(); rowIndex++) {
		if (allColumnsMatch(source, row, rowIndex)) {
		    return rowIndex;
		}
	    }
	    return NO_FEATURE;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return NO_FEATURE;
	}
    }

    private static boolean allColumnsMatch(SelectableDataSource source,
	    HashMap<String, String> row, int rowIndex) {
	try {
	    boolean checkRow = false;
	    for (String key : row.keySet()) {
		int colIndex = source.getFieldIndexByName(key);
		/*
		 * Fields from table which are not in the source will be
		 * discarded. This is useful, for example, in the case the table
		 * loaded is not the source, but a link-table between 2 other
		 * tables. In that case, only the fields in the table which are
		 * in source will be used to compare.
		 */
		if (colIndex != NO_FIELD) {
		    String fieldValue = source.getFieldValue(rowIndex, 
			    colIndex).getStringValue(new ValueFormatNT());
		    if (fieldValue.equals(row.get(key))) {
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

}
