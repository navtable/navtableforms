package es.icarto.gvsig.navtableforms.gui.i18n;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<I18nResource> resources;

    public I18nResourceManager(I18nResource[] resources) {
	this.resources = (resources != null ?
		new ArrayList<I18nResource>(Arrays.asList(resources)) : new ArrayList<I18nResource>());
    }

    public I18nResource[] getResources() {
	return resources.toArray(new I18nResource[0]);
    }

    public void addResource(I18nResource resource) {
	resources.add(resource);
    }

    public boolean containsKey(String key) {
	for (I18nResource resource : resources) {
	    if (resource.containsKey(key)) {
		return true;
	    }
	}
	return false;
    }

    public String getString(String key, String defaultValue) {
	String value;
	for (I18nResource resource : resources) {
	    value = resource.getString(key);
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
