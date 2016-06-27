package es.icarto.gvsig.navtableforms.gui.tables;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.gvsig.andami.PluginServices;
import org.gvsig.andami.messages.NotificationManager;
import org.gvsig.andami.ui.mdiFrame.MDIFrame;
import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.andami.ui.mdiManager.IWindowListener;
import org.gvsig.andami.ui.mdiManager.WindowInfo;
import org.gvsig.fmap.dal.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.commons.gvsig2.IEditableSource;
import es.icarto.gvsig.navtableforms.DependencyHandler;
import es.icarto.gvsig.navtableforms.FillHandler;
import es.icarto.gvsig.navtableforms.I18nHandler;
import es.icarto.gvsig.navtableforms.II18nForm;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ValidationHandler;
import es.icarto.gvsig.navtableforms.calculation.Calculation;
import es.icarto.gvsig.navtableforms.calculation.CalculationHandler;
import es.icarto.gvsig.navtableforms.chained.ChainedHandler;
import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandler;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandlerManager;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

@SuppressWarnings("serial")
public abstract class AbstractSubForm extends JPanel implements IForm,
IValidatableForm, IWindow, IWindowListener, II18nForm {

    
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractSubForm.class);

    private FormPanel formPanel;
    private HashMap<String, JComponent> widgets;
    private final I18nHandler i18nHandler;
    private final ValidationHandler validationHandler;
    private IController iController;
    private final ORMLite ormlite;
    private FillHandler fillHandler;
    private final DependencyHandler dependencyHandler;
    private final CalculationHandler calculationHandler;
    private final ChainedHandler chainedHandler;
    private ImageHandlerManager imageHandlerManager;
    private boolean isFillingValues;
    private boolean changedValues;

    private JPanel southPanel;
    protected JButton saveButton;
    private Map<String, String> foreingKey;
    private final List<BaseTableHandler> tableHandlers = new ArrayList<BaseTableHandler>();

    private WindowInfo windowInfo;
    private final static int windowInfoCode = WindowInfo.MODELESSDIALOG
	    | WindowInfo.PALETTE | WindowInfo.RESIZABLE;
    protected long position;
    protected ActionListener action;
    protected AlphanumericTableModel model;

    private String basicName;

    public AbstractSubForm(String basicName) {
	super(null);
	this.basicName = basicName;
	i18nHandler = new I18nHandler(this);
	initGUI();
	ormlite = new ORMLite(getMetadataPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);
	calculationHandler = new CalculationHandler();
	chainedHandler = new ChainedHandler();
	imageHandlerManager = new ImageHandlerManager();
    }

    public AbstractSubForm() {
	super();
	i18nHandler = new I18nHandler(this);
	initGUI();
	ormlite = new ORMLite(getMetadataPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);
	calculationHandler = new CalculationHandler();
	chainedHandler = new ChainedHandler();
	imageHandlerManager = new ImageHandlerManager();
    }

    private void initGUI() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	getFormPanel();
	JScrollPane scrollPane = new JScrollPane(formPanel);
	widgets = AbeilleParser.getWidgetsFromContainer(formPanel);
	for (JComponent c : getWidgets().values()) {
	    if (c instanceof JDateChooser) {
		initDateChooser((JDateChooser) c);
	    }
	}
	// AbeilleUtils au = new AbeilleUtils();
	// au.formatLabels(formPanel);
	// au.formatTextArea(formPanel);
	add(scrollPane);
	add(getSouthPanel());
	setFocusCycleRoot(true);
	i18nHandler.translateFormStaticTexts();
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

    public void setForeingKey(Map<String, String> foreingKey) {
	this.foreingKey = foreingKey;
    }

    public Map<String, String> getForeignKey() {
	return foreingKey;
    }

    private JPanel getSouthPanel() {
	if (southPanel == null) {
	    southPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
	    saveButton = new JButton("Guardar");
	    action = new CreateAction(this);
	    saveButton.addActionListener(action);
	    southPanel.add(saveButton);
	}
	return southPanel;
    }

    public void setListeners() {
	validationHandler.setListeners(widgets);
	dependencyHandler.setListeners();
	for (BaseTableHandler tableHandler : tableHandlers) {
	    tableHandler.reload();
	}
	calculationHandler.setListeners();
	chainedHandler.setListeners();
	imageHandlerManager.setListeners();
    }

    public void removeListeners() {
	validationHandler.removeListeners(widgets);
	dependencyHandler.removeListeners();
	for (BaseTableHandler tableHandler : tableHandlers) {
	    tableHandler.removeListeners();
	}
	calculationHandler.removeListeners();
	chainedHandler.removeListeners();
	imageHandlerManager.removeListeners();
    }

    public void fillEmptyValues() {
	setFillingValues(true);
	iController.clearAll();
	((TableController) iController).initMetadata();
	fillHandler.fillEmptyWidgetsAndController();
	for (String f : foreingKey.keySet()) {
	    String value = foreingKey.get(f);
	    JComponent widget = widgets.get(f);
	    if (widget != null) {
		if (widget instanceof JTextField) {
		    ((JTextField) widgets.get(f)).setText(value);
		} else if (widget instanceof JComboBox) {
		    JComboBox combo = (JComboBox) widgets.get(f);
		    for (int i = combo.getItemCount() - 1; i >= 0; i--) {
			if (combo.getItemAt(i).equals(value)) {
			    combo.setSelectedIndex(i);
			}
		    }
		}
	    }
	    iController.setValue(f, value);
	}
	fillSpecificValues();
	dependencyHandler.fillValues();
	chainedHandler.fillEmptyValues();
	imageHandlerManager.fillEmptyValues(); // fillValues()
	setFillingValues(false);
    }

    protected void setFillingValues(boolean b) {
	isFillingValues = b;
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

    /*
     * TODO call a method in a constructor (getBasicName) that can be override
     * for subclasses is no considered a good programming practice, in the
     * medium term this should be changed override for subclasses is no
     * considered a good programming practice, in the medium term this should be
     * changed
     */
    protected String getBasicName() {
	return this.basicName;
    }

    public void fillValues() {
	setFillingValues(true);
	try {
	    iController.read(position);
	    fillHandler.fillValues();
	    dependencyHandler.fillValues();
	    fillSpecificValues();
	} catch (DataException e) {
	    logger.error(e.getMessage(), e);
	}
	chainedHandler.fillValues();
	imageHandlerManager.fillValues();
	setFillingValues(false);
	validationHandler.validate();
    }

    public FormPanel getFormPanel() {
	if (formPanel == null) {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream("/forms/" + getBasicName() + ".jfrm");
	    if (stream == null) {
		stream = getClass().getClassLoader().getResourceAsStream(
			"/forms/" + getBasicName() + ".xml");
	    }
	    try {
		formPanel = new FormPanel(stream);
	    } catch (FormException e) {
	    	logger.error(e.getMessage(), e);
	    }
	}
	return formPanel;
    }

    @Override
    public I18nResource[] getI18nResources() {
	return null;
    }

    @Override
    public I18nHandler getI18nHandler() {
	return i18nHandler;
    }

    protected String getMetadataPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/" + getBasicName() + "_metadata.xml")
		.getPath();
    }

    @Deprecated
    public HashMap<String, JComponent> getWidgetComponents() {
	return widgets;
    }

    @Override
    public Map<String, JComponent> getWidgets() {
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

    @Override
    public ValidatorForm getValidatorForm() {
	return validationHandler.getValidatorForm();
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
	    // To calculate the maximum size of a form we take the size of the
	    // main frame minus a "magic number" for the menus, toolbar, state
	    // bar
	    // Take into account that in edition mode there is less available
	    // space
	    MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
	    int maxHeight = a.getHeight() - 205;
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

	    // getPreferredSize doesn't take into account the borders and other
	    // stuff
	    // introduced by Andami, neither scroll bars so we must increase the
	    // "preferred"
	    // dimensions
	    windowInfo.setWidth(width + 25);
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
	setListeners();
	fillEmptyValues();
	PluginServices.getMDIManager().addCentredWindow(this);
    }

    @Override
    public void actionUpdateRecord(long position) {
	this.position = position;
	saveButton.removeActionListener(action);
	action = new SaveAction(this);
	saveButton.addActionListener(action);
	setListeners();
	fillValues();
	PluginServices.getMDIManager().addCentredWindow(this);
    }

    @Override
    public void actionDeleteRecord(long position) {
	try {
	    iController.delete((int) position);
	    model.dataChanged();
	} catch (Exception e) {
	    NotificationManager.addError(e);
	}
    }

    @Override
    public IEditableSource getSource() {
	return model.getSource();
    }

    @Override
    public void setModel(AlphanumericTableModel model) {
	this.model = model;
	iController = model.getController().clone();
	fillHandler = new FillHandler(widgets, iController,
		ormlite.getAppDomain());
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
     * Instead of create an implementation of ImageHandler that only sets a path (FixedImageHandler) this utiliy method
     * sets the image without doing anything more
     * @param imgComponent
     *            . Name of the abeille widget
     * @param absPath
     *            . Absolute path to the image or relative path from andami.jar
     */
    protected void addImageHandler(String imgComponent, String absPath) {
	ImageComponent image = (ImageComponent) formPanel
		.getComponentByName(imgComponent);
	ImageIcon icon = new ImageIcon(absPath);
	image.setIcon(icon);
    }
    
    protected void addImageHandler(ImageHandler imageHandler) {
	imageHandlerManager.addHandler(imageHandler);
    }

    private final class CreateAction implements ActionListener {

	private final IWindow iWindow;

	public CreateAction(IWindow iWindow) {
	    this.iWindow = iWindow;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    HashMap<String, String> values = iController.getValues();
	    try {
		iController.create(values);
		model.dataChanged();
	    } catch (Exception e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getMessage(), e);
	    }
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}
    }

    private final class SaveAction implements ActionListener {

	private final IWindow iWindow;

	public SaveAction(IWindow iWindow) {
	    this.iWindow = iWindow;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    try {
		iController.update((int) position);
		model.dataChanged();
	    } catch (DataException e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getMessage(), e);
		String errorMessage = (e.getCause() != null) ? e.getCause()
				.getMessage() : e.getMessage(), auxMessage = errorMessage
				.replace("ERROR: ", "").replace(" ", "_")
				.replace("\n", ""), auxMessageIntl = PluginServices
				.getText(this, auxMessage);
				if (auxMessageIntl.compareToIgnoreCase(auxMessage) != 0) {
				    errorMessage = auxMessageIntl;
				}
				JOptionPane.showMessageDialog(
					(Component) PluginServices.getMainFrame(),
					errorMessage,
					PluginServices.getText(this, "save_layer_error"),
					JOptionPane.ERROR_MESSAGE);
	    } 
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}
    }
}
