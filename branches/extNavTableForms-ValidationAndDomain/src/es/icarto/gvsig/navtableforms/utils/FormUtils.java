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

import javax.swing.JTable;

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
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.navtableforms.launcher.NonEditableTableModel;

public class FormUtils {

    public static void fillJTableFromAlphanumericTable(JTable component,
	    String sourceTable, String filterName, String filterValue) {
	IEditableSource sds = getSource(sourceTable);
	FieldDescription[] columnNames;
	columnNames = sds.getFieldsDescription();
	Object[][] rows = getRowsFromSource(sds, filterName, filterValue);
	component.setModel(new NonEditableTableModel(rows, columnNames));
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

    public static void fillJTableFromGeomTable(JTable component, String source,
	    String filterName, String filterValue,
	    ArrayList<String> columnsToRetrieve) {
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	FLyrVect layer = null;
	for (IWindow w : windows) {
	    if (w instanceof View) {
		layer = Utils.getFlyrVect((BaseView) w, source);
		break;
	    }
	}
	Object[] columnNames; // fielddescription[]
	Object[][] rows; // value[][]
	try {
	    columnNames = getColumnsFromSource(layer.getRecordset(),
		    columnsToRetrieve);
	    rows = getSomeRowsFromSource(layer.getRecordset(), filterName,
		    filterValue);
	    component.setModel(new NonEditableTableModel(rows, columnNames));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
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

}
