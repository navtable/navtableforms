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

import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class DoubleFormatterOnDisplaying extends AbstractFormatter {

    private NumberFormat decimalFormat;
    private ValueFormatNT valueFormatter;

    public DoubleFormatterOnDisplaying() {
	valueFormatter = new ValueFormatNT();
	decimalFormat = DoubleFormatNT.getDisplayingFormat();
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
	if(arg.equals("") || (arg == null)) {
	    return null;
	}
	return decimalFormat.parseObject(arg);
    }

    @Override
    public String valueToString(Object arg) throws ParseException {
	if(arg == null) {
	    return valueFormatter.getNullStatementString();
	} else if (arg instanceof String) {
	    return (String) arg;
	}
	return decimalFormat.format(arg);
    }

}
