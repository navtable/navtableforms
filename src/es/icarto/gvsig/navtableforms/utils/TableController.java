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

import java.util.HashMap;
import java.util.Set;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

/**
 * Class to manage CRUD (Create, Read, Update, Delete) operations on a Table.
 * 
 * @author Andrés Maneiro <amaneiro@icarto.es>
 * 
 */
public class TableController {

    private IEditableSource model;
    private HashMap<String, Integer> indexes;
    private HashMap<String, Integer> types;
    private HashMap<String, String> values;
    private HashMap<String, String> valuesChanged;

    public TableController(IEditableSource model) {
	this.model = model;
	this.indexes = new HashMap<String, Integer>();
	this.types = new HashMap<String, Integer>();
	this.values = new HashMap<String, String>();
	this.valuesChanged = new HashMap<String, String>();
    }

    public void read(long position) throws ReadDriverException {
	SelectableDataSource sds = model.getRecordset();
	if(position != AbstractNavTable.EMPTY_REGISTER) {
	    clearAll();
	    for (int i = 0; i < sds.getFieldCount(); i++) {
		String name = sds.getFieldName(i);
		values.put(
			name,
			sds.getFieldValue(position, i).getStringValue(
				new ValueFormatNT()));
		indexes.put(name, i);
		types.put(name, sds.getFieldType(i));
	    }
	}
    }

    public void save(long position) throws ReadDriverException {
	ToggleEditing te = new ToggleEditing();
	boolean wasEditing = model.isEditing();
	if (!wasEditing) {
	    te.startEditing(model);
	}
	te.modifyValues(model, (int) position,
		this.getIndexesOfValuesChanged(),
		this.getValuesChanged().values().toArray(new String[0]));
	if (!wasEditing) {
	    te.stopEditing(model);
	}
	this.read((int) position);
    }

    public void clearAll() {
	indexes.clear();
	types.clear();
	values.clear();
	valuesChanged.clear();
    }

    public int getIndex(String fieldName) {
	return indexes.get(fieldName);
    }

    public int[] getIndexesOfValuesChanged() {
	int[] idxs = new int[valuesChanged.size()];
	Set<String> names = valuesChanged.keySet();
	int i=0;
	for(String name : names) {
	    idxs[i] = indexes.get(name);
	    i++;
	}
	return idxs;
    }

    public String getValue(String fieldName) {
	if(valuesChanged.containsKey(fieldName)) {
	    return valuesChanged.get(fieldName);
	}
	return values.get(fieldName);
    }

    public String getValueInLayer(String fieldName) {
	return values.get(fieldName);
    }

    public HashMap<String, String> getValuesOriginal() {
	return values;
    }

    public HashMap<String, String> getValuesChanged() {
	return valuesChanged;
    }

    /**
     * Make sure the value setted is a formatted value, as the ones from layer.
     * See {@link #fill(SelectableDataSource, long)} For example: if value is a
     * double in layer, the string should be something like 1000,00 instead of
     * 1000.
     */
    public void setValue(String fieldName, String value) {
	valuesChanged.put(fieldName, value);
    }

    public int getType(String fieldName) {
	return types.get(fieldName);
    }

}
