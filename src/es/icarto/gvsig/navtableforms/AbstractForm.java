/*
 * Copyright (c) 2011. iCarto
 * 
 * This file is part of extNavTableForms
 * 
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package es.icarto.gvsig.navtableforms;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.gui.formattedtextfields.DoubleFormatterOnDisplaying;
import es.icarto.gvsig.navtableforms.gui.formattedtextfields.DoubleFormatterOnEditing;
import es.icarto.gvsig.navtableforms.ormlite.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.KeyValue;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.FormController;
import es.icarto.gvsig.navtableforms.utils.FormParserUtils;
import es.icarto.gvsig.navtableforms.validation.ComponentValidator;
import es.icarto.gvsig.navtableforms.validation.FormValidator;
import es.icarto.gvsig.navtableforms.validation.listeners.ValidationHandlerForCheckBoxes;
import es.icarto.gvsig.navtableforms.validation.listeners.ValidationHandlerForComboBoxes;
import es.icarto.gvsig.navtableforms.validation.listeners.ValidationHandlerForTextAreas;
import es.icarto.gvsig.navtableforms.validation.listeners.ValidationHandlerForTextFields;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;

@SuppressWarnings("serial")
public abstract class AbstractForm extends AbstractNavTable {

    private FormValidator formValidator;
    private FormController formController;
    private final FormPanel formBody;
    private boolean isFillingValues;

    private JPanel NorthPanel;
    private JPanel SouthPanel;
    private JPanel CenterPanel;

    private HashMap<String, JComponent> widgetsVector;

    private FLyrVect layer = null;
    private ValidationHandlerForFormattedTextFields validationHandlerForFormattedTextFields;
    private ValidationHandlerForTextFields validationHandlerForTextFields;
    private ValidationHandlerForComboBoxes validationHandlerForComboBoxes;
    private ValidationHandlerForCheckBoxes validationHandlerForCheckBoxes;
    private ValidationHandlerForTextAreas validationHandlerForTextAreas;

    private static Logger logger = null;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	this.layer = layer;
	logger = getLoggerName();
	formBody = getFormBody();
	initValidation();
    }

    private void initValidation() {
	formValidator = new FormValidator();
	validationHandlerForFormattedTextFields = new ValidationHandlerForFormattedTextFields(
		this);
	validationHandlerForTextFields = new ValidationHandlerForTextFields(
		this);
	validationHandlerForComboBoxes = new ValidationHandlerForComboBoxes(
		this);
	validationHandlerForCheckBoxes = new ValidationHandlerForCheckBoxes(
		this);
	validationHandlerForTextAreas = new ValidationHandlerForTextAreas(
		this);
    }

    public abstract FormPanel getFormBody();

    public abstract String getXMLPath();

    public abstract Logger getLoggerName();

    private JPanel getThisNorthPanel() {
	if (NorthPanel == null) {
	    NorthPanel = new JPanel();
	}
	return NorthPanel;
    }

    private JPanel getThisSouthPanel() {
	if (SouthPanel == null) {
	    SouthPanel = new JPanel();
	}
	return SouthPanel;
    }

    @Override
    public JPanel getCenterPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	JScrollPane scrollPane = new JScrollPane(formBody);
	panel.add(scrollPane);
	return panel;
    }

    private JPanel getThisCenterPanel() {
	if (CenterPanel == null) {
	    CenterPanel = new JPanel();
	    BorderLayout CenterPanelLayout = new BorderLayout();
	    CenterPanel.setLayout(CenterPanelLayout);
	}
	return CenterPanel;
    }

    public String getValueFromLayer(String colName) {
	return formController.getValue(colName);
    }

    protected void removeListeners() {
	for (JComponent c : widgetsVector.values()) {
	    if (c instanceof JFormattedTextField) {
		((JTextField) c).removeKeyListener(validationHandlerForFormattedTextFields);
	    } else if (c instanceof JTextField) {
		((JTextField) c).removeKeyListener(validationHandlerForTextFields);
	    } else if (c instanceof JComboBox) {
		((JComboBox) c).removeActionListener(validationHandlerForComboBoxes);
	    } else if (c instanceof JCheckBox) {
		((JCheckBox) c).removeActionListener(validationHandlerForCheckBoxes);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c).removeKeyListener(validationHandlerForTextAreas);
	    }
	}
    }

    public void initWidgets() {
	widgetsVector = FormParserUtils.getWidgetsFromContainer(formBody);
	widgetsVector.size();
    }

    /**
     * This method has been deprecated in favor of formController,
     * use instead getFormController().getValuesOriginal() 
     * or getFormController.getValuesChanged()
     */
    @Deprecated
    public HashMap<String, String> getWidgetValues() {
	return formController.getValuesChanged();
    }

    /**
     * This method has been deprecated in favor of formController,
     * use instead getFormController().setValue(key, value)
     */
    @Deprecated
    public void setWidgetValues(String key, String value) {
	formController.setValue(key, value);
    }


    public FormController getFormController() {
	return formController;
    }

    public HashMap<String, JComponent> getWidgetComponents() {
	return widgetsVector;
    }

    private void initGUI() {
	MigLayout thisLayout = new MigLayout("inset 0, align center", "[grow]",
		"[][grow][]");
	this.setLayout(thisLayout);

	this.add(getThisNorthPanel(), "shrink, wrap, align center");
	this.add(getThisCenterPanel(), "shrink, growx, growy, wrap");
	this.add(getThisSouthPanel(), "shrink, align center");
    }

    protected void setListeners() {
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JFormattedTextField) {
		((JTextField) comp)
		.addKeyListener(validationHandlerForFormattedTextFields);
		ComponentValidator cv = new ComponentValidator(comp);
		formValidator.addComponentValidator(cv);
	    } else if (comp instanceof JTextField) {
		((JTextField) comp)
		.addKeyListener(validationHandlerForTextFields);
		ComponentValidator cv = new ComponentValidator(comp);
		formValidator.addComponentValidator(cv);
	    } else if (comp instanceof JComboBox) {
		((JComboBox) comp)
		.addActionListener(validationHandlerForComboBoxes);
	    } else if (comp instanceof JCheckBox) {
		((JCheckBox) comp)
		.addActionListener(validationHandlerForCheckBoxes);
	    } else if (comp instanceof JTextArea) {
		((JTextArea) comp).addKeyListener(validationHandlerForTextAreas);
	    }
	}
    }

    @Override
    public boolean init() {

	try {
	    if (recordset.getRowCount() <= 0) {
		JOptionPane.showMessageDialog(this,
			PluginServices.getText(this, "emptyLayer"));
		return false;
	    }
	} catch (HeadlessException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}

	initGUI();

	JPanel northPanel = getNorthPanel();
	getThisNorthPanel().add(northPanel);

	JPanel centerPanel = getCenterPanel();
	getThisCenterPanel().add(centerPanel);

	JPanel southPanel = getSouthPanel();
	getThisSouthPanel().add(southPanel);

	initWidgets();
	setListeners();

	formController = new FormController();
	try {
	    formController.fill(layer.getSource().getRecordset(), getPosition());
	    addPositionListener(formController);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	// super.last();
	refreshGUI();
	super.repaint();
	super.setVisible(true);
	super.setFocusCycleRoot(true);

	super.setChangedValues(false);
	super.enableSaveButton(true);
	setOpenNavTableForm(true);
	return true;
    }

    @Override
    public void fillEmptyValues() {
	setFillingValues(true);
	for (JComponent comp : widgetsVector.values()) {
	    if ((comp instanceof JFormattedTextField) ||
		    (comp instanceof JTextField) ||
		    (comp instanceof JTextArea)) {
		((JTextComponent) comp).setText("");
	    } else if (comp instanceof JComboBox) {
		if (((JComboBox) comp).getItemCount() > 0) {
		    ((JComboBox) comp).removeAllItems();
		}
		((JComboBox) comp).addItem("");
		((JComboBox) comp).setSelectedIndex(0);
	    } 
	}
	setFillingValues(false);
    }

    protected void fillJTextField(JTextField field) {
	String colName = field.getName();
	String fieldValue = formController.getValue(colName);
	field.setText(fieldValue);
    }

    protected void fillJFormattedTextField(JFormattedTextField field) {
	DoubleFormatterOnDisplaying displayFormatter = new DoubleFormatterOnDisplaying();
	DoubleFormatterOnEditing editFormatter = new DoubleFormatterOnEditing();
	DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory();
	formatterFactory.setDefaultFormatter(displayFormatter);
	formatterFactory.setDisplayFormatter(displayFormatter);
	formatterFactory.setEditFormatter(editFormatter);
	formatterFactory.setNullFormatter(displayFormatter);
	field.setFormatterFactory(formatterFactory);
	String fieldValue = formController.getValue(field.getName());
	//field.setText(fieldValue);
	field.setValue(fieldValue);
    }

    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = checkBox.getName();
	String fieldValue = formController.getValue(colName);
	checkBox.setSelected(Boolean.parseBoolean(fieldValue));
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = textArea.getName();
	String fieldValue = formController.getValue(colName);
	textArea.setText(fieldValue);
    }

    protected void fillJComboBox(JComboBox combobox) {
	String colName = combobox.getName();
	String fieldValue = formController.getValue(colName);
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(colName);
	if (dv != null) { // the component has domain values defined
	    addDomainValuesToComboBox(combobox, dv.getValues());
	    setDomainValueSelected(combobox, fieldValue);
	} else {
	    fillJComboBoxWithAbeilleValues(combobox, fieldValue);
	}
    }

    public void fillJComboBox(JComboBox combobox, ArrayList<String> foreignKeys) {
	String colName = combobox.getName();
	String fieldValue = formController.getValue(colName);
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(colName);
	if (dv != null) { // the component has domain values defined
	    addDomainValuesToComboBox(combobox, dv.getValuesFilteredBy(foreignKeys));
	    setDomainValueSelected(combobox, fieldValue);
	} else {
	    fillJComboBoxWithAbeilleValues(combobox, fieldValue);
	}
    }

    protected void addDomainValuesToComboBox(JComboBox cb,
	    ArrayList<KeyValue> keyValueList) {

	if (cb.getItemCount() > 0) {
	    cb.removeAllItems();
	}
	for (KeyValue kv : keyValueList) {
	    cb.addItem(kv);
	}
    }

    protected void setDomainValueSelected(JComboBox combobox, String fieldValue) {
	// the value in this case here is the key in the key-value pair
	// value = alias to be shown
	// key = value to save in the database
	if(fieldValue != null) {	    
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
	if(fieldValue != null) {	    
	    for (int j = 0; j < combobox.getItemCount(); j++) {
		if (combobox.getItemAt(j).toString().compareTo(fieldValue.trim()) == 0) {
		    combobox.setSelectedIndex(j);
		    break;
		}
	    }
	}
    }

    protected abstract void fillSpecificValues();

    @Override
    public void fillValues() {
	setFillingValues(true);
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
	fillSpecificValues();
	setFillingValues(false);
	formValidator.validate();
    }

    public boolean isFillingValues() {
	return isFillingValues;
    }

    protected void setFillingValues(boolean b) {
	isFillingValues = b;
    }

    protected boolean validationHasErrors() {
	boolean hasError = formValidator.hasValidationErrors();
	if (hasError) {
	    JOptionPane.showMessageDialog(this, formValidator.getMessages(),
		    PluginServices.getText(this, "Error de validacion"),
		    JOptionPane.ERROR_MESSAGE);
	}
	return hasError;
    }

    protected boolean isSaveable() {
	if (validationHasErrors()) {
	    return false;
	}
	return true;
    }

    /*
     * @return an vector whith the values changed. Take into account that this
     * method will only check the values defined in the form which can be a
     * subset of the ones in the layer. For example: gid field, which is
     * something gvsig needs, is not useful for the user so showing it in the
     * form makes no sense.
     */
    protected Vector<Integer> getIndexesOfChangedValues() {
	int[] idxs = getIndexes();
	Vector<Integer> indexes = new Vector<Integer>();
	for(int i=0; i<idxs.length; i++) {
	    indexes.add(idxs[i]);
	}
	return indexes;
    }

    public void setChangedValues() {
	Vector<Integer> indexes = getIndexesOfChangedValues();
	if (indexes.size() > 0) {
	    setChangedValues(true);
	} else {
	    setChangedValues(false);
	}
	enableSaveButton(isChangedValues());
    }

    protected String[] getValues() {
	return formController.getValuesChanged().values().toArray(new String[0]);
    }

    public int[] getIndexes() {
	return formController.getIndexesOfValuesChanged();
    }

    @Override
    protected boolean saveRecord() {
	if (isSaveable()) {
	    int currentPos = Long.valueOf(getPosition()).intValue();
	    int[] attIndexes = getIndexes();
	    String[] attValues = getValues();

	    try {
		ToggleEditing te = new ToggleEditing();
		boolean wasEditing = layer.isEditing();
		if (!wasEditing) {
		    te.startEditing(layer);
		}
		te.modifyValues(layer, currentPos, attIndexes, attValues);
		if (!wasEditing) {
		    te.stopEditing(layer, false);
		}
		setChangedValues(false);
		return true;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		return false;
	    }
	}
	return false;
    }

    @Override
    public void windowClosed() {
	removeListeners();
	super.windowClosed();
    }

    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void selectRow(int row) {

    }

    public FormValidator getFormValidator() {
	return this.formValidator;
    }

}
