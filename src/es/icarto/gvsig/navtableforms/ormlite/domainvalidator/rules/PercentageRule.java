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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

public class PercentageRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	return isEmpty(value) || isPercentage(value);
    }

    private boolean isPercentage(String value) {
	boolean percentage = false;
	try {
	    int int_value = Math.abs(Integer.parseInt(value));
	    if ((int_value >= 0) && (int_value <= 100)) {
		percentage = true;
	    }
	    return percentage;
	} catch (NumberFormatException nfe) {
	    return percentage;
	}
    }

}
