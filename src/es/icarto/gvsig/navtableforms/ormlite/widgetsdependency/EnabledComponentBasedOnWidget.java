package es.icarto.gvsig.navtableforms.ormlite.widgetsdependency;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EnabledComponentBasedOnWidget implements ActionListener {
    
    private JComponent component;
    private JComponent widget;
    private String value;
    private boolean removeDependentValues;

    public EnabledComponentBasedOnWidget(JComponent widget,
	    JComponent component, String value) {
	this.widget = widget;
	this.component = component;
	this.value = value;
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

    public void actionPerformed(ActionEvent e) {
	enableComponent();
    }

    private void enableComponent() {
	if (widget instanceof JCheckBox) {
	    enableComponentIfWidgetIsCheckBox();
	}else if (widget instanceof JComboBox) {
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
	component.setEnabled(enabled);
	if (removeDependentValues) {
	removeValue(component);
	}
    }

    private void removeValue(JComponent c) {
	if (c instanceof JFormattedTextField) {
	    ((JFormattedTextField) c).setText("");
	} else if (c instanceof JTextField) {
	    ((JTextField) c).setText("");
	} else if (c instanceof JComboBox) {
	    ((JComboBox) c).setSelectedIndex(0);
	} else if (c instanceof JCheckBox) {
	    ((JCheckBox) c).setSelected(false);
	} else if (c instanceof JTextArea) {
	    ((JTextArea) c).setText("");
	}
    }

    public void removeListeners() {
	if (widget instanceof JCheckBox) {
	    ((JCheckBox)widget).removeActionListener(this);
	}
	if (widget instanceof JComboBox) {
	    ((JComboBox)widget).removeActionListener(this);
	}
    }

    public void fillValues() {
	enableComponent();
    }

}
