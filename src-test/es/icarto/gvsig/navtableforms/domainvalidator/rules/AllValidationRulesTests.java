package es.icarto.gvsig.navtableforms.domainvalidatior.rules;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DoublePositiveTests.class, IntegerPositiveTests.class,
	MandatoryTests.class })
public class AllValidationRulesTests {

}
