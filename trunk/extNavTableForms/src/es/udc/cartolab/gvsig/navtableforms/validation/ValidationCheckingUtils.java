/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
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
package es.udc.cartolab.gvsig.navtableforms.validation;

public class ValidationCheckingUtils {

    public static boolean isReal(String str) {
	// BUG: it parses numbers ended in f as float as well (ie: 0.01f)
	try {
	    Float.parseFloat(str);
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    public static boolean isLowerThan(String valueToCheck, String boundary) {
	if (boundary.trim().length() == 0) {
	    boundary = "0";
	}
	if (valueToCheck.trim().length() == 0) {
	    valueToCheck = "0";
	}
	double limit = Double.parseDouble(boundary);
	double value = Double.parseDouble(valueToCheck);
	if (value < limit) {
	    return true;
	} else {
	    return false;
	}
    }

    public static boolean isBiggerThan(String valueToCheck, String boundary) {
	if (boundary.equals("")) {
	    boundary = "0";
	}
	if (valueToCheck.equals("")) {
	    valueToCheck = "0";
	}
	double limit = Double.parseDouble(boundary);
	double value = Double.parseDouble(valueToCheck);
	if (value > limit) {
	    return true;
	} else {
	    return false;
	}
    }

}
