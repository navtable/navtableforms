package es.icarto.gvsig.navtableforms.gui.i18n.resource;

import org.gvsig.i18n.Messages;

/**
 * I18n resource based onto gvSIG's default i18n support (in the same way as
 * PluginServices).
 * 
 * It will simply call org.gvsig.i18n.Messages.getText.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 *
 */
public class GvSIGI18nResource implements I18nResource {

	@Override
	public boolean containsKey(String key) {
		return Messages.getText(key, false) != null;
	}

	@Override
	public String getString(String key) {
		return Messages.getText(key, false);
	}

}
