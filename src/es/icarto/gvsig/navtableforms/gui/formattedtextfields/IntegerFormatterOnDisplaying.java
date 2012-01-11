package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.IntegerFormatNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class IntegerFormatterOnDisplaying extends AbstractFormatter {

    private DecimalFormat integerFormat;
    private ValueFormatNT valueFormatter;

    public IntegerFormatterOnDisplaying() {
	valueFormatter = new ValueFormatNT();
	integerFormat = IntegerFormatNT.getDisplayingFormat();
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
	    return (String) arg;
	}
	return integerFormat.format(arg);
    }

}
