package es.icarto.gvsig.navtableforms.validation;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class ComponentValidator {

    JComponent c = null;
    Color defaultbg = null;

    public ComponentValidator(JComponent c) {
	this.c = c;
	defaultbg = c.getBackground();
    }

    public boolean validate() {
	if (c instanceof JTextField) {
	    if (isValid(c.getName(), ((JTextField) c).getText())) {
		c.setBackground(defaultbg);
		return true;
	    }
	    c.setBackground(Color.RED);
	    return false;
	}
	return true;
    }

    public boolean isValid(String name, String value) {
	DomainValidator domain = new DomainValidator(name);
	boolean val = domain.validate(value);
	return val;
    }

    public String getComponentName() {
	return c.getName();
    }
}
