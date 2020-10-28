package es.icarto.gvsig.navtableforms.gui.buttons.fileslink;

import static es.icarto.gvsig.commons.i18n.I18n._;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import es.icarto.gvsig.navtableforms.AbstractForm;

@SuppressWarnings("serial")
public class FilesLinkButton extends JButton {

	/**
	 * Will open the folder defined as: baseDirectory + File.separator + folderField
	 * value (the value in the field folderField from the current form register).
	 */
	public FilesLinkButton(AbstractForm form, FilesLinkData data) {
		this.setIcon(new ImageIcon(AbstractForm.class.getResource("/fileslink.png")));
		this.setToolTipText(_("fileslink_tooltip"));
		this.addActionListener(new FilesLinkListener(form, data));
	}

}