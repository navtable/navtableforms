package es.icarto.gvsig.navtableforms.utils;

import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;

/**
 * FormFactory
 * 
 * This abstract class must be extended by every form factory we create. These
 * form factories are intended to be the way of easily obtaining forms and
 * subforms and everything related to them, while also providing simple methods
 * for checking and loading layers and tables.
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 */

public abstract class FormFactory {

    /**
     * The registered default instance of a form factory
     */
    private static FormFactory instance;
    
    /**
     * Method for obtaining a form for a particular layer.
     * 
     * @param layer
     * @return an AbstractForm for that layer
     */
    public abstract AbstractForm createForm(FLyrVect layer);

    /**
     * Method for obtaining the singleton form for a particular layer.
     * 
     * @param layer
     * @return an AbstractForm for that layer
     */
    public abstract AbstractForm createSingletonForm(FLyrVect layer);

    /**
     * Method for obtaining a form for a particular layer by its name.
     * 
     * @param layerName
     * @return an AbstractForm for that layer
     */
    public abstract AbstractForm createForm(String layerName);

    /**
     * Method for obtaining the singleton form for a particular layer by its
     * name.
     * 
     * @param layerName
     * @return an AbstractForm for that layer
     */
    public abstract AbstractForm createSingletonForm(String layerName);

    /**
     * Method for obtaining a subform for a particular table.
     * 
     * @param tableName
     * @return an AbstractSubForm for that table
     */
    public abstract AbstractSubForm createSubForm(String tableName);

    /**
     * Checks whether the provided string is the name of a layer with a form.
     * 
     * @param layerName
     * @return true if it has a registered form, false otherwise
     */
    public abstract boolean hasMainForm(String layerName);

    /**
     * Checks whether all the mandatory layers are currently loaded.
     * 
     * @return true if all of them are loaded, false otherwise
     */
    public abstract boolean allLayersLoaded();

    /**
     * Checks whether a specific layer is currently loaded.
     * 
     * @param layerName
     * @return true if the layer is loaded, false otherwise
     */
    public abstract boolean checkLayerLoaded(String layerName);

    /**
     * Checks whether a specific table is currently loaded.
     * 
     * @param tableName
     * @return true if the table is loaded, false otherwise
     */
    public abstract boolean checkTableLoaded(String tableName);

    /**
     * Loads the layer with the specified name in the current/default View.
     * 
     * @param layerName
     */
    public abstract void loadLayer(String layerName);

    /**
     * Loads the table with the specified name.
     * 
     * @param tableName
     */
    public abstract void loadTable(String tableName);

    /**
     * Loads the layer with the specified name if it isn't.
     * 
     * @param layerName
     */
    public void checkAndLoadLayer(String layerName) {
	if (!checkLayerLoaded(layerName)) {
	    loadLayer(layerName);
	}
    }

    /**
     * Loads the table with the specified name if it isn't.
     * 
     * @param tableName
     */
    public void checkAndLoadTable(String tableName) {
	if (!checkTableLoaded(tableName)) {
	    loadTable(tableName);
	}
    }

    /**
     * Registers a factory as the default one.
     * 
     * @param factory
     */
    public static void registerFormFactory(FormFactory factory) {
	instance = factory;
    }

    /**
     * Checks whether we have a registered default factory or not.
     * 
     * @return true if we have a registered factory, false otherwise
     */
    public static boolean hasRegisteredFactory() {
	return (instance != null);
    }

    /**
     * Calls the loadLayer method in the registered factory.
     * 
     * @param layerName
     * @see loadLayer
     */
    public static void loadLayerRegistered(String layerName) {
	if (instance != null) {
	    instance.loadLayer(layerName);
	}
    }

    /**
     * Calls the loadTable method in the registered factory.
     * 
     * @param tableName
     * @see loadTable
     */
    public static void loadTableRegistered(String tableName) {
	if (instance != null) {
	    instance.loadTable(tableName);
	}
    }

    /**
     * Calls the checkLayerLoaded method in the registered factory.
     * 
     * @param layerName
     * @see checkLayerLoaded
     */
    public static boolean checkLayerLoadedRegistered(String layerName) {
	if (instance != null) {
	    return instance.checkLayerLoaded(layerName);
	}
	return false;
    }

    /**
     * Calls the checkTableLoaded method in the registered factory.
     * 
     * @param tableName
     * @see checkTableLoaded
     */
    public static boolean checkTableLoadedRegistered(String tableName) {
	if (instance != null) {
	    return instance.checkTableLoaded(tableName);
	}
	return false;
    }

    /**
     * Calls the checkAndLoadLayer method in the registered factory.
     * 
     * @param layerName
     * @see checkAndLoadLayer
     */
    public static void checkAndLoadLayerRegistered(String layerName) {
	if (instance != null) {
	    instance.checkAndLoadLayer(layerName);
	}
    }

    /**
     * Calls the checkAndLoadTable method in the registered factory.
     * 
     * @param tableName
     * @see checkAndLoadTable
     */
    public static void checkAndLoadTableRegistered(String tableName) {
	if (instance != null) {
	    instance.checkAndLoadTable(tableName);
	}
    }

    /**
     * Calls the createForm method in the registered factory.
     * 
     * @param layer
     * @return an AbstractForm for that layer
     * @see createForm
     */
    public static AbstractForm createFormRegistered(FLyrVect layer) {
	if (instance != null) {
	    return instance.createForm(layer);
	}
	return null;
    }

    /**
     * Calls the createSingletonForm method in the registered factory.
     * 
     * @param layerName
     * @return an AbstractForm for that layer
     * @see createSingletonForm
     */
    public static AbstractForm createSingletonFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.createSingletonForm(layerName);
	}
	return null;
    }

    /**
     * Calls the createForm method in the registered factory.
     * 
     * @param layerName
     * @return an AbstractForm for that layer
     * @see createForm
     */
    public static AbstractForm createFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.createForm(layerName);
	}
	return null;
    }

    /**
     * Calls the createSingletonForm method in the registered factory.
     * 
     * @param layer
     * @return an AbstractForm for that layer
     * @see createSingletonForm
     */
    public static AbstractForm createSingletonFormRegistered(FLyrVect layer) {
	if (instance != null) {
	    return instance.createSingletonForm(layer);
	}
	return null;
    }

    /**
     * Calls the createSubFormRegistered method in the registered factory.
     * 
     * @param tableName
     * @return an AbstractSubForm for that table
     * @see createSubFormRegistered
     */
    public static AbstractSubForm createSubFormRegistered(String tableName) {
	if (instance != null) {
	    return instance.createSubForm(tableName);
	}
	return null;
    }

    /**
     * Calls the createForm method in the registered factory.
     * 
     * @return true if it has a registered form, false otherwise
     * @see createForm
     */
    public static boolean hasMainFormRegistered(String layerName) {
	if (instance != null) {
	    return instance.hasMainForm(layerName);
	}
	return false;
    }

    /**
     * Calls the allLayersLoaded method in the registered factory.
     * 
     * @return true if all of them are loaded, false otherwise
     * @see allLayersLoaded
     */
    public static boolean allLayersLoadedRegistered() {
	if (instance != null) {
	    return instance.allLayersLoaded();
	}
	return false;
    }
}
