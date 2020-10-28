package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DoubleRuleTests extends DoublePositiveRule {

	ValidationRule rule;

	@Before
	public void setUp() {
		rule = new DoubleRule();
	}

	@Test
	public void signedNumbers() {
		assertTrue(rule.validate("-7,2"));
		assertFalse(rule.validate("- 7,2"));
		assertFalse(rule.validate("+7,2"));

	}

}