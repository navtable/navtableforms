package es.icarto.gvsig.navtableforms.tests;

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

public class CRUDOperationsTest {

    public static IProjection TEST_PROJECTION = CRSFactory
	    .getCRS("EPSG:23030");

    @BeforeClass
    public static void loadTestShape() throws Exception {
	doSetup();
    }

    @Test
    public void readOperation() throws LoadLayerException, ReadDriverException,
    DriverLoadException {
	File file = new File("data-test/", "110m_land.shp");
	FLyrVect layer = (FLyrVect) LayerFactory.createLayer(
		"Municipios",
		(VectorialFileDriver) LayerFactory.getDM().getDriver(
			"gvSIG shp driver"), file,
			CRSFactory.getCRS("EPSG:23030"));
	System.out.println("Shape count: " + layer.getSource().getShapeCount());
	LayerController lc = new LayerController(layer);
	lc.read(0); // FeatureCla = "Country" (string), FIRST_Scal = 1 (integer)
	String value = lc.getValue("FeatureCla");
	boolean check = value.equals("Country");
	assertEquals(true, check);
    }

    @Test
    public void saveOperation() throws LoadLayerException, ReadDriverException,
    DriverLoadException {
	File file = new File("data-test/", "110m_land.shp");
	FLyrVect layer = (FLyrVect) LayerFactory.createLayer(
		"Municipios",
		(VectorialFileDriver) LayerFactory.getDM().getDriver(
			"gvSIG shp driver"), file,
			CRSFactory.getCRS("EPSG:23030"));
	LayerController lc = new LayerController(layer);
	lc.read(1); // FeatureCla = "Country" (string), FIRST_Scal = 1 (integer)
	String oldValue = lc.getValue("FeatureCla");
	lc.setValue("FeatureCla", oldValue + "Test");
	lc.save(1);
	String newValue = lc.getValue("FeatureCla");
	boolean check = newValue.equals(oldValue + "Test");
	assertEquals(true, check);
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

}
