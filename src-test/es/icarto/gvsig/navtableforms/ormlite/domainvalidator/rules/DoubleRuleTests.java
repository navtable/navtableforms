package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;


public class DoubleRuleTests {

    ValidationRule rule;

    @Before
    public void setUp() {
	rule = new DoubleRule();
    }

    @Test
    public void emptyIsDouble() {
	assertTrue(rule.validate(""));
    }

    @Test
    public void zeroIsDouble() {
	assertTrue(rule.validate("0.0"));
    }

    @Test
    public void fiveDotSevenIsDouble() {
	assertTrue(rule.validate("5.7"));
    }

    @Test
    public void letterIsNotDouble() {
	assertFalse(rule.validate("f"));
    }

    @Test
    public void ramdonIsDouble() {
	Double value = new Random().nextDouble() - 0.5;
	boolean doublePositive = value >= 0.0 ? true : false;
	assertEquals(doublePositive, rule.validate(value.toString()));
    }

    @Test
    public void minusSevenDotTwoIsDouble() {
	assertFalse(rule.validate("-7.2"));
    }
}