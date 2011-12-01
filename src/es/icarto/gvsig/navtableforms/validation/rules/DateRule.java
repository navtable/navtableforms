package es.icarto.gvsig.navtableforms.validation.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.udc.cartolab.gvsig.navtable.utils.DateFormatter;

public class DateRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	SimpleDateFormat formatter = new SimpleDateFormat(
		DateFormatter.DATE_PATTERN);
	try {
	    formatter.parse(value);
	    return true;
	} catch (ParseException e) {
	    //e.printStackTrace();
	    return false;
	}
    }

}
