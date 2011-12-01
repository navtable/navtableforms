package es.icarto.gvsig.navtableforms.validation.listeners;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

import es.icarto.gvsig.navtableforms.AbstractForm;


public class ValidationHandlerForTextAreas implements KeyListener {

    private AbstractForm dialog = null;
    
    public ValidationHandlerForTextAreas(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void keyReleased(KeyEvent e) {
	if(!dialog.isFillingValues()) {
	    JTextArea textArea = ((JTextArea) e.getSource());
	    dialog.getWidgetValues().put(textArea.getName().toUpperCase(), 
		    textArea.getText());
	    dialog.setChangedValues();
	    dialog.getFormValidator().validate();
	}
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

}
