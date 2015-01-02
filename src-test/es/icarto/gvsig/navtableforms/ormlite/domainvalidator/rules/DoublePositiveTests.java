package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DoublePositiveTests {

    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void nullRaisesException() {
	// Maybe we should protect against null. This test just reminds that
	exception.expect(NullPointerException.class);
	rule.validate(null);
    }

    @Test
    public void spacesOnlyAllowedBeforeOrAfter() {
	assertTrue(rule.validate(" "));
	assertTrue(rule.validate(" 1"));
	assertTrue(rule.validate("1 "));
	assertFalse(rule.validate("2 3"));
    }

    @Test
    // Take care. Those tests, are used for spanish locale, but rules are built
    // to take into account locale.
    public void localeSensible() {
	assertFalse(rule.validate("0.0"));
    }

    @Test
    public void zeroIsDoublePositive() {
	assertTrue(rule.validate("0,0"));
    }

    @Test
    public void fiveDotSevenIsDoublePositive() {
	assertTrue(rule.validate("5,7"));
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
    public void scientificFormatIsAllowed() {
	assertTrue(rule.validate("5E-10"));
    }

    @Test
    public void lessThan1() {
	assertTrue(rule.validate("0,003"));
    }

    @Test
    public void signedNumbers() {
	assertFalse(rule.validate("-7,2"));
    }

}