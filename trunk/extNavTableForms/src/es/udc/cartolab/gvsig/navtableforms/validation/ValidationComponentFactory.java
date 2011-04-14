/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
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
package es.udc.cartolab.gvsig.navtableforms.validation;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jeta.forms.components.panel.FormPanel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

public class ValidationComponentFactory {

    protected ValidationComponentFactory() {
    }

    public static JFormattedTextField createFormattedTextField(
	    ValueModel valueModel, // model to connect to
	    boolean commitOnFocusLost, // whether checking is on focus lost OR
				       // key typed
	    FormPanel form, // container form
	    String componentName) { // widget name

	JFormattedTextField formattedTextField = (JFormattedTextField) form
		.getComponentByName(componentName);
	Bindings.bind(formattedTextField, valueModel, commitOnFocusLost);
	return formattedTextField;
    }

    public static void bindFormattedTextField(JFormattedTextField comp,
	    ValueModel valueModel, boolean commitOnFocusLost) {

	Bindings.bind(comp, valueModel, commitOnFocusLost);
    }

    public static void bindTextField(JTextField comp, ValueModel valueModel,
	    boolean commitOnFocusLost) {

	Bindings.bind(comp, valueModel, commitOnFocusLost);
    }

    public static void bindCheckBox(JCheckBox comp, ValueModel valueModel) {

	Bindings.bind(comp, valueModel);
    }

    public static void bindComboBox(JComboBox comp, String[] values,
	    ValueModel valueModel) {
	ComboBoxAdapter adapter = new ComboBoxAdapter(values, valueModel);
	comp.setModel(adapter);
	comp.setSelectedIndex(0);
    }

    public static void bindComboBoxAsSIL(JComboBox comp,
	    SelectionInList<String> sil) {
	ComboBoxAdapter adapter = new ComboBoxAdapter(sil);
	comp.setModel(adapter);
	comp.setSelectedIndex(0);
	// Bindings.bind(comp, sil);
    }

    public static void bindCBWithListener(JComboBox comp,
	    SelectionInList<String> sil, ActionListener actionListener) {
	ComboBoxAdapter adapter = new ComboBoxAdapter(sil);
	comp.setModel(adapter);
	comp.setSelectedIndex(0);
	comp.addActionListener(actionListener);
	// Bindings.bind(comp, sil);
    }

    public static void bindTable(JTable comp, ValueModel valueModel) {

	Bindings.bind(comp, "model", valueModel);
    }

    public static void bindTextArea(JTextArea comp, ValueModel valueModel,
	    boolean commitOnFocusLost) {

	Bindings.bind(comp, valueModel, commitOnFocusLost);
    }

}
