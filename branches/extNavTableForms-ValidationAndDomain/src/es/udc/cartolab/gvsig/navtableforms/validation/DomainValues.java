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

    public ArrayList<KeyValue> getValuesFilteredBy(String id) {
	ArrayList<KeyValue> subset = new ArrayList<KeyValue>();
	for (KeyValue kv : data) {
	    if (kv.getForeignKey().equalsIgnoreCase(id)) {
		subset.add(kv);
	    }
	}
	return subset;
    }

    public void addValue(KeyValue value) {
	data.add(value);
    }

    public void addValues(ArrayList<KeyValue> values) {
	values.addAll(values);
    }
}