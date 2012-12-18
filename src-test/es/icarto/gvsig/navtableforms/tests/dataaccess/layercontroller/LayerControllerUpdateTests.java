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

import es.icarto.gvsig.navtableforms.dataacces.LayerController;

public class LayerControllerUpdateTests {

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
    public void testUpdateTextFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException,
    DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_text");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_text", oldValue + " working");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_text");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals(oldValue + " working");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateDoubleFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_double");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_double", "5.9");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_double");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("5.9");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateFloatFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_float");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_float", "666.333");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_float");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("666.333");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateDateFieldFromShapeFile() throws LoadLayerException,
    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_date");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_date", "2/27/2002");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_date");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("2/27/2002");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateShortIntFieldFromShapeFile()
	    throws LoadLayerException,
	    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_int_shor");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_int_shor", "123");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_int_shor");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("123");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateLongIntFieldFromShapeFile()
	    throws LoadLayerException,
	    ReadDriverException, DriverLoadException {
	FLyrVect layer = getFLyrVectFromFile();
	LayerController lc = new LayerController(layer);
	lc.read(0);
	String oldValue = lc.getValue("f_int_long");
	System.out.println("oldValue: " + oldValue);
	lc.setValue("f_int_long", "987");
	lc.save(0);
	lc.read(0);
	String newValue = lc.getValue("f_int_long");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("987");
	assertEquals(true, check);
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
