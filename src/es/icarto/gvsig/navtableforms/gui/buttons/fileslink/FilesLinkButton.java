package es.icarto.gvsig.navtableforms.gui.buttons.fileslink;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class FilesLinkButton extends JButton {

    /**
     * Will open the folder defined as: baseDirectory + File.separator +
     * folderField value (the value in the field folderField from the current
     * form register).
     */
    public FilesLinkButton(AbstractForm form, FilesLinkData data) {
	this.setIcon(new ImageIcon(AbstractForm.class
		.getResource("/fileslink.png")));
	this.setToolTipText(
		PluginServices.getText(this, "filesLinkTooltip"));
	this.addActionListener(new FilesLinkListener(form, data));
    }

}