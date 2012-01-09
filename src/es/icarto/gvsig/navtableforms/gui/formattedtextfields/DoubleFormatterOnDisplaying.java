package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatter;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatter;

public class DoubleFormatterOnDisplaying extends AbstractFormatter {

    private DecimalFormat decimalFormat;
    private ValueFormatter valueFormatter;

    public DoubleFormatterOnDisplaying() {
	valueFormatter = new ValueFormatter();
	decimalFormat = DoubleFormatter.getFormatForDisplaying();
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
