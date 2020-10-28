package es.icarto.gvsig.navtableforms;

import java.util.Map;

import javax.swing.JComponent;

import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public interface IValidatableForm {

	public boolean isFillingValues();

	public IController getFormController();

	public void setChangedValues();

	public FillHandler getFillHandler();

	public void validateForm();

	public ValidatorForm getValidatorForm();

	public Map<String, JComponent> getWidgets();

	public FormPanel getFormPanel();

}
