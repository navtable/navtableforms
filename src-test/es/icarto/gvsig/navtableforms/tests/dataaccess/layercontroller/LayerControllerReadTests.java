package es.icarto.gvsig.navtableforms.tests.dataaccess.layercontroller;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.cresques.cts.IProjection;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.LoadLayerException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.udc.cartolab.gvsig.navtable.dataacces.LayerController;

public class LayerControllerReadTests {

    public static IProjection TEST_PROJECTION = CRSFactory
	    .getCRS("EPSG:23030");

    @BeforeClass
    public static void loadDrivers() throws Exception {
	doSetup();
    }

    private static void doSetup() throws Exception {
	String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new Exception("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new Exception("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
    }

    @Test
    public void testReadTextFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException,
    DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_text value: " + lc.getValue("f_text"));
	assertEquals(true, lc.getValue("f_text").equals("test"));
	System.out.println("f_text type: " + lc.getType("f_text"));
	assertEquals(true, lc.getType("f_text") == java.sql.Types.VARCHAR);
    }

    @Test
    public void testReadDoubleFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_double value: " + lc.getValue("f_double"));
	assertEquals(true, lc.getValue("f_double").equals("2.4"));
	System.out.println("f_double type: " + lc.getType("f_double"));
	assertEquals(true, lc.getType("f_double") == java.sql.Types.DOUBLE);
    }

    @Test
    public void testReadFloatFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_float value: " + lc.getValue("f_float"));
	assertEquals(true, lc.getValue("f_float").equals("2.9"));
	System.out.println("f_float type: " + lc.getType("f_float"));
	assertEquals(true, lc.getType("f_float") == java.sql.Types.DOUBLE);
    }

    @Test
    public void testReadDateFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_date value: " + lc.getValue("f_date"));
	assertEquals(true, lc.getValue("f_date").equals("8/25/1983"));
	System.out.println("f_date type: " + lc.getType("f_date"));
	assertEquals(true, lc.getType("f_date") == java.sql.Types.DATE);
    }

    @Test
    public void testReadShortIntFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_int_shor value: " + lc.getValue("f_int_shor"));
	assertEquals(true, lc.getValue("f_int_shor").equals("2"));
	System.out.println("f_int_shor type: " + lc.getType("f_int_shor"));
	assertEquals(true, lc.getType("f_int_shor") == java.sql.Types.INTEGER);
    }

    @Test
    public void testReadLongIntFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	System.out.println("f_int_long value: " + lc.getValue("f_int_long"));
	assertEquals(true, lc.getValue("f_int_long").equals("290"));
	System.out.println("f_int_long type: " + lc.getType("f_int_long"));
	assertEquals(true, lc.getType("f_int_long") == java.sql.Types.INTEGER);
    }

    private FLyrVect getFLyrVectFromFile() throws DriverLoadException {
	File file = new File("data-test/", "test.shp");
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	FLyrVect layer = (FLyrVect) LayerFactory.createLayer(
		"Countries",
		(VectorialFileDriver) LayerFactory.getDM().getDriver(
			"gvSIG shp driver"), file,
			CRSFactory.getCRS("EPSG:23030"));
	return layer;
    }

}
