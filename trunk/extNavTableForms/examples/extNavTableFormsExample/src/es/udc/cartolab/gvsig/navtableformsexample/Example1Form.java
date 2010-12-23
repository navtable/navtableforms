package es.udc.cartolab.gvsig.navtableformsexample;

import java.awt.Container;

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
	viewInfo.setHeight(300);
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
	// TODO Auto-generated method stub
    }

}
