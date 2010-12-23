package es.udc.cartolab.gvsig.navtableformsexample.validation;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.validation.Validator;

import es.udc.cartolab.gvsig.navtableforms.validation.FormBinding;
import es.udc.cartolab.gvsig.navtableforms.validation.FormModel;

/**
 * Provides all models to bind the view to its domain model
 */
public class Example1Binding extends FormBinding<FormModel> {

    // Instance Creation ******************************************************

    public Example1Binding(Model model) {
	super(model);
    }

    @Override
    protected Validator getValidator() {
	return new Example1Validator();
    }

}
