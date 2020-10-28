package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.gui.formattedtextfields.FormatterFactory;

public class ValidationHandlerForFormattedTextFields implements KeyListener, FocusListener {

	private static final Logger logger = LoggerFactory.getLogger(ValidationHandlerForFormattedTextFields.class);

	private IValidatableForm dialog = null;

	public ValidationHandlerForFormattedTextFields(IValidatableForm dialog) {
		this.dialog = dialog;
	}

	private void confirmChange(ComponentEvent e) {
		if (!dialog.isFillingValues()) {
			JFormattedTextField c = ((JFormattedTextField) e.getSource());
			try {
				c.commitEdit(); // make sure value and displayed text are the
				// same
				AbstractFormatter formatter = FormatterFactory
						.createFormatter(dialog.getFormController().getType(c.getName()));
				String value = formatter.valueToString(c.getValue());
				dialog.getFormController().setValue(c.getName(), value);
				dialog.setChangedValues(); // placed after updating widgetvalues
				dialog.validateForm();
			} catch (ParseException e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		confirmChange(e);
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	@Override
	public void focusLost(FocusEvent e) {
		confirmChange(e);
	}

}
