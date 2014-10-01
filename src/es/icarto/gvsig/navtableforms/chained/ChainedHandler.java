package es.icarto.gvsig.navtableforms.chained;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;

public class ChainedHandler {

    private final Collection<DependentComboboxHandler> chainedList = new ArrayList<DependentComboboxHandler>();

    public void removeListeners() {
	for (DependentComboboxHandler handler : chainedList) {
	    for (JComponent c : handler.getParents()) {
		if (c instanceof JTextField) {
		    ((JTextField) c).removeFocusListener(handler);
		} else if (c instanceof JComboBox) {
		    ((JComboBox) c).removeActionListener(handler);
		}
	    }
	}
    }

    public void setListeners() {
	for (DependentComboboxHandler handler : chainedList) {
	    for (JComponent c : handler.getParents()) {
		if (c instanceof JTextField) {
		    ((JTextField) c).addFocusListener(handler);
		} else if (c instanceof JComboBox) {
		    ((JComboBox) c).addActionListener(handler);
		}
	    }
	}
    }

    public void fillEmptyValues() {
	fillValues();
    }

    public void fillValues() {
	for (DependentComboboxHandler handler : chainedList) {
	    handler.fillChainedComponent();
	}
    }

    public void add(IValidatableForm form, JComponent chained, JComponent parent) {
	chainedList.add(new DependentComboboxHandler(form, parent, chained));
    }

    public void add(IValidatableForm form, JComponent chained,
	    List<JComponent> parents) {
	chainedList.add(new DependentComboboxHandler(form, parents, chained));
    }

}
