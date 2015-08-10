package es.icarto.gvsig.navtableforms.gui.i18n.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18n resource based onto Java's default i18n support (based on properties files).
 * 
 * It will look in the specified path for a properties file with the specified name that matches
 * the defined (or default) locale (e.g. 'messages_es_ES.properties'), and if it doesn't exist,
 * for the default one (e.g. 'messages.properties').
 * The specified path may contain no properties file with the specified name (in which case
 * {@link #getString()} will always return null), but the folder itself must exist or an Exception
 * will be thrown.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 *
 */
public class JavaBundleI18nResource implements I18nResource {

    private ResourceBundle i18nBundle;

    /**
     * @param i18nBundle an already constructed Java resource bundle
     */
    public JavaBundleI18nResource(ResourceBundle i18nBundle) {
	this.i18nBundle = i18nBundle;
    }

    /**
     * @param i18nName the base name of the i18n file(s).
     * @param i18nFolder a Java URL object pointing to the i18n folder.
     * @param locale the locale to search for.
     */
    public JavaBundleI18nResource(String i18nName, URL i18nFolder, Locale locale) {
	try {
	    i18nBundle = ResourceBundle.getBundle(i18nName, locale,
		    new URLClassLoader(new URL[]{i18nFolder}));
	} catch (MissingResourceException e) {
	    i18nBundle = null;
	}
    }

    /**
     * @param i18nName the base name of the i18n file(s).
     * @param i18nJavaFolderPath String with the path to the i18n folder.
     * @param locale the locale to search for.
     * @throws MalformedURLException thrown if i18nJavaFolderPath has a wrong path.
     */
    public JavaBundleI18nResource(String i18nName, String i18nJavaFolderPath, Locale locale)
	    throws MalformedURLException {
	this(i18nName, new File(i18nJavaFolderPath).toURI().toURL(), locale);
    }

    /**
     * This method uses the default locale for the current Java VM.
     * 
     * @param i18nName the base name of the i18n file(s).
     * @param i18nJavaFolderPath String with the path to the i18n folder.
     * @throws MalformedURLException thrown if i18nJavaFolderPath has a wrong path.
     */
    public JavaBundleI18nResource(String i18nName, String i18nJavaFolderPath)
	    throws MalformedURLException {
	this(i18nName, i18nJavaFolderPath, Locale.getDefault());
    }

    @Override
    public boolean containsKey(String key) {
	return (i18nBundle != null && key != null) ? i18nBundle.containsKey(key) : false;
    }

    @Override
    public String getString(String key) {
	return containsKey(key) ? i18nBundle.getString(key) : null;
    }

}
