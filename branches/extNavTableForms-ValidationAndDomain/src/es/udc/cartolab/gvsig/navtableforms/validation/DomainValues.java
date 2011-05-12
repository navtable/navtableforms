package es.udc.cartolab.gvsig.navtableforms.validation;

import java.util.ArrayList;

public class DomainValues {

    ArrayList<String> values;

    public DomainValues(ArrayList<String> values) {
	this.values = values;
    }

    public ArrayList<String> getValues() {
	return values;
    }

    public void addValue(String value) {
	values.add(value);
    }

    public void addValues(ArrayList<String> values) {
	values.addAll(values);
    }
}
