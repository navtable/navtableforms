package es.icarto.gvsig.navtableforms.gui.tables.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.table.TableModel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;


@SuppressWarnings("serial")
public class EditableNNRelTableForm extends JPanel implements IWindow,
	ActionListener {

    private WindowInfo viewInfo;

    private EditableNNRelTableHandler tableRelationship;

    private FormPanel formPanel;
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
	InputStream stream = getClass().getClassLoader().getResourceAsStream(
		"ui/r_add_subform.xml");
	try {
	    formPanel = new FormPanel(stream);
	    this.add(formPanel);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	secondaryPKValueCB = (JComboBox) formPanel
		.getComponentByName("secondaryPKValueCB");
	addButton = (JButton) formPanel.getComponentByName("addButton");
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
