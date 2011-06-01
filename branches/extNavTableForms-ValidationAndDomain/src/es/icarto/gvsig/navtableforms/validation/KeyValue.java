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

package es.icarto.gvsig.navtableforms.validation;

import java.util.ArrayList;

public class KeyValue {

    private String key;
    private String value;
    private ArrayList<String> foreignKeys = new ArrayList<String>();

    public KeyValue() {
    }

    public KeyValue(String key, String value, String fk) {
	this.key = key;
	this.value = value;
	this.foreignKeys.add(fk);
    }

    public KeyValue(String key, String value, ArrayList<String> fk) {
	this.key = key;
	this.value = value;
	this.foreignKeys = fk;
    }

    KeyValue(String key, String value) {
	this.key = key;
	this.value = value;
    }

    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public void addForeignKey(String d) {
	this.foreignKeys.add(d);
    }

    public ArrayList<String> getForeignKeys() {
	return this.foreignKeys;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String toString() {
	return this.value;
    }

}