package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DoublePositiveRule;

public class DoublePositiveTests {

    DoublePositiveRule rule;

    @Before
    public void setUp() {
	rule = new DoublePositiveRule();
    }

    @Test
    public void emptyIsDoublePositive() {
	assertTrue(rule.validate(""));
    }

    @Test
    public void zeroIsDoublePositive() {
	assertTrue(rule.validate("0.0"));
    }

    @Test
    public void fiveDotSevenIsDoublePositive() {
	assertTrue(rule.validate("5.7"));
    }

    @Test
    public void letterIsNotDoublePositive() {
	assertFalse(rule.validate("f"));
    }

    @Test
    public void ramdonCouldBeAnDoublePositive() {
	Double value = new Random().nextDouble() - 0.5;
	boolean doublePositive = value >= 0.0 ? true : false;
	assertEquals(doublePositive, rule.validate(value.toString()));
    }

    @Test
    public void minusSevenDotTwoIsNotDoublePositive() {
	assertFalse(rule.validate("-7.2"));
    }
}