package es.udc.cartolab.gvsig.navtableformsexample;

import java.io.File;

public class Preferences {

    private static final String xmlFile = "gvSIG" + File.separator
	    + "extensiones" + File.separator
	    + "es.udc.cartolab.gvsig.navtableformsexample" + File.separator
	    + "example1.xml";

    public static String getXMLFileName() {
	return xmlFile;
    }


}
