package es.udc.cartolab.gvsig.navtableforms.validation;

public class KeyValue {

    private String key;
    private String value;
    private String foreignKey;

    public KeyValue() {
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

    public void setForeignKey(String d) {
	this.foreignKey = d;
    }

    public String getForeignKey() {
	return this.foreignKey;
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