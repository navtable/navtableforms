package es.icarto.gvsig.navtableforms.gui.tables;

import java.awt.Component;
import java.awt.FlowLayout;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.toedter.calendar.JDateChooser;

import es.icarto.gvsig.commons.gui.AbstractIWindow;
import es.icarto.gvsig.navtableforms.DependencyHandler;
import es.icarto.gvsig.navtableforms.FillHandler;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ValidationHandler;
import es.icarto.gvsig.navtableforms.calculation.Calculation;
import es.icarto.gvsig.navtableforms.calculation.CalculationHandler;
import es.icarto.gvsig.navtableforms.chained.ChainedHandler;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

@SuppressWarnings("serial")
public abstract class AbstractSubForm extends AbstractIWindow implements IForm,
	IValidatableForm, IWindowListener {

    private static final Logger logger = Logger
	    .getLogger(AbstractSubForm.class);

    private FormPanel formPanel;
    private HashMap<String, JComponent> widgets;
    private final ValidationHandler validationHandler;
    private IController iController;
    private final ORMLite ormlite;
    private FillHandler fillHandler;
    private final DependencyHandler dependencyHandler;
    private final CalculationHandler calculationHandler;
    private final ChainedHandler chainedHandler;
    private boolean isFillingValues;
    private boolean changedValues;
    private JPanel southPanel;
    private JButton saveButton;
    private Map<String, String> foreingKey;
    private final List<BaseTableHandler> tableHandlers = new ArrayList<BaseTableHandler>();

    private long position;
    private ActionListener action;
    private AlphanumericTableModel model;

    public AbstractSubForm() {
	super();
	setWindowInfoProperties(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE
		| WindowInfo.RESIZABLE);
	setWindowTitle(PluginServices.getText(this, getBasicName()));
	initGUI();
	ormlite = new ORMLite(getMetadataPath());
	validationHandler = new ValidationHandler(ormlite, this);
	dependencyHandler = new DependencyHandler(ormlite, widgets, this);
	calculationHandler = new CalculationHandler();
	chainedHandler = new ChainedHandler();
    }

    private void initGUI() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	getFormPanel("ui/" + getBasicName() + ".xml");
	JScrollPane scrollPane = new JScrollPane(formPanel);
	widgets = AbeilleParser.getWidgetsFromContainer(formPanel);
	for (JComponent c : getWidgets().values()) {
	    if (c instanceof JDateChooser) {
		SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
		((JDateChooser) c).setDateFormatString(dateFormat.toPattern());
		((JDateChooser) c).getDateEditor().setEnabled(false);
	    }
	}
	// AbeilleUtils au = new AbeilleUtils();
	// au.formatLabels(formPanel);
	// au.formatTextArea(formPanel);
	add(scrollPane);
	add(getSouthPanel());
	setFocusCycleRoot(true);
    }

    public void setForeingKey(Map<String, String> foreingKey) {
	this.foreingKey = foreingKey;
    }

    private JPanel getSouthPanel() {
	if (southPanel == null) {
	    southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
    }

    public void removeListeners() {
	validationHandler.removeListeners(widgets);
	dependencyHandler.removeListeners();
	for (BaseTableHandler tableHandler : tableHandlers) {
	    tableHandler.removeListeners();
	}
	calculationHandler.removeListeners();
	chainedHandler.removeListeners();
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
		} else {
		    if (widget instanceof JComboBox) {
			JComboBox combo = (JComboBox) widgets.get(f);
			for (int i = combo.getItemCount() - 1; i >= 0; i--) {
			    if (combo.getItemAt(i).equals(value)) {
				combo.setSelectedIndex(i);
			    }
			}
		    }
		}
	    }
	    iController.setValue(f, value);
	}
	fillSpecificValues();
	dependencyHandler.fillValues();
	chainedHandler.fillEmptyValues();
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
	chainedHandler.fillValues();
	setFillingValues(false);
	validationHandler.validate();
    }

    protected FormPanel getFormPanel(String resourcePath) {
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
		logger.error(e.getStackTrace());
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
	    } catch (ReadDriverException e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getStackTrace());
	    } catch (StopWriterVisitorException e) {
		logger.error(e.getStackTrace());
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
