package es.icarto.gvsig.navtableforms.gui.tables.menu;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import es.icarto.gvsig.navtableforms.gui.tables.IForm;
import es.icarto.gvsig.navtableforms.gui.tables.JTableUtils;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
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
	int selectedRow = getSelectedRowIdx();
	if ((e.getClickCount() == 2) && (selectedRow > -1)) {
	    TableModel model = table.getModel();
	    if (model instanceof AlphanumericTableModel) {
		form.actionUpdateRecord(((AlphanumericTableModel) model)
			.convertRowIndexToModel(selectedRow));
	    } else {
		form.actionUpdateRecord(table
			.convertRowIndexToModel(selectedRow));
	    }
	} else if (e.getButton() == BUTTON_RIGHT) {
	    if (!JTableUtils.hasRows(table) || (selectedRow == NO_ROW_SELECTED)) {
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
	if ((table.getRowSorter() != null) && (selectedRow > -1)) {
	    return table.getRowSorter().convertRowIndexToModel(selectedRow);
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
	    super("Añadir");
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
	    if ((table != null) && (table.getSelectedRowCount() != 1)) {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
		return;
	    }
	    TableModel model = table.getModel();
	    final int selectedRow = getSelectedRowIdx();
	    if (model instanceof BaseTableModel) {
		form.actionUpdateRecord(((BaseTableModel) model)
			.convertRowIndexToModel(selectedRow));
	    } else {
		form.actionUpdateRecord(table
			.convertRowIndexToModel(selectedRow));
	    }
	}
    }

    private class DeleteAction extends AbstractAction {

	public DeleteAction() {
	    super("Eliminar");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	    if ((table != null) && (table.getSelectedRowCount() != 1)) {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para eliminar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
		return;
	    } else {
		Object[] options = { "Eliminar", "Cancelar" };
		int response = JOptionPane
			.showOptionDialog(
				null,
				"Los datos seleccionados se eliminarán de forma permanente.",
				"Eliminar", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options,
				options[0]);
		if (response == JOptionPane.YES_OPTION) {
		    TableModel model = table.getModel();
		    final int selectedRow = getSelectedRowIdx();
		    if (model instanceof AlphanumericTableModel) {
			form.actionDeleteRecord(((AlphanumericTableModel) model)
				.convertRowIndexToModel(selectedRow));
		    } else {
			form.actionDeleteRecord(table
				.convertRowIndexToModel(selectedRow));
		    }
		}

	    }
	}
    }
}
