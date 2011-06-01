/*
 * Copyright (c) 2011 iCarto
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

package es.icarto.gvsig.navtableforms.ormlite;

import java.util.HashMap;

/**
 * Class storing the metadata of the DataBase to work on.
 * 
 * @author Andrés Maneiro <amaneiro@cartolab.es>
 */
public class ORMLiteDataBase {

    private HashMap<String, ORMLiteTable> tables = null;

    ORMLiteDataBase() {
	tables = new HashMap<String, ORMLiteTable>();
    }

    public void addTable(String key, ORMLiteTable ft) {
	this.tables.put(key, ft);
    }

    public HashMap<String, ORMLiteTable> getTableList() {
	return this.tables;
    }

    public int getTableListCount() {
	return tables.size();
    }

    /**
     * @param key
     *            . The same as the tableAlias.
     * @return
     */
    public ORMLiteTable getTable(String key) {
	return tables.get(key);
    }

    public ORMLiteTable getTableByName(String tableName) {
	for (ORMLiteTable ft : tables.values()) {
	    if (tableName.equals(ft.getTableName())) {
		return ft;
	    }
	}
	return null;
    }

    public class ORMLiteTable {

	private String tableName = "";
	private String tableAlias = "";
	private String[] primaryKey = null;

	public String getTableName() {
	    return this.tableName;
	}

	public void setTableName(String name) {
	    this.tableName = name;
	}

	/**
	 * Alias for this table. The same than the key from XML.
	 * 
	 * @return the table alias
	 */
	public String getTableAlias() {
	    return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
	    this.tableAlias = tableAlias;
	}

	public void setPrimaryKey(String[] pk) {
	    this.primaryKey = pk;
	}

	public String[] getPrimaryKey() {
	    return this.primaryKey;
	}
    }
}
