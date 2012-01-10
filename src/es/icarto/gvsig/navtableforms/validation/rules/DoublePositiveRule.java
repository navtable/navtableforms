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
package es.icarto.gvsig.navtableforms.validation.rules;

import java.text.DecimalFormat;
import java.text.ParseException;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatter;

public class DoublePositiveRule extends ValidationRule {

    private DecimalFormat format;

    public DoublePositiveRule() {
	format = DoubleFormatter.getEditingFormat();
    }

    @Override
    public boolean validate(String value) {
	try {
	    double doubleValue = format.parse(value).doubleValue();
	    if (doubleValue >= 0.0) {
		return true;
	    }
	    return false;
	} catch (NumberFormatException nfe) {
	    return false;
	} catch (ParseException e) {
	    return false;
	}
    }

}
