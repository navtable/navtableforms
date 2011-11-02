package es.icarto.gvsig.navtableforms.validation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.KeyValue;

public class DependentComboboxesHandler implements ActionListener {

    private ArrayList<JComboBox> parentComponents;
    private JComboBox comboBoxToFill;
    private AbstractForm form;

    public DependentComboboxesHandler(AbstractForm form, JComboBox parentComponent, JComboBox comboBoxToFill) {
	this.form = form;
	this.comboBoxToFill = comboBoxToFill;
	this.parentComponents = new ArrayList<JComboBox>();	
	this.parentComponents.add(parentComponent);
    }

    public DependentComboboxesHandler(AbstractForm form, ArrayList<JComboBox> parentComponents, JComboBox comboBoxToFill) {
	this.form = form;
	this.parentComponents = parentComponents;
	this.comboBoxToFill = comboBoxToFill;
    }

    public void actionPerformed(ActionEvent e) {
	if(parentComponentsHaveItemSelected()) {	    
	    ArrayList<String> foreignKeys = new ArrayList<String>();
	    for (JComboBox cb : parentComponents) {
		String key = ((KeyValue) cb.getSelectedItem()).getKey();
		foreignKeys.add(key);
	    }
	    form.fillJComboBox(comboBoxToFill, foreignKeys);
	}
    }

    private boolean parentComponentsHaveItemSelected() {
	for (JComboBox cb : parentComponents) {
	    if(cb.getSelectedItem() == null) {
		return false;
	    }
	}
	return true;
    }

}
