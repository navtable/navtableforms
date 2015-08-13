package es.icarto.gvsig.navtableforms.utils;

import java.util.ArrayList;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class TOCLayerManager {

    private BaseView view = null;
    private FLayers layersInTOC = null;
    private MapControl mapControl = null;

    public TOCLayerManager() {
	IWindow[] windows = PluginServices.getMDIManager().getOrderedWindows();
	for (IWindow w : windows) {
	    if (w instanceof BaseView) {
		view = (BaseView) w;
		break;
	    }
	}
	if (view != null) {
	    mapControl = view.getMapControl();
	    layersInTOC = mapControl.getMapContext().getLayers();
	}
    }

    public TOCLayerManager(MapControl mapControl) {
	this.mapControl = mapControl;
	this.layersInTOC = mapControl.getMapContext().getLayers();
    }

    public void setVisibleAllLayers() {
	if (layersInTOC != null) {
	    layersInTOC.setAllVisibles(true);
	}
    }

    public void setActiveAndVisibleLayer(String layerName) {
	if (layersInTOC != null) {
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
	if (layersInTOC != null) {
	    List<FLyrVect> innerLayers = getInnerLayers(layersInTOC);
	    for (FLyrVect l : innerLayers) {
		if (hasName(l, layerName)) {
		    return l;
		}
	    }
	}
	return null;
    }

    public FLyrVect getActiveLayer() {
	if (mapControl != null) {
	    FLayer[] activeLayers = mapControl.getMapContext().getLayers()
		    .getActives();
	    for (FLayer layer : activeLayers) {
		if (isFLyrVect(layer)) {
		    return (FLyrVect) layer;
		}
	    }
	}
	return null;
    }

    public FLyrVect[] getActiveLayers() {
	List<FLyrVect> layers = new ArrayList<FLyrVect>();
	if (mapControl != null) {
	    FLayer[] activeLayers = mapControl.getMapContext().getLayers()
		    .getActives();
	    for (FLayer layer : activeLayers) {
		if (isFLyrVect(layer)) {
		    layers.add((FLyrVect) layer);
		}
	    }
	}
	return layers.toArray(new FLyrVect[0]);
    }

    public FLyrVect[] getVisibleLayers() {
	List<FLyrVect> layers = new ArrayList<FLyrVect>();
	if (mapControl != null) {
	    FLayer[] activeLayers = mapControl.getMapContext().getLayers()
		    .getVisibles();
	    for (FLayer layer : activeLayers) {
		if (isFLyrVect(layer)) {
		    layers.add((FLyrVect) layer);
		}
	    }
	}
	return layers.toArray(new FLyrVect[0]);
    }

    public String getNameOfActiveLayer() {
	FLyrVect layer = getActiveLayer();
	if (layer != null) {
	    return layer.getName();
	}
	return null;
    }

    public FLyrVect[] getAllLayers() {
	List<FLyrVect> layers = new ArrayList<FLyrVect>();
	if (mapControl != null) {
	    layers.addAll(getInnerLayers(mapControl.getMapContext().getLayers()));
	}
	return layers.toArray(new FLyrVect[0]);
    }

    public List<FLyrVect> getJoinedLayers() {
	FLyrVect[] layers = getAllLayers();
	List<FLyrVect> joinedLayers = new ArrayList<FLyrVect>();
	for (FLyrVect l : layers) {
	    if (l.isJoined()) {
		joinedLayers.add(l);
	    }
	}
	return joinedLayers;
    }

    public View getView() {
	if (view instanceof View) {
	    return (View) view;
	}
	return null;
    }

    protected List<FLyrVect> getInnerLayers(FLayers layerGroup) {
	List<FLyrVect> layers = new ArrayList<FLyrVect>();
	for (int i = 0, len = layerGroup.getLayersCount(); i < len; i++) {
	    FLayer layer = layerGroup.getLayer(i);
	    if (isFLayers(layer)) {
		layers.addAll(getInnerLayers((FLayers) layer));
		continue;
	    }
	    if (isFLyrVect(layer)) {
		layers.add((FLyrVect) layer);
		continue;
	    }
	}
	return layers;
    }

    public void removeAllLayers() {
	layersInTOC.setAllVisibles(false);
	removeAllLayers(layersInTOC);
    }

    private void removeAllLayers(FLayers layers) {
	while (layers.getLayersCount() > 0) {
	    FLayer layer = layers.getLayer(0);
	    if (isFLayers(layer)) {
		removeAllLayers((FLayers) layer);
	    }
	    layers.removeLayer(0);
	}
    }

    public void removeAllOverviewLayer() {
	FLayers layers = view.getMapOverview().getMapContext().getLayers();
	removeAllLayers(layers);
    }

    public boolean hasName(FLayer layer, String layerName) {
	return layer.getName().equalsIgnoreCase(layerName);
    }

    public boolean isFLyrVect(FLayer layer) {
	return layer instanceof FLyrVect;
    }

    public boolean isFLayers(final FLayer layer) {
	return layer instanceof FLayers;
    }
}
