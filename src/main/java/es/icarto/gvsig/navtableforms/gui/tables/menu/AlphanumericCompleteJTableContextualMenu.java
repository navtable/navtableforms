package es.icarto.gvsig.navtableforms.gui.tables.menu;

import static es.icarto.gvsig.commons.i18n.I18n._;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.BaseTableModel;

/**
 * AlphanumericCompleteJTableContextualMenu
 *
 * Relationship table contextual menus which opens subforms and has options for
 * updating, creating and deleting registers.
 *
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */
public class AlphanumericCompleteJTableContextualMenu extends
AlphanumericJTableContextualMenu {

	private CreateAction createAction;
	private UpdateAction updateAction;
	private DeleteAction deleteAction;

	public AlphanumericCompleteJTableContextualMenu(IForm form) {
		super(form);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		table = (JTable) e.getComponent();
		int position = getSelectedRowIdx();
		if ((e.getClickCount() == 2) && (position > -1)) {
			BaseTableModel model = (BaseTableModel) table.getModel();
			form.actionUpdateRecord(model.getFeatureAt(position));
		} else if (e.getButton() == BUTTON_RIGHT) {
			if (!JTableUtils.hasRows(table) || (position == NO_ROW_SELECTED)) {
				updateMenuItem.setEnabled(false);
				deleteMenuItem.setEnabled(false);
			} else {
				updateMenuItem.setEnabled(true);
				deleteMenuItem.setEnabled(true);
			}
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private int getSelectedRowIdx() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow > -1) {
			return table.convertRowIndexToModel(selectedRow);
		}
		return selectedRow;
	}

	@Override
	protected void initContextualMenu() {
		createAction = new CreateAction();
		updateAction = new UpdateAction();
		deleteAction = new DeleteAction();

		newMenuItem.setAction(createAction);
		popupMenu.add(newMenuItem);

		updateMenuItem.setAction(updateAction);
		popupMenu.add(updateMenuItem);

		deleteMenuItem.setAction(deleteAction);
		popupMenu.add(deleteMenuItem);
	}

	// TODO: fpuga. {Create, Update, Delete}Action and the methods related with
	// it, are used to handle the case that we want an interface with add,
	// update, delete buttons instead or plus the listener in the table. This
	// implementation should be improved before used, take it only as a
	// prototype
	public AbstractAction getCreateAction() {
		return createAction;
	}

	public AbstractAction getUpdateAction() {
		return updateAction;
	}

	public AbstractAction getDeleteAction() {
		return deleteAction;
	}

	private class CreateAction extends AbstractAction {

		public CreateAction() {
			super(_("add"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			form.actionCreateRecord();
		}
	}

	private class UpdateAction extends AbstractAction {

		public UpdateAction() {
			super("Editar");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ((table == null) || (table.getSelectedRowCount() != 1)) {
				JOptionPane.showMessageDialog(null, _("no_file_selected"),
						_("no_file_selected_title"),
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			BaseTableModel model = (BaseTableModel) table.getModel();
			final int position = getSelectedRowIdx();
			form.actionUpdateRecord(model.getFeatureAt(position));
		}
	}

	private class DeleteAction extends AbstractAction {

		public DeleteAction() {
			super(_("delete"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if ((table == null) || (table.getSelectedRowCount() != 1)) {
				JOptionPane.showMessageDialog(null, _("no_file_selected"),
						_("no_file_selected_title"),
						JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
				Object[] options = { _("delete"), _("cancel") };
				int response = JOptionPane.showOptionDialog(null,
						_("ask_for_deletion"), _("delete"),
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
				if (response == JOptionPane.YES_OPTION) {
					BaseTableModel model = (BaseTableModel) table.getModel();
					final int position = getSelectedRowIdx();
					form.actionUpdateRecord(model.getFeatureAt(position));
				}

			}
		}
	}
}
