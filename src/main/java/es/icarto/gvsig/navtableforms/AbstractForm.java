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

import static es.icarto.gvsig.commons.i18n.I18n._;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.andami.Launcher;
import org.gvsig.andami.PluginServices;
import org.gvsig.andami.ui.mdiManager.WindowInfo;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.navtableforms.calculation.Calculation;
import es.icarto.gvsig.navtableforms.calculation.CalculationHandler;
import es.icarto.gvsig.navtableforms.chained.ChainedHandler;
import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowProperties;
import es.icarto.gvsig.navtableforms.forms.windowproperties.FormWindowPropertiesSerializator;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandler;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandlerManager;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

@SuppressWarnings("serial")
public abstract class AbstractForm extends AbstractNavTable implements
	IValidatableForm, II18nForm {

    
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractForm.class);

    protected FormPanel formBody;
    private boolean isFillingValues;
    private boolean isSavingValues = false;
    private final List<BaseTableHandler> tableHandlers = new ArrayList<BaseTableHandler>();

    HashMap<String, JComponent> widgets;

    private final ORMLite ormlite;

    private FillHandler fillHandler;

    private final I18nHandler i18nHandler;
    private final ValidationHandler validationHandler;
    private final DependencyHandler dependencyHandler;
    private final CalculationHandler calculationHandler;
    private final ChainedHandler chainedHandler;
    private ImageHandlerManager imageHandlerManager;

    private String title;

    public AbstractForm(FLyrVect layer) {
	super(layer);
	formBody = getFormPanel();
	i18nHandler = new I18nHandler(this);
	widgets = AbeilleParser.getWidgetsFromContainer(formBody);
	ormlite = new ORMLite(getXMLPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);
	calculationHandler = new CalculationHandler();
	chainedHandler = new ChainedHandler();
	imageHandlerManager = new ImageHandlerManager();
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    super.getWindowInfo();
	    if (title != null) {
		windowInfo.setTitle(title);
	    }
	    for (FormWindowProperties fwp : getFormWindowProperties()) {
		if (fwp.getFormName().equalsIgnoreCase(getClass().getName())) {
		    // WindowInfoSupport.getWindowInfo adds 40 to the
		    // getWindowInfo declared
		    // by IWindow objects
		    final int ANDAMI_CORRECTION = 40;
		    windowInfo.setHeight(fwp.getFormWindowHeight()
			    - ANDAMI_CORRECTION);
		    windowInfo.setWidth(fwp.getFormWindowWidth());
		    windowInfo.setX(fwp.getFormWindowXPosition());
		    windowInfo.setY(fwp.getFormWindowYPosition());
		}
	    }
	}
	return windowInfo;
    }

    @Deprecated
    /**
     * Use getFormPanel instead
     * @return
     */
    public abstract FormPanel getFormBody();
    
    public FormPanel getFormPanel() {
	return getFormBody();
    }

    @Override
    public I18nResource[] getI18nResources() {
	return null;
    }

    @Override
    public I18nHandler getI18nHandler() {
	return i18nHandler;
    }

    public abstract String getXMLPath();

    public String getFormWindowPropertiesXMLPath() {
	return Launcher.getAppHomeDir() + File.separator
		+ "FormWindowProperties.xml";
    }

    @Override
    public void validateForm() {
	validationHandler.validate();
    }

    @Override
    public ValidatorForm getValidatorForm() {
	return validationHandler.getValidatorForm();
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
	for (BaseTableHandler tableHandler : tableHandlers) {
	    tableHandler.removeListeners();
	}
	calculationHandler.removeListeners();
	chainedHandler.removeListeners();
	imageHandlerManager.removeListeners();
    }

    @Override
    protected void initGUI() {
	super.initGUI();
	i18nHandler.translateFormStaticTexts();
    }

    @Override
    protected void initWidgets() {
	setListeners();
	fillHandler = new FillHandler(getWidgets(), layerController,
		ormlite.getAppDomain());
	for (JComponent c : getWidgets().values()) {
	    if (c instanceof JDateChooser) {
		initDateChooser((JDateChooser) c);
	    }
	}
    }

    private void initDateChooser(JDateChooser c) {
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
	c.setDateFormatString(dateFormat.toPattern());
	c.getDateEditor().setEnabled(false);
	c.getDateEditor().getUiComponent()
		.setBackground(new Color(255, 255, 255));
	c.getDateEditor().getUiComponent()
		.setFont(new Font("Arial", Font.PLAIN, 11));
	c.getDateEditor().getUiComponent().setToolTipText(null);

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

    @Deprecated
    public HashMap<String, JComponent> getWidgetComponents() {
	return widgets;
    }

    @Override
    public Map<String, JComponent> getWidgets() {
	return widgets;
    }

    protected void setListeners() {
	validationHandler.setListeners(widgets);
	dependencyHandler.setListeners();
	for (BaseTableHandler tableHandler : tableHandlers) {
	    tableHandler.reload();
	    // When the layer is in edition mode, subforms must be disabled,
	    // because if the user, adds a new subelement
	    // with an fk or modifies the pk of the element it will fail
	    // We remove it after the reload, to allow propper initializations,
	    // and avoid NullPointerException when
	    // removing the listeners or when layerEvent is called
	    if (layer.isEditing()) {
		tableHandler.removeListeners();
	    }
	}
	calculationHandler.setListeners();
	chainedHandler.setListeners();
	imageHandlerManager.setListeners();
    }

    @Override
    public void resetListeners() {
	super.resetListeners();
	removeListeners();
	setListeners();
    }

    @Override
    public void fillEmptyValues() {
	setFillingValues(true);
	fillHandler.fillEmptyValues();
	dependencyHandler.fillValues();
	chainedHandler.fillEmptyValues();
	imageHandlerManager.fillEmptyValues();
	setFillingValues(false);
    }

    protected void fillSpecificValues() {
	String key = getPrimaryKeyValue();
	if (key != null) {
	    for (BaseTableHandler tableHandler : tableHandlers) {
		tableHandler.fillValues(key);
	    }
	}
    }

    protected String getPrimaryKeyValue() {
	return null;
    }

    @Override
    public void fillValues() {
	setFillingValues(true);
	fillHandler.fillValues();
	dependencyHandler.fillValues();
	chainedHandler.fillValues();
	imageHandlerManager.fillValues();
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
		    validationHandler.getMessages(),_("validation_error"), JOptionPane.ERROR_MESSAGE);
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
    public boolean saveRecord() throws DataException {
	if (isSaveable()) {
	    setSavingValues(true);
	    try {
		layerController.update(getPosition());
		setChangedValues(false);
		setSavingValues(false);
		return true;
	    } catch (DataException e) {
		logger.error(e.getMessage(), e);
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
	List<FormWindowProperties> formWindowPropertiesList = getFormWindowProperties();
	for (FormWindowProperties fwp : formWindowPropertiesList) {
	    if (fwp.getFormName().equalsIgnoreCase(getClass().getName())) {
		fwp.setFormWindowHeight(windowInfo.getHeight());
		fwp.setFormWindowWidth(windowInfo.getWidth());
		fwp.setFormWindowXPosition(windowInfo.getX());
		fwp.setFormWindowYPosition(windowInfo.getY());
		update = true;
		break;
	    }
	}

	if (!update) {
	    FormWindowProperties fwpToAdd = new FormWindowProperties();
	    fwpToAdd.setFormName(getClass().getName());
	    fwpToAdd.setFormWindowHeight(windowInfo.getHeight());
	    fwpToAdd.setFormWindowWidth(windowInfo.getWidth());
	    fwpToAdd.setFormWindowXPosition(windowInfo.getX());
	    fwpToAdd.setFormWindowYPosition(windowInfo.getY());
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

    private List<FormWindowProperties> getFormWindowProperties() {
	if (!new File(getFormWindowPropertiesXMLPath()).exists()) {
	    return new ArrayList<FormWindowProperties>();
	} else {
	    return FormWindowPropertiesSerializator.fromXML(new File(
		    getFormWindowPropertiesXMLPath()));
	}
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    protected void setTitle(String title) {
	this.title = title;
    }

    @Override
    public void selectRow(int row) {

    }

    @Override
    protected void undoAction() {
	setChangedValues(false);
	try {
	    layerController.read(getPosition());
	} catch (DataException e) {
	    logger.error(e.getMessage(), e.getCause());
	    layerController.clearAll();
	}
	refreshGUI();
    }

    @Override
    public FillHandler getFillHandler() {
	return fillHandler;

    }

    protected void addTableHandler(BaseTableHandler tableHandler) {
	tableHandlers.add(tableHandler);
    }

    public List<BaseTableHandler> getTableHandlers() {
	return tableHandlers;
    }

    protected void addCalculation(Calculation calculation) {
	calculationHandler.add(calculation);
    }

    protected void addChained(JComponent chained, JComponent parent) {
	chainedHandler.add(this, chained, parent);
    }

    protected void addChained(String chained, String parent) {
	chainedHandler.add(this, widgets.get(chained), widgets.get(parent));
    }

    protected void addChained(JComponent chained, JComponent... parents) {
	chainedHandler.add(this, chained, Arrays.asList(parents));
    }

    protected void addChained(String chained, String... parents) {
	List<JComponent> parentList = new ArrayList<JComponent>();
	for (String parent : parents) {
	    parentList.add(widgets.get(parent));
	}
	chainedHandler.add(this, widgets.get(chained), parentList);
    }

    /**
     * Instead of create an implementation of ImageHandler that only sets a path
     * (FixedImageHandler) this utiliy method sets the image without doing
     * anything more
     * 
     * @param imgComponent
     *            . Name of the abeille widget
     * @param absPath
     *            . Absolute path to the image or relative path from andami.jar
     */
    protected void addImageHandler(String imgComponent, String absPath) {
	ImageComponent image = (ImageComponent) formBody
		.getComponentByName(imgComponent);
	ImageIcon icon = new ImageIcon(absPath);
	image.setIcon(icon);
    }

    protected void addImageHandler(ImageHandler imageHandler) {
	imageHandlerManager.addHandler(imageHandler);
    }

    @Override
    public void layerEvent(LayerEvent e) {
	layerEventTables(e);
	super.layerEvent(e);
    }

    private void layerEventTables(LayerEvent e) {
	// When the layer is in edition mode, subforms must be disabled, because
	// if the user, adds a new subelement
	// with an fk or modifies the pk of the element it will fail
	if (e.getEventType() == LayerEvent.EDITION_CHANGED) {
	    if (layer.isEditing()) {
		for (BaseTableHandler bth : tableHandlers) {
		    bth.removeListeners();
		}
	    } else {
		for (BaseTableHandler bth : tableHandlers) {
		    final BaseTableModel bthModel = bth.getModel();
		    if (bthModel != null) {
			// This should never happen, but tablehandlers are not
			// correctly constructed and model field is not
			// correctly set
			bthModel.reloadUnderlyingData();
		    }
		    bth.reload();
		}
	    }
	}
    }
}
