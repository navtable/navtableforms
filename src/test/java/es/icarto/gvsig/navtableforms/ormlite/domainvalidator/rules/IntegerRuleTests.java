package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class IntegerRuleTests {

	ValidationRule rule;

	@Before
	public void setUp() {
		rule = new IntegerRule();
	}

	@Test
	public void emptyIsInteger() {
		assertTrue(rule.validate(""));
	}

	@Test
	public void zeroIsInteger() {
		assertTrue(rule.validate("0"));
	}

	@Test
	public void fiveIsInteger() {
		assertTrue(rule.validate("5"));
	}

	@Test
	public void letterIsNotInteger() {
		assertFalse(rule.validate("f"));
	}

	@Test
	public void ramdonIsAnInteger() {
		Integer value = new Random().nextInt();
		assertTrue(rule.validate(value.toString()));
	}

	@Test
	public void minusSevenIsInteger() {
		assertTrue(rule.validate("-7"));
	}
}
