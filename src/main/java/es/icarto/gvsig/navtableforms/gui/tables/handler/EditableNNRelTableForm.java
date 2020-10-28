package es.icarto.gvsig.navtableforms.gui.tables.handler;

import static es.icarto.gvsig.commons.i18n.I18n._;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.andami.ui.mdiManager.MDIManagerFactory;
import org.gvsig.andami.ui.mdiManager.WindowInfo;

@SuppressWarnings("serial")
public class EditableNNRelTableForm extends JPanel implements IWindow, ActionListener {

	private WindowInfo viewInfo;

	private final EditableNNRelTableHandler tableRelationship;

	private JComboBox secondaryPKValueCB;
	private JButton addButton;
	private int keyColumn = 0;

	public EditableNNRelTableForm(EditableNNRelTableHandler tableRelationship, int keyColumn) {
		this.tableRelationship = tableRelationship;
		this.keyColumn = keyColumn;
		this.viewInfo = this.getWindowInfo();
		createForm();
	}

	public EditableNNRelTableForm(EditableNNRelTableHandler tableRelationship) {
		this.tableRelationship = tableRelationship;
		this.viewInfo = this.getWindowInfo();
		createForm();
	}

	private void createForm() {

		this.setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();

		this.secondaryPKValueCB = new JComboBox();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 5; // make this component tall
		c.insets = new Insets(10, 5, 10, 5);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		this.add(this.secondaryPKValueCB, c);

		this.addButton = new JButton(_("add"));
		c.insets = new Insets(10, 10, 10, 10);
		c.ipady = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		this.add(this.addButton, c);

		for (final String value : this.tableRelationship.getUnlinkedSecondaryValues()) {
			this.secondaryPKValueCB.addItem(value);
		}

		this.addButton.addActionListener(this);
	}

	public void open() {
		MDIManagerFactory.getManager().addCentredWindow(this);
	}

	public void addAction() {
		open();
	}

	public void deleteAction() {
		final int row = this.tableRelationship.getJTable().getSelectedRow();
		final TableModel tableModel = this.tableRelationship.getJTable().getModel();
		final String secondaryPKValue = tableModel.getValueAt(row, this.keyColumn).toString();
		this.tableRelationship.deleteRow(secondaryPKValue);
	}

	@Override
	public WindowInfo getWindowInfo() {
		if (this.viewInfo == null) {
			this.viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
		}
		this.viewInfo.setHeight(75);
		this.viewInfo.setWidth(200);
		return this.viewInfo;
	}

	@Override
	public Object getWindowProfile() {
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.addButton) {
			if (this.secondaryPKValueCB.getSelectedItem() != null) {
				this.tableRelationship.insertRow(this.secondaryPKValueCB.getSelectedItem().toString());
				MDIManagerFactory.getManager().closeWindow(this);
			}
		}
	}

}
