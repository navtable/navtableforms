package es.icarto.gvsig.navtableforms.gui.i18n.resource;

/**
 * Simple interface that i18n resources must implement.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 *
 */
public interface I18nResource {

    public boolean containsKey(String key);

    /**
     * I18n resources should return null when the key doesn't exist.
     */
    public String getString(String key);

}
