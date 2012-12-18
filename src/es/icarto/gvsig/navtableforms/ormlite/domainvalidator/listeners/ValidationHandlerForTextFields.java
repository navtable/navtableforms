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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class ValidationHandlerForTextFields implements KeyListener {

    private AbstractForm dialog = null;

    public ValidationHandlerForTextFields(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
	if (!dialog.isFillingValues()) {
	    JTextField c = ((JTextField) e.getSource());
	    dialog.getFormController().setValue(c.getName(),
		    c.getText());
	    dialog.setChangedValues(); // placed after updating widgetvalues
	    dialog.getFormValidator().validate();
	}
    }

}
