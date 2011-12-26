package es.icarto.gvsig.navtableforms.utils;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class TOCLayerManager {

    private FLayers layersInTOC = null;
    private MapControl mapControl = null;

    public TOCLayerManager() {
	IWindow[] windows = PluginServices.getMDIManager().getOrderedWindows();
	BaseView view = null;
	for (IWindow w : windows) {
	    if (w instanceof BaseView) {
		view = (BaseView) w;
		break;
	    }
	}
	if(view != null) {
	    mapControl = view.getMapControl();
	    layersInTOC = mapControl.getMapContext().getLayers();	    
	}
    }

    public void setVisibleAllLayers() {
	if(layersInTOC != null) {	    
	    layersInTOC.setAllVisibles(true);
	}
    }

    public void setActiveAndVisibleLayer(String layerName) {
	if(layersInTOC != null) {
	    layersInTOC.setAllVisibles(false);
	    layersInTOC.setAllActives(false);
	    for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
		FLayer layer = layersInTOC.getLayer(i);
		String name = layer.getName();
		if (name.equalsIgnoreCase(layerName)) {
		    layer.setVisible(true);
		    layer.setActive(true);
		}
	    }
	}
    }

    public FLyrVect getLayerByName(String layerName) {
	if(layersInTOC != null) {
	    for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
		if (layersInTOC.getLayer(i).getName().equalsIgnoreCase(layerName)) {
		    return (FLyrVect) layersInTOC.getLayer(i);
		}
		//Checking if layer is a group
		if (layersInTOC.getLayer(i) instanceof FLayers) {
			FLayers layersInGroup = (FLayers) layersInTOC.getLayer(i);
			for (int j = 0; j < layersInGroup.getLayersCount(); j++) {
				if (layersInGroup.getLayer(j).getName().equalsIgnoreCase(layerName)) {
				    return (FLyrVect) layersInGroup.getLayer(j);
				}
			}
		}
	    }
	}
	return null;
    }

    public FLyrVect getActiveLayer() {
	if(mapControl != null) {
	    return (FLyrVect) mapControl.getMapContext().getLayers().getActives()[0];
	}
	return null;
    }

    public String getNameOfActiveLayer() {
	if(mapControl != null) {
	    return mapControl.getMapContext().getLayers().getActives()[0].getName();
	}
	return null;
    }

}
