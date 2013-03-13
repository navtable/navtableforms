package es.icarto.gvsig.navtableforms;

import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public interface IValidatableForm {

    public boolean isFillingValues();

    public IController getFormController();

    public void setChangedValues();

    public FillFactory getFillFactory();

    public void validateForm();

}
