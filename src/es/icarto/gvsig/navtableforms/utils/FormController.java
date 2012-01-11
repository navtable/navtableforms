package es.icarto.gvsig.navtableforms.utils;

import java.util.HashMap;
import java.util.Set;

import javax.swing.JFormattedTextField.AbstractFormatter;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.navtableforms.gui.formattedtextfields.FormatterFactory;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;
import es.udc.cartolab.gvsig.navtable.listeners.PositionEvent;
import es.udc.cartolab.gvsig.navtable.listeners.PositionListener;

public class FormController implements PositionListener{

    private HashMap<String, Integer> indexes;
    private HashMap<String, Integer> types;
    private HashMap<String, String> values;
    private HashMap<String, String> valuesChanged;

    public FormController() {
	indexes = new HashMap<String, Integer>();
	types = new HashMap<String, Integer>();
	values = new HashMap<String, String>();
	valuesChanged = new HashMap<String, String>();
    }

    public void fill(SelectableDataSource sds, long position) {
	if(position != AbstractNavTable.EMPTY_REGISTER) {
	    clearAll();
	    try {
		for(int i=0; i<sds.getFieldCount(); i++) {
		    String name = sds.getFieldName(i); 
		    values.put(name, 
			    sds.getFieldValue(position, i).getStringValue(
				    new ValueFormatNT()));
		    indexes.put(name, 
			    i);
		    types.put(name, 
			    sds.getFieldType(i));
		}
	    } catch (ReadDriverException e) {
		e.printStackTrace();
		clearAll();
	    }
	}
    }

    private void clearAll() {
	indexes.clear();
	types.clear();
	values.clear();
	valuesChanged.clear();
    }

    public int getIndex(String componentName) {
	return indexes.get(componentName);
    }

    public int[] getIndexesOfValuesChanged() {
	int[] idxs = new int[valuesChanged.size()];
	Set<String> names = valuesChanged.keySet();
	int i=0;
	for(String name : names) {
	    idxs[i] = indexes.get(name);
	    i++;
	}
	return idxs;
    }

    public String getValue(String componentName) {
	if(valuesChanged.containsKey(componentName)) {
	    return valuesChanged.get(componentName);
	}
	return values.get(componentName);
    }

    public String getValueInLayer(String componentName) {
	return values.get(componentName);
    }

    public HashMap<String, String> getValuesOriginal() {
	return values;
    }

    public HashMap<String, String> getValuesChanged() {
	return valuesChanged;
    }

    /**
     * Make sure the value setted is a formatted value, 
     * as the ones from layer. See {@link #fill(SelectableDataSource, long)}
     * For example: if value is a double in layer, 
     * the string should be something like 1.000,00 instead of 1000.
     */
    public void setValue(String componentName, String value) {
	valuesChanged.put(componentName, value);
    }

    public AbstractFormatter getFormatter(String componentName) {
	return FormatterFactory.createFormatter(types.get(componentName));
    }

    public int getType(String name) {
	return types.get(name);
    }

    public void onPositionChange(PositionEvent e) {
	long position = ((AbstractNavTable) e.getSource()).getPosition();
	try {
	    ((AbstractNavTable) e.getSource()).reloadRecordset();
	    SelectableDataSource sds = ((AbstractNavTable) e.getSource()).getRecordset();
	    fill(sds, position);
	} catch (ReadDriverException rde) {
	    rde.printStackTrace();
	    clearAll();
	}
    }

}
