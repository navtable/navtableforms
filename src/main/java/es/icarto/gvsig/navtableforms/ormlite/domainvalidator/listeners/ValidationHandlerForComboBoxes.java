package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class ValidationHandlerForComboBoxes implements ActionListener {

	private IValidatableForm dialog = null;

	public ValidationHandlerForComboBoxes(IValidatableForm dialog) {
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!dialog.isFillingValues()) {
			JComboBox c = ((JComboBox) e.getSource());
			if (c.getSelectedItem() instanceof KeyValue) {
				dialog.getFormController().setValue(c.getName(), ((KeyValue) c.getSelectedItem()).getKey());
			} else if (c.getSelectedItem() != null) {
				dialog.getFormController().setValue(c.getName(), c.getSelectedItem().toString());
			} else {
				// when remove items from a combobox, if isFillingValues is
				// not set to true, we will get a NullPointerException as
				// the change provokes this listener to activate
				// logger.warn("combobox " + c.getName() +
				// " has no value.");
				dialog.getFormController().setValue(c.getName(), "");
			}
			dialog.setChangedValues();
			dialog.validateForm();
		}
	}
}