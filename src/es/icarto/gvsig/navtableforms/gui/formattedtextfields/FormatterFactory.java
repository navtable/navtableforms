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
