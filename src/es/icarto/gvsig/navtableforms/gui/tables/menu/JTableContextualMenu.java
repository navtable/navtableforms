package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;

public abstract class JTableContextualMenu implements MouseListener {

    protected static final int NO_ROW_SELECTED = -1;
    protected static final int BUTTON_RIGHT = 3;

    protected JTable table;
    protected IForm form;
    protected JMenuItem newMenuItem;
    protected JMenuItem updateMenuItem;
    protected JMenuItem deleteMenuItem;
    protected JPopupMenu popupMenu;

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

    public JTableContextualMenu() {
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
