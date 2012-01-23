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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class DateRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	SimpleDateFormat formatter = DateFormatNT.getDateFormat();
	try {
	    formatter.parse(value);
	    return true;
	} catch (ParseException e) {
	    //e.printStackTrace();
	    return false;
	}
    }

}
