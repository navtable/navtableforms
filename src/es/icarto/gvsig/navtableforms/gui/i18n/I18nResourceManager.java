package es.icarto.gvsig.navtableforms.gui.i18n;

import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;

/**
 * This class stores and manages the multiple i18n resources, traversing all them
 * when a key is required until one provides its translation.
 * Keep in mind that if we have multiple resources, it'll provide the first translation
 * it finds that matches the required key.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 *
 */
public class I18nResourceManager {

    private I18nResource[] resources;

    public I18nResourceManager(I18nResource[] resources) {
	this.resources = (resources != null ? resources : new I18nResource[0]);
    }

    public boolean containsKey(String key) {
	for (int i = 0, len = resources.length; i < len; i++) {
	    if (resources[i].containsKey(key)) {
		return true;
	    }
	}
	return false;
    }

    public String getString(String key, String defaultValue) {
	String value;
	for (int i = 0, len = resources.length; i < len; i++) {
	    value = resources[i].getString(key);
	    if (value != null) {
		return value;
	    }
	}
	return defaultValue;
    }

    public String getString(String key) {
	return getString(key, key);
    }
}
