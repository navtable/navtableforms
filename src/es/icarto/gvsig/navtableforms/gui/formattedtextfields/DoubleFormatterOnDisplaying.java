package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class DoubleFormatterOnDisplaying extends AbstractFormatter {

    private DecimalFormat decimalFormat;
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
