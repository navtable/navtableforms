package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

import es.icarto.gvsig.navtableforms.IValidatableForm;

public class ValidationHandlerForTextAreas implements KeyListener {

	private IValidatableForm dialog = null;

	public ValidationHandlerForTextAreas(IValidatableForm dialog) {
		this.dialog = dialog;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!dialog.isFillingValues()) {
			JTextArea textArea = ((JTextArea) e.getSource());
			dialog.getFormController().setValue(textArea.getName(), textArea.getText());
			dialog.setChangedValues();
			dialog.validateForm();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
