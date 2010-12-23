package es.udc.cartolab.gvsig.navtableformsexample.validation;

import java.awt.Container;
import java.util.Map;

import es.udc.cartolab.gvsig.navtableforms.validation.FormModel;

public class Example1Model extends FormModel {

    private String codigo;
    private String capacidad;
    private String gestion;

    public Example1Model(Container c) {
	super(c);
    }

    @Override
    public String getModelName() {
	return "Example1Model";
    }

    @Override
    protected void setDefaultValues() {
	setDefaultValuesForIntFields();
	setDefaultValuesForStringFields();
	setDefaultValuesForWidgetMap();
    }

    private void setDefaultValuesForWidgetMap() {
	// string values
	widgetValues.put("codigo", codigo);
	widgetValues.put("gestion", gestion);
	// int values
	widgetValues.put("capacidad", capacidad);
    }

    private void setDefaultValuesForStringFields() {
	gestion = getGvsigDefaultString();
	codigo = getGvsigDefaultString();
    }

    private void setDefaultValuesForIntFields() {
	capacidad = Integer.toString(getGvsigDefaultInt());
    }

    // Getters & Setters
    // *************************************************************

    public String getCodigo() {
	return codigo;
    }

    public void setCodigo(String newValue) {
	String oldValue = getCodigo();
	if (newValue.equals(null)) {
	    newValue = getGvsigDefaultString();
	}
	codigo = newValue;
	widgetValues.put("codigo", codigo);
	firePropertyChange((String) PROPERTIES_MAP.get("CODIGO"), oldValue,
		newValue);
    }

    public String getCapacidad() {
	return capacidad;
    }

    public void setCapacidad(String newValue) {
	String oldValue = getCapacidad();
	if (newValue.equals(null)) {
	    newValue = Integer.toString(getGvsigDefaultInt());
	}
	capacidad = newValue;
	widgetValues.put("capacidad", capacidad);
	firePropertyChange((String) PROPERTIES_MAP.get("CAPACIDAD"), oldValue,
		newValue);
    }

    public String getGestion() {
	return gestion;
    }

    public void setGestion(String newValue) {
	String oldValue = getGestion();
	if (newValue.equals(null)) {
	    newValue = getGvsigDefaultString();
	}
	gestion = newValue;
	widgetValues.put("gestion", gestion);
	firePropertyChange((String) PROPERTIES_MAP.get("GESTION"), oldValue,
		newValue);
    }

    // map with widget values
    @Override
    public Map<String, String> getWidgetValues() {
	return widgetValues;
    }

}
