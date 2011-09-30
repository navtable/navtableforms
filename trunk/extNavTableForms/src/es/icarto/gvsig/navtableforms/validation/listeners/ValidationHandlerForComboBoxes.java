package es.icarto.gvsig.navtableforms.validation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.KeyValue;

public class ValidationHandlerForComboBoxes implements ActionListener {

    private AbstractForm dialog = null;

    public ValidationHandlerForComboBoxes(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void actionPerformed(ActionEvent e) {
	if (!dialog.isFillingValues()) {
	    JComboBox c = ((JComboBox) e.getSource());
	    if (c.getSelectedItem() instanceof KeyValue) {
		dialog.getWidgetValues().put(c.getName().toUpperCase(),
			((KeyValue) c.getSelectedItem()).getKey());
	    } else if (c.getSelectedItem() != null) {
		dialog.getWidgetValues().put(c.getName().toUpperCase(),
			c.getSelectedItem().toString());
	    } else {
		// when remove items from a combobox, if isFillingValues is
		// not set to true, we will get a NullPointerException as
		// the change provokes this listener to activate
		// logger.warn("combobox " + c.getName() +
		// " has no value.");
		dialog.getWidgetValues().put(c.getName().toUpperCase(), "");
	    }
	    dialog.setChangedValues();
	    dialog.getFormValidator().validate();
	}
    }

}