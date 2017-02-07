package es.icarto.gvsig.navtableforms.gui.buttons.fileslink;

import es.icarto.gvsig.navtableforms.AbstractForm;

public interface FilesLinkData {

    public String getBaseDirectory();

    public String getRegisterField();

    /**
     * Commonly it will be baseDirectory + File.separator +
     * valueOf(registerField)
     */
    public String getFolder(AbstractForm form);

}
