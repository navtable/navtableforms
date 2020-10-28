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

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;

public class ValidationHandlerForTextFields implements KeyListener, FocusListener {

	private IValidatableForm dialog = null;

	public ValidationHandlerForTextFields(IValidatableForm dialog) {
		this.dialog = dialog;
	}

	private void confirmChange(ComponentEvent e) {
		if (!dialog.isFillingValues()) {
			JTextField c = ((JTextField) e.getSource());
			dialog.getFormController().setValue(c.getName(), c.getText());
			dialog.setChangedValues(); // placed after updating widgetvalues
			dialog.validateForm();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		confirmChange(e);
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		confirmChange(e);
	}

}
