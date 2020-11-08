package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;

/**
 * AlphanumericUpdateJTableContextualMenu
 *
 * Relationship table contextual menus which opens subforms and has option only
 * for updating registers.
 *
 * @author Jorge L�pez Fern�ndez <jlopez@cartolab.es>
 */

public class AlphanumericUpdateJTableContextualMenu extends AlphanumericJTableContextualMenu {

	public AlphanumericUpdateJTableContextualMenu(IForm form) {
		super(form);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		table = (JTable) e.getComponent();
		if ((e.getClickCount() == 2) && (table.getSelectedRow() > -1)) {
			BaseTableModel model = (BaseTableModel) table.getModel();
			int position = table.convertColumnIndexToModel(table.getSelectedRow());
			form.actionUpdateRecord(model.getFeatureAt(position));
		} else if (e.getButton() == BUTTON_RIGHT) {
			if (!JTableUtils.hasRows(table) || (table.getSelectedRow() == NO_ROW_SELECTED)) {
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
				BaseTableModel model = (BaseTableModel) table.getModel();
				int position = table.convertColumnIndexToModel(table.getSelectedRow());
				form.actionUpdateRecord(model.getFeatureAt(position));
			}
		});
		popupMenu.add(updateMenuItem);
	}

}
