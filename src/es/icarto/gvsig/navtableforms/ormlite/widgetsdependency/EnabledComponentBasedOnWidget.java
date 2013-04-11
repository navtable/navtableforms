package es.icarto.gvsig.navtableforms.ormlite.widgetsdependency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;

public class EnabledComponentBasedOnWidget implements ActionListener {

    private JComponent component;
    private JComponent widget;
    private String value;
    private boolean removeDependentValues;
    private IValidatableForm form;

    public EnabledComponentBasedOnWidget(JComponent widget,
	    JComponent component, String value, IValidatableForm form) {
	this.widget = widget;
	this.component = component;
	this.value = value;
	this.form = form;
    }

    public void setRemoveDependentValues(boolean removeDependentValues) {
	this.removeDependentValues = removeDependentValues;
    }

    public void setListeners() {
	if (widget instanceof JCheckBox) {
	    ((JCheckBox) widget).addActionListener(this);
	} else if (widget instanceof JComboBox) {
	    ((JComboBox) widget).addActionListener(this);
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (!form.isFillingValues()) {
	    enableComponent();
	}
    }

    private void enableComponent() {
	if (widget instanceof JCheckBox) {
	    enableComponentIfWidgetIsCheckBox();
	} else if (widget instanceof JComboBox) {
	    enableComponentIfWidgetIsCombBox();
	}
    }

    private void enableComponentIfWidgetIsCombBox() {
	if (((JComboBox) widget).getSelectedItem() != null) {
	    boolean enabled = ((JComboBox) widget).getSelectedItem().toString()
		    .equalsIgnoreCase(value);
	    component.setEnabled(enabled);
	    if (removeDependentValues) {
		removeValue(component);
	    }
	}
    }

    private void enableComponentIfWidgetIsCheckBox() {
	boolean enabled = ((JCheckBox) widget).isSelected();
	if (String.valueOf(enabled).equalsIgnoreCase(value)) {
	    component.setEnabled(true);
	} else {
	    component.setEnabled(false);
	}
	if (removeDependentValues) {
	    removeValue(component);
	}
    }

    private void removeValue(JComponent c) {
	String fieldName = c.getName();
	if (c instanceof JFormattedTextField) {
	    ((JFormattedTextField) c).setText("");
	    form.getFormController().setValue(fieldName, "");
	} else if (c instanceof JTextField) {
	    ((JTextField) c).setText("");
	    form.getFormController().setValue(fieldName, "");
	} else if (c instanceof JComboBox) {
	    ((JComboBox) c).setSelectedIndex(0);
	    form.getFormController().setValue(fieldName, " ");
	} else if (c instanceof JCheckBox) {
	    ((JCheckBox) c).setSelected(false);
	    form.getFormController().setValue(fieldName, "false");
	} else if (c instanceof JTextArea) {
	    ((JTextArea) c).setText("");
	    form.getFormController().setValue(fieldName, "");
	}
    }

    public void removeListeners() {
	if (widget instanceof JCheckBox) {
	    ((JCheckBox) widget).removeActionListener(this);
	}
	if (widget instanceof JComboBox) {
	    ((JComboBox) widget).removeActionListener(this);
	}
    }

    public void fillValues() {
	enableComponent();
    }

}
