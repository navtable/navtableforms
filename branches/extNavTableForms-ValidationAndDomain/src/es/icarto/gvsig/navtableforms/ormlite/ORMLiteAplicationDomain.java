/*
 * Copyright (c) 2011. iCarto
 *
 * This file is part of extNavTableForms
 *
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.icarto.gvsig.navtableforms.ormlite;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import es.icarto.gvsig.navtableforms.validation.DomainValues;
import es.icarto.gvsig.navtableforms.validation.DoublePositiveRule;
import es.icarto.gvsig.navtableforms.validation.IntegerPositiveRule;
import es.icarto.gvsig.navtableforms.validation.MandatoryRule;
import es.icarto.gvsig.navtableforms.validation.ValidationRule;

public class ORMLiteAplicationDomain {

    private HashMap<String, Set<ValidationRule>> domainRules;
    private HashMap<String, DomainValues> domainValues;

    public ORMLiteAplicationDomain() {
	domainRules = new HashMap<String, Set<ValidationRule>>();
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
	    Set<ValidationRule> rules = domainRules.get(componentName
		    .toUpperCase());
	    if (rules == null) {
		rules = new HashSet<ValidationRule>();
	    }
	    rules.add(rule);
	    domainRules.put(componentName.toUpperCase(), rules);
	}
    }

    /**
     * This method is case insensitive: whatever the name of the component it
     * will uppercase it and return the proper component
     */
    public DomainValues getDomainValuesForComponent(String componentName) {
	return domainValues.get(componentName.toUpperCase());
    }

    public Set<ValidationRule> getValidationRulesForComponent(
	    String componentName) {
	return domainRules.get(componentName.toUpperCase());
    }

    public ValidationRule createRule(String id) {
	if (id.equalsIgnoreCase("INTEGER_POSITIVE")) {
	    return new IntegerPositiveRule();
	} else if (id.equalsIgnoreCase("DOUBLE_POSITIVE")) {
	    return new DoublePositiveRule();
	} else if (id.equalsIgnoreCase("MANDATORY")) {
	    return new MandatoryRule();
	}
	return null;
    }

}
