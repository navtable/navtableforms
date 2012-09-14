package es.icarto.gvsig.navtableforms.dataaccess;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;

import org.cresques.cts.IProjection;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.data.NoSuchTableException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.navtableforms.dataacces.TableController;

public class TableControllerTests {

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
	IEditableSource model = getIEditableSourceFromFile("data-test/110m_land.dbf");

	TableController tc = new TableController(model);
	HashMap<String, String> newValues = new HashMap<String, String>();
	String fieldName = "FeatureCla";
	String fieldValue = "test";
	newValues.put(fieldName, fieldValue);
	int rowNumberBeforeAdding = tc.getRowCount();
	long lastPosition = tc.create(newValues);
	int rowNumberAfterAdding = tc.getRowCount();
	assertEquals(rowNumberAfterAdding, rowNumberBeforeAdding + 1);
    }

    @Test
    public void testOperationReadDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	IEditableSource model = getIEditableSourceFromFile("data-test/110m_land.dbf");

	TableController tc = new TableController(model);
	tc.read(0); // FeatureCla = "Country" (string), FIRST_Scal = 1 (integer)
	String value = tc.getValue("FeatureCla");
	boolean check = value.equals("Country");
	assertEquals(true, check);
    }

    @Test
    public void testOperationUpdateDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException {
	IEditableSource model = getIEditableSourceFromFile("data-test/110m_land.dbf");

	TableController tc = new TableController(model);
	tc.read(1); // FeatureCla = "Country" (string), FIRST_Scal = 1 (integer)
	String oldValue = tc.getValue("FeatureCla");
	tc.setValue("FeatureCla", oldValue + "Test");
	tc.update(1);
	String newValue = tc.getValue("FeatureCla");
	boolean check = newValue.equals(oldValue + "Test");
	assertEquals(true, check);
    }

    @Test
    public void testOperationDeleteDBF() throws ReadDriverException,
    DriverLoadException, NoSuchTableException,
    StopWriterVisitorException, StartWriterVisitorException,
    InitializeWriterException {
	IEditableSource model = getIEditableSourceFromFile("data-test/110m_land.dbf");

	TableController tc = new TableController(model);
	long lastPosition = tc.getRowCount();
	tc.delete(lastPosition);
	long lastPositionAfterDelete = tc.getRowCount();
	assertEquals(lastPositionAfterDelete, lastPosition - 1);
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
