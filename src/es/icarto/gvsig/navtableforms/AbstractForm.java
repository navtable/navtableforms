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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowProperties;
import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowPropertiesSerializator;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

@SuppressWarnings("serial")
public abstract class AbstractForm extends AbstractNavTable implements
	IValidatableForm {

    protected FormPanel formBody;
    private boolean isFillingValues;
    private boolean isSavingValues = false;

    HashMap<String, JComponent> widgets;

    protected static Logger logger = null;

    private ORMLite ormlite;

    private FormWindowProperties formWindowProperties;
    private List<FormWindowProperties> formWindowPropertiesList;
    private FillHandler fillHandler;

    private ValidationHandler validationHandler;
    private DependencyHandler dependencyHandler;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	logger = Logger.getLogger(getClass());
	formBody = getFormBody();
	initFormWindowProperties();
	widgets = AbeilleParser.getWidgetsFromContainer(formBody);
	ormlite = new ORMLite(getXMLPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);

    }

    private void initFormWindowProperties() {
	boolean hasPropertiesSaved = false;
	getFormWindowProperties();

	for (FormWindowProperties fwp : formWindowPropertiesList) {
	    if (fwp.getFormName().equalsIgnoreCase(getClass().getName())) {
		viewInfo.setHeight(fwp.getFormWindowHeight());
		viewInfo.setWidth(fwp.getFormWindowWidth());
		viewInfo.setX(fwp.getFormWindowXPosition());
		viewInfo.setY(fwp.getFormWindowYPosition());
		hasPropertiesSaved = true;
	    }
	}

	if (!hasPropertiesSaved) {
	    formWindowProperties = new FormWindowProperties();
	    formWindowProperties.setFormName(getClass().getName());

	    MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
	    final int SCROLL_AND_BORDER = 50;
	    final int TOOL_MENU_STATE_BAR = 180;
	    int maxHeight = a.getHeight() - TOOL_MENU_STATE_BAR;

	    int calculateTotalFormWindowHeight = formBody.getPreferredSize().height
		    + getNorthPanel().getPreferredSize().height
		    + getSouthPanel().getPreferredSize().height
		    + SCROLL_AND_BORDER;

	    if (maxHeight < calculateTotalFormWindowHeight) {
		viewInfo.setHeight(maxHeight);
	    } else {
		viewInfo.setHeight(calculateTotalFormWindowHeight);
	    }

	    final int calculateTotalFormWidth = getNorthPanel()
		    .getPreferredSize().width + SCROLL_AND_BORDER;
	    if (calculateTotalFormWidth > formBody.getPreferredSize().width) {
		viewInfo.setWidth(calculateTotalFormWidth);
	    } else {
		viewInfo.setWidth(formBody.getPreferredSize().width);
	    }

	}
    }

    public abstract FormPanel getFormBody();

    public abstract String getXMLPath();

    public String getFormWindowPropertiesXMLPath() {
	return Launcher.getAppHomeDir() + File.separator
		+ "FormWindowProperties.xml";
    }

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
	validationHandler.removeListeners(widgets);
	dependencyHandler.removeListeners();

    }

    @Override
    protected void initWidgets() {
	setListeners();
	fillHandler = new FillHandler(getWidgetComponents(), layerController,
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
	return widgets;
    }

    protected void setListeners() {
	validationHandler.setListeners(widgets);
	dependencyHandler.setListeners();
    }

    @Override
    public void fillEmptyValues() {
	setFillingValues(true);
	fillHandler.fillEmptyValues();
	dependencyHandler.fillValues();
	setFillingValues(false);
    }

    protected abstract void fillSpecificValues();

    @Override
    public void fillValues() {
	setFillingValues(true);
	fillHandler.fillValues();
	dependencyHandler.fillValues();
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
		fwp.setFormWindowXPosition(viewInfo.getX());
		fwp.setFormWindowYPosition(viewInfo.getY());
		update = true;
		break;
	    }
	}

	if (!update) {
	    FormWindowProperties fwpToAdd = new FormWindowProperties();
	    fwpToAdd.setFormName(getClass().getName());
	    fwpToAdd.setFormWindowHeight(viewInfo.getHeight());
	    fwpToAdd.setFormWindowWidth(viewInfo.getWidth());
	    fwpToAdd.setFormWindowXPosition(viewInfo.getX());
	    fwpToAdd.setFormWindowYPosition(viewInfo.getY());
	    formWindowPropertiesList.add(fwpToAdd);
	}

	String xml = FormWindowPropertiesSerializator
		.toXML(formWindowPropertiesList);
	try {
	    FileWriter fileWriter = new FileWriter(new File(
		    getFormWindowPropertiesXMLPath()));
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
	    formWindowPropertiesList = FormWindowPropertiesSerializator
		    .fromXML(new File(getFormWindowPropertiesXMLPath()));
	}
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
    public FillHandler getFillHandler() {
	return fillHandler;

    }
}
