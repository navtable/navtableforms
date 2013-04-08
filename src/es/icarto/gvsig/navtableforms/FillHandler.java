package es.icarto.gvsig.navtableforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import es.icarto.gvsig.navtableforms.gui.formattedtextfields.FormatterFactory;
import es.icarto.gvsig.navtableforms.ormlite.ORMLiteAppDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public class FillHandler {

    // TODO: make as much methods as possible private
    private HashMap<String, JComponent> widgetsVector;
    private IController iController;
    private ORMLiteAppDomain appDomain;

    public FillHandler(HashMap<String, JComponent> widgetsVector,
	    IController layerController, ORMLiteAppDomain appDomain) {
	this.widgetsVector = widgetsVector;
	this.iController = layerController;
	this.appDomain = appDomain;
    }

    public void fillEmptyValues() {
	for (JComponent comp : widgetsVector.values()) {
	    if ((comp instanceof JFormattedTextField)
		    || (comp instanceof JTextField)
		    || (comp instanceof JTextArea)) {
		((JTextComponent) comp).setText("");
	    } else if (comp instanceof JComboBox) {
		if (((JComboBox) comp).getItemCount() > 0) {
		    ((JComboBox) comp).removeAllItems();
		}
		((JComboBox) comp).addItem("");
		((JComboBox) comp).setSelectedIndex(0);
	    } else if (comp instanceof JCheckBox) {
		((JCheckBox) comp).setEnabled(false);
	    }
	}
    }

    public void fillEmptyWidgetsAndController() {
	for (String w : widgetsVector.keySet()) {
	    JComponent comp = widgetsVector.get(w);
	    if ((comp instanceof JFormattedTextField)
		    || (comp instanceof JTextField)
		    || (comp instanceof JTextArea)) {
		((JTextComponent) comp).setText("");
		iController.setValue(w, "");
	    } else if (comp instanceof JComboBox) {
		iController.setValue(w, " ");
		fillJComboBox((JComboBox) comp);
	    } else if (comp instanceof JCheckBox) {
		iController.setValue(w, "false");
		((JCheckBox) comp).setSelected(false);
	    }
	}
    }

    protected void fillJTextField(JTextField field) {
	String colName = field.getName();
	String fieldValue = iController.getValue(colName);
	field.setText(fieldValue);
    }

    protected void fillJFormattedTextField(JFormattedTextField field) {
	field.setFormatterFactory(FormatterFactory
		.createFormatterFactory(iController.getType(field.getName())));
	String fieldValue = iController.getValue(field.getName());
	field.setValue(fieldValue);
    }

    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = checkBox.getName();
	String fieldValue = iController.getValue(colName);
	checkBox.setSelected(Boolean.parseBoolean(fieldValue));
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = textArea.getName();
	String fieldValue = iController.getValue(colName);
	textArea.setText(fieldValue);
    }

    public void fillJComboBox(JComboBox combobox) {
	String colName = combobox.getName();
	String fieldValue = iController.getValue(colName);
	DomainValues dv = appDomain.getDomainValuesForComponent(colName);
	if (dv != null) { // the component has domain values defined
	    addDomainValuesToComboBox(combobox, dv.getValues());
	    setDomainValueSelected(combobox, fieldValue);
	} else {
	    fillJComboBoxWithAbeilleValues(combobox, fieldValue);
	}
    }

    private void addDomainValuesToComboBox(JComboBox cb,
	    ArrayList<KeyValue> keyValueList) {

	if (cb.getItemCount() > 0) {
	    cb.removeAllItems();
	}
	for (KeyValue kv : keyValueList) {
	    cb.addItem(kv);
	}
    }

    private void setDomainValueSelected(JComboBox combobox, String fieldValue) {
	// the value in this case here is the key in the key-value pair
	// value = alias to be shown
	// key = value to save in the database
	if (fieldValue != null) {
	    for (int j = 0; j < combobox.getItemCount(); j++) {
		String value = ((KeyValue) combobox.getItemAt(j)).getKey();
		if (value.compareTo(fieldValue.trim()) == 0) {
		    combobox.setSelectedIndex(j);
		    break;
		}
	    }
	}
	combobox.setEnabled(true);
	if (combobox.getSelectedIndex() == -1) {
	    combobox.addItem(new KeyValue("", "", ""));
	    combobox.setSelectedIndex(0);
	    combobox.setEnabled(false);
	}
    }

    private void fillJComboBoxWithAbeilleValues(JComboBox combobox,
	    String fieldValue) {
	if (combobox.getItemCount() > 0) {
	    combobox.setSelectedIndex(0);
	}
	if (fieldValue != null) {
	    for (int j = 0; j < combobox.getItemCount(); j++) {
		if (combobox.getItemAt(j).toString()
			.compareTo(fieldValue.trim()) == 0) {
		    combobox.setSelectedIndex(j);
		    break;
		}
	    }
	}
    }

    public void fillJComboBox(JComboBox combobox, ArrayList<String> foreignKeys) {
	String colName = combobox.getName();
	String fieldValue = iController.getValue(colName);
	DomainValues dv = appDomain.getDomainValuesForComponent(colName);
	if (dv != null) { // the component has domain values defined
	    addDomainValuesToComboBox(combobox,
		    dv.getValuesFilteredBy(foreignKeys));
	    setDomainValueSelected(combobox, fieldValue);
	} else {
	    fillJComboBoxWithAbeilleValues(combobox, fieldValue);
	}
    }

    public void fillValues() {
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JFormattedTextField) {
		fillJFormattedTextField((JFormattedTextField) comp);
	    } else if (comp instanceof JTextField) {
		fillJTextField((JTextField) comp);
	    } else if (comp instanceof JCheckBox) {
		fillJCheckBox((JCheckBox) comp);
	    } else if (comp instanceof JTextArea) {
		fillJTextArea((JTextArea) comp);
	    } else if (comp instanceof JComboBox) {
		fillJComboBox((JComboBox) comp);
	    }
	}
    }

}
