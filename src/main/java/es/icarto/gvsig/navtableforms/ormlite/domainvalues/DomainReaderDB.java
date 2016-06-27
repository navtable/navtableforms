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

import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.cartolab.gvsig.users.utils.DBSession;

/**
 * DomainReader which reads the values from a database.
 * 
 * With ADDVOIDVALUE we can tell it to add an additional void value before the
 * ones retrieved from DB.
 * 
 * XML syntax example:
 * 
 * <DOMAINREADER> <DRTYPE>db</DRTYPE> <DRDBTABLE>table</DRDBTABLE>
 * <DRDBCOLUMNALIAS>alias</DRDBCOLUMNALIAS>
 * <DRDBCOLUMNVALUE>value</DRDBCOLUMNVALUE> <DRDBFOREIGNKEY>key</DRDBFOREIGNKEY>
 * <DRADDVOIDVALUE>boolean</DRADDVOIDVALUE> </DOMAINREADER>
 * 
 * @author Andrés Maneiro <amaneiro@icarto.es>
 * @author Jorge López <jlopez@cartolab.es>
 * 
 */
public class DomainReaderDB implements DomainReader {

	
	private static final Logger logger = LoggerFactory
			.getLogger(DomainReaderDB.class);
    private String table = null;
    private String schema = null;
    private String columnAlias = null;
    private String columnValue = null;
    private boolean addVoidValue = false;
    private ArrayList<String> columnForeignKey = new ArrayList<String>();

    public DomainReaderDB() {
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public void setTable(String name) {
	this.table = name;
    }

    public void setColumnAlias(String name) {
	this.columnAlias = name;
    }

    public void setColumnValue(String name) {
	this.columnValue = name;
    }

    public void addColumnForeignKey(String name) {
	this.columnForeignKey.add(name);
    }

    public void setAddVoidValue(boolean addVoidValue) {
	this.addVoidValue = addVoidValue;
    }

    private String[] convertArrayToString(ArrayList<String> array) {
	String[] strings = new String[array.size()];
	for (int i = 0; i < array.size(); i++) {
	    strings[i] = array.get(i);
	}
	return strings;
    }

    private String[] getFieldColumns() {
	if (columnAlias == null) {
	    columnAlias = columnValue;
	}
	if (columnForeignKey == null) {
	    columnForeignKey.add(columnValue);
	}
	ArrayList<String> cols = new ArrayList<String>();
	cols.add(columnValue);
	cols.add(columnAlias);
	for (String fk : columnForeignKey) {
	    cols.add(fk);
	}
	return convertArrayToString(cols);
    }

    public DomainValues getDomainValues() {
	if (table != null && columnValue != null) {
	    ArrayList<KeyValue> list = new ArrayList<KeyValue>();
	    DBSession ds = DBSession.getCurrentSession();

 	    try {
		String[][] values = ds.getTable(table, schema,
			getFieldColumns(), "", new String[] {columnAlias}, false);
		// ds.getDistinctValues(table, columns[0]);
		for (int i = 0; i < values.length; i++) {
		    KeyValue kv = new KeyValue();
		    kv.setKey(values[i][0]);
		    kv.setValue(values[i][1]);
		    for (int fkIndex = 0; fkIndex < columnForeignKey.size(); fkIndex++) {
			kv.addForeignKey(values[i][2 + fkIndex]);
		    }
		    list.add(kv);
		}
	    } catch (SQLException e) {
	    	logger.error(e.getMessage(), e);
		return null;
	    } catch (NullPointerException e) {
	    	logger.error(e.getMessage(), e);
		return null;
	    }
	    return new DomainValues(list, addVoidValue);
	}
	return null;
    }
}
