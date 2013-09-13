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

import java.text.NumberFormat;
import java.text.ParseException;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;


public class DoublePositiveRule extends ValidationRule {

    private NumberFormat format;

    public DoublePositiveRule() {
	format = DoubleFormatNT.getDisplayingFormat();
    }

    @Override
    public boolean validate(String value) {
	return isEmpty (value) || isDoublePositive(value);
    }

    private boolean isDoublePositive(String value) {
	try {
	    value = removeStartingTrailingZeros(value);
	    Double doubleValue = format.parse(value).doubleValue();
	    if ((doubleValue.toString().length() == value.length())
		    && (doubleValue >= 0.0)) {
		return true;
	    }
	    return false;
	} catch (ParseException nfe) {
	    return false;
	}
    }

    private String removeStartingTrailingZeros(String number) {
	char decimalSeparator = format.format(1.1).charAt(1);
	String aux = number.replaceAll("^[0]*", "").replaceAll("[0]*$", "")
		.replaceAll("\\" + decimalSeparator + "$", "");
	if (!aux.contains("" + decimalSeparator)) {
	    aux += decimalSeparator + "0";
	}
	if (aux.startsWith("" + decimalSeparator)) {
	    aux = "0" + aux;
	}
	return aux;
    }

}
