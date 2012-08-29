package es.icarto.gvsig.navtableforms.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import es.icarto.gvsig.navtableforms.validation.rules.IntegerPositiveRule;
import es.icarto.gvsig.navtableforms.validation.rules.ValidationRule;

public class IntegerPositiveTest {

    ValidationRule rule;

    @Before
    public void setUp() {
	rule = new IntegerPositiveRule();
    }

    @Test
    public void emptyIsIntegerPositive() {
	assertTrue(rule.validate(""));
    }

    @Test
    public void zeroIsIntegerPositive() {
	assertTrue(rule.validate("0"));
    }

    @Test
    public void fiveIsIntegerPositive() {
	assertTrue(rule.validate("5"));
    }

    @Test
    public void ramdonCouldBeAnIntegerPositive() {
	Integer value = new Random().nextInt();
	boolean integerPositive = value >= 0 ? true : false;
	assertEquals(integerPositive, rule.validate(value.toString()));
    }

    @Test
    public void minusSevenIsNotIntegerPositive() {
	assertFalse(rule.validate("-7"));
    }
}
