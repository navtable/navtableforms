package es.udc.cartolab.gvsig.navtableformsexample;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.navtableforms.validation.FormBinding;
import es.udc.cartolab.gvsig.navtableforms.validation.FormModel;
import es.udc.cartolab.gvsig.navtableformsexample.validation.Example1Binding;
import es.udc.cartolab.gvsig.navtableformsexample.validation.Example1Model;

public class Example1Form extends AbstractForm {

    public Example1Form(FLyrVect layer) {
	super(layer);
	viewInfo.setHeight(350);
	viewInfo.setWidth(450);
	viewInfo.setTitle("Example 1");
    }

    @Override
    public FormBinding getFormBinding(FormModel model) {
	return new Example1Binding(model);
    }

    @Override
    public FormModel getFormModel(Container c) {
	return new Example1Model(c);
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("NTForms example 1");
    }

    @Override
    public FormPanel getFormBody() {
	return new FormPanel("exampleform1.xml");
    }

    @Override
    protected void fillSpecificValues() {
	enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    }

    protected void enableComponentIfCheckBoxIsSelected(String chbName, String cmpName) {
	JCheckBox chb = (JCheckBox) formBody.getComponentByName(chbName);
	Component cmp = formBody.getComponentByName(cmpName);

	if (chb.isSelected()) {
	    cmp.setEnabled(true);
	} else {
	    cmp.setEnabled(false);
	}
    }


    protected void setListeners() {
	super.setListeners();
	JCheckBox hay_anali = (JCheckBox) formBody.getComponentByName("hay_anali");
	hay_anali.setActionCommand("hay_anali");
	hay_anali.addActionListener(this);
    }

    protected void removeListeners() {
	JCheckBox hay_anali = (JCheckBox) formBody.getComponentByName("hay_anali");
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
}
