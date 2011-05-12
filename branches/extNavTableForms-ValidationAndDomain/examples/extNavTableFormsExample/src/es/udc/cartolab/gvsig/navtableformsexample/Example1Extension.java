package es.udc.cartolab.gvsig.navtableformsexample;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.udc.cartolab.gvsig.navtableforms.Utils;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public class Example1Extension extends Extension {

    FLyrVect layer;
    private static Logger logger = Logger.getLogger("NTForms Example 1");

    public void execute(String actionCommand) {
	layer = getLayerNameFromXML();
	Example1Form dialog = new Example1Form(layer);
	if (dialog.init()) {
	    PluginServices.getMDIManager().addWindow(dialog);
	}
    }

    private FLyrVect getLayerNameFromXML() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	String layerName = ORMLite
		.getDataBaseObject(Preferences.getXMLFilePath())
		.getTable("Example 1").getTableName();
	return Utils.getFlyrVect((BaseView) window, layerName);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"example1-ntforms",
		this.getClass().getClassLoader()
			.getResource("images/example1.png"));
    }

    public void initialize() {
	registerIcons();
    }

    public boolean isEnabled() {
	return true;
    }

    public boolean isVisible() {
	return true;
    }

}
