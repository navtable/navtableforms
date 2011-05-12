package es.udc.cartolab.gvsig.navtableforms.validation;

import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public class DomainValidator {

    ValidationRule rule = null;

    public DomainValidator(String name) {
	rule = getValidationRuleForComponent(name);
    }

    public boolean validate(String value) {
	if (rule != null) {
	    return rule.validate(value);
	}
	return true;
    }

    private ValidationRule getValidationRuleForComponent(String name) {
	return ORMLite.getAplicationDomainObject("file.xml")
		.getValidationRuleForComponent(name);
    }

}
