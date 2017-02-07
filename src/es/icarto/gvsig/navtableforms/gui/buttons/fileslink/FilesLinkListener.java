package es.icarto.gvsig.navtableforms.gui.buttons.fileslink;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class FilesLinkListener implements ActionListener {

    private static final Logger logger = Logger
	    .getLogger(FilesLinkListener.class);

    private final AbstractForm dialog;
    private final FilesLinkData data;

    public FilesLinkListener(AbstractForm dialog, FilesLinkData data) {
	this.dialog = dialog;
	this.data = data;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	showFiles();
    }

    public void showFiles() {
	String folderName = data.getFolder(dialog);
	File folder = new File(folderName);

	if (folder.exists() || createFolder(folder)) {
	    openFolder(folder);
	}
    }

    private boolean createFolder(File folder) {
	boolean folderCreated = false;
	int answer = JOptionPane.showConfirmDialog(null, String.format(
		PluginServices.getText(this,
			"fileslink_folder_not_exists_create_it"), folder
			.getAbsolutePath()), PluginServices.getText(this,
		"warning"), JOptionPane.YES_NO_OPTION);
	if (answer == JOptionPane.YES_OPTION) {
	    // will make *all* directories in the path
	    if (!folder.mkdirs()) {
		JOptionPane.showMessageDialog(
			null,
			PluginServices.getText(this,
				"fileslink_folder_could_not_be_created")
				+ ": "
				+ folder.getAbsolutePath());
	    } else {
		folderCreated = true;
	    }
	}
	return folderCreated;
    }

    private void openFolder(File folder) {
	/*
	 * TODO: Improve how to do this. *Theorically*, Java is multiplatform :/
	 *
	 * Desktop API only works with JVM 1.6, so when using this extension
	 * with gvSIG portable on windows (jvm 1.5) is needed to change for the
	 * next.
	 *
	 * Disclaimer: I do know that is possible to do
	 * System.getProperty(os.version) to know which OS you are running, but
	 * as users can use different versions of Windows and I don't know which
	 * information gives each one depending on its version (if "W7",
	 * "Windows 2000", "Win XP",...) I prefer do that explicity.
	 *
	 * Patches to improve this are welcome!
	 */

	// Code to use in windows platform with gvSIG portable
	// String commands = "explorer /select," +
	// folderName;//.getAbsolutePath();
	// String[] commands = new String[] { "cmd.exe", "/C", "start",
	// folder.getAbsolutePath() };
	// Process process = Runtime.getRuntime().exec(commands);

	Desktop desktop = Desktop.getDesktop();
	try {
	    desktop.open(folder);
	} catch (IOException e) {
	    logger.error(e.getMessage(), e);
	}

    }
}
