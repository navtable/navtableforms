package es.udc.cartolab.gvsig.navtableforms;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.udc.cartolab.fileslink.FilesLink;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public abstract class AbstractAlphanumericFormWithFilesLink extends AbstractAlphanumericForm {

    JButton filesLinkB = null;

    public AbstractAlphanumericFormWithFilesLink(IEditableSource model) throws ReadDriverException {
	super(model);
	logger.debug("extFilesLink: loading extension ....");
    }


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

    protected abstract String getXmlFileName();
    protected abstract String getAliasInXML();
    protected abstract String getBaseDirectory();

    protected String getPrimaryKey(){
	return ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(getAliasInXML()).getPrimaryKey()[0];
    }

    private String getLayerName() {
	return ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(getAliasInXML()).getTableName();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);

	if (e.getSource() == filesLinkB) {
	    String layerName = getLayerName();
	    String baseDirectory;
	    try {
		baseDirectory = getBaseDirectory();
		logger.debug("extFilesLink: baseDirectory is: " + baseDirectory);
		FilesLink fl = new FilesLink(model.getRecordset(),
			baseDirectory, layerName, getPrimaryKey());
		fl.showFiles(currentPosition);
	    } catch (ReadDriverException e1) {
		logger.error(e1.getMessage(), e1);
	    }
	}

    }

}
