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

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FormParserUtils {

    private static String getNameBeforeDots(String widgetName) {
	if (widgetName.contains(".")) {
	    return widgetName.substring(0, widgetName.indexOf("."));
	} else {
	    return widgetName;
	}
    }

    public static HashMap<String, JComponent> getWidgetsFromContainer(
	    Container c) {

	HashMap<String, JComponent> map = new HashMap<String, JComponent>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component comp = c.getComponent(count++);
	    if ((comp instanceof JTextField)
		    || (comp instanceof JFormattedTextField)
		    || (comp instanceof JCheckBox)
		    || (comp instanceof JTextArea)
		    || (comp instanceof JComboBox) || (comp instanceof JTable)) {
		String newName = getNameBeforeDots(comp.getName());
		comp.setName(newName);
		map.put(comp.getName().toUpperCase(), (JComponent) comp);
		return map;
	    }

	    if (comp instanceof Container) {
		HashMap<String, JComponent> recursiveMap = getWidgetsFromContainer((Container) comp);
		if (recursiveMap.size() > 0) {
		    for (JComponent w : recursiveMap.values()) {
			String newName = getNameBeforeDots(w.getName());
			comp.setName(newName);
			map.put(w.getName().toUpperCase(), w);
		    }
		}
	    }
	}
	return map;
    }
}
