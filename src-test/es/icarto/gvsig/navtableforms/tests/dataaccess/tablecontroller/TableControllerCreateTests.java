package es.icarto.gvsig.navtableforms.tests.dataaccess.tablecontroller;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;

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

public class TableControllerCreateTests {

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
    public void testOperationCreateDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException, ParseException {
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");

	TableController tc = new TableController(model);
	HashMap<String, String> newValues = new HashMap<String, String>();
	String textName = "f_text";
	String textValue = "test";
	newValues.put(textName, textValue);
	String intShorName = "f_int_shor";
	String intShorValue = "1";
	newValues.put(intShorName, intShorValue);
	String intLongName = "f_int_long";
	String intLongValue = "99";
	newValues.put(intLongName, intLongValue);
	String doubleName = "f_double";
	String doubleValue = "3.9";
	newValues.put(doubleName, doubleValue);
	String floatName = "f_float";
	String floatValue = "66.12";
	newValues.put(floatName, floatValue);
	String dateName = "f_date";
	String dateValue = "08/25/2005";
	newValues.put(dateName, dateValue);
	int rowNumberBeforeAdding = tc.getRowCount();
	long lastPosition = tc.create(newValues);
	int rowNumberAfterAdding = tc.getRowCount();
	assertEquals(rowNumberAfterAdding, rowNumberBeforeAdding + 1);
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
