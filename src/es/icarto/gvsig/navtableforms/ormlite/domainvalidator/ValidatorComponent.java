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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class ValidatorComponent {

    private JComponent c = null;
    private Color defaultbg = null;
    private ValidatorDomain domain = null;

    public ValidatorComponent(JComponent c, ValidatorDomain dv) {
	this.c = c;
	defaultbg = c.getBackground();
	this.domain = dv;
    }

    public boolean validate() {
	String name = null;
	String value = null;

	if (c instanceof JTextField) {
	    name = c.getName();
	    value = ((JTextField) c).getText();
	} else if (c instanceof JComboBox) {
	    if (((JComboBox) c).getSelectedItem() != null) {
		name = c.getName();
		if (((JComboBox) c).getSelectedItem() instanceof KeyValue) {
		    value = ((KeyValue) ((JComboBox) c).getSelectedItem())
			    .getValue();
		} else {
		    value = ((JComboBox) c).getSelectedItem().toString();
		}
	    }
	}

	if (name != null) {
	    if (isValid(name, value)) {
		if (c.isEnabled()) {
		    c.setBackground(defaultbg);
		} else {
		    setDisabledBackground();
		}
		return true;
	    }
	    if (c.isEnabled()) {
		c.setBackground(new Color(249, 112, 140));
	    } else {
		setDisabledBackground();
	    }
	    return false;
	}
	return true;

    }

    private void setDisabledBackground() {
	// Only way I found that works properly in both windows and
	// linux for setting the default disabled background
	c.setEnabled(true);
	c.setBackground(defaultbg);
	c.setEnabled(false);
    }

    public boolean isValid(String name, String value) {
	boolean val = domain.validate(value);
	return val;
    }

    public String getComponentName() {
	return c.getName();
    }
}
