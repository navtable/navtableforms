package es.icarto.gvsig.navtableforms.validation;

import java.util.ArrayList;

public class FormValidator {

    boolean hasValidationErrors = false;
    ArrayList<ComponentValidator> cvs = new ArrayList<ComponentValidator>();

    public FormValidator() {
    }

    public boolean hasValidationErrors() {
	return hasValidationErrors;
    }

    public String getMessages() {
	// TODO: improve by allowing to define messages
	return "El formulario tiene errores";
    }

    public void setValidationErrors(boolean bol) {
	hasValidationErrors = bol;
    }

    public void addComponentValidator(ComponentValidator cv) {
	cvs.add(cv);
    }

    public ArrayList<ComponentValidator> getComponentValidators() {
	return cvs;
    }

    public void validate() {
	setValidationErrors(false);
	for (ComponentValidator cv : cvs) {
	    if (!cv.validate()) {
		setValidationErrors(true);
	    }
	}
    }
}