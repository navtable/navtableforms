package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

/**
 * Handler for filling a dependent ComboBox
 * 
 * The Handler right now works with two different parent components: JTextfield
 * and JComboBox. It implements two different interfaces with two different
 * types of events: Action and Focus. Action is fired when you click onto a
 * combo or press enter while inside a textfield, whereas focus is activated
 * when one of those components loses it. You can assign the handler as the
 * listener type you prefer as both events trigger the same code, though we
 * suggest using the focus one on text fields, and the action one with combos.
 */

public class DependentComboboxHandler implements ActionListener,
	FocusListener {

    private List<JComponent> parentComponents;
    private JComboBox comboBoxToFill;
    private IValidatableForm form;

    public DependentComboboxHandler(IValidatableForm form,
	    JComponent parentComponent, JComboBox comboBoxToFill) {
	this.form = form;
	this.comboBoxToFill = comboBoxToFill;
	this.parentComponents = new ArrayList<JComponent>();
	this.parentComponents.add(parentComponent);
    }

    public DependentComboboxHandler(IValidatableForm form,
	    List<JComponent> parentComponents, JComboBox comboBoxToFill) {
	this.form = form;
	this.parentComponents = parentComponents;
	this.comboBoxToFill = comboBoxToFill;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	// Value may have changed in a ComboBox,
	// or enter is pressed inside a TextField.
	if (!form.isFillingValues() && parentComponentsHaveItemSelected()) {
	    updateComboBoxValues();
	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	// This event is of no use to us.
    }

    @Override
    public void focusLost(FocusEvent e) {
	// Value may have changed in a TextField or
	// a ComboBox
	if (!form.isFillingValues() && parentComponentsHaveItemSelected()) {
	    updateComboBoxValues();
	}
    }

    public void updateComboBoxValues() {
	ArrayList<String> foreignKeys = new ArrayList<String>();
	for (JComponent cp : parentComponents) {
	    if (cp instanceof JComboBox) {
		JComboBox cb = (JComboBox) cp;
		if (cb.getSelectedItem() instanceof KeyValue) {
		    String key = ((KeyValue) cb.getSelectedItem()).getKey();
		    foreignKeys.add(key);
		}
	    } else {
		if (cp instanceof JTextField) {
		    JTextField tf = (JTextField) cp;
		    String key = tf.getText();
		    if (key != null) {
			foreignKeys.add(key);
		    }
		}
	    }
	}
	form.getFillHandler().fillJComboBox(comboBoxToFill, foreignKeys);
    }

    private boolean parentComponentsHaveItemSelected() {
	for (JComponent cp : parentComponents) {
	    if (cp instanceof JComboBox) {
		JComboBox cb = (JComboBox) cp;
		if (cb.getSelectedItem() == null) {
		    return false;
		}
	    } else {
		if (cp instanceof JTextField) {
		    JTextField tf = (JTextField) cp;
		    String value = tf.getText();
		    if ((value == null) || (value.length() == 0)) {
			return false;
		    }
		}
	    }
	}
	return true;
    }

}
