package es.icarto.gvsig.navtableforms.gui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableContextualMenu implements MouseListener {

    private static final int NO_ROW_SELECTED = -1;
    private static final int BUTTON_RIGHT = 3;

    private JTable table;
    private IForm form;
    private JMenuItem openMenuItem;
    private JMenuItem updateMenuItem;
    private JMenuItem deleteMenuItem;
    private JPopupMenu popupMenu;

    /**
     * When attaching this listener to your table, you should care if it fills
     * the whole space of its viewport or on empty tables it won't show up.
     * 
     * So, for this to work on empty tables, you should make:
     * yourTable.setFillsViewportHeight(true);
     * 
     * More info:
     * http://docs.oracle.com/javase/6/docs/api/javax/swing/JTable.html
     * #setFillsViewportHeight(boolean)
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4310721
     */
    public JTableContextualMenu(IForm form) {
	this.form = form;
	initContextualMenu();
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
		deleteMenuItem.setEnabled(false);
	    } else {
		updateMenuItem.setEnabled(true);
		deleteMenuItem.setEnabled(true);
	    }
	    popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    private void initContextualMenu() {
	popupMenu = new JPopupMenu();

	// Create record
	openMenuItem = new JMenuItem("Crear nuevo");
	openMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionCreateRecord();
	    }
	});
	popupMenu.add(openMenuItem);

	updateMenuItem = new JMenuItem("Actualizar registro");
	updateMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		TableModel model = table.getModel();
		if (model instanceof TableModelAlphanumeric) {
		    form.actionUpdateRecord(((TableModelAlphanumeric) model)
			    .convertRowIndexToModel(table.getSelectedRow()));
		} else {
		    form.actionUpdateRecord(table.convertRowIndexToModel(table
			    .getSelectedRow()));
		}
	    }
	});
	popupMenu.add(updateMenuItem);

	deleteMenuItem = new JMenuItem("Borrar registro");
	deleteMenuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		TableModel model = table.getModel();
		if (model instanceof TableModelAlphanumeric) {
		    form.actionDeleteRecord(((TableModelAlphanumeric) model)
			    .convertRowIndexToModel(table.getSelectedRow()));
		} else {
		    form.actionDeleteRecord(table.convertRowIndexToModel(table
			    .getSelectedRow()));
		}
	    }
	});
	popupMenu.add(deleteMenuItem);
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
