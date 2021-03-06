package es.icarto.gvsig.navtableforms.ormlite.domainvalidator;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DateRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DoublePositiveRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DoubleRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.IntegerPositiveRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.IntegerRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.MandatoryRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.PercentageRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;

public class DomainRulesFactory {

    public static ValidationRule createRule(String id) {
	if (id.equalsIgnoreCase("INTEGER_POSITIVE")) {
	    return new IntegerPositiveRule();
	} else if (id.equalsIgnoreCase("DOUBLE_POSITIVE")) {
	    return new DoublePositiveRule();
	} else if (id.equalsIgnoreCase("PERCENTAGE")) {
	    return new PercentageRule();
	} else if (id.equalsIgnoreCase("MANDATORY")) {
	    return new MandatoryRule();
	} else if (id.equalsIgnoreCase("DATE")) {
	    return new DateRule();
	} else if (id.equalsIgnoreCase("DOUBLE")) {
	    return new DoubleRule();
	} else if (id.equalsIgnoreCase("INTEGER")) {
	    return new IntegerRule();
	}
	return null;
    }

}
