/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
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
package es.udc.cartolab.gvsig.navtableforms.validation;

import java.awt.Container;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import com.jgoodies.binding.beans.Model;

public abstract class FormModel extends Model {

    // TODO check where gvSIG do that and take values from there
    static final String GVSIG_DEFAULT_STRING = "";
    static final int GVSIG_DEFAULT_INT = 0;
    static final double GVSIG_DEFAULT_DOUBLE = 0.0;
    static final boolean GVSIG_DEFAULT_BOOLEAN = false;

    public static final Hashtable<String, String> PROPERTIES_MAP = new Hashtable<String, String>();

    protected final Map<String, String> widgetValues = new LinkedHashMap<String, String>();

    public FormModel(Container c) {
	setDefaultValues();
	createPropertiesVectorFromWidgets(c);
    }

    /**
     * Setting all model values. The model represent ALL values from Modulo and
     * the forms don't have all the values but SOME of them.
     * 
     * So, it's necessary to set a default value for every model property in
     * order to validation doesn't depend on properties not being in a
     * particular form. i.e.: "rios form" could not have the property "altura".
     * If "altura" is not initialized, validation will break because the
     * variable doesn't obey the constraints.
     * 
     */
    protected abstract void setDefaultValues();

    public abstract String getModelName();

    private void createPropertiesVectorFromWidgets(Container c) {

	String name;
	Vector<JComponent> widgetsVector;
	widgetsVector = FormParserUtils.getWidgetsWithContentFromContainer(c);

	for (JComponent widget : widgetsVector) {
	    name = widget.getName();
	    if (name.contains(".")) {
		name = name.substring(0, name.indexOf("."));
	    }
	    PROPERTIES_MAP.put(name.toUpperCase(), name);
	}

    }

    protected int getGvsigDefaultInt() {
	return GVSIG_DEFAULT_INT;
    }

    protected boolean getGvsigDefaultBoolean() {
	return GVSIG_DEFAULT_BOOLEAN;
    }

    protected String getGvsigDefaultString() {
	return GVSIG_DEFAULT_STRING;
    }

    public abstract Map<String, String> getWidgetValues();

    public String getWidgetValue(Object key) {
	return widgetValues.get(key);
    }

    protected void setProperty(String property) {
	PROPERTIES_MAP.put(property.toUpperCase(), property);
    }

    public Object getWindowProfile() {
	return null;
    }
}
