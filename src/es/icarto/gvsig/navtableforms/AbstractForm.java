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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import sun.print.PSPrinterJob.PluginPrinter;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;


import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowProperties;
import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowPropertiesSerializator;
import es.icarto.gvsig.navtableforms.gui.formattedtextfields.FormatterFactory;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorComponent;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForCheckBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForComboBoxes;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForFormattedTextFields;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextAreas;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.ValidationHandlerForTextFields;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.DependencyReader;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.EnabledComponentBasedOnWidget;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.dataacces.LayerController;


@SuppressWarnings("serial")
public abstract class AbstractForm extends AbstractNavTable {


    private ValidatorForm formValidator;
    private LayerController layerController;
    protected FormPanel formBody;
    private boolean isFillingValues;
    private boolean isSavingValues = false;

    private HashMap<String, JComponent> widgetsVector;

    private ValidationHandlerForFormattedTextFields validationHandlerForFormattedTextFields;
    private ValidationHandlerForTextFields validationHandlerForTextFields;
    private ValidationHandlerForComboBoxes validationHandlerForComboBoxes;
    private ValidationHandlerForCheckBoxes validationHandlerForCheckBoxes;
    private ValidationHandlerForTextAreas validationHandlerForTextAreas;

    protected static Logger logger = null;
    private ORMLite ormlite;
    
    private FormWindowProperties formWindowProperties;
    private List<FormWindowProperties> formWindowPropertiesList;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	logger = Logger.getLogger(getClass());
	formBody = getFormBody();
	initFormWindowProperties();
	initValidation();
    }
    
    private void initFormWindowProperties() {
	boolean hasPropertiesSaved = false;
	getFormWindowProperties();
	
	for (FormWindowProperties fwp : formWindowPropertiesList) {
	    if (fwp.getFormName().equalsIgnoreCase(getClass().getName())) {
		viewInfo.setHeight(fwp.getFormWindowHeight());
		viewInfo.setWidth(fwp.getFormWindowWidth());
		hasPropertiesSaved = true;
	    }
	}
	
	if (!hasPropertiesSaved) {
	    formWindowProperties = new FormWindowProperties();
	    formWindowProperties.setFormName(getClass().getName());
	    viewInfo.setHeight(
		    formBody.getPreferredSize().height + 
		    getNorthPanel().getPreferredSize().height +
		    getSouthPanel().getPreferredSize().height);
	    if (getNorthPanel().getPreferredSize().width > 
	    	formBody.getPreferredSize().width) {
		viewInfo.setWidth(getNorthPanel().getPreferredSize().width);
	    }else {
		viewInfo.setWidth(formBody.getPreferredSize().width);
	    }
	    
	}
    }

    private void initValidation() {
	ormlite = new ORMLite(getXMLPath());
	formValidator = new ValidatorForm();
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
    
    public String getFormWindowPropertiesXMLPath() {
	return Launcher.getAppHomeDir() + File.separator +
		"FormWindowProperties.xml";
    }

    @Deprecated
    public Logger getLoggerName() {
	return Logger.getLogger(getClass());
    }

    @Override
    public JPanel getCenterPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	JScrollPane scrollPane = new JScrollPane(formBody);
	panel.add(scrollPane);
	return panel;
    }

    public String getValueFromLayer(String colName) {
	return layerController.getValue(colName);
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
	
	for (JComponent comp : widgetsVector.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain().getDependencyValuesForComponent(
			comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = 
			new EnabledComponentBasedOnWidget(getWidgetComponents().get(
				values.getComponent()), comp, values.getValue());
		componentBasedOnWidget.removeListeners();
	    }
	}
    }

    @Override
    protected void initWidgets() {
	widgetsVector = AbeilleParser.getWidgetsFromContainer(formBody);
	setListeners();
    }

    /**
     * This method has been deprecated in favor of formController,
     * use instead getFormController().getValuesOriginal()
     * or getFormController.getValuesChanged()
     */
    @Deprecated
    public HashMap<String, String> getWidgetValues() {
	return layerController.getValuesChanged();
    }

    /**
     * This method has been deprecated in favor of formController,
     * use instead getFormController().setValue(key, value)
     */
    @Deprecated
    public void setWidgetValues(String key, String value) {
	layerController.setValue(key, value);
    }


    public LayerController getFormController() {
	return layerController;
    }

    public HashMap<String, JComponent> getWidgetComponents() {
	return widgetsVector;
    }


    protected void setListeners() {
	for (JComponent comp : widgetsVector.values()) {
	    if (comp instanceof JFormattedTextField) {
		((JFormattedTextField) comp).addKeyListener(
			validationHandlerForFormattedTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(
				comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JTextField) {
		((JTextField) comp).addKeyListener(
			validationHandlerForTextFields);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(
				comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JComboBox) {
		((JComboBox) comp).addActionListener(
			validationHandlerForComboBoxes);
		ValidatorDomain dv = ormlite.getAppDomain()
			.getDomainValidatorForComponent(
				comp.getName());
		if (dv != null) {
		    ValidatorComponent cv = new ValidatorComponent(comp, dv);
		    formValidator.addComponentValidator(cv);
		}
	    } else if (comp instanceof JCheckBox) {
		((JCheckBox) comp)
			.addActionListener(validationHandlerForCheckBoxes);
	    } else if (comp instanceof JTextArea) {
		((JTextArea) comp)
			.addKeyListener(validationHandlerForTextAreas);
	    }
	}
	    
	for (JComponent comp : widgetsVector.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(
		    comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain().getDependencyValuesForComponent(
			comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = 
			new EnabledComponentBasedOnWidget(getWidgetComponents().get(
				values.getComponent()), comp, values.getValue());
		componentBasedOnWidget.setRemoveDependentValues(true);
		componentBasedOnWidget.setListeners();
	    }
	    
	    if (ormlite.getAppDomain().isNonEditableComponent(comp.getName()) != null
		    && ormlite.getAppDomain().isNonEditableComponent(
			    comp.getName())) {
		getWidgetComponents().get(comp.getName()).setEnabled(false);
	    }
	}
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
	String fieldValue = layerController.getValue(colName);
	field.setText(fieldValue);
    }

    protected void fillJFormattedTextField(JFormattedTextField field) {
	field.setFormatterFactory(FormatterFactory.createFormatterFactory(
		layerController.getType(field.getName())));
	String fieldValue = layerController.getValue(field.getName());
	//field.setText(fieldValue);
	field.setValue(fieldValue);
    }

    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = checkBox.getName();
	String fieldValue = layerController.getValue(colName);
	checkBox.setSelected(Boolean.parseBoolean(fieldValue));
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = textArea.getName();
	String fieldValue = layerController.getValue(colName);
	textArea.setText(fieldValue);
    }

    protected void fillJComboBox(JComboBox combobox) {
	String colName = combobox.getName();
	String fieldValue = layerController.getValue(colName);
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		colName);
	if (dv != null) { // the component has domain values defined
	    addDomainValuesToComboBox(combobox, dv.getValues());
	    setDomainValueSelected(combobox, fieldValue);
	} else {
	    fillJComboBoxWithAbeilleValues(combobox, fieldValue);
	}
    }

    public void fillJComboBox(JComboBox combobox, ArrayList<String> foreignKeys) {
	String colName = combobox.getName();
	String fieldValue = layerController.getValue(colName);
	DomainValues dv = ormlite.getAppDomain().getDomainValuesForComponent(
		colName);
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
	
	for (JComponent comp : widgetsVector.values()) {
	    if (ormlite.getAppDomain().getDependencyValuesForComponent(comp.getName()) != null) {
		DependencyReader values = ormlite.getAppDomain().getDependencyValuesForComponent(
			comp.getName());
		EnabledComponentBasedOnWidget componentBasedOnWidget = 
			new EnabledComponentBasedOnWidget(getWidgetComponents().get(
				values.getComponent()), comp, values.getValue());
		componentBasedOnWidget.fillSpecificValues();
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


    /**
     * Use getFormController.getIndexesOfValuesChanged() instead
     */
    @Deprecated
    protected Vector<Integer> getIndexesOfChangedValues() {
	int[] idxs = layerController.getIndexesOfValuesChanged();
	Vector<Integer> indexes = new Vector<Integer>();
	for(int i=0; i<idxs.length; i++) {
	    indexes.add(idxs[i]);
	}
	return indexes;
    }

    public void setChangedValues() {
	int[] indexes = layerController.getIndexesOfValuesChanged();

	if (indexes.length > 0) {
	    setChangedValues(true);
	} else {
	    setChangedValues(false);
	}
	enableSaveButton(isChangedValues());
    }

    protected String[] getValues() {
	return layerController.getValuesChanged().values().toArray(new String[0]);
    }

    public boolean isSavingValues() {
	return isSavingValues;
    }

    public void setSavingValues(boolean bool) {
	isSavingValues = bool;
    }

    @Override
    public boolean saveRecord() {
	if (isSaveable()) {
	    setSavingValues(true);
	    try {
		layerController.save(getPosition());
		setChangedValues(false);
		setSavingValues(false);
		return true;
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		layerController.clearAll();
		setChangedValues(false);
		setSavingValues(false);
		return false;
	    }
	}
	return false;
    }

    @Override
    public void windowClosed() {
	super.windowClosed();
	removeListeners();
	writeFormWindowProperties();
    }

    private void writeFormWindowProperties() {
	boolean update = false;
	for (FormWindowProperties fwp : formWindowPropertiesList) {
	    if (fwp.getFormName().equalsIgnoreCase(getClass().getName())) {
		fwp.setFormWindowHeight(viewInfo.getHeight());
		fwp.setFormWindowWidth(viewInfo.getWidth());
		update = true;
		break;
	    }
	}

	if (!update) {
	    FormWindowProperties fwpToAdd = new FormWindowProperties();
	    fwpToAdd.setFormName(getClass().getName());
	    fwpToAdd.setFormWindowHeight(viewInfo.getHeight());
	    fwpToAdd.setFormWindowWidth(viewInfo.getWidth());
	    formWindowPropertiesList.add(fwpToAdd);
	}
	
	String xml = FormWindowPropertiesSerializator.toXML(formWindowPropertiesList);
	try {
	    FileWriter fileWriter= new FileWriter(new File(getFormWindowPropertiesXMLPath()));
            Writer writer = new BufferedWriter(fileWriter);
            writer.write(xml);
            writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    private void getFormWindowProperties() {
	if (!new File(getFormWindowPropertiesXMLPath()).exists()) {
	    formWindowPropertiesList = new ArrayList<FormWindowProperties>();
	} else {
	    formWindowPropertiesList = FormWindowPropertiesSerializator.fromXML(
		    new File(getFormWindowPropertiesXMLPath()));
	}
    }

    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void selectRow(int row) {

    }

    public ValidatorForm getFormValidator() {
	return this.formValidator;
    }

    @Override
    protected void undoAction() {
	setChangedValues(false);
	try {
	    layerController.read(getPosition());
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e.getCause());
	    layerController.clearAll();
	}
	refreshGUI();
    }

}
