package es.icarto.gvsig.navtableforms.forms.windowproperties;

import java.io.File;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;


public class FormWindowPropertiesSerializator {
    
    public static String toXML(List<FormWindowProperties> fields) {
	XStream xstream = new XStream();
	xstream.alias("forms-window-properties", List.class);
	xstream.alias("form", FormWindowProperties.class);
	return xstream.toXML(fields);
    }
    
    public static List<FormWindowProperties> fromXML(File xml) throws XStreamException {
 	XStream xstream = new XStream();
 	xstream.alias("forms-window-properties", List.class);
 	xstream.alias("form", FormWindowProperties.class);
 	return (List<FormWindowProperties>) xstream.fromXML(xml);
     }

}
