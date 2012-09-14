package es.icarto.gvsig.navtableforms.validation.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.icarto.gvsig.navtableforms.validation.rules.MandatoryRule;
import es.icarto.gvsig.navtableforms.validation.rules.ValidationRule;

public class MandatoryTests {

    ValidationRule rule;

    @Before
    public void setUp() {
	rule = new MandatoryRule();
    }

    @Test
    public void emptyValue() {
	assertFalse(rule.validate(""));
    }

    @Test
    public void filledValue() {
	assertTrue(rule.validate("foo"));
    }

}
