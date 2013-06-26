package es.icarto.gvsig.navtableforms;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorForm;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public interface IValidatableForm {

    public boolean isFillingValues();

    public IController getFormController();

    public void setChangedValues();

    public FillHandler getFillHandler();

    public void validateForm();

    public ValidatorForm getValidatorForm();

}
