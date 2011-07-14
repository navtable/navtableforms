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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.icarto.gvsig.navtableforms.utils.TableUtils;
import es.udc.cartolab.gvsig.navtable.AlphanumericNavTable;


public class AlphanumericNavTableLauncher implements MouseListener {

    private ILauncherForm form;
    private LauncherParams params;
    private AlphanumericNavTable ant;

    public AlphanumericNavTableLauncher(ILauncherForm form,
	    LauncherParams params) {
	this.form = form;
	this.params = params;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	JTable table = (JTable) e.getComponent();
	TableModel model = table.getModel();
	int rowSelectedIndex = table.getSelectedRow();
	IEditableSource source = getTableSource(params.getTableName());
	try {
	    ant = new AlphanumericNavTable(source,
		    params.getAlphanumericNavTableTitle());
	    if (ant.init() && tableHasRows(model)) {
		ant.setPosition(TableUtils.getPositionOfRowSelected(
			source.getRecordset(), model, rowSelectedIndex));
		selectFeaturesInANT(source.getRecordset(), model);
		PluginServices.getMDIManager().addCentredWindow(ant);
		JInternalFrame parent = (JInternalFrame) ant.getRootPane()
			.getParent();
		// this listener will call the form passed once
		// alphanumericnavtable is closed
		parent.addInternalFrameListener(form);
	    }
	} catch (ReadDriverException e1) {
	    e1.printStackTrace();
	}
    }

    public boolean tableHasRows(TableModel model) {
	if ((model.getRowCount() > 0) && (!firstRowIsVoid(model))) {
	    return true;
	}
	return false;
    }

    public boolean firstRowIsVoid(TableModel model) {
	boolean isVoid = true;
	for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
	    if (model.getValueAt(0, colIndex) == null) {
		isVoid = true;
	    } else {
		isVoid = false;
		break;
	    }
	}
	return isVoid;
    }

    private void selectFeaturesInANT(SelectableDataSource source,
	    TableModel model) {
	ArrayList<Integer> rowList;
	rowList = TableUtils.getIndexesOfRowsInTable(source, model);
	ant.clearSelectedFeatures();
	for (int row : rowList) {
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

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
