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

package es.icarto.gvsig.navtableforms.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;

import com.hardcode.driverManager.Driver;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.ExtensionDecorator;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileWriteException;
import com.iver.cit.gvsig.exceptions.validate.ValidateRowException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.SHPLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.edition.writers.shp.ShpWriter;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.vividsolutions.jts.geom.Geometry;

import es.udc.cartolab.gvsig.navtable.ToggleEditing;

public class Utils {

    private static Logger logger = Logger.getLogger("NavTable Forms Utils");

    public final static FieldDescription defineFieldDescription(
	    String fieldName, int fieldType, int fieldLength,
	    int fieldDecimalCount) {

	FieldDescription fd = new FieldDescription();

	fd.setFieldName(fieldName);
	fd.setFieldType(fieldType);
	fd.setFieldLength(fieldLength);
	fd.setFieldDecimalCount(fieldDecimalCount);

	return fd;
    }

    /**
     * @param name
     *            of the layer in the toc
     * @return the layer if it's a FlyrVect. null if not exists or it's not a
     *         FlyrVect
     */
    public final static FLyrVect getFlyrVect(BaseView view, String layerName) {
	FLayer layer = null;
	if (view != null) {
	    layer = view.getModel().getMapContext().getLayers()
		    .getLayer(layerName);
	}

	if (layer instanceof FLyrVect) {
	    return (FLyrVect) layer;
	} else {
	    return null;
	}

    }

    public final static FLayer createLayer(String name, String filePath,
	    int geom, FieldDescription[] fieldDesc, IProjection proj,
	    boolean overwrite) {

	File newFile = new File(filePath);
	if (!newFile.exists() || overwrite) {

	    SHPLayerDefinition lyrDef = new SHPLayerDefinition();
	    lyrDef.setFieldsDesc(fieldDesc);
	    lyrDef.setFile(newFile);
	    lyrDef.setName(name);
	    lyrDef.setShapeType(geom);
	    ShpWriter writer = null;
	    try {
		writer = (ShpWriter) LayerFactory.getWM().getWriter(
			"Shape Writer");
	    } catch (DriverLoadException e1) {
		logger.error(e1.getMessage(), e1);
	    }
	    writer.setFile(newFile);
	    try {
		writer.initialize(lyrDef);
		writer.preProcess();
		writer.postProcess();
	    } catch (InitializeWriterException e) {
		logger.error(e.getMessage(), e);
	    } catch (StartWriterVisitorException e) {
		logger.error(e.getMessage(), e);
	    } catch (StopWriterVisitorException e) {
		logger.error(e.getMessage(), e);
	    }

	}

	Driver driver = null;
	try {
	    driver = LayerFactory.getDM().getDriver("gvSIG shp driver");
	} catch (DriverLoadException e) {
	    logger.error(e.getMessage(), e);
	}
	FLayer layer = LayerFactory.createLayer(name,
		(VectorialFileDriver) driver, newFile, proj);

	return layer;

    }

    @SuppressWarnings("unchecked")
    public final static void createFeature(FLyrVect vectLayer,
	    IGeometry feature, Value[] values) throws DriverException {

	// TODO: check if this only works with points!

	try {
	    if (vectLayer.getShapeType() != feature.getGeometryType()) {
		throw new IllegalArgumentException("Feature type invalid");
	    }
	} catch (ReadDriverException e1) {
	    logger.error(e1.getMessage(), e1);
	}

	vectLayer.setActive(true);
	ToggleEditing te = new ToggleEditing();
	// System.out.println("createFeature: " + vectLayer.toString());
	// System.out.println("point: " + feature);
	// for (int i = 0; i < values.length; i++) {
	// System.out.println("values: " + values[i].toString());
	// }

	te.startEditing(vectLayer);
	VectorialLayerEdited vle = (VectorialLayerEdited) CADExtension
		.getEditionManager().getActiveLayerEdited();
	VectorialEditableAdapter vea = vle.getVEA();
	String newFID;
	try {
	    newFID = vea.getNewFID();

	    DefaultFeature df = new DefaultFeature(feature, values, newFID);
	    int index = vea.addRow(df, "_newTramo", EditionEvent.GRAPHIC);
	    // clearSelection();
	    ArrayList selectedRow = vle.getSelectedRow();
	    ViewPort vp = vle.getLayer().getMapContext().getViewPort();
	    BufferedImage selectionImage = new BufferedImage(
		    vp.getImageWidth(), vp.getImageHeight(),
		    BufferedImage.TYPE_INT_ARGB);
	    Graphics2D gs = selectionImage.createGraphics();
	    int inversedIndex = vea.getInversedIndex(index);
	    selectedRow.add(new DefaultRowEdited(df, IRowEdited.STATUS_ADDED,
		    inversedIndex));
	    vea.getSelection().set(inversedIndex);
	    IGeometry geom = df.getGeometry();
	    geom.cloneGeometry().draw(gs, vp, DefaultCADTool.selectionSymbol);
	    vle.drawHandlers(geom.cloneGeometry(), gs, vp);
	    vea.setSelectionImage(selectionImage);
	} catch (ExpansionFileWriteException e) {
	    logger.error(e.getMessage(), e);
	} catch (ValidateRowException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    /**
     * Returns codes of every element of a given layer contained on the given
     * geometry
     * 
     * @param geom
     *            the geometry (usually it should be a (multi)polygon)
     * @param layer
     *            the layer whose elements are selected
     * @param field
     *            the field of the code
     * @return list of the codes as strings (it'll return an empty list if the
     *         field doesn't exist)
     * @throws DriverIOException
     * @throws DriverException
     * @throws com.hardcode.gdbms.engine.data.driver.DriverException
     */
    public static ArrayList<String> getCodesIn(IGeometry geom, FLyrVect layer,
	    String field) throws DriverIOException, DriverException,
	    com.hardcode.gdbms.engine.data.driver.DriverException {

	ArrayList<String> list = new ArrayList<String>();

	SelectableDataSource recordset = null;
	try {
	    recordset = layer.getRecordset();
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	int fieldIdx = 0;
	try {
	    fieldIdx = recordset.getFieldIndexByName(field);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}

	if (fieldIdx > -1) {
	    Geometry jtsgeom = geom.toJTSGeometry();
	    ReadableVectorial rv = layer.getSource();

	    try {
		rv.start();
	    } catch (InitializeDriverException e) {
		logger.error(e.getMessage(), e);
	    } catch (ReadDriverException e) {
		logger.error(e.getMessage(), e);
	    }

	    try {
		for (int i = 0; i < rv.getShapeCount(); i++) {
		    IGeometry g = rv.getShape(i);
		    Geometry jtsg = g.toJTSGeometry();
		    if (jtsgeom.intersects(jtsg)) {
			Value value = recordset.getFieldValue(i, fieldIdx);
			list.add(value
				.getStringValue(ValueWriter.internalValueWriter));
		    }
		}
	    } catch (ExpansionFileReadException e) {
		logger.error(e.getMessage(), e);
	    } catch (ReadDriverException e) {
		logger.error(e.getMessage(), e);
	    }

	    try {
		rv.stop();
	    } catch (ReadDriverException e) {
		logger.error(e.getMessage(), e);
	    }

	} else {
	    // the field doesn't exist on the layer!
	}
	return list;

    }

    public static void saveProject(String gvpPath, boolean overwrite) {

	ProjectExtension pExt = (ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class);
	Project proj = pExt.getProject();
	File file = new File(gvpPath);
	if (!file.exists() || overwrite) {
	    pExt.writeProject(file, proj, false);
	}

    }

    /**
     * removes all CAD related buttons
     */
    public static void removeCADButtons() {

	ExtensionDecorator decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertLineExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPointExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertMultiPointExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPolygonExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.CADExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.ComplexSelectionGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.SelectionGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.LayoutCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.MoveGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InternalPolygonExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.ViewCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.TableCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPolyLineExtension.class);
	decorator.setVisibility(ExtensionDecorator.ALWAYS_INVISIBLE);

    }

    /**
     * Restores CAD related buttons, after using removeCADButtons
     */
    public static void restoreCADButtons() {

	ExtensionDecorator decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertLineExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPointExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertMultiPointExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPolygonExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.CADExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.ComplexSelectionGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.SelectionGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.LayoutCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.MoveGeometryExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InternalPolygonExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.ViewCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.TableCommandStackExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

	decorator = PluginServices
		.getDecoratedExtension(com.iver.cit.gvsig.InsertPolyLineExtension.class);
	decorator.setVisibility(ExtensionDecorator.INACTIVE);

    }

    /**
     * @param recordset
     * @param attNames
     *            the name of the attributes wich index on the recordset we + to
     *            know
     * @return an array that represents the positions of the attributes in the +
     *         recordset
     * @throws Exception
     */
    public static int[] getIndexes(SelectableDataSource recordset,
	    String[] attNames) throws Exception {
	int[] attPos = new int[attNames.length];
	for (int i = 0; i < attNames.length; i++) {
	    attPos[i] = recordset.getFieldIndexByName(attNames[i]);
	}
	return attPos;
    }

    /**
     * Dummy method that calls getIndexes with the recordset instead of the
     * layer
     * 
     * @param layer
     * @return an array that represents the positions of the attributes in the +
     *         recordset
     * @throws Exception
     */
    public static int[] getIndexes(FLyrVect layer, String[] attNames)
	    throws Exception {
	return getIndexes(layer.getRecordset(), attNames);
    }

    /**
     * Get the length of a line
     * 
     * @param layer
     *            vectorial layer
     * @param geomPos
     *            the position of the geometry of which length will be
     *            calculated
     * @return
     * @throws DriverException
     * @throws DriverIOException
     * @throws IllegalArgumentException
     *             if the given position doesn't exist on the layer
     */
    public static double getGeometryLength(FLyrVect layer, int geomPos)
	    throws DriverException, DriverIOException, IllegalArgumentException {

	double length = 0;

	ReadableVectorial source = layer.getSource();
	try {
	    source.start();
	} catch (InitializeDriverException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	try {
	    if (geomPos < source.getShapeCount()) {
		IGeometry gvGeom = source.getShape(geomPos);
		Geometry geom = gvGeom.toJTSGeometry();
		source.stop();
		length = geom.getLength();
	    } else {
		source.stop();
		throw new IllegalArgumentException(
			"The geometry position doesn't exist on the layer.");
	    }
	} catch (ExpansionFileReadException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}

	return length;
    }

    // @SuppressWarnings("unchecked")
    // public final static void createFeature2(FLyrVect vectLayer, IGeometry
    // feature,
    // Value[] values, VectorialLayerEdited vle) throws DriverException {
    //
    // //TODO: check if this only works with points!
    //
    // if (vectLayer.getShapeType() != feature.getGeometryType()) {
    // throw new IllegalArgumentException("Feature type invalid");
    // }
    //
    //
    // try {
    // VectorialEditableAdapter vea = vle.getVEA();
    //
    // String newFID;
    // try {
    // newFID = vea.getNewFID();
    //
    // DefaultFeature df = new DefaultFeature(feature, values, newFID);
    // int index = vea.addRow(df, "_newTramo", EditionEvent.GRAPHIC);
    // //clearSelection();
    // ArrayList selectedRow = vle.getSelectedRow();
    // ViewPort vp = vle.getLayer().getMapContext().getViewPort();
    // BufferedImage selectionImage = new BufferedImage(vp
    // .getImageWidth(), vp.getImageHeight(),
    // BufferedImage.TYPE_INT_ARGB);
    // Graphics2D gs = selectionImage.createGraphics();
    // int inversedIndex=vea.getInversedIndex(index);
    // selectedRow.add(new DefaultRowEdited(df,
    // IRowEdited.STATUS_ADDED, inversedIndex ));
    // vea.getSelection().set(inversedIndex);
    // IGeometry geom = df.getGeometry();
    // geom.cloneGeometry().draw(gs, vp, DefaultCADTool.selectionSymbol);
    // vle.drawHandlers(geom.cloneGeometry(), gs, vp);
    // vea.setSelectionImage(selectionImage);
    // } catch (IOException e) {
    // //logger.debug(e);
    // NotificationManager.addError(e);
    // return;
    // }
    //
    // // SelectableDataSource recordset = vectLayer.getRecordset();
    // // recordset.clearSelection();
    // // te.stopEditing(vectLayer, false);
    //
    // }catch (DriverIOException e) {
    // NotificationManager.addError(e.getMessage(), e);
    // } catch (DriverLoadException e) {
    // NotificationManager.addError(e.getMessage(), e);
    // }
    // }

    // /**
    // * Create a new feature, and initialize the attributes indexed on colPos
    // with the values
    // * passed on attValues
    // *
    // * @param layer: The layer where add the new feature
    // * @param geom: The IGeometry of the new feature
    // * @param colPos: Index of the attributes that are going to be initialized
    // * @param attValues: The values of the attributes
    // */
    // public final static void createFeature(FLyrVect layer, IGeometry geom,
    // int[] colPos, String[] attStringValues) {
    // try {
    // VectorialLayerEdited vle = getVLE(layer);
    // VectorialEditableAdapter edAdapter = vle.getVEA();
    //
    // ITableDefinition tableDef = edAdapter.getTableDefinition();
    // FieldDescription[] fieldDesc; fieldDesc = tableDef.getFieldsDesc();
    //
    // /* create an Value's array with the size of the number of fields in the
    // feature
    // * an initilize it to NullValue
    // */
    // Value[] attValues = new Value[fieldDesc.length];
    // Arrays.fill(attValues, ValueFactory.createNullValue());
    // insertTheStringsAsValuesInTheirPosition(attValues, colPos,
    // attStringValues, fieldDesc);
    // System.out.println(attValues.toString());
    // createFeature2(layer, geom, attValues, vle);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    // public static final VectorialLayerEdited getVLE(FLyrVect layer) {
    // layer.setActive(true);
    // ToggleEditing te = new ToggleEditing();
    //
    // te.startEditing(layer);
    // VectorialLayerEdited vle = (VectorialLayerEdited)
    // CADExtension.getEditionManager().getActiveLayerEdited();
    // return vle;
    // }

    public final static void insertTheStringsAsValuesInTheirPosition(
	    Value[] attValues, int[] colPos, String[] attStringValues,
	    FieldDescription[] fieldDesc) {
	int type;
	// iterate throw the values that changed creating an array with all the
	// new values of the row
	for (int i = 0; i < colPos.length; i++) {
	    // get the type of the layer's field
	    type = fieldDesc[colPos[i]].getFieldType();
	    if (type == 16) { // in this case type is boolean ¿fpuga: Why this
			      // must be checked?
		type = Types.BIT;
	    }

	    // modify the value that changed
	    if (attStringValues[i] == null || attStringValues[i].length() == 0) {
		// attValues should be initialize to Null so we can avoid create
		// it here. Just in case.
		attValues[colPos[i]] = ValueFactory.createNullValue();
	    } else {
		try {
		    attValues[colPos[i]] = ValueFactory.createValueByType(
			    attStringValues[i], type);
		} catch (ParseException e) {
		    logger.error("Tipo incorrecto: El valor "
			    + attStringValues[i] + "debería ser "
			    + FieldDescription.typeToString(type));
		} catch (NumberFormatException nfe) {
		    logger.error("Tipo incorrecto: El valor "
			    + attStringValues[i] + "debería ser "
			    + FieldDescription.typeToString(type));
		}
	    }
	}
    }

    public static String[] getValuesFromLayer(FLyrVect layer, long pos,
	    String[] fields) {

	String[] values = null;

	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();

	    values = new String[fields.length];
	    for (int i = 0; i < fields.length; i++) {
		int fieldIdx = recordset.getFieldIndexByName(fields[i]);
		if (fieldIdx > -1) {
		    Value val = recordset.getFieldValue(pos, fieldIdx);
		    String aux = val
			    .getStringValue(ValueWriter.internalValueWriter);
		    if (aux.equalsIgnoreCase("null")) {
			aux = "";
		    } else if (aux.equalsIgnoreCase("'null'")) {
			aux = "''";
		    }
		    if ((val.getSQLType() == Types.CHAR)
			    || (val.getSQLType() == Types.VARCHAR)
			    || (val.getSQLType() == Types.LONGVARCHAR)
			    || (val.getSQLType() == Types.LONGNVARCHAR)) {
			// remove quotation marks
			values[i] = aux.substring(1, aux.length() - 1);
		    } else {
			values[i] = aux;
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	return values;
    }

    public static String getValueFromLayer(FLyrVect layer, long pos,
	    String field) {
	return getValuesFromLayer(layer, pos, new String[] { field })[0];
    }

    public static String getValue(SelectableDataSource recordset, long pos,
	    String field) {
	return getValues(recordset, pos, new String[] { field })[0];
    }

    public static String[] getValues(SelectableDataSource recordset, long pos,
	    String[] fields) {
	ArrayList<String> values = new ArrayList<String>();
	try {
	    for (String field : fields) {
		int index = recordset.getFieldIndexByName(field);
		if (index > -1) {
		    Value val = recordset.getFieldValue(pos, index);
		    String aux = val
			    .getStringValue(ValueWriter.internalValueWriter);
		    if ((val.getSQLType() == Types.CHAR)
			    || (val.getSQLType() == Types.VARCHAR)
			    || (val.getSQLType() == Types.LONGVARCHAR)
			    || (val.getSQLType() == Types.LONGNVARCHAR)) {
			// remove quotation marks
			values.add(aux.substring(1, aux.length() - 1));
		    } else {
			values.add(aux);
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	}
	return values.toArray(new String[0]);
    }

    public static boolean deleteDirectory(File directory) {
	boolean resultado;
	if (directory.isDirectory()) {
	    File[] files = directory.listFiles();
	    for (int i = 0; i < files.length; i++) {
		if (files[i].isDirectory()) {
		    deleteDirectory(files[i]);
		} else {
		    files[i].delete();
		}
	    }
	    resultado = directory.delete();
	} else {
	    resultado = false;
	}

	return resultado;
    }
}
