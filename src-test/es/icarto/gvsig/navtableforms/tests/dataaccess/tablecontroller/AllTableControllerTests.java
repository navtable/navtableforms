package es.icarto.gvsig.navtableforms.tests.dataaccess.tablecontroller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TableControllerReadTests.class,
    TableControllerUpdateTests.class, TableControllerCreateTests.class,
    TableControllerDeleteTests.class })
public class AllTableControllerTests {

}
