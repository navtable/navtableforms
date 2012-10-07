package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.formattedtextfields.FormatterFactory;

public class ValidationHandlerForFormattedTextFields implements KeyListener {

    private AbstractForm dialog = null;

    public ValidationHandlerForFormattedTextFields(AbstractForm dialog) {
	this.dialog = dialog;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
	if (!dialog.isFillingValues()) {
	    JFormattedTextField c = ((JFormattedTextField) e.getSource());
	    try {
		c.commitEdit();  // make sure value and displayed text are the same
		AbstractFormatter formatter = FormatterFactory
			.createFormatter(dialog.getFormController().getType(
				c.getName()));
		String value = formatter.valueToString(c.getValue());
		dialog.getFormController().setValue(c.getName(), value);
		dialog.setChangedValues(); // placed after updating widgetvalues
		dialog.getFormValidator().validate();
	    } catch (ParseException e1) {
		e1.printStackTrace();
	    }
	}
    }

}
