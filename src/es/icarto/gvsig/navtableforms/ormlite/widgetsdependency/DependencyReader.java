/*
 * Copyright (c) 2013. iCarto
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

package es.icarto.gvsig.navtableforms.ormlite.widgetsdependency;

/**
 * DependencyReader which saves information about the widgets dependency.
 * 
 * XML syntax example:
 * 
 * <ENABLEIF>
 *	<TYPE>checkbox | combobox</TYPE>
 *	<COMPONENT>componentName</COMPONENT>
 *	<VALUE>value</VALUE>
 * </ENABLEIF>
 * 
 * @author Pablo Sanxiao <psanxiao@icarto.es>
 * 
 */
public class DependencyReader {

    private String type = null;
    private String component = null;
    private String value = null;
    
    public DependencyReader() {
	
    }
    
    public void setType(String type) {
	this.type = type;
    }
    
    public String getType() {
	return this.type;
    }
    
    public void setComponent(String component) {
	this.component = component;
    }
    
    public String getComponent() {
 	return this.component;
     }
    
    public void setValue(String value) {
	this.value = value;
    }
    
    public String getValue() {
 	return this.value;
     }
}
