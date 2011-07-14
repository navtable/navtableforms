//package es.udc.cartolab.gvsig.navtableformsexample.validation;
//
//import com.jgoodies.validation.ValidationResult;
//import com.jgoodies.validation.util.PropertyValidationSupport;
//import com.jgoodies.validation.util.ValidationUtils;
//
//public final class Example1Validator {
//
//    /**
//     * Validates this Validators Order and returns the result as an instance of
//     * {@link ValidationResult}.
//     * 
//     * @param order
//     *            the Order to be validated
//     * @return the ValidationResult of the order validation
//     */
//    public ValidationResult validate(Example1Model example1Model) {
//
//	PropertyValidationSupport support = new PropertyValidationSupport(
//		example1Model, "Example1Model");
//
//	// codigo (primary key + numeric)
//	if (!ValidationUtils.isNumeric(example1Model.getCodigo())) {
//	    support.addError("codigo", "debe ser un numero");
//	} else if (ValidationUtils.isBlank(example1Model.getCodigo())) {
//	    support.addError("codigo", "es obligatorio, es la clave primaria");
//	}
//
//	// Check numerics and no black fields
//	if (!ValidationUtils.isNumeric(example1Model.getCapacidad())) {
//	    support.addError("capacidad", "debe ser un numero");
//	} else if (ValidationUtils.isBlank(example1Model.getCapacidad())) {
//	    support.addError("capacidad", "es obligatorio");
//	}
//
//	return support.getResult();
//    }
// }
