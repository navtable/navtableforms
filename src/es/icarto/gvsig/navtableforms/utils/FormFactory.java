package es.icarto.gvsig.navtableforms.utils;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;

public abstract class FormFactory {

    protected static FormFactory instance;
    
    public abstract AbstractForm createForm(FLyrVect layer);

    public abstract AbstractForm createSingletonForm(FLyrVect layer);

    public abstract AbstractForm createForm(String layerName);

    public abstract AbstractForm createSingletonForm(String layerName);

    public abstract AbstractSubForm createSubForm(String tableName);

    public abstract boolean hasMainForm(String layerName);

    public abstract boolean allLayersLoaded();

    public abstract void checkLayerLoaded(String layerName);

    public abstract void checkTableLoaded(String tableName);

    public static void registerFormFactory(FormFactory factory) {
	instance = factory;
    }

    public static boolean hasRegisteredFactory() {
	return (instance != null);
    }

    public static void checkLayerLoadedRegistered(String layerName) {
	if (instance != null) {
	    instance.checkLayerLoaded(layerName);
	}
    }

    public static void checkTableLoadedRegistered(String layerName) {
	if (instance != null) {
	    instance.checkTableLoaded(layerName);
	}
    }

    public static AbstractForm createFormRegistered(FLyrVect layer) {
	if (instance != null) {
	    return instance.createForm(layer);
	}
	return null;
    }

    public static AbstractForm createSingletonFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.createSingletonForm(layerName);
	}
	return null;
    }

    public static AbstractForm createFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.createForm(layerName);
	}
	return null;
    }

    public static AbstractForm createSingletonFormRegistered(FLyrVect layer) {
	if (instance != null) {
	    return instance.createSingletonForm(layer);
	}
	return null;
    }

    public static AbstractSubForm createSubFormRegistered(String tableName) {
	if (instance != null) {
	    return instance.createSubForm(tableName);
	}
	return null;
    }

    public static boolean hasMainFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.hasMainForm(layerName);
	}
	return false;
    }

    public static boolean allLayersLoadedRegistered() {
	if (instance != null) {
	    return instance.allLayersLoaded();
	}
	return false;
    }
}
