package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.NavTableFormats;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatter;

public class DoubleFormatterOnEditing extends AbstractFormatter {

    private ValueFormatter valueFormatter;
    private NavTableFormats formats;
    private DecimalFormat format;

    public DoubleFormatterOnEditing() {
	valueFormatter = new ValueFormatter();
	formats = new NavTableFormats();
	format = formats.getDoubleFormatForEditingInstance();	
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
	if(arg.equals("")) {
	    arg = "0";
	}
	return format.parseObject(arg);
    }

    @Override
    public String valueToString(Object arg) throws ParseException {
	if(arg == null) {
	    return valueFormatter.getNullStatementString();
	} else if (arg instanceof String) {
	    return (String) arg;
	}
	return format.format(arg);
    }

}
