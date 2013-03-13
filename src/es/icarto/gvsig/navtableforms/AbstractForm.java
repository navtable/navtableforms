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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.DependencyReader;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.EnabledComponentBasedOnWidget;

import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

@SuppressWarnings("serial")
public abstract class AbstractForm extends AbstractNavTable implements
	IValidatableForm {

    private IController layerController;
    protected FormPanel formBody;
    private boolean isFillingValues;
    private boolean isSavingValues = false;

    private HashMap<String, JComponent> widgetsVector;

    protected static Logger logger = null;
    private ORMLite ormlite;
    public FillFactory fillFactory;

    private ValidationHandler validationHandler;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	logger = Logger.getLogger(getClass());
	formBody = getFormBody();
	validationHandler = new ValidationHandler(getXMLPath(), this);

    }

    public abstract FormPanel getFormBody();

    public abstract String getXMLPath();

    @Deprecated
    public Logger getLoggerName() {
	return Logger.getLogger(getClass());
    }

    @Override
    public void validateForm() {
	validationHandler.validate();
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
	validationHandler.removeListeners(widgetsVector);
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
	fillFactory = new FillFactory(getWidgetComponents(), layerController,
		ormlite.getAppDomain());
    }

    /**
     * This method has been deprecated in favor of formController, use instead
     * getFormController().getValuesOriginal() or
     * getFormController.getValuesChanged()
     */
    @Deprecated
    public HashMap<String, String> getWidgetValues() {
	return layerController.getValuesChanged();
    }

    /**
     * This method has been deprecated in favor of formController, use instead
     * getFormController().setValue(key, value)
     */
    @Deprecated
    public void setWidgetValues(String key, String value) {
	layerController.setValue(key, value);
    }

    @Override
    public IController getFormController() {
	return layerController;
    }

    public HashMap<String, JComponent> getWidgetComponents() {
	return widgetsVector;
    }

    protected void setListeners() {  
	validationHandler.setListeners(widgetsVector);
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
	fillFactory.fillEmptyValues();
	setFillingValues(false);
    }

    protected abstract void fillSpecificValues();

    @Override
    public void fillValues() {
	setFillingValues(true);
	fillFactory.fillValues();
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
	validationHandler.validate();
    }

    @Override
    public boolean isFillingValues() {
	return isFillingValues;
    }

    protected void setFillingValues(boolean b) {
	isFillingValues = b;
    }

    protected boolean validationHasErrors() {
	boolean hasError = validationHandler.hasValidationErrors();
	if (hasError) {
	    JOptionPane.showMessageDialog(this,
		    validationHandler.getMessages(),
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
	for (int i = 0; i < idxs.length; i++) {
	    indexes.add(idxs[i]);
	}
	return indexes;
    }

    @Override
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
	return layerController.getValuesChanged().values()
		.toArray(new String[0]);
    }

    @Override
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
		layerController.update(getPosition());
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
	removeListeners();
	super.windowClosed();
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void selectRow(int row) {

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

    @Override
    public FillFactory getFillFactory() {
	return fillFactory;
    }
}
