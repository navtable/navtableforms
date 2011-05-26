package es.udc.cartolab.gvsig.navtableforms.launcher;

import javax.swing.event.InternalFrameListener;

public interface ILauncherForm extends InternalFrameListener {

    public String getSQLQuery(String queryID);

}
