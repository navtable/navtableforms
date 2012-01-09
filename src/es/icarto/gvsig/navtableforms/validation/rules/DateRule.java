package es.icarto.gvsig.navtableforms.validation.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.udc.cartolab.gvsig.navtable.format.DateFormatter;

public class DateRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	SimpleDateFormat formatter = DateFormatter.getDateFormatter();
	try {
	    formatter.parse(value);
	    return true;
	} catch (ParseException e) {
	    //e.printStackTrace();
	    return false;
	}
    }

}
