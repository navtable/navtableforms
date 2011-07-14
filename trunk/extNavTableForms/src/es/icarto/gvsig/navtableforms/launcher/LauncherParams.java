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

package es.icarto.gvsig.navtableforms.launcher;

public class LauncherParams {

    private ILauncherForm form;
    private String tableName;
    private String antTitle;

    public LauncherParams(ILauncherForm form, String tableName, String antTitle) {
	this.tableName = tableName;
	this.antTitle = antTitle;
	this.form = form;
    }

    public String getTableName() {
	return this.tableName;
    }

    public String getAlphanumericNavTableTitle() {
	return this.antTitle;
    }

    /**
     * Get SQLQuery from form. This way, dinamic values as the ones from widgets
     * could be retrieved in real time. The ID used to get the sql query is the
     * name of alphanumericnavtable, which allows the form to have several sql
     * queries.
     * 
     * @return a SQL query from the form. The keyID will be the
     *         alphanumericnavtable name
     */
    public String getSQLQuery() {
	return form.getSQLQuery(antTitle);
    }
}
