package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import es.icarto.gvsig.navtableforms.IValidatableForm;

public class ValidationHandlerForCheckBoxes implements ActionListener {

    private IValidatableForm dialog = null;

    public ValidationHandlerForCheckBoxes(IValidatableForm dialog) {
	this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (!dialog.isFillingValues()) {
	    JCheckBox c = ((JCheckBox) e.getSource());
	    if (c.isSelected()) {
		dialog.getFormController().setValue(c.getName(), "true");
	    } else {
		dialog.getFormController().setValue(c.getName(), "false");
	    }
	    dialog.setChangedValues();
	    dialog.validateForm();
	}
    }

}
