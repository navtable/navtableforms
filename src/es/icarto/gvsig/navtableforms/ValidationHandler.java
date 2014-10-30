package es.icarto.gvsig.navtableforms;

import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorComponent;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForCheckBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForComboBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForFormattedTextFields;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForJDateChooser;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextAreas;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextFields;

public class ValidationHandler {

    private ORMLite ormlite;
    private ValidatorForm validatorForm;
    private ValidationHandlerForFormattedTextFields validationHandlerForFormattedTextFields;
    private ValidationHandlerForTextFields validationHandlerForTextFields;
    private ValidationHandlerForComboBoxes validationHandlerForComboBoxes;
    private ValidationHandlerForCheckBoxes validationHandlerForCheckBoxes;
    private ValidationHandlerForTextAreas validationHandlerForTextAreas;
    private ValidationHandlerForJDateChooser validationHandlerForJDateChooser;
    private IValidatableForm validatableForm;

    public ValidationHandler(ORMLite ormLite, IValidatableForm validatableForm) {
	this.ormlite = ormLite;
	this.validatableForm = validatableForm;
	initValidation();
    }

    private void initValidation() {

	validatorForm = new ValidatorForm();
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
	validationHandlerForJDateChooser = new ValidationHandlerForJDateChooser(validatableForm);
    }

    public void removeListeners(HashMap<String, JComponent> widgets) {
	for (JComponent c : widgets.values()) {
	    if (c instanceof JFormattedTextField) {
		((JFormattedTextField) c)
			.removeKeyListener(validationHandlerForFormattedTextFields);
		((JFormattedTextField) c)
			.removeFocusListener(validationHandlerForFormattedTextFields);
	    } else if (c instanceof JTextField) {
		((JTextField) c)
			.removeKeyListener(validationHandlerForTextFields);
		((JTextField) c)
			.removeFocusListener(validationHandlerForTextFields);
	    } else if (c instanceof JComboBox) {
		((JComboBox) c)
			.removeActionListener(validationHandlerForComboBoxes);
	    } else if (c instanceof JCheckBox) {
		((JCheckBox) c)
			.removeActionListener(validationHandlerForCheckBoxes);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c)
			.removeKeyListener(validationHandlerForTextAreas);
	    } else if (c instanceof JDateChooser) {
		((JDateChooser) c).removePropertyChangeListener("date", validationHandlerForJDateChooser);
	    }
	}
    }

    public void setListeners(HashMap<String, JComponent> widgets) {
	for (JComponent c : widgets.values()) {
	    if (c instanceof JFormattedTextField) {
		((JFormattedTextField) c)
			.addKeyListener(validationHandlerForFormattedTextFields);
		((JFormattedTextField) c)
			.addFocusListener(validationHandlerForFormattedTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(c.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(c, dv);
		    validatorForm.addComponentValidator(cv);
		}
	    } else if (c instanceof JTextField) {
		((JTextField) c)
			.addKeyListener(validationHandlerForTextFields);
		((JTextField) c)
			.addFocusListener(validationHandlerForTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(c.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(c, dv);
		    validatorForm.addComponentValidator(cv);
		}
	    } else if (c instanceof JComboBox) {
		((JComboBox) c)
			.addActionListener(validationHandlerForComboBoxes);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(c.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(c, dv);
		    validatorForm.addComponentValidator(cv);
		}
	    } else if (c instanceof JCheckBox) {
		((JCheckBox) c)
			.addActionListener(validationHandlerForCheckBoxes);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c)
			.addKeyListener(validationHandlerForTextAreas);
	    }  else if (c instanceof JDateChooser) {
		((JDateChooser) c).addPropertyChangeListener("date", validationHandlerForJDateChooser);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(c.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(c, dv);
		    validatorForm.addComponentValidator(cv);
		}
	    }
	}
    }

    public boolean hasValidationErrors() {
	return validatorForm.hasValidationErrors();
    }

    public String getMessages() {
	return validatorForm.getMessages();
    }

    public void validate() {
	validatorForm.validate();
    }

    public ValidatorForm getValidatorForm() {
	return validatorForm;
    }

}
