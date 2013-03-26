package es.icarto.gvsig.navtableforms;

import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorComponent;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForCheckBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForComboBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForFormattedTextFields;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextAreas;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextFields;

public class ValidationHandler {

    private ORMLite ormlite;
    private ValidatorForm formValidator;
    private ValidationHandlerForFormattedTextFields validationHandlerForFormattedTextFields;
    private ValidationHandlerForTextFields validationHandlerForTextFields;
    private ValidationHandlerForComboBoxes validationHandlerForComboBoxes;
    private ValidationHandlerForCheckBoxes validationHandlerForCheckBoxes;
    private ValidationHandlerForTextAreas validationHandlerForTextAreas;
    private IValidatableForm validatableForm;

    public ValidationHandler(ORMLite ormLite, IValidatableForm validatableForm) {
	this.ormlite = ormLite;
	this.validatableForm = validatableForm;
	initValidation();
    }

    private void initValidation() {

	formValidator = new ValidatorForm();
	validationHandlerForFormattedTextFields = new ValidationHandlerForFormattedTextFields(
		validatableForm);
	validationHandlerForTextFields = new ValidationHandlerForTextFields(
		validatableForm);
	validationHandlerForComboBoxes = new ValidationHandlerForComboBoxes(
		validatableForm);
	validationHandlerForCheckBoxes = new ValidationHandlerForCheckBoxes(
		validatableForm);
	validationHandlerForTextAreas = new ValidationHandlerForTextAreas(
		validatableForm);
    }

    public void removeListeners(HashMap<String, JComponent> widgets) {
	for (JComponent c : widgets.values()) {
	    if (c instanceof JFormattedTextField) {
		((JTextField) c)
			.removeKeyListener(validationHandlerForFormattedTextFields);
	    } else if (c instanceof JTextField) {
		((JTextField) c)
			.removeKeyListener(validationHandlerForTextFields);
	    } else if (c instanceof JComboBox) {
		((JComboBox) c)
			.removeActionListener(validationHandlerForComboBoxes);
	    } else if (c instanceof JCheckBox) {
		((JCheckBox) c)
			.removeActionListener(validationHandlerForCheckBoxes);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c)
			.removeKeyListener(validationHandlerForTextAreas);
	    }
	}
    }

    public void setListeners(HashMap<String, JComponent> widgets) {
	for (JComponent comp : widgets.values()) {
	    if (comp instanceof JFormattedTextField) {
		((JFormattedTextField) comp)
			.addKeyListener(validationHandlerForFormattedTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JTextField) {
		((JTextField) comp)
			.addKeyListener(validationHandlerForTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JComboBox) {
		((JComboBox) comp)
			.addActionListener(validationHandlerForComboBoxes);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JCheckBox) {
		((JCheckBox) comp)
			.addActionListener(validationHandlerForCheckBoxes);
	    } else if (comp instanceof JTextArea) {
		((JTextArea) comp)
			.addKeyListener(validationHandlerForTextAreas);
	    }
	}
    }

    public boolean hasValidationErrors() {
	return formValidator.hasValidationErrors();
    }

    public String getMessages() {
	return formValidator.getMessages();
    }

    public void validate() {
	formValidator.validate();
    }

}
