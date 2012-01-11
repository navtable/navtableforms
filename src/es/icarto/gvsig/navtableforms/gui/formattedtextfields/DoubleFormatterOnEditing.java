package es.icarto.gvsig.navtableforms.gui.formattedtextfields;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class DoubleFormatterOnEditing extends AbstractFormatter {

    private ValueFormatNT valueFormatter;
    private DecimalFormat format;

    public DoubleFormatterOnEditing() {
	valueFormatter = new ValueFormatNT();
	format = DoubleFormatNT.getEditingFormat();	
    }

    @Override
    public Object stringToValue(String arg) throws ParseException {
	if(arg.equals("") || (arg == null)) {
	    return null;
	}
	return format.parseObject(arg);
    }

    @Override
    public String valueToString(Object arg) throws ParseException {
	if(arg == null) {
	    return valueFormatter.getNullStatementString();
	} else if (arg instanceof String) {
	    DecimalFormat displayFormat = DoubleFormatNT.getDisplayingFormat();
	    return format.format(displayFormat.parse((String) arg));
	}
	return format.format(arg);
    }

}
