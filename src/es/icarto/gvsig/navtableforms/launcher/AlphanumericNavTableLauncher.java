/*
 * Copyright (c) 2011. iCarto
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

package es.icarto.gvsig.navtableforms.launcher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.icarto.gvsig.navtableforms.gui.tables.TableUtils;
import es.udc.cartolab.gvsig.navtable.AlphanumericNavTable;

public class AlphanumericNavTableLauncher implements MouseListener {

    private static final int BUTTON_RIGHT = 3;

    private ILauncherForm form;
    private LauncherParams params;
    private AlphanumericNavTable ant;
    private JTable table;

    public AlphanumericNavTableLauncher(ILauncherForm form,
	    LauncherParams params) {
	this.form = form;
	this.params = params;
    }

    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if((e.getClickCount() == 2)
		&& TableUtils.tableHasRows(table.getModel())) {
	    openANT(table.getModel(), 
		    table.getSelectedRow());
	} else if ((e.getButton() == BUTTON_RIGHT)
		&& TableUtils.tableHasRows(table.getModel())) {
	    JPopupMenu popup = new JPopupMenu();

	    JMenuItem menuOpenANT = new JMenuItem(
		    PluginServices.getText(this, "open_table")
		    + " " + params.getAlphanumericNavTableTitle());
	    menuOpenANT.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    openANT(table.getModel(),
			    table.getSelectedRow());
		}
	    });
	    popup.add(menuOpenANT);

	    popup.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void openANT(TableModel model, int rowIndex) {
	IEditableSource source = getTableSource(params.getTableName());
	try {
	    ant = new AlphanumericNavTable(source,
		    params.getAlphanumericNavTableTitle());
	    if (ant.init()) {
		ant.setPosition(TableUtils.getIndexOfRowSelected(
			source.getRecordset(), 
			model, 
			rowIndex));
		selectFeaturesInANT(source.getRecordset(), model);
		PluginServices.getMDIManager().addCentredWindow(ant);
		//Listening closing actions of parent form
		JInternalFrame parent = (JInternalFrame) ant.getRootPane()
			.getParent();
		parent.addInternalFrameListener(form);
	    }
	} catch (ReadDriverException e1) {
	    e1.printStackTrace();
	}
    }

    private void selectFeaturesInANT(SelectableDataSource source,
	    TableModel model) {
	ArrayList<Long> rowList;
	rowList = TableUtils.getIndexesOfAllRowsInTable(source, model);
	ant.clearSelectedFeatures();
	for (long row : rowList) {
	    ant.selectFeature(row);
	}
	ant.setOnlySelected(true);
    }

    private IEditableSource getTableSource(String tableName) {
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if ((w instanceof Table)
		    && (((Table) w).getModel().getName()
			    .equalsIgnoreCase(tableName))) {
		return ((Table) w).getModel().getModelo();
	    }
	}
	return null;
    }

}
