/*
 * Copyright (c) 2011. iCarto
 * 
 * This file is part of extNavTableForms
 * 
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package es.icarto.gvsig.navtableforms.ormlite;

import java.util.HashMap;

import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;

/**
 * Class storing the metadata of all layers of the project.
 * 
 * @author Andrés Maneiro <amaneiro@cartolab.es>
 * 
 */
public class ORMLiteLayerSet {

    private HashMap<String, CartoLayer> layerSet = null;

    ORMLiteLayerSet() {
	layerSet = new HashMap<String, CartoLayer>();
    }

    public void addLayer(String key, CartoLayer value) {
	this.layerSet.put(key, value);
    }

    public HashMap<String, CartoLayer> getLayerSet() {
	return this.layerSet;
    }

    /**
     * @param key
     *            . The same as the layerAlias.
     * @return the Layer
     */
    public CartoLayer getLayer(String key) {
	return this.layerSet.get(key);
    }

    public CartoLayer getLayerByName(String layerName) {
	for (CartoLayer fl : layerSet.values()) {
	    if (layerName.equals(fl.getLayerName())) {
		return fl;
	    }
	}
	return null;
    }

    /**
     * Class storing the metadata of one layer.
     * 
     * @author Andrés Maneiro <amaneiro@cartolab.es>
     * 
     */
    public class CartoLayer {

	/* indexes for recordSet are the index in the DBF of that field */
	private HashMap<Integer, FieldDescription> recordSet = null;
	private String layerName = "";
	private String layerAlias = "";
	private int layerGeometry = -1;

	private final HashMap<String, Integer> geometry = new HashMap<String, Integer>();

	CartoLayer() {
	    recordSet = new HashMap<Integer, FieldDescription>();
	    geometry.put("NULL", FShape.NULL);
	    geometry.put("ALPHANUMERIC", FShape.NULL);
	    geometry.put("POINT", FShape.POINT);
	    geometry.put("LINE", FShape.LINE);
	    geometry.put("POLYGON", FShape.POLYGON);
	    // geometry.put("Text", FShape.TEXT);
	    // geometry.put("Multi", FShape.MULTI);
	    // geometry.put("Multipoint", FShape.MULTIPOINT);
	    // geometry.put("Circle", FShape.CIRCLE);
	    // geometry.put("Arc", FShape.ARC);
	    // geometry.put("Ellipse", FShape.ELLIPSE);
	}

	public String getLayerName() {
	    return layerName;
	}

	public void setLayerName(String layerName) {
	    this.layerName = layerName;
	}

	/**
	 * Layer alias. The same as the key from XML.
	 * 
	 * @return the alias of the layer
	 */
	public String getLayerAlias() {
	    return layerAlias;
	}

	public void setLayerAlias(String layerAlias) {
	    this.layerAlias = layerAlias;
	}

	public int getLayerGeometry() {
	    return layerGeometry;
	}

	public void setLayerGeometryFromString(String layerGeometry) {
	    this.layerGeometry = geometry.get(layerGeometry.toUpperCase());
	}

	public void setLayerGeometry(int layerGeometry) {
	    this.layerGeometry = layerGeometry;
	}

	public void addField(Integer key, FieldDescription ff) {
	    this.recordSet.put(key, ff);
	}

	public HashMap<Integer, FieldDescription> getRecordSet() {
	    return this.recordSet;
	}

	public int getRecordSetCount() {
	    return recordSet.size();
	}

	/**
	 * 
	 * @param key
	 *            . The same as the fieldAlias from FieldDescription class.
	 * @return the fieldDescription
	 */
	public FieldDescription getField(Integer key) {
	    return recordSet.get(key);
	}

	public FieldDescription getFieldByName(String fieldName) {
	    for (FieldDescription ff : recordSet.values()) {
		if (fieldName.equals(ff.getFieldName())) {
		    return ff;
		}
	    }
	    return null;
	}
    }

}
