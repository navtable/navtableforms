package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.handler.EditableNNRelTableForm;
import es.icarto.gvsig.navtableforms.gui.tables.handler.VectorialEditableNNRelTableHandler;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

public class JTableEditableNNRelContextualMenu extends JTableContextualMenu {

    protected VectorialEditableNNRelTableHandler tableRelationship;
    protected AbstractForm dialog;
    protected boolean dialogInitialized = false;

    public JTableEditableNNRelContextualMenu(
	    VectorialEditableNNRelTableHandler tableRelationship, AbstractForm dialog) {
	newMenuItem
		.setText(PluginServices.getText(this, "create_new_relation"));
	updateMenuItem.setText(PluginServices.getText(this,
		"update_item_relation"));
	deleteMenuItem.setText(PluginServices.getText(this,
		"delete_item_relation"));
	this.dialog = dialog;
	this.tableRelationship = tableRelationship;
	initContextualMenu();
    }

    protected void initContextualMenu() {
	newMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new EditableNNRelTableForm(tableRelationship, tableRelationship
			.getKeyColumn()).addAction();
	    }
	});
	popupMenu.add(newMenuItem);

	updateMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		openDialog();
	    }
	});
	popupMenu.add(updateMenuItem);

	deleteMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new EditableNNRelTableForm(tableRelationship, tableRelationship
			.getKeyColumn()).deleteAction();
	    }
	});
	popupMenu.add(deleteMenuItem);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if ((e.getClickCount() == 2) && (table.getSelectedRow() > -1)) {
	    openDialog();
	} else if (e.getButton() == BUTTON_RIGHT) {
	    if (!JTableUtils.hasRows(table)
		    || (table.getSelectedRow() == NO_ROW_SELECTED)) {
		deleteMenuItem.setEnabled(false);
		updateMenuItem.setEnabled(false);
	    } else {
		deleteMenuItem.setEnabled(true);
		updateMenuItem.setEnabled(true);
	    }
	    popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    private void openDialog() {
	if (!dialogInitialized) {
	    if ((dialog != null) && (dialog.init())) {
		dialogInitialized = true;
		readSelectedPosition();
		PluginServices.getMDIManager().addWindow(dialog);
	    }
	} else {
	    readSelectedPosition();
	    PluginServices.getMDIManager().addWindow(dialog);
	}
    }

    private void readSelectedPosition() {
	IController controller = dialog.getFormController();
	String keySelected = table.getModel()
		.getValueAt(
			table.convertRowIndexToModel(table.getSelectedRow()),
			tableRelationship.getKeyColumn())
		.toString();
	try {
	    for (long i = controller.getRowCount() - 1; i >= 0; i--) {
		controller.read(i);
		if (controller.getValue(tableRelationship.getDestinationKey())
			.equals(keySelected)) {
		    dialog.setPosition(i);
		    break;
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

}
