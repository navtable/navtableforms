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

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class ComponentValidator {

    private JComponent c = null;
    private Color defaultbg = null;

    public ComponentValidator(JComponent c) {
	this.c = c;
	defaultbg = c.getBackground();
    }

    public boolean validate() {
	String name = null;
	String value = null;
	
	if (c instanceof JTextField) {
	    name = c.getName();
	    value = ((JTextField) c).getText();
	} else if (c instanceof JComboBox) {
	    name = c.getName();
	    value = ((KeyValue) ((JComboBox) c).getSelectedItem()).getValue();
	}
	
	if (name != null) {
	    if (isValid(name, value)) {
		c.setBackground(defaultbg);
		return true;
	    }
	    c.setBackground(new Color(249, 112, 140));
	    return false;
	}
	return true;
	
    }
	
    public boolean isValid(String name, String value) {
	DomainValidator domain = new DomainValidator(name);
	boolean val = domain.validate(value);
	return val;
    }

    public String getComponentName() {
	return c.getName();
    }
}
