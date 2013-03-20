package es.icarto.gvsig.navtableforms;

import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public interface IValidatableForm {

    public boolean isFillingValues();

    public IController getFormController();

    public void setChangedValues();

    public FillHandler getFillFactory();

    public void validateForm();

}
