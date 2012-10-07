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

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;

public class ORMLiteAppDomain {

    private HashMap<String, ValidatorDomain> domainValidators;
    private HashMap<String, DomainValues> domainValues;

    public ORMLiteAppDomain() {
	domainValidators = new HashMap<String, ValidatorDomain>();
	domainValues = new HashMap<String, DomainValues>();
    }

    public HashMap<String, DomainValues> getDomainValues() {
	return this.domainValues;
    }

    public DomainValues getDomainValuesForComponent(String componentName) {
	return domainValues.get(componentName);
    }

    public void addDomainValues(String component, DomainValues values) {
	this.domainValues.put(component, values);
    }

    public HashMap<String, ValidatorDomain> getDomainValidators() {
	return this.domainValidators;
    }

    public ValidatorDomain getDomainValidatorForComponent(String componentName) {
	return domainValidators.get(componentName);
    }

    public void addRule(String componentName, ValidationRule rule) {
	if (rule != null) {
	    ValidatorDomain dv = domainValidators.get(componentName);
	    if (dv == null) {
		dv = new ValidatorDomain(null);
	    }
	    dv.addRule(rule);
	    domainValidators.put(componentName, dv);
	}
    }

}
