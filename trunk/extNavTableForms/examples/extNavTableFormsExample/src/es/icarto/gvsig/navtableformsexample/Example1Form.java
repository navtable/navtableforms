package es.icarto.gvsig.navtableformsexample;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

public class Example1Form extends AbstractForm {

    private FormPanel form;
    private JCheckBox chb;
    private JTextField cmp;
    private ComponentEnablerListener componentEnablerListener;

    public Example1Form(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	viewInfo.setHeight(350);
	viewInfo.setWidth(450);
	viewInfo.setTitle("Example 1");
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

    @Override
    protected void fillSpecificValues() {
	enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    }

    private void enableComponentIfCheckBoxIsSelected(String chbName,
	    String cmpName) {

	if (chb.isSelected()) {
	    cmp.setEnabled(true);
	} else {
	    cmp.setEnabled(false);
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	cmp = (JTextField) widgets.get("RESULTADO");
	chb = (JCheckBox) widgets.get("HAY_ANALI");

	componentEnablerListener = new ComponentEnablerListener();
	chb.addActionListener(componentEnablerListener);
    }

    @Override
    protected void removeListeners() {
	chb.removeActionListener(componentEnablerListener);
	super.removeListeners();
    }

    public class ComponentEnablerListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
	}

    }

}
