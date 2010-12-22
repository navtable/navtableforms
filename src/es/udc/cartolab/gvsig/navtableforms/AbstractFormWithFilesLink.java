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
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.udc.cartolab.fileslink.FilesLink;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public abstract class AbstractFormWithFilesLink extends AbstractForm {

    JButton filesLinkB = null;
    protected SelectableDataSource recordset = null;

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
    protected abstract String getBaseDirectory();

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

    @Override
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);

	if (e.getSource() == filesLinkB) {
	    String baseDirectory;
	    baseDirectory = getBaseDirectory();
	    logger.debug("extFilesLink: baseDirectory is: " + baseDirectory);
	    FilesLink fl = new FilesLink(getRecordSet(), baseDirectory,
		    getLayerName(), getPrimaryKey());
	    fl.showFiles(currentPosition);
	}

    }

}
