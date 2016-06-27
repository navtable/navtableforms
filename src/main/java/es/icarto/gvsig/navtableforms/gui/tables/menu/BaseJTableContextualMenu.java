package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.gvsig.andami.PluginServices;


/**
 * BaseJTableContextualMenu
 * 
 * Core code for relationship table contextual menus used by table handlers.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class BaseJTableContextualMenu implements MouseListener {

    protected static final int NO_ROW_SELECTED = -1;
    protected static final int BUTTON_RIGHT = 3;

    protected JTable table;
    protected JMenuItem newMenuItem = new JMenuItem(PluginServices.getText(
	    this, "create_new"));
    protected JMenuItem updateMenuItem = new JMenuItem(PluginServices.getText(
	    this, "update_item"));
    protected JMenuItem deleteMenuItem = new JMenuItem(PluginServices.getText(
	    this, "delete_item"));
    protected JPopupMenu popupMenu = new JPopupMenu();

    public BaseJTableContextualMenu() {
	initContextualMenu();
    }

    public abstract void mouseClicked(MouseEvent e);

    protected abstract void initContextualMenu();

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
