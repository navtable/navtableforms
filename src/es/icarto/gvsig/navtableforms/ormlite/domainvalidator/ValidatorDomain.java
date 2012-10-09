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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator;

import java.util.HashSet;
import java.util.Set;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;

public class ValidatorDomain {

    private Set<ValidationRule> rules = null;

    public ValidatorDomain(Set<ValidationRule> rules) {
	this.rules = rules;
    }

    public void addRule(ValidationRule rule) {
	if (rules == null) {
	    rules = new HashSet<ValidationRule>();
	}
	this.rules.add(rule);
    }

    public Set<ValidationRule> getRules() {
	return rules;
    }

    public boolean validate(String value) {
	if (rules != null) {
	    for (ValidationRule rule : rules) {
		if (!rule.validate(value)) {
		    return false;
		}
	    }
	}
	return true;
    }

}
