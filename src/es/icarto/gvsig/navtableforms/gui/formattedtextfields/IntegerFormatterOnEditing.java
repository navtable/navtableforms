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

package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.IntegerFormatNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class IntegerFormatterOnEditing extends AbstractFormatter {

    private DecimalFormat integerFormat;
    private ValueFormatNT valueFormatter;

    public IntegerFormatterOnEditing() {
	valueFormatter = new ValueFormatNT();
	integerFormat = IntegerFormatNT.getEditingFormat();
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
	if(arg.equals("") || (arg == null)) {
	    return null;
	}
	return integerFormat.parseObject(arg);
    }

    @Override
    public String valueToString(Object arg) throws ParseException {
	if(arg == null) {
	    return valueFormatter.getNullStatementString();   
	} else if (arg instanceof String) {
	    DecimalFormat displayFormat = IntegerFormatNT.getDisplayingFormat();
	    return integerFormat.format(displayFormat.parse((String) arg));
	}
	return integerFormat.format(arg);
    }

}
