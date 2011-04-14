/*
 * Copyright (c) 2010. Cartolab (Universidade da Coru�a)
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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
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

import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultRow;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.drivers.ITableDefinition;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IWriteable;
import com.iver.cit.gvsig.fmap.edition.IWriter;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.jeta.forms.components.panel.FormPanel;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.ToggleEditing;
import es.udc.cartolab.gvsig.navtableforms.validation.FormBinding;
import es.udc.cartolab.gvsig.navtableforms.validation.FormModel;
import es.udc.cartolab.gvsig.navtableforms.validation.FormParserUtils;
import es.udc.cartolab.gvsig.navtableforms.validation.ValidationComponentFactory;

public abstract class AbstractAlphanumericForm extends AbstractNavTable
	implements ItemListener, KeyListener {

    protected final FormModel formModel;
    protected final FormBinding formBinding;
    protected IEditableSource model;
    protected final FormPanel formBody;

    private JPanel NorthPanel;
    private JPanel SouthPanel;
    private JPanel CenterPanel;
    protected JButton newB = null;
    protected Vector<JComponent> widgetsVector;

    protected static Logger logger = null;
    private ValidationChangeHandler validationChangeHandler;
    private boolean isFillingValues;

    public AbstractAlphanumericForm(IEditableSource model)
	    throws ReadDriverException {
	super(model.getRecordset());
	this.model = model;
	formBody = getFormBody();
	formModel = getFormModel(formBody);
	formBinding = getFormBinding(formModel);
	logger = getLoggerName();
    }

    @Override
    protected JPanel getActionsToolBar() {
	JPanel actionsToolBar = new JPanel(new FlowLayout());
	actionsToolBar.add(getButton(BUTTON_COPY_SELECTED));
	actionsToolBar.add(getButton(BUTTON_COPY_PREVIOUS));
	actionsToolBar.add(getButton(BUTTON_SELECTION));
	actionsToolBar.add(getButton(BUTTON_SAVE));
	actionsToolBar.add(getButton(BUTTON_REMOVE));
	newB = getNavTableButton(newB, "/table_add.png", "new_register");
	actionsToolBar.add(newB);
	return actionsToolBar;
    }

    @Override
    protected JPanel getOptionsPanel() {
	JPanel optionsPanel = new JPanel(new FlowLayout());
	optionsPanel.add(onlySelectedCB);
	optionsPanel.add(alwaysSelectCB);
	return optionsPanel;
    }

    public abstract FormPanel getFormBody();

    public abstract FormModel getFormModel(Container c);

    public abstract FormBinding getFormBinding(FormModel model);

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

    protected String getPropertyKey(String widgetName) {
	String name = getNameBeforeDots(widgetName);
	return name.trim().toUpperCase();
    }

    private String getValidateKey(String widgetName) {
	return formModel.getModelName() + "." + getNameBeforeDots(widgetName);
    }

    protected void initJFormattedTextField(JFormattedTextField field) {
	String propertyKey = getPropertyKey(field.getName());
	String validateKey = getValidateKey(field.getName());
	ValidationComponentFactory
		.bindFormattedTextField(field, formBinding
			.getModel(FormModel.PROPERTIES_MAP.get(propertyKey)),
			false);

	// ValidationComponentUtils.setMandatory(comp, true);
	ValidationComponentUtils.setMessageKey(field, validateKey);
	field.addKeyListener(this);
    }

    protected void initJTextField(JTextField field) {
	String propertyKey = getPropertyKey(field.getName());
	String validateKey = getValidateKey(field.getName());
	ValidationComponentFactory
		.bindTextField(field, formBinding
			.getModel(FormModel.PROPERTIES_MAP.get(propertyKey)),
			false);
	// ValidationComponentUtils.setMandatory(comp, true);
	ValidationComponentUtils.setMessageKey(field, validateKey);
	field.addKeyListener(this);
    }

    protected void initJTextArea(JTextArea textArea) {
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	String propertyKey = getPropertyKey(textArea.getName());
	// String validateKey = getValidateKey(textArea.getName());
	ValidationComponentFactory
		.bindTextArea(textArea, formBinding
			.getModel(FormModel.PROPERTIES_MAP.get(propertyKey)),
			true);
	textArea.addKeyListener(this);
    }

    protected void initJCheckBox(JCheckBox checkBox) {
	String propertyKey = getPropertyKey(checkBox.getName());
	// String validateKey = getValidateKey(checkBox.getName());
	ValidationComponentFactory
		.bindCheckBox(checkBox, formBinding
			.getModel(FormModel.PROPERTIES_MAP.get(propertyKey)));
	checkBox.addItemListener(this);
    }

    protected String[] getJComboBoxValues(JComboBox comboBox) {
	int nvalues = comboBox.getItemCount();
	String[] values;
	if (nvalues > 0) {
	    values = new String[nvalues];
	    for (int j = 0; j < nvalues; j++) {
		values[j] = comboBox.getItemAt(j).toString();
	    }
	} else {
	    values = new String[] { "" };
	}
	return values;
    }

    protected void initJComboBox(JComboBox comboBox) {
	String propertyKey = getPropertyKey(comboBox.getName());
	String[] values = getJComboBoxValues(comboBox);
	ValidationComponentFactory
		.bindComboBox(comboBox, values, formBinding
			.getModel(FormModel.PROPERTIES_MAP.get(propertyKey)));
	comboBox.addItemListener(this);
    }

    protected void setListeners() {
	validationChangeHandler = new ValidationChangeHandler();
    }

    protected void removeListeners() {
	for (JComponent c : widgetsVector) {
	    if (c instanceof JCheckBox) {
		((JCheckBox) c).removeItemListener(this);
	    } else if (c instanceof JComboBox) {
		((JComboBox) c).removeItemListener(this);
	    } else if ((c instanceof JTextField)
		    || (c instanceof JFormattedTextField)) {
		((JTextField) c).removeKeyListener(this);
	    }
	}
	formBinding.removePropertyChangeListener(validationChangeHandler);
    }

    public void initWidgets() {
	widgetsVector = FormParserUtils
		.getWidgetsWithContentFromContainer(formBody);
	for (int i = 0; i < widgetsVector.size(); i++) {
	    JComponent comp = widgetsVector.get(i);
	    if (comp instanceof JFormattedTextField) {
		initJFormattedTextField((JFormattedTextField) comp);
	    } else if (comp instanceof JTextField) {
		initJTextField((JTextField) comp);
	    } else if (comp instanceof JTextArea) {
		initJTextArea((JTextArea) comp);
	    } else if (comp instanceof JCheckBox) {
		initJCheckBox((JCheckBox) comp);
	    } else if (comp instanceof JComboBox) {
		initJComboBox((JComboBox) comp);
	    }
	}
    }

    protected void initEventHandling() {
	formBinding.getValidationResultModel().addPropertyChangeListener(
		ValidationResultModel.PROPERTYNAME_RESULT,
		validationChangeHandler);
    }

    private void initGUI() {
	MigLayout thisLayout = new MigLayout("inset 0, align center", "[grow]",
		"[][grow][]");
	this.setLayout(thisLayout);

	this.add(getThisNorthPanel(), "shrink, wrap, align center");
	this.add(getThisCenterPanel(), "shrink, growx, growy, wrap");
	this.add(getThisSouthPanel(), "shrink, align center");
    }

    @Override
    public boolean init() {
	initGUI();

	JPanel northPanel = getNorthPanel();
	getThisNorthPanel().add(northPanel);

	JPanel centerPanel = getCenterPanel();
	getThisCenterPanel().add(centerPanel);

	JPanel southPanel = getSouthPanel();
	getThisSouthPanel().add(southPanel);

	initWidgets();
	setListeners();
	initEventHandling();

	// Synchronize the presentation with the current validation state.
	// If you want to show no validation info before the user has typed
	// anything in the form, use the commented EMPTY result
	// instead of the model's validation result.
	updateComponentTreeMandatoryAndSeverity(formBinding
		.getValidationResultModel().getResult()
	// ValidationResult.EMPTY
	);

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

    private void showErrorTableIsMainWindow() {
	// TODO Auto-generated method stub

    }

    private IWindow getActiveWindow() {
	return PluginServices.getMDIManager().getActiveWindow();
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
	String fieldValue = Utils.getValue(recordset, currentPosition, colName);
	field.setText(fieldValue);
    }

    protected void fillJFormattedTextField(JFormattedTextField field) {
	fillJTextField(field);
    }

    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = getNameBeforeDots(checkBox.getName());
	String fieldValue = Utils.getValue(recordset, currentPosition, colName);
	checkBox.setSelected(Boolean.parseBoolean(fieldValue));
    }

    protected void fillJTextArea(JTextArea textArea) {
	String colName = getNameBeforeDots(textArea.getName());
	String fieldValue = Utils.getValue(recordset, currentPosition, colName);
	textArea.setText(fieldValue);
    }

    protected void fillJComboBox(JComboBox combobox) {
	String colName = getNameBeforeDots(combobox.getName());
	String fieldValue = Utils.getValue(recordset, currentPosition, colName);

	if (combobox.getItemCount() > 0) {
	    combobox.setSelectedIndex(0);
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
	}
    }

    @Override
    public void fillValues(long currentPos) {
	currentPosition = currentPos;
	fillValues();
    }

    protected boolean validationHasErrors() {
	boolean hasError = false;
	ValidationResult vr = formBinding.getValidationResultModel()
		.getResult();
	if (vr.hasErrors()) {
	    hasError = true;
	    JOptionPane.showMessageDialog(this, vr.getMessagesText(),
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

    protected String[] getValues() {
	Map<String, String> values;
	String[] attValues;
	values = formModel.getWidgetValues();
	attValues = values.values().toArray(new String[0]);
	return attValues;
    }

    public int[] getIndexes() {
	String[] names = null;
	int[] indexes = null;
	try {
	    Set<String> widgetsValuesKeys = formModel.getWidgetValues()
		    .keySet();
	    names = new String[widgetsValuesKeys.size()];
	    Iterator<String> it = widgetsValuesKeys.iterator();
	    int i = 0;
	    while (it.hasNext()) {
		names[i] = it.next();
		i++;
	    }
	    indexes = Utils.getIndexes(recordset, names);
	    return indexes;
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    return indexes;
	}
    }

    @Override
    protected boolean saveRecord() {
	if (isSaveable()) {
	    int[] attIndexes = getIndexes();
	    String[] attValues = getValues();
	    int currentPos = Long.valueOf(currentPosition).intValue();
	    try {
		ToggleEditing te = new ToggleEditing();
		boolean wasEditing = model.isEditing();
		if (!wasEditing) {
		    te.startEditing(model);
		}
		te.modifyValues(model, currentPos, attIndexes, attValues);
		if (!wasEditing) {
		    te.stopEditing(model);
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
    protected void deleteRecord() {
	try {
	    model.startEdition(EditionEvent.ALPHANUMERIC);

	    IWriteable w = (IWriteable) model;
	    IWriter writer = w.getWriter();

	    ITableDefinition tableDef = model.getTableDefinition();
	    writer.initialize(tableDef);

	    model.doRemoveRow((int) currentPosition, EditionEvent.ALPHANUMERIC);
	    model.stopEdition(writer, EditionEvent.ALPHANUMERIC);

	    refreshGUI();

	} catch (StartWriterVisitorException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	} catch (InitializeWriterException e) {
	    logger.error(e.getMessage(), e);
	} catch (StopWriterVisitorException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    // Listeners
    // Properties Event Handling
    // *********************************************************
    protected void updateComponentTreeMandatoryAndSeverity(
	    ValidationResult result) {
	// ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(
	// CenterPanel);
	ValidationComponentUtils.updateComponentTreeSeverityBackground(
		CenterPanel, result);
    }

    /**
     * Updates the component background in the mandatory panel and the
     * validation background in the severity panel. Invoked whenever the
     * observed validation result changes.
     */
    protected final class ValidationChangeHandler implements
	    PropertyChangeListener {

	public void propertyChange(PropertyChangeEvent evt) {
	    updateComponentTreeMandatoryAndSeverity((ValidationResult) evt
		    .getNewValue());
	}
    }

    private void addRecord() {
	// Create a new empty record
	// showWarning();
	if (onlySelectedCB.isSelected()) {
	    onlySelectedCB.setSelected(false);
	}
	try {
	    model.startEdition(EditionEvent.ALPHANUMERIC);

	    if (model instanceof IWriteable) {

		IRow row;
		int numAttr = recordset.getFieldCount();
		Value[] values = new Value[numAttr];
		for (int i = 0; i < numAttr; i++) {
		    values[i] = ValueFactory.createNullValue();
		}
		row = new DefaultRow(values);
		model.doAddRow(row, EditionEvent.ALPHANUMERIC);

		IWriteable w = (IWriteable) model;
		IWriter writer = w.getWriter();

		ITableDefinition tableDef = model.getTableDefinition();
		writer.initialize(tableDef);

		model.stopEdition(writer, EditionEvent.ALPHANUMERIC);
		last();
		setChangedValues(true);
		refreshGUI();
	    }
	} catch (StartWriterVisitorException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	} catch (InitializeWriterException e) {
	    logger.error(e.getMessage(), e);
	} catch (StopWriterVisitorException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == newB) {
	    addRecord();
	} else if (e.getSource() == removeB) {
	    deleteRecord();
	} else {
	    super.actionPerformed(e);
	}
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
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
	if (!isFillingValues()) {
	    setChangedValues();
	}
    }

    private boolean isFillingValues() {
	return isFillingValues;
    }

    private void setFillingValues(boolean b) {
	isFillingValues = b;
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

    protected Vector<Integer> getIndexesOfChangedValues() {
	Vector<Integer> changedValues = new Vector<Integer>();
	try {
	    SelectableDataSource rs = model.getRecordset();
	    Map<String, String> widgetValues = formModel.getWidgetValues();
	    Value value;
	    String key;
	    String valueInRecordSet;
	    String valueInModel;
	    for (int index = 0; index < rs.getFieldCount(); index++) {
		value = rs.getFieldValue(currentPosition, index);
		valueInRecordSet = value
			.getStringValue(ValueWriter.internalValueWriter);
		key = rs.getFieldName(index);
		valueInModel = widgetValues.get(key.toLowerCase());
		valueInRecordSet = valueInRecordSet.replaceAll("''", "").trim();
		valueInModel = valueInModel.trim();
		if (!valueInRecordSet.equals(valueInModel)) {
		    changedValues.add(new Integer(index));
		}
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	return changedValues;
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void selectRow(int row) {
	// TODO Auto-generated method stub
    }
}
