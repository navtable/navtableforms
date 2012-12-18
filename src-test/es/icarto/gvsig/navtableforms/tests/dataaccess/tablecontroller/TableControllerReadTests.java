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

public class TableControllerReadTests {

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
    public void testReadTextFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_text value: " + tc.getValue("f_text"));
	assertEquals(true, tc.getValue("f_text").equals("test"));
	System.out.println("f_text type: " + tc.getType("f_text"));
	assertEquals(true, tc.getType("f_text") == java.sql.Types.VARCHAR);
    }

    @Test
    public void testReadDoubleFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_double value: " + tc.getValue("f_double"));
	assertEquals(true, tc.getValue("f_double").equals("2.4"));
	System.out.println("f_double type: " + tc.getType("f_double"));
	assertEquals(true, tc.getType("f_double") == java.sql.Types.DOUBLE);
    }

    @Test
    public void testReadFloatFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_float value: " + tc.getValue("f_float"));
	assertEquals(true, tc.getValue("f_float").equals("2.9"));
	System.out.println("f_float type: " + tc.getType("f_float"));
	assertEquals(true, tc.getType("f_float") == java.sql.Types.DOUBLE);
    }

    @Test
    public void testReadDateFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_date value: " + tc.getValue("f_date"));
	assertEquals(true, tc.getValue("f_date").equals("8/25/1983"));
	System.out.println("f_date type: " + tc.getType("f_date"));
	assertEquals(true, tc.getType("f_date") == java.sql.Types.DATE);
    }

    @Test
    public void testReadShortIntFieldFromDBF()
	    throws ReadDriverException, DriverLoadException,
	    NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_int_shor value: " + tc.getValue("f_int_shor"));
	assertEquals(true, tc.getValue("f_int_shor").equals("2"));
	System.out.println("f_int_shor type: " + tc.getType("f_int_shor"));
	assertEquals(true, tc.getType("f_int_shor") == java.sql.Types.INTEGER);
    }

    @Test
    public void testReadLongIntFieldFromDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	// fields and values:
	// f_text=test; f_double=2.4; f_float=2.9; f_int_shor=2; f_int_long=290;
	// f_date=1983-08-25;
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
	TableController tc = new TableController(model);
	tc.read(0);
	System.out.println("f_int_long value: " + tc.getValue("f_int_long"));
	assertEquals(true, tc.getValue("f_int_long").equals("290"));
	System.out.println("f_int_long type: " + tc.getType("f_int_long"));
	assertEquals(true, tc.getType("f_int_long") == java.sql.Types.INTEGER);
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
