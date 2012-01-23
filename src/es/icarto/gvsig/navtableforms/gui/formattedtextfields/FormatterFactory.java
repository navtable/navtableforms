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

import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatterFactory;

public class FormatterFactory {

    public static AbstractFormatter createFormatter(Integer type) {
	switch(type) {
	case java.sql.Types.INTEGER:
	    return new IntegerFormatterOnDisplaying();
	case java.sql.Types.DOUBLE:
	    return new DoubleFormatterOnDisplaying();
	default: 
	    return null;
	}
    }

    public static AbstractFormatterFactory createFormatterFactory(int type) {
	AbstractFormatter displayFormatter = null;
	AbstractFormatter editFormatter = null;
	switch(type) {
	case java.sql.Types.DOUBLE:
	    displayFormatter = new DoubleFormatterOnDisplaying();
	    editFormatter = new DoubleFormatterOnEditing();
	    break;
	case java.sql.Types.INTEGER:
	    displayFormatter = new IntegerFormatterOnDisplaying();
	    editFormatter = new IntegerFormatterOnEditing();
	    break;
	default:
	    break;
	}
	if((editFormatter == null) && (displayFormatter == null)) {
	    return null;
	}
	DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory();
	formatterFactory.setDefaultFormatter(displayFormatter);
	formatterFactory.setDisplayFormatter(displayFormatter);
	formatterFactory.setEditFormatter(editFormatter);
	formatterFactory.setNullFormatter(displayFormatter);
	return formatterFactory;
    }

}
