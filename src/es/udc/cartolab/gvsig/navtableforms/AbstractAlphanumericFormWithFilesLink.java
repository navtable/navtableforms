/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of extNavTableForms
 * 
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
*/
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
	newB = getNavTableButton(newB, "/table_add.png", "new_register");
	actionsToolBar.add(newB);
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
	} else {
	    super.actionPerformed(e);
	}
    }

}
