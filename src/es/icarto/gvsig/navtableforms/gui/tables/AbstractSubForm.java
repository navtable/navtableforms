package es.icarto.gvsig.navtableforms.gui.tables;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.navtableforms.DependencyHandler;
import es.icarto.gvsig.navtableforms.FillHandler;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ValidationHandler;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public abstract class AbstractSubForm extends JPanel implements IForm,
	IValidatableForm, IWindow, IWindowListener {
    private FormPanel formPanel;
    private HashMap<String, JComponent> widgets;
    private ValidationHandler validationHandler;
    private IController iController;
    private ORMLite ormlite;
    private FillHandler fillHandler;
    private DependencyHandler dependencyHandler;
    private boolean isFillingValues;
    private boolean changedValues;
    private Logger logger;
    private JPanel southPanel;
    private JButton saveButton;
    private Map<String, String> foreingKey;

    private WindowInfo windowInfo;
    private final static int windowInfoCode = WindowInfo.MODELESSDIALOG
	    | WindowInfo.PALETTE | WindowInfo.RESIZABLE;
    private long position;
    private ActionListener action;
    private TableModelAlphanumeric model;

    public AbstractSubForm() {
	super();
	logger = Logger.getLogger(getClass());
	initGUI();
	ormlite = new ORMLite(getMetadataPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);
    }

    private void initGUI() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	FormPanel formPanel = getFormPanel("ui/" + getBasicName() + ".xml");
	widgets = AbeilleParser.getWidgetsFromContainer(formPanel);
	// AbeilleUtils au = new AbeilleUtils();
	// au.formatLabels(formPanel);
	// au.formatTextArea(formPanel);
	add(formPanel);
	add(getSouthPanel());
	setFocusCycleRoot(true);
    }

    public void setForeingKey(Map<String, String> foreingKey) {
	this.foreingKey = foreingKey;
    }

    private JPanel getSouthPanel() {
	if (southPanel == null) {
	    southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    saveButton = new JButton("Salvar");
	    action = new CreateAction(this);
	    saveButton.addActionListener(action);
	    southPanel.add(saveButton);
	}
	return southPanel;
    }

    public void setListeners() {
	validationHandler.setListeners(widgets);
	dependencyHandler.setListeners();
    }

    public void removeListeners() {
	validationHandler.removeListeners(widgets);
	dependencyHandler.removeListeners();
    }

    public void fillEmptyValues() {
	setFillingValues(true);
	iController.clearAll();
	((TableController) iController).initMetadata();
	fillHandler.fillEmptyWidgetsAndController();
	for (String f : foreingKey.keySet()) {
	    String value = foreingKey.get(f);
	    ((JTextField) widgets.get(f)).setText(value);
	    iController.setValue(f, value);
	}
	fillSpecificValues();
	dependencyHandler.fillValues();
	setFillingValues(false);
    }

    protected void setFillingValues(boolean b) {
	isFillingValues = b;
    }

    protected abstract void fillSpecificValues();

    protected abstract String getBasicName();

    public void fillValues() {
	setFillingValues(true);
	try {
	    iController.read(position);
	    fillHandler.fillValues();
	    dependencyHandler.fillValues();
	    fillSpecificValues();
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace());
	}
	setFillingValues(false);
	validationHandler.validate();
    }

    private FormPanel getFormPanel(String resourcePath) {
	if (formPanel == null) {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream(resourcePath);
	    try {
		formPanel = new FormPanel(stream);
	    } catch (FormException e) {
		logger.error(e.getStackTrace());
	    }
	}
	return formPanel;
    }

    private String getMetadataPath() {
	return this.getClass().getClassLoader()
		.getResource("metadata/" + getBasicName() + ".xml").getPath();
    }

    public HashMap<String, JComponent> getWidgets() {
	return widgets;
    }

    @Override
    public boolean isFillingValues() {
	return isFillingValues;
    }

    @Override
    public IController getFormController() {
	return iController;
    }

    @Override
    public void setChangedValues() {
	int[] indexes = iController.getIndexesOfValuesChanged();

	if (indexes.length > 0) {
	    setChangedValues(true);
	} else {
	    setChangedValues(false);
	}
    }

    protected void enableSaveButton(boolean bool) {

	if (model != null && model.getSource().isEditing()) {
	    saveButton.setEnabled(false);
	} else if (!isChangedValues()) {
	    saveButton.setEnabled(false);
	} else if (validationHandler.hasValidationErrors()) {
	    saveButton.setEnabled(false);
	} else {
	    saveButton.setEnabled(bool);
	}
    }

    protected boolean isChangedValues() {
	return changedValues;
    }

    @Override
    public FillHandler getFillHandler() {
	return fillHandler;
    }

    @Override
    public void validateForm() {
	validationHandler.validate();
	enableSaveButton(isChangedValues());
    }

    protected void setChangedValues(boolean bool) {
	changedValues = bool;
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(windowInfoCode);
	    windowInfo.setTitle(PluginServices.getText(this, getBasicName()));
	    Dimension dim = getPreferredSize();
	    MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
	    int maxHeight = a.getHeight() - 175;
	    int maxWidth = a.getWidth() - 15;

	    int width, heigth = 0;
	    if (dim.getHeight() > maxHeight) {
		heigth = maxHeight;
	    } else {
		heigth = new Double(dim.getHeight()).intValue();
	    }
	    if (dim.getWidth() > maxWidth) {
		width = maxWidth;
	    } else {
		width = new Double(dim.getWidth()).intValue();
	    }
	    windowInfo.setWidth(width + 15);
	    windowInfo.setHeight(heigth + 15);
	}
	return windowInfo;
    }

    @Override
    public Object getWindowProfile() {
	return WindowInfo.DIALOG_PROFILE;
    }

    @Override
    public void windowClosed() {
	removeListeners();
    }

    @Override
    public void windowActivated() {
    }

    @Override
    public void actionCreateRecord() {
	this.position = -1;
	saveButton.removeActionListener(action);
	action = new CreateAction(this);
	saveButton.addActionListener(action);
	fillEmptyValues();
	setListeners();
	PluginServices.getMDIManager().addWindow(this);
    }

    @Override
    public void actionUpdateRecord(long position) {
	this.position = position;
	saveButton.removeActionListener(action);
	action = new SaveAction(this);
	saveButton.addActionListener(action);
	fillValues();
	setListeners();
	PluginServices.getMDIManager().addWindow(this);
    }

    @Override
    public void actionDeleteRecord(long position) {
	try {
	    model.delete((int) position);
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    @Override
    public IEditableSource getSource() {
	return model.getSource();
    }

    @Override
    public void setModel(TableModelAlphanumeric model) {
	this.model = model;
	iController = model.getController();
	fillHandler = new FillHandler(widgets, iController,
		ormlite.getAppDomain());
    }

    private final class CreateAction implements ActionListener {

	private IWindow iWindow;

	public CreateAction(IWindow iWindow) {
	    this.iWindow = iWindow;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    HashMap<String, String> values = iController.getValues();
	    try {
		model.create(values);
	    } catch (Exception e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getStackTrace());
	    }
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}
    }

    private final class SaveAction implements ActionListener {

	private IWindow iWindow;

	public SaveAction(IWindow iWindow) {
	    this.iWindow = iWindow;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    try {
		model.update((int) position);
	    } catch (ReadDriverException e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getStackTrace());
	    }
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}
    }
}
