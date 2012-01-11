package es.icarto.gvsig.navtableforms.validation.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class DateRule extends ValidationRule {

    @Override
    public boolean validate(String value) {
	SimpleDateFormat formatter = DateFormatNT.getDateFormat();
	try {
	    formatter.parse(value);
	    return true;
	} catch (ParseException e) {
	    //e.printStackTrace();
	    return false;
	}
    }

}
