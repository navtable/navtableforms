package es.icarto.gvsig.navtableforms;

import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource;

public interface II18nForm {

    public I18nResource[] getI18nResources();

    public I18nHandler getI18nHandler();
    
    public FormPanel getFormPanel();

}
