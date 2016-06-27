package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.gvsig.andami.PluginServices;

import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.handler.EditableNNRelTableForm;
import es.icarto.gvsig.navtableforms.gui.tables.handler.VectorialEditableNNRelTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.VectorialTableModel;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

/**
 * VectorialEditableNNRelJTableContextualMenu
 * 
 * n:n relationship table contextual menus which opens forms and has options for
 * updating, creating and deleting registers.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class VectorialEditableNNRelJTableContextualMenu extends
	VectorialJTableContextualMenu {

    protected VectorialEditableNNRelTableHandler tableRelationship;

    public VectorialEditableNNRelJTableContextualMenu(
	    VectorialEditableNNRelTableHandler tableRelationship) {
	super(tableRelationship.getSourceTableName());
	newMenuItem
		.setText(PluginServices.getText(this, "create_new_relation"));
	updateMenuItem.setText(PluginServices.getText(this,
		"update_item_relation"));
	deleteMenuItem.setText(PluginServices.getText(this,
		"delete_item_relation"));
	this.form = FormFactory
		.createSingletonFormRegistered(tableRelationship
			.getSourceTableName());
	this.tableRelationship = tableRelationship;
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

    protected void openDialog() {
	if (!formInitialized) {
	    if ((form != null) && (form.init())) {
		formInitialized = true;
		readSelectedPosition();
		PluginServices.getMDIManager().addWindow(form);
	    }
	} else {
	    form.reinit();
	    readSelectedPosition();
	    PluginServices.getMDIManager().addWindow(form);
	}
    }

    private void readSelectedPosition() {
	form.setPosition(((VectorialTableModel) table.getModel())
		.convertRowIndexToModel(table.getSelectedRow()));
    }

}
