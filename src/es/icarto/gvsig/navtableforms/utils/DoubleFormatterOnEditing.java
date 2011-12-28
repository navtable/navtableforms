package es.icarto.gvsig.navtableforms.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DoubleFormatterOnEditing extends AbstractFormatter {

    private DecimalFormat decimalFormat;
    private ValueFormatter valueFormatter;

    public DoubleFormatterOnEditing() {
	decimalFormat = new DecimalFormat("##################.##");
	valueFormatter = new ValueFormatter();
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
	if(arg.equals("")) {
	    arg = "0";
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
