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

import org.gvsig.andami.PluginServices;
import org.gvsig.andami.ui.mdiManager.IWindow;
import org.gvsig.andami.ui.mdiManager.WindowInfo;


@SuppressWarnings("serial")
public class EditableNNRelTableForm extends JPanel implements IWindow,
	ActionListener {

    private WindowInfo viewInfo;

    private EditableNNRelTableHandler tableRelationship;

    private JComboBox secondaryPKValueCB;
    private JButton addButton;
    private int keyColumn = 0;

    public EditableNNRelTableForm(EditableNNRelTableHandler tableRelationship,
	    int keyColumn) {
	this.tableRelationship = tableRelationship;
	this.keyColumn = keyColumn;
	viewInfo = this.getWindowInfo();
	createForm();
    }

    public EditableNNRelTableForm(EditableNNRelTableHandler tableRelationship) {
	this.tableRelationship = tableRelationship;
	viewInfo = this.getWindowInfo();
	createForm();
    }

    private void createForm() {

	this.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();

	secondaryPKValueCB = new JComboBox();
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 5; // make this component tall
	c.insets = new Insets(10, 5, 10, 5);
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 0;
	this.add(secondaryPKValueCB, c);

	addButton = new JButton(_("add"));
	c.insets = new Insets(10, 10, 10, 10);
	c.ipady = 0;
	c.gridy = 1;
	c.anchor = GridBagConstraints.PAGE_END; // bottom of space
	this.add(addButton, c);

	for (String value : tableRelationship.getUnlinkedSecondaryValues()) {
	    secondaryPKValueCB.addItem(value);
	}

	addButton.addActionListener(this);
    }

    public void open() {
	PluginServices.getMDIManager().addCentredWindow(this);
    }

    public void addAction() {
	open();
    }

    public void deleteAction() {
	int row = tableRelationship.getJTable().getSelectedRow();
	TableModel tableModel = (TableModel) tableRelationship
		.getJTable().getModel();
	String secondaryPKValue = tableModel.getValueAt(row, keyColumn)
		.toString();
	tableRelationship.deleteRow(secondaryPKValue);
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (viewInfo == null) {
	    viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	}
	viewInfo.setHeight(75);
	viewInfo.setWidth(200);
	return viewInfo;
    }

    @Override
    public Object getWindowProfile() {
	return null;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
	if (event.getSource() == addButton) {
	    if (secondaryPKValueCB.getSelectedItem() != null) {
		tableRelationship.insertRow(secondaryPKValueCB
			.getSelectedItem().toString());
		PluginServices.getMDIManager().closeWindow(this);
	    }
	}
    }

}
