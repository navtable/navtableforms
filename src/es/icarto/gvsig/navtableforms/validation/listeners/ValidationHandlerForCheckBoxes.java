package es.icarto.gvsig.navtableforms.validation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class ValidationHandlerForCheckBoxes implements ActionListener {

    private AbstractForm dialog = null;

    public ValidationHandlerForCheckBoxes(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void actionPerformed(ActionEvent e) {
	if (!dialog.isFillingValues()) {
	    JCheckBox c = ((JCheckBox) e.getSource());
	    if (c.isSelected()) {
		dialog.getFormController().setValue(c.getName(), "true");
	    } else {
		dialog.getFormController().setValue(c.getName(), "false");
	    }
	    dialog.setChangedValues();
	    dialog.getFormValidator().validate();
	}
    }

}
