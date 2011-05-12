/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
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
package es.udc.cartolab.gvsig.navtableforms;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
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

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;
import es.udc.cartolab.gvsig.navtableforms.validation.ComponentValidator;
import es.udc.cartolab.gvsig.navtableforms.validation.DomainValues;
import es.udc.cartolab.gvsig.navtableforms.validation.FormValidator;

public abstract class AbstractForm extends AbstractNavTable implements
	KeyListener {

    private FormValidator formValidator = null;
    protected final FormPanel formBody;
    private boolean isFillingValues;

    private JPanel NorthPanel;
    private JPanel SouthPanel;
    private JPanel CenterPanel;

    protected Vector<JComponent> widgetsVector;
    protected HashMap<String, String> widgetValues;

    protected static BaseView view;
    protected FLyrVect layer = null;

    protected static Logger logger = null;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	formBody = getFormBody();
	formValidator = new FormValidator();
	logger = getLoggerName();
	this.layer = layer;
	widgetValues = new HashMap<String, String>();
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

    protected String getNameBeforeDots(String widgetName) {
	if (widgetName.contains(".")) {
	    return widgetName.substring(0, widgetName.indexOf("."));
	} else {
	    return widgetName;
	}
    }

    protected void removeListeners() {
	for (JComponent c : widgetsVector) {
	    if (c instanceof JTextField) {
		((JTextField) c).removeKeyListener(this);
	    }
	}
    }

    public void initWidgets() {
	widgetsVector = FormParserUtils
		.getWidgetsWithContentFromContainer(formBody);
	for (int i = 0; i < widgetsVector.size(); i++) {
	    JComponent comp = widgetsVector.get(i);
	    if (comp instanceof JTextField) {
		comp.addKeyListener(this);
		ComponentValidator cv = new ComponentValidator(comp);
		formValidator.addComponentValidator(cv);
	    }
	}
    }

    private HashMap<String, String> getWidgetValues() {
	for (JComponent c : widgetsVector) {
	    if (c instanceof JTextField) {
		widgetValues.put(getNameBeforeDots(c.getName()),
			((JTextField) c).getText());
	    } else if (c instanceof JComboBox) {
		widgetValues.put(getNameBeforeDots(c.getName()),
			((JComboBox) c).getSelectedItem().toString());
	    }
	}
	return widgetValues;
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
	// To be used by children
    }

    @Override
    public boolean init() {

	view = (BaseView) PluginServices.getMDIManager().getActiveWindow();

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

	currentPosition = 0;
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
	super.fillEmptyValues();
	for (JComponent widget : widgetsVector) {
	    if (widget instanceof JFormattedTextField) {
		((JFormattedTextField) widget).setText("");
	    }
	    if (widget instanceof JComboBox) {
		if ((((JComboBox) widget).getItemCount() > 0)) {
		    ((JComboBox) widget).setSelectedIndex(0);
		}
	    }
	}
    }

    protected void fillJTextField(JTextField field) {
	String colName = getNameBeforeDots(field.getName());
	String fieldValue = Utils.getValueFromLayer(layer, currentPosition,
		colName);
	field.setText(fieldValue);
    }

    protected void fillJFormattedTextField(JFormattedTextField field) {
	fillJTextField(field);
    }

    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = getNameBeforeDots(checkBox.getName());
	String fieldValue = Utils.getValueFromLayer(layer, currentPosition,
		colName);
	checkBox.setSelected(Boolean.parseBoolean(fieldValue));
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = getNameBeforeDots(textArea.getName());
	String fieldValue = Utils.getValueFromLayer(layer, currentPosition,
		colName);
	textArea.setText(fieldValue);
    }

    protected void fillJComboBox(JComboBox combobox) {
	String colName = getNameBeforeDots(combobox.getName());
	String fieldValue = Utils.getValueFromLayer(layer, currentPosition,
		colName);
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLPath())
		.getDomainValuesForComponent(colName);
	if (dv != null) { // the component has domain values defined
	    if (combobox.getItemCount() > 0) {
		combobox.removeAllItems();
	    }
	    for (String value : dv.getValues()) {
		combobox.addItem(value);
	    }
	    combobox.setSelectedIndex(0);
	} else { // got the values from the abeille
	    if (combobox.getItemCount() > 0) {
		combobox.setSelectedIndex(0);
	    }
	}
	for (int j = 0; j < combobox.getItemCount(); j++) {
	    if (combobox.getItemAt(j).toString().compareTo(fieldValue.trim()) == 0) {
		combobox.setSelectedIndex(j);
		break;
	    }
	}
    }

    protected abstract void fillSpecificValues();

    @Override
    public void fillValues() {
	try {
	    setFillingValues(true);
	    if (currentPosition >= recordset.getRowCount()) {
		currentPosition = recordset.getRowCount() - 1;
	    }
	    if (currentPosition < 0) {
		currentPosition = 0;
	    }
	    for (int i = 0; i < widgetsVector.size(); i++) {
		JComponent comp = widgetsVector.get(i);
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
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	} finally {
	    setFillingValues(false);
	    formValidator.validate();
	}
    }

    private boolean isFillingValues() {
	return isFillingValues;
    }

    private void setFillingValues(boolean b) {
	isFillingValues = b;
    }

    @Override
    public void fillValues(long currentPos) {
	currentPosition = currentPos;
	fillValues();
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
	Vector<Integer> changedValues = new Vector<Integer>();
	try {
	    SelectableDataSource rs = layer.getRecordset();
	    HashMap<String, String> widgetValues = getWidgetValues();
	    Value value;
	    String valueInRecordSet;
	    String valueInModel;
	    for (String field : widgetValues.keySet()) {
		int index = -1;
		String[] modelFields = rs.getFieldNames();
		for (int i = 0; i < modelFields.length; i++) {
		    if (modelFields[i].toLowerCase().equals(field)) {
			index = i;
		    }
		}
		if (index > -1) {
		    value = rs.getFieldValue(currentPosition, index);
		    valueInRecordSet = value
			    .getStringValue(ValueWriter.internalValueWriter);
		    valueInModel = widgetValues.get(field);
		    valueInRecordSet = valueInRecordSet.replaceAll("''", "")
			    .trim();
		    valueInModel = valueInModel.trim();

		    if (!valueInRecordSet.equals(valueInModel)) {
			changedValues.add(new Integer(index));
		    }

		}
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	return changedValues;
    }

    protected void setChangedValues() {
	Vector<Integer> indexes = getIndexesOfChangedValues();
	if (indexes.size() > 0) {
	    setChangedValues(true);
	} else {
	    setChangedValues(false);
	}
	enableSaveButton(isChangedValues());
    }

    protected String[] getValues() {
	HashMap<String, String> layerValues;
	String[] attValues;
	layerValues = getWidgetValues();
	attValues = layerValues.values().toArray(new String[0]);
	return attValues;
    }

    public int[] getIndexes() {
	String[] names = null;
	int[] indexes = null;
	try {
	    Set<String> widgetsValuesKeys = getWidgetValues().keySet();
	    names = new String[widgetsValuesKeys.size()];
	    Iterator<String> it = widgetsValuesKeys.iterator();
	    int i = 0;
	    while (it.hasNext()) {
		names[i] = it.next();
		i++;
	    }
	    indexes = Utils.getIndexes(layer, names);
	    return indexes;
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    return indexes;
	}
    }

    @Override
    protected boolean saveRecord() {
	if (isSaveable()) {
	    int currentPos = Long.valueOf(currentPosition).intValue();
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

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
	if (!isFillingValues()) {
	    setChangedValues();
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
	formValidator.validate();
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void selectRow(int row) {
    }
}
