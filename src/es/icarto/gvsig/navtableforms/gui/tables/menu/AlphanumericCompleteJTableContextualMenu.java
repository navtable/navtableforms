package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;

public class AlphanumericCompleteJTableContextualMenu extends AlphanumericJTableContextualMenu {

    public AlphanumericCompleteJTableContextualMenu(IForm form) {
	super(form);
    }

    public void mouseClicked(MouseEvent e) {
	// Make sure the JTable fills the parent viewport for this listener to
	// work on empty tables (see comment on constructor):
	//
	// yourTable.setFillsViewportHeight(true);
	//
	table = (JTable) e.getComponent();
	if ((e.getClickCount() == 2) && (table.getSelectedRow() > -1)) {
	    TableModel model = table.getModel();
	    if (model instanceof AlphanumericTableModel) {
		form.actionUpdateRecord(((AlphanumericTableModel) model)
			.convertRowIndexToModel(table.getSelectedRow()));
	    } else {
		form.actionUpdateRecord(table.convertRowIndexToModel(table
			.getSelectedRow()));
	    }
	} else if (e.getButton() == BUTTON_RIGHT) {
	    if (!JTableUtils.hasRows(table)
		    || (table.getSelectedRow() == NO_ROW_SELECTED)) {
		updateMenuItem.setEnabled(false);
		deleteMenuItem.setEnabled(false);
	    } else {
		updateMenuItem.setEnabled(true);
		deleteMenuItem.setEnabled(true);
	    }
	    popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    protected void initContextualMenu() {
	newMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionCreateRecord();
	    }
	});
	popupMenu.add(newMenuItem);

	updateMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		TableModel model = table.getModel();
		if (model instanceof BaseTableModel) {
		    form.actionUpdateRecord(((BaseTableModel) model)
			    .convertRowIndexToModel(table.getSelectedRow()));
		} else {
		    form.actionUpdateRecord(table.convertRowIndexToModel(table
			    .getSelectedRow()));
		}
	    }
	});
	popupMenu.add(updateMenuItem);

	deleteMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		TableModel model = table.getModel();
		if (model instanceof AlphanumericTableModel) {
		    form.actionDeleteRecord(((AlphanumericTableModel) model)
			    .convertRowIndexToModel(table.getSelectedRow()));
		} else {
		    form.actionDeleteRecord(table.convertRowIndexToModel(table
			    .getSelectedRow()));
		}
	    }
	});
	popupMenu.add(deleteMenuItem);
    }

}
