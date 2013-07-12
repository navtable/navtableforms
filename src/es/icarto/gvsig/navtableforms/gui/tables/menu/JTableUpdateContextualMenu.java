package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelAlphanumeric;

public class JTableUpdateContextualMenu extends JTableContextualMenu {

    public JTableUpdateContextualMenu(IForm form) {
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
	    if (model instanceof TableModelAlphanumeric) {
		form.actionUpdateRecord(((TableModelAlphanumeric) model)
			.convertRowIndexToModel(table.getSelectedRow()));
	    } else {
		form.actionUpdateRecord(table.convertRowIndexToModel(table
			.getSelectedRow()));
	    }
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

    protected void initContextualMenu() {
	updateMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionUpdateRecord(((TableModelAlphanumeric) table
			.getModel()).convertRowIndexToModel(table
			.getSelectedRow()));
	    }
	});
	popupMenu.add(updateMenuItem);
    }

}
