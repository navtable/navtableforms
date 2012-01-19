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

import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class TableUtils {

    private static final int NO_ROW = -1;
    private static final int NO_COLUMN = -1;

    /**
     * Compare the values in the row selected in JTable with the values
     * in SelectableDataSource and returns the index of feature.
     * 
     * @return feature index in SelectableDataSource or 
     * AbstractNavTable.EMPTY_REGISTER if there is none.
     */
    public static long getFeatureIndexFromJTable(JTable table, 
	    SelectableDataSource sds) {	
	int rowIndex = table.getSelectedRow();
	return getFeatureIndexFromJTable(table, sds, rowIndex);
    }

    /**
     * Compare the values in row at rowIndex in JTable with the values
     * in SelectableDataSource and returns the index of feature.
     * 
     * @return feature index in SelectableDataSource or 
     * AbstractNavTable.EMPTY_REGISTER if there is none.
     */
    public static long getFeatureIndexFromJTable(JTable table, 
	    SelectableDataSource sds,
	    int rowIndex) {	
	if((sds != null)
		&& (rowIndex != NO_ROW)) {
	    HashMap<String, String> rowSelected = getRow(table.getModel(), rowIndex);
	    return doGetIndexOfRow(sds, rowSelected);
	}
	return NO_ROW;
    }

    /**
     * Compare the values in the row selected in JTable with the values
     * in SelectableDataSource and returns the index of feature. In this case,
     * only 1 cell will be compared: the one specified in columnName.
     *  
     * @return feature index in SelectableDataSource or 
     * AbstractNavTable.EMPTY_REGISTER if there is none.
     */
    public static long getFeatureIndexFromJTable(JTable table, 
	    SelectableDataSource sds, 
	    String columnName) {
	int rowIndex = table.getSelectedRow();
	return getFeatureIndexFromJTable(table, sds, columnName, rowIndex);
    }

    /**
     * Compare the values in the row at rowIndex in JTable with the values
     * in SelectableDataSource and returns the index of feature. In this case,
     * only 1 cell will be compared: the one specified in columnName.
     *  
     * @return feature index in SelectableDataSource or 
     * AbstractNavTable.EMPTY_REGISTER if there is none.
     */
    public static long getFeatureIndexFromJTable(JTable table, 
	    SelectableDataSource sds, 
	    String columnName,
	    int rowIndex) {
	int colIndex = getColumnIndex(table, columnName);
	if ((sds != null)
		&& (rowIndex != NO_ROW)
		&& (colIndex != NO_COLUMN)) {
	    HashMap<String, String> rowSelected = getCell(table.getModel(), rowIndex, colIndex);
	    return doGetIndexOfRow(sds, rowSelected);
	}
	return NO_ROW;
    }

    /**
     * Return the indexes of the feature present in JTable.
     *  
     * @return feature indexes in SelectableDataSource.
     */
    public static ArrayList<Long> getFeatureIndexesFromJTable(JTable table,
	    SelectableDataSource sds) {
	ArrayList<Long> rowList = new ArrayList<Long>();
	TableModel model = table.getModel();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    long featIndex = getFeatureIndexFromJTable(table, sds);
	    if (featIndex != NO_ROW) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    /**
     * Return the indexes of the feature present in JTable. Will compare only
     * the cell specified in columnName
     *  
     * @return feature indexes in SelectableDataSource.
     */
    public static ArrayList<Long> getFeatureIndexesFromJTable(JTable table,
	    SelectableDataSource sds, 
	    String columnName) {
	ArrayList<Long> rowList = new ArrayList<Long>();
	TableModel model = table.getModel();
	for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
	    long featIndex = getFeatureIndexFromJTable(table, sds, columnName);
	    if (featIndex != NO_ROW) {
		rowList.add(featIndex);
	    }
	}
	return rowList;
    }

    public static int getColumnIndex(JTable table, String columnName) {
	TableModel model = table.getModel();
	for(int i=0; i<model.getColumnCount(); i++) {
	    if(columnName.equals(model.getColumnName(i))) {
		return i;
	    }
	}
	return NO_COLUMN;
    }

    public static boolean hasRows(JTable table) {
	TableModel model = table.getModel();
	if ((model.getRowCount() > 0) && (!firstRowIsVoid(model))) {
	    return true;
	}
	return false;
    }

    private static boolean firstRowIsVoid(TableModel model) {
	boolean isVoid = true;
	for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
	    if (model.getValueAt(0, colIndex) == null) {
		isVoid = true;
	    } else {
		isVoid = false;
		break;
	    }
	}
	return isVoid;
    }

    private static HashMap<String, String> getCell(TableModel model,
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

    private static HashMap<String, String> getRow(TableModel model, int rowIndex) {
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
	    return NO_ROW;
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return NO_ROW;
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
		if (colIndex != NO_COLUMN) {
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
