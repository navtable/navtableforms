package es.udc.cartolab.gvsig.navtableformsexample;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class Example1Form extends AbstractForm {

    private FormPanel form;

    public Example1Form(FLyrVect layer) {
	super(layer);
	viewInfo.setHeight(350);
	viewInfo.setWidth(450);
	viewInfo.setTitle("Example 1");
    }

    @Override
    protected void fillSpecificValues() {
	enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    }

    protected void enableComponentIfCheckBoxIsSelected(String chbName,
	    String cmpName) {
	JCheckBox chb = (JCheckBox) getFormBody().getComponentByName(chbName);
	Component cmp = getFormBody().getComponentByName(cmpName);

	if (chb.isSelected()) {
	    cmp.setEnabled(true);
	} else {
	    cmp.setEnabled(false);
	}
    }

    protected void setListeners() {
	super.setListeners();
	JCheckBox hay_anali = (JCheckBox) getFormBody().getComponentByName(
		"hay_anali");
	hay_anali.setActionCommand("hay_anali");
	hay_anali.addActionListener(this);
    }

    protected void removeListeners() {
	JCheckBox hay_anali = (JCheckBox) getFormBody().getComponentByName(
		"hay_anali");
	hay_anali.removeActionListener(this);
	super.removeListeners();
    }

    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
	String action = e.getActionCommand();
	if (action.equals("hay_anali")) {
	    enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
	}
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("exampleform1.xml");
	}
	return form;
    }

    @Override
    public String getXMLPath() {
	return Preferences.XMLDATAFILE_PATH;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("Example1Form");
    }
}
