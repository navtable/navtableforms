package es.icarto.gvsig.navtableforms.gui.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class JTableContextualMenu implements MouseListener {

    private static final int NO_ROW_SELECTED = -1;
    private static final int BUTTON_RIGHT = 3;

    private JTable table;
    private IForm form;

    public JTableContextualMenu(IForm form) {
	this.form = form;
    }

    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if (!JTableUtils.hasRows(table)
		|| (table.getSelectedRow() == NO_ROW_SELECTED)) {
	    return;
	}
	if (e.getClickCount() == 2) {
	    form.actionUpdateRecord(table.getSelectedRow());
	} else if (e.getButton() == BUTTON_RIGHT) {
	    openContextualMenu(e);
	}
    }

    private void openContextualMenu(MouseEvent e) {
	JPopupMenu popup = new JPopupMenu();

	// Create new record
	JMenuItem open = new JMenuItem("Crear nuevo");
	open.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionCreateRecord();
	    }
	});
	popup.add(open);

	// Update record
	JMenuItem update = new JMenuItem("Actualizar registro");
	update.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionUpdateRecord(table.getSelectedRow());
	    }
	});
	popup.add(update);

	// Delete record
	JMenuItem delete = new JMenuItem("Borrar registro");
	delete.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		form.actionDeleteRecord(table.getSelectedRow());
	    }
	});
	popup.add(delete);
	popup.show(e.getComponent(), e.getX(), e.getY());
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
