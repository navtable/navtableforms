package es.icarto.gvsig.navtableforms.utils;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DoubleFormatterOnDisplaying extends AbstractFormatter {

    private NavTableFormats formats;
    private DecimalFormat decimalFormat;
    private ValueFormatter valueFormatter;

    public DoubleFormatterOnDisplaying() {
	valueFormatter = new ValueFormatter();
	formats = new NavTableFormats();
	decimalFormat = formats.getDoubleFormatForDisplayingInstance();	
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
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
