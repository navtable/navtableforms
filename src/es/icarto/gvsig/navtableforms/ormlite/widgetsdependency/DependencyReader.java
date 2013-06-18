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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DependencyReader which saves information about the widgets dependency.
 * 
 * Please note multiple dependencies onto the same component are considered
 * an OR, whereas dependencies among different ones are always evaluated with
 * an AND. There is no problem combining them, this behaviour is transparent.
 * 
 * XML syntax example:
 * 
 * <ENABLEIF>
 * 	<CONDITION>
 *	<COMPONENT>componentName</COMPONENT>
 *	<VALUE>value</VALUE>
 * 	</CONDITION>
 * 	[<CONDITION>...]
 * </ENABLEIF>
 * 
 * @author Pablo Sanxiao <psanxiao@icarto.es>
 * @author Jorge López Fernández <jlopez@cartolab.es>
 * 
 */
public class DependencyReader {
    
    private Map<String, List<String>> conditions = new HashMap<String, List<String>>();
    
    public DependencyReader() {
	
    }
    
    public void addCondition(String component, String value) {
	if (!conditions.containsKey(component)) {
	    conditions.put(component, new ArrayList<String>());
	}
	conditions.get(component).add(value);
    }

    public Map<String, List<String>> getConditions() {
	return conditions;
    }

    public List<String> getCondition(String component) {
	return conditions.get(component);
    }
}
