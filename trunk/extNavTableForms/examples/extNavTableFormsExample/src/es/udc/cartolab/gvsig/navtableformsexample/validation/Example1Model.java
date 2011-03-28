package es.udc.cartolab.gvsig.navtableformsexample.validation;

import java.awt.Container;
import java.util.Map;

import es.udc.cartolab.gvsig.navtableforms.validation.FormModel;

public class Example1Model extends FormModel {

    private String codigo;
    private String capacidad;
    private String gestion;
    private boolean hay_anali;
    private String resultado;

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
	widgetValues.put("resultado", resultado);
	// int values
	widgetValues.put("capacidad", capacidad);
	//boolean values
	widgetValues.put("hay_anali", String.valueOf(hay_anali));
    }

    private void setDefaultValuesForStringFields() {
	gestion = getGvsigDefaultString();
	codigo = getGvsigDefaultString();
	resultado = getGvsigDefaultString();
    }

    private void setDefaultValuesForIntFields() {
	capacidad = Integer.toString(getGvsigDefaultInt());
    }

    private void setDefaultValuesForBooleanFields() {
	hay_anali = getGvsigDefaultBoolean();
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


    public boolean getHay_anali() {
	return hay_anali;
    }

    public void setHay_anali(boolean newValue) {
	boolean oldValue = getHay_anali();
	if (newValue != true && newValue != false) {
	    newValue = getGvsigDefaultBoolean();
	}
	hay_anali = newValue;
	widgetValues.put("hay_anali", String.valueOf(hay_anali));
	firePropertyChange((String) PROPERTIES_MAP.get("HAY_ANALI"), oldValue,
		newValue);
    }
    public String getResultado(){
	return resultado;
    }

    public void setResultado(String newValue) {
	String oldValue = getResultado();
	if (newValue.equals(null)) {
	    newValue = getGvsigDefaultString();
	}
	resultado = newValue;
	widgetValues.put("resultado", resultado);
	firePropertyChange((String) PROPERTIES_MAP.get("RESULTADO"), oldValue, newValue);
    }

    // map with widget values
    @Override
    public Map<String, String> getWidgetValues() {
	return widgetValues;
    }

}
