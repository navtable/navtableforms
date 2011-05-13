package es.udc.cartolab.gvsig.navtableforms.validation;

import java.util.ArrayList;

public class DomainValues {

    ArrayList<KeyValue> data;

    public DomainValues(ArrayList<KeyValue> values) {
	this.data = values;
    }

    public ArrayList<KeyValue> getValues() {
	return data;
    }

    public void addValue(KeyValue value) {
	data.add(value);
    }

    public void addValues(ArrayList<KeyValue> values) {
	values.addAll(values);
    }
}
