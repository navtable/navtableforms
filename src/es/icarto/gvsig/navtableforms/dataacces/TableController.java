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

package es.icarto.gvsig.navtableforms.dataacces;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Set;

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileWriteException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultRow;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.drivers.ITableDefinition;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IWriteable;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;
import es.udc.cartolab.gvsig.navtable.format.ValueFactoryNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

/**
 * Class to manage CRUD (Create, Read, Update, Delete) operations on a Table.
 * 
 * @author Andrés Maneiro <amaneiro@icarto.es>
 * 
 */
public class TableController {

    public static int NO_ROW = -1;

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

    private void initMetadata() {
	try {
	    SelectableDataSource sds = model.getRecordset();
	    for (int i = 0; i < sds.getFieldCount(); i++) {
		String name = sds.getFieldName(i);
		indexes.put(name, i);
		types.put(name, sds.getFieldType(i));
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    clearAll();
	}
    }

    public long create(HashMap<String, String> newValues)
	    throws ExpansionFileWriteException,
	    ReadDriverException, ParseException {

	initMetadata();
	Value[] vals = createValuesFromHashMap(newValues);

	ToggleEditing te = new ToggleEditing();
	if (!model.isEditing()) {
	    te.startEditing(model);
	}
	long newPosition = NO_ROW;
	if (model instanceof IWriteable) {
	    IRow row = new DefaultRow(vals);
	    newPosition = model.doAddRow(row, EditionEvent.ALPHANUMERIC);
	}
	te.stopEditing(model);
	read(newPosition);
	return newPosition;
    }

    private Value[] createValuesFromHashMap(HashMap<String, String> newValues) {
	Value[] vals = new Value[indexes.size()];
	for (int i = 0; i < indexes.size(); i++) {
	    vals[i] = ValueFactoryNT.createNullValue();
	}
	for (String key : newValues.keySet()) {
	    try {
		vals[getIndex(key)] = ValueFactoryNT.createValueByType(
			newValues.get(key), types.get(key));
	    } catch (ParseException e) {
		vals[getIndex(key)] = ValueFactoryNT.createNullValue();
	    }
	}
	return vals;
    }

    public void read(long position) throws ReadDriverException {
	SelectableDataSource sds = model.getRecordset();
	clearAll();
	if (position != AbstractNavTable.EMPTY_REGISTER) {
	    for (int i = 0; i < sds.getFieldCount(); i++) {
		String name = sds.getFieldName(i);
		indexes.put(name, i);
		types.put(name, sds.getFieldType(i));
		values.put(
			name,
			sds.getFieldValue(position, i).getStringValue(
				new ValueFormatNT()));
	    }
	}
    }

    public void update(long position) throws ReadDriverException {
	ToggleEditing te = new ToggleEditing();
	boolean wasEditing = model.isEditing();
	if (!wasEditing) {
	    te.startEditing(model);
	}
	te.modifyValues(model, (int) position,
		getIndexesOfValuesChanged(),
		getValuesChanged().values().toArray(new String[0]));
	if (!wasEditing) {
	    te.stopEditing(model);
	}
	read((int) position);
    }

    public void delete(long position) throws StopWriterVisitorException,
    InitializeWriterException, StartWriterVisitorException,
    ReadDriverException {

	model.startEdition(EditionEvent.ALPHANUMERIC);

	IWriteable w = (IWriteable) model;
	IWriter writer = w.getWriter();

	ITableDefinition tableDef = model.getTableDefinition();
	writer.initialize(tableDef);

	model.doRemoveRow((int) position, EditionEvent.ALPHANUMERIC);
	model.stopEdition(writer, EditionEvent.ALPHANUMERIC);
	model.getRecordset().reload();
	clearAll();
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

    public HashMap<String, String> getValues() {
	HashMap<String, String> val = values;
	for (String k : valuesChanged.keySet()) {
	    val.put(k, valuesChanged.get(k));
	}
	return val;
    }

    public HashMap<String, String> getValuesOriginal() {
	return values;
    }

    public HashMap<String, String> getValuesChanged() {
	return valuesChanged;
    }

    /**
     * Make sure the value set is a formatted value, as the ones from layer. See
     * {@link #fill(SelectableDataSource, long)} For example: if value is a
     * double in layer, the string should be something like 1000,00 instead of
     * 1000.
     */
    public void setValue(String fieldName, String value) {
	valuesChanged.put(fieldName, value);
    }

    public int getType(String fieldName) {
	return types.get(fieldName);
    }

    public int getRowCount() throws ReadDriverException {
	return model.getRowCount();
    }
}
