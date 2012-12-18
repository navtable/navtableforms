package es.icarto.gvsig.navtableforms.tests.dataaccess.tablecontroller;

import static org.junit.Assert.assertEquals;

import java.io.File;

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

public class TableControllerDeleteTests {

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
    public void testOperationDeleteDBF() throws ReadDriverException,
	    DriverLoadException, NoSuchTableException,
	    StopWriterVisitorException, StartWriterVisitorException,
	    InitializeWriterException {
	IEditableSource model = getIEditableSourceFromFile("data-test/test.dbf");
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
