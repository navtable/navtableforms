package es.icarto.gvsig.navtableforms.ormlite.widgetsdependency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import es.icarto.gvsig.navtableforms.IValidatableForm;

public class EnabledComponentBasedOnWidget implements ActionListener {

    private JComponent component;
    private JComponent widget;
    private String value;
    private boolean removeDependentValues;
    private IValidatableForm form;
    // to store the table listeners when the component is a jtable
    private MouseListener[] listeners = new MouseListener[0];

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
	listeners = component.getMouseListeners() != null ? component
		.getMouseListeners() : new MouseListener[0];
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
	    enableComponentIfWidgetIsComboBox();
	}
    }

    private void enableComponentIfWidgetIsComboBox() {
	if (((JComboBox) widget).getSelectedItem() != null) {
	    String selected = ((JComboBox) widget).getSelectedItem().toString();
	    changeComponentState(selected.equalsIgnoreCase(value));
	    if (removeDependentValues) {
		removeValue(component);
	    }
	}
    }

    private void enableComponentIfWidgetIsCheckBox() {
	boolean selected = ((JCheckBox) widget).isSelected();
	changeComponentState(String.valueOf(selected).equalsIgnoreCase(value));
	if (removeDependentValues) {
	    removeValue(component);
	}
    }

    private void changeComponentState(boolean enabled) {
	if (component instanceof JTable) {
	    // If the component is a table, we adjust its viewport and
	    // remove/restore its listeners.
	    if (enabled) {
		((JTable) component).setFillsViewportHeight(false);
		listeners = component.getMouseListeners();
		for (MouseListener l : listeners) {
		    component.removeMouseListener(l);
		}
	    } else {
		if (component instanceof JTable) {
		    ((JTable) component).setFillsViewportHeight(true);
		    for (MouseListener l : listeners) {
			component.addMouseListener(l);
		    }
		}
	    }
	} else {
	    if ((component instanceof JComboBox) && !enabled) {
		// If the component is a combobox and we are disabling
		// it, prior to that we select the default item.
		((JComboBox) component).setSelectedIndex(0);
	    } else {
		if ((component instanceof JCheckBox) && !enabled
			&& ((JCheckBox) component).isSelected()) {
		    // If the component is a checkbox, we are disabling
		    // it and it was checked, prior to that we uncheck it.
		    ((JCheckBox) component).doClick();
		}
	    }
	}
	component.setEnabled(enabled);
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

	listeners = new MouseListener[0];
    }

    public void fillValues() {
	enableComponent();
    }

}
