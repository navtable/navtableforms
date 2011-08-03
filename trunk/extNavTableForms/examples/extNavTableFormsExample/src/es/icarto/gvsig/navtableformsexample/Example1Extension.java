package es.icarto.gvsig.navtableformsexample;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.Utils;

public class Example1Extension extends Extension {

    FLyrVect layer;
    private static Logger logger = Logger.getLogger("NTForms Example 1");

    public void execute(String actionCommand) {
	layer = getLayerFromTOC();
	Example1Form dialog = new Example1Form(layer);
	if (dialog.init()) {
	    PluginServices.getMDIManager().addWindow(dialog);
	}
    }

    private FLyrVect getLayerFromTOC() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if (window instanceof BaseView) {
	    String layerName = ORMLite
		    .getDataBaseObject(Preferences.XMLDATAFILE_PATH)
		    .getTable("Example 1").getTableName();
	    return Utils.getFlyrVect((BaseView) window, layerName);
	}
	return null;
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
	if (isExampleDataSetLoaded()) {
	    return true;
	}
	return false;
    }

    private boolean isExampleDataSetLoaded() {
	if (getLayerFromTOC() == null) {
	    return false;
	}
	return true;
    }

    public boolean isVisible() {
	return true;
    }

}
