package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class ValidationHandlerForJDateChooser implements PropertyChangeListener {

    private final IValidatableForm dialog;
    private final SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();

    public ValidationHandlerForJDateChooser(IValidatableForm dialog) {
	this.dialog = dialog;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
	if (!dialog.isFillingValues()) {
	    JDateChooser widget = ((JDateChooser) e.getSource());
	    String widgetValue = dateFormat.format(widget.getDate());
	    dialog.getFormController().setValue(widget.getName(), widgetValue);
	    dialog.setChangedValues();
	    dialog.validateForm();
	}
    }

}
