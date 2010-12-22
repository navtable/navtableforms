package es.udc.cartolab.gvsig.navtableforms;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.fileslink.FilesLink;
import es.udc.cartolab.gvsig.arqueoponte.preferences.Preferences;
import es.udc.cartolab.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public abstract class AbstractFormWithFilesLink extends AbstractForm {

    JButton filesLinkB = null;
    SelectableDataSource recordset = null;

    public AbstractFormWithFilesLink(FLyrVect layer) {
	super(layer);
	try {
	    this.recordset = layer.getRecordset();
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    protected abstract String getXmlFileName();
    protected abstract String getAliasInXML();

    @Override
    protected JPanel getActionsToolBar() {
	JPanel actionsToolBar = new JPanel(new FlowLayout());
	actionsToolBar.add(getButton(BUTTON_COPY_SELECTED));
	actionsToolBar.add(getButton(BUTTON_COPY_PREVIOUS));
	actionsToolBar.add(getButton(BUTTON_ZOOM));
	actionsToolBar.add(getButton(BUTTON_SELECTION));
	actionsToolBar.add(getButton(BUTTON_SAVE));
	actionsToolBar.add(getButton(BUTTON_REMOVE));

	filesLinkB = getNavTableButton(filesLinkB, "/fileslink.png",
		"filesLinkTooltip");
	actionsToolBar.add(filesLinkB);

	return actionsToolBar;
    }

    private String getPrimaryKey() {
	return ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(getAliasInXML()).getPrimaryKey()[0];
    }

    private String getLayerName() {
	return ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(getAliasInXML()).getTableName();
    }

    private SelectableDataSource getRecordSet() {
	return this.recordset;
    }

    private String getBaseDirectory() throws IOException {
	return Preferences.getPreferences().getBaseDirectory();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);

	if (e.getSource() == filesLinkB) {
	    String baseDirectory;
	    try {
		baseDirectory = getBaseDirectory();
		logger.debug("extFilesLink: baseDirectory is: " + baseDirectory);
		FilesLink fl = new FilesLink(getRecordSet(), baseDirectory,
			getLayerName(), getPrimaryKey());
		fl.showFiles(currentPosition);
	    } catch (IOException e1) {
		logger.error(e1.getMessage(), e1);
	    }
	}

    }

}
