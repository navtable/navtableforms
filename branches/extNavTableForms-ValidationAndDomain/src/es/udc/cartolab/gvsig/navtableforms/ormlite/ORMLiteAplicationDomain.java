package es.udc.cartolab.gvsig.navtableforms.ormlite;

import java.util.HashMap;

import es.udc.cartolab.gvsig.navtableforms.validation.DomainValues;
import es.udc.cartolab.gvsig.navtableforms.validation.IntegerPositiveRule;
import es.udc.cartolab.gvsig.navtableforms.validation.ValidationRule;

public class ORMLiteAplicationDomain {

    private HashMap<String, ValidationRule> domainRules;
    private HashMap<String, DomainValues> domainValues;

    public ORMLiteAplicationDomain() {
	domainRules = new HashMap<String, ValidationRule>();
	domainValues = new HashMap<String, DomainValues>();
    }

    public void addDomainValues(String component, DomainValues values) {
	domainValues.put(component, values);
    }

    public void addRule(String component, ValidationRule rule) {
	if (rule != null) {
	    domainRules.put(component, rule);
	}
    }

    public DomainValues getDomainValuesForComponent(String name) {
	return domainValues.get(name);
    }

    public ValidationRule getValidationRuleForComponent(String name) {
	return domainRules.get(name);
    }

    public ValidationRule createRule(String id) {
	if (id.equalsIgnoreCase("INTEGER_POSITIVE")) {
	    return new IntegerPositiveRule();
	}
	return null;
    }

}
