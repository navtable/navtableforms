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

package es.icarto.gvsig.navtableforms.ormlite.domainvalues;
import java.util.ArrayList;
import java.util.List;

public class DomainValues {

    private ArrayList<KeyValue> data;
    private boolean addVoidValue = false;

    public DomainValues(ArrayList<KeyValue> values) {
	this.data = values;
    }

    public DomainValues(ArrayList<KeyValue> values, boolean addVoidValue) {
	this.data = values;
	this.addVoidValue = addVoidValue;
    }

    public ArrayList<KeyValue> getValues() {
	ArrayList<KeyValue> subset = new ArrayList<KeyValue>();
	if (addVoidValue) {
	    subset.add(new KeyValue("", " "));
	}
	subset.addAll(data);
	return subset;
    }

    public ArrayList<KeyValue> getValuesFilteredBy(List<String> ids) {
	ArrayList<KeyValue> subset = new ArrayList<KeyValue>();
	if (addVoidValue) {
	    subset.add(new KeyValue("", " "));
	}
	for (KeyValue kv : data) {
	    if (kv.getForeignKeys().equals(ids)) {
		subset.add(kv);
	    }
	}
	return subset;
    }

    public void addValue(KeyValue value) {
	data.add(value);
    }

    public void addValues(ArrayList<KeyValue> values) {
	values.addAll(values);
    }
}
