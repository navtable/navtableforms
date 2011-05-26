package es.icarto.gvsig.navtableforms.validation;


public class IntegerPositiveRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	try {
	    if (Integer.parseInt(value) > 0) {
		return true;
	    }
	    return false;
	} catch (NumberFormatException nfe) {
	    return false; // it's not an admissible value
	}
    }
}
