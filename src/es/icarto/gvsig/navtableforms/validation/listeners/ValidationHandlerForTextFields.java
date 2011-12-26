package es.icarto.gvsig.navtableforms.validation.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class ValidationHandlerForTextFields implements KeyListener {

    private AbstractForm dialog = null;

    public ValidationHandlerForTextFields(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
	if (!dialog.isFillingValues()) {
	    JTextField c = ((JTextField) e.getSource());
	    dialog.getFormController().setValue(c.getName(),
		    c.getText());
	    dialog.setChangedValues(); // placed after updating widgetvalues
	    dialog.getFormValidator().validate();
	}
    }

}
