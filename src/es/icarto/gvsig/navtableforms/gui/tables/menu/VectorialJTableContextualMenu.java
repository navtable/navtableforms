package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class VectorialJTableContextualMenu extends BaseJTableContextualMenu {

    private String layerName;

    public VectorialJTableContextualMenu(String layerName) {
	this.layerName = layerName;
	initContextualMenu();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if ((e.getClickCount() == 2) && (table.getSelectedRow() > -1)) {
	    openDialog();
	} else if (e.getButton() == BUTTON_RIGHT) {
	    if (!JTableUtils.hasRows(table)
		    || (table.getSelectedRow() == NO_ROW_SELECTED)) {
		updateMenuItem.setEnabled(false);
	    } else {
		updateMenuItem.setEnabled(true);
	    }
	    popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    @Override
    protected void initContextualMenu() {
	updateMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		openDialog();
	    }
	});
	popupMenu.add(updateMenuItem);
    }

    protected void openDialog() {
	AbstractForm form = FormFactory.createFormRegistered(layerName);
	form.init();
	form.setPosition(((BaseTableModel) table.getModel())
		.convertRowIndexToModel(table.getSelectedRow()));
	PluginServices.getMDIManager().addWindow(form);
    }

}
