package es.icarto.gvsig.navtableforms.validation;

import java.util.ArrayList;

public class KeyValue {

    private String key;
    private String value;
    private ArrayList<String> foreignKeys = new ArrayList<String>();

    public KeyValue() {
    }

    public KeyValue(String key, String value, String fk) {
	this.key = key;
	this.value = value;
	this.foreignKeys.add(fk);
    }

    public KeyValue(String key, String value, ArrayList<String> fk) {
	this.key = key;
	this.value = value;
	this.foreignKeys = fk;
    }

    KeyValue(String key, String value) {
	this.key = key;
	this.value = value;
    }

    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public void addForeignKey(String d) {
	this.foreignKeys.add(d);
    }

    public ArrayList<String> getForeignKeys() {
	return this.foreignKeys;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String toString() {
	return this.value;
    }

}