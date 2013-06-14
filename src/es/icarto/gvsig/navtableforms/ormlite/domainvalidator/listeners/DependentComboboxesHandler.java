package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class DependentComboboxesHandler implements ActionListener {

    private ArrayList<JComponent> parentComponents;
    private JComboBox comboBoxToFill;
    private IValidatableForm form;

    public DependentComboboxesHandler(IValidatableForm form,
	    JComponent parentComponent, JComboBox comboBoxToFill) {
	this.form = form;
	this.comboBoxToFill = comboBoxToFill;
	this.parentComponents = new ArrayList<JComponent>();
	this.parentComponents.add(parentComponent);
    }

    public DependentComboboxesHandler(IValidatableForm form,
	    ArrayList<JComponent> parentComponents, JComboBox comboBoxToFill) {
	this.form = form;
	this.parentComponents = parentComponents;
	this.comboBoxToFill = comboBoxToFill;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
