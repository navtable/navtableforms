package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;

/**
 * VectorialUpdateJTableContextualMenu
 * 
 * Relationship table contextual menus which opens forms and has option only for
 * updating registers.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public class VectorialUpdateJTableContextualMenu extends
	VectorialJTableContextualMenu {

    public VectorialUpdateJTableContextualMenu(String layerName) {
	super(layerName);
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

}
