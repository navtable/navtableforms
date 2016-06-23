package es.icarto.gvsig.navtableforms.tests.formwindowproperties;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.PersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlArrayList;

import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowProperties;
import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowPropertiesSerializator;

public class FormWindowPropertiesTests {
    
    private static List<FormWindowProperties> getFields() {
	List<FormWindowProperties> formsList = new ArrayList<FormWindowProperties>();
	
	FormWindowProperties fwp = new FormWindowProperties();
	fwp.setFormName("MyForm");
	fwp.setFormWindowHeight(600);
	fwp.setFormWindowWidth(400);
	formsList.add(fwp);
	
	FormWindowProperties fwp2 = new FormWindowProperties();
	fwp2.setFormName("MyForm2");
	fwp2.setFormWindowHeight(600);
	fwp2.setFormWindowWidth(400);
	formsList.add(fwp2);
	
	FormWindowProperties fwp3 = new FormWindowProperties();
	fwp3.setFormName("MyForm3");
	fwp3.setFormWindowHeight(800);
	fwp3.setFormWindowWidth(600);
	formsList.add(fwp3);
	
	return formsList;
    }
    
    @Test
    public void testSaveFormWindowPropertiesToXmlStructure() {
	
	List<FormWindowProperties> formsList = getFields();
	String xml = FormWindowPropertiesSerializator.toXML(formsList);
	
	assertTrue(xml instanceof String);
	assertTrue(xml.contains("<forms-window-properties>"));
	assertTrue(xml.contains("<form>"));
	assertTrue(xml.contains("<formName>"));
	assertTrue(xml.contains("<formWindowHeight>"));
	assertTrue(xml.contains("<formWindowWidth>"));
    }
    
    @Test
    public void testSaveFormWindowPropertiesToXmlData() {
	
	List<FormWindowProperties> formsList = getFields();
	String xml = FormWindowPropertiesSerializator.toXML(formsList);
	
	assertTrue(xml instanceof String);
	assertTrue(xml.contains(formsList.get(0).getFormName()));
	assertTrue(xml.contains(formsList.get(1).getFormName()));
	assertTrue(xml.contains(formsList.get(2).getFormName()));
    }
    
    @Test
    public void testGetFormWindowPropertiesFromXml() {
	
	List<FormWindowProperties> fwp = 
		FormWindowPropertiesSerializator.fromXML(
			new File("data-test/test_formsWindowProperties.xml"));
	assertTrue(fwp.size() == 3);
    }

}
