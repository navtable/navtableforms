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

    /**
     * This method is case insensitive: whatever the name of the component it
     * will uppercase it
     */
    public void addDomainValues(String component, DomainValues values) {
	domainValues.put(component.toUpperCase(), values);
    }

    /**
     * This method is case insensitive: whatever the name of the component it
     * will uppercase it
     */
    public void addRule(String componentName, ValidationRule rule) {
	if (rule != null) {
	    domainRules.put(componentName.toUpperCase(), rule);
	}
    }

    /**
     * This method is case insensitive: whatever the name of the component it
     * will uppercase it and return the proper component
     */
    public DomainValues getDomainValuesForComponent(String componentName) {
	return domainValues.get(componentName.toUpperCase());
    }

    public ValidationRule getValidationRuleForComponent(String componentName) {
	return domainRules.get(componentName.toUpperCase());
    }

    public ValidationRule createRule(String id) {
	if (id.equalsIgnoreCase("INTEGER_POSITIVE")) {
	    return new IntegerPositiveRule();
	}
	return null;
    }

}
