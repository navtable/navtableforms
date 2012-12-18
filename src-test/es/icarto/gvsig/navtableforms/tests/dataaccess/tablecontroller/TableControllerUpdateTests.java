package es.icarto.gvsig.navtableforms.tests.dataaccess.tablecontroller;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.cresques.cts.IProjection;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.data.NoSuchTableException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.navtableforms.dataacces.TableController;

public class TableControllerUpdateTests {

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
    public void testUpdateTextFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_text");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_text", oldValue + " working");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_text");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals(oldValue + " working");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateDoubleFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_double");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_double", "5.9");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_double");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("5.9");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateFloatFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_float");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_float", "666.333");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_float");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("666.333");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateDateFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_date");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_date", "2/27/2002");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_date");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("2/27/2002");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateShortIntFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_int_shor");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_int_shor", "123");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_int_shor");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("123");
	assertEquals(true, check);
    }

    @Test
    public void testUpdateLongIntFieldFromDBF() throws ReadDriverException,
	    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	String oldValue = tc.getValue("f_int_long");
	System.out.println("oldValue: " + oldValue);
	tc.setValue("f_int_long", "987");
	tc.update(0);
	tc.read(0);
	String newValue = tc.getValue("f_int_long");
	System.out.println("newValue: " + newValue);
	boolean check = newValue.equals("987");
	assertEquals(true, check);
    }

    private IEditableSource getIEditableSourceFromFile(String filePath)
	    throws ReadDriverException, DriverLoadException,
	    NoSuchTableException {
	LayerFactory.getDataSourceFactory().addFileDataSource(
		"gdbms dbf driver", "countries", filePath);
	SelectableDataSource sds2 = new SelectableDataSource(LayerFactory
		.getDataSourceFactory().createRandomDataSource("countries",
			DataSourceFactory.MANUAL_OPENING));
	EditableAdapter ea2 = new EditableAdapter();
	ea2.setOriginalDataSource(sds2);
	return (IEditableSource) ea2;
    }

}
