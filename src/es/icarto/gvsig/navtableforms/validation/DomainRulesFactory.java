package es.icarto.gvsig.navtableforms.validation;

import es.icarto.gvsig.navtableforms.validation.rules.DateRule;
import es.icarto.gvsig.navtableforms.validation.rules.DoublePositiveRule;
import es.icarto.gvsig.navtableforms.validation.rules.IntegerPositiveRule;
import es.icarto.gvsig.navtableforms.validation.rules.MandatoryRule;
import es.icarto.gvsig.navtableforms.validation.rules.ValidationRule;

public class DomainRulesFactory {

    public static ValidationRule createRule(String id) {
	if (id.equalsIgnoreCase("INTEGER_POSITIVE")) {
	    return new IntegerPositiveRule();
	} else if (id.equalsIgnoreCase("DOUBLE_POSITIVE")) {
	    return new DoublePositiveRule();
	} else if (id.equalsIgnoreCase("MANDATORY")) {
	    return new MandatoryRule();
	} else if (id.equalsIgnoreCase("DATE")) {
	    return new DateRule();
	}
	return null;
    }

}
