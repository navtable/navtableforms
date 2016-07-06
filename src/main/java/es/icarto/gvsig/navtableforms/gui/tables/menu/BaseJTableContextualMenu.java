package es.icarto.gvsig.navtableforms.gui.tables.menu;

import static es.icarto.gvsig.commons.i18n.I18n._;

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
    protected JMenuItem newMenuItem = new JMenuItem(_("create_new"));
    protected JMenuItem updateMenuItem = new JMenuItem(_("update_item"));
    protected JMenuItem deleteMenuItem = new JMenuItem(_("delete_item"));
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
