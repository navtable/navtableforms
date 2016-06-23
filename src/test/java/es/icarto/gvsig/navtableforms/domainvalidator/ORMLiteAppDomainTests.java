package es.icarto.gvsig.navtableforms.domainvalidator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.XMLSAXParser;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DateRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.DoublePositiveRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.MandatoryRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;

public class ORMLiteAppDomainTests {

    private ORMLite ormlite;
    String xmlFile = "data-test/test-metadata.xml";

    @Before
    public void setUp() {
	ormlite = new ORMLite(xmlFile);
    }

    @Test
    public void isFileLoaded() {
	assertTrue(ormlite.getAppDomain() != null);
    }

    @Test
    public void isFileParsed() throws ParserConfigurationException,
	    SAXException, IOException {
	XMLSAXParser p = new XMLSAXParser(xmlFile);
	assertTrue(p.getAD() != null);
    }

    @Test
    public void appDomainHasDomainValidators()
	    throws ParserConfigurationException, SAXException, IOException {
	XMLSAXParser p = new XMLSAXParser(xmlFile);
	assertTrue(5 == p.getAD().getDomainValidators().size());
    }

    @Test
    public void hasFiveValidationsDefined() {
	assertTrue(5 == ormlite.getAppDomain().getDomainValidators().size());
    }

    @Test
    public void checkNumberOfValidations() {
	assertFalse(!(5 == ormlite.getAppDomain().getDomainValidators().size()));
    }

    @Test
    public void inventedFieldHasNoDomainValidator() {
	assertTrue(ormlite.getAppDomain().getDomainValidatorForComponent("foo") == null);
    }

    @Test
    public void fieldMyCodeHasDomainValidator() {
	assertTrue(ormlite.getAppDomain().getDomainValidatorForComponent(
		"my_code") != null);
    }

    @Test
    public void fieldMyWidgetHasDependency() {
	assertTrue(ormlite.getAppDomain().getDependencyValuesForComponent(
		"my_widget") != null);
    }

    @Test
    public void checkDependencyValuesOfMyWidget() {
	Map<String, List<String>> conditions = ormlite.getAppDomain()
		.getDependencyValuesForComponent("my_widget").getConditions();
	assertTrue(conditions.keySet().size() == 1);
	String component = conditions.keySet().toArray(new String[0])[0];
	assertTrue(component.equalsIgnoreCase("other_widget"));
	assertTrue(conditions.get(component).size() == 1);
	String value = conditions.get(component).toArray(new String[0])[0];
	assertTrue(value.equalsIgnoreCase("true"));
    }

    @Test
    public void fieldMyCodeisNonEditable() {
	assertTrue(ormlite.getAppDomain().isNonEditableComponent("my_code"));
    }

    @Test
    public void fieldMyCodeHasRules() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_code").getRules() != null);
    }

    @Test
    public void fieldMyCodeHasOneRule() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_code").getRules().size() == 1);
    }

    @Test
    public void fieldMyCodeHasMandatoryRule() {
	Set<ValidationRule> rules = ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_code").getRules();
	boolean hasRule = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof MandatoryRule) {
		hasRule = true;
		break;
	    }
	}
	assertTrue(hasRule);
    }

    @Test
    public void fieldMyDoubleHasOneRule() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_double").getRules().size() == 1);
    }

    @Test
    public void fieldMyDoubleHasDoubleRule() {
	Set<ValidationRule> rules = ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_double").getRules();
	boolean hasRule = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof DoublePositiveRule) {
		hasRule = true;
		break;
	    }
	}
	assertTrue(hasRule);
    }

    @Test
    public void fieldMyDateHasOneRule() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_date").getRules().size() == 1);
    }

    @Test
    public void fieldMyDateHasDateRule() {
	Set<ValidationRule> rules = ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_date").getRules();
	boolean hasRule = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof DateRule) {
		hasRule = true;
		break;
	    }
	}
	assertTrue(hasRule);
    }

    @Test
    public void fieldMyDoubleMandatoryHasTwoRules() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_double_mandatory")
		.getRules().size() == 2);
    }

    @Test
    public void fieldMyDoubleMandatoryHasDoubleAndMandatoryRules() {
	Set<ValidationRule> rules = ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_double_mandatory")
		.getRules();

	boolean hasRuleMandatory = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof MandatoryRule) {
		hasRuleMandatory = true;
		break;
	    }
	}
	assertTrue(hasRuleMandatory);

	boolean hasRuleDouble = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof DoublePositiveRule) {
		hasRuleDouble = true;
		break;
	    }
	}
	assertTrue(hasRuleDouble);
    }

    @Test
    public void fieldMyDateMandatoryHasTwoRules() {
	assertTrue(ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_date_mandatory").getRules()
		.size() == 2);
    }

    @Test
    public void fieldMyDateMandatoryHasDoubleAndMandatoryRules() {
	Set<ValidationRule> rules = ormlite.getAppDomain()
		.getDomainValidatorForComponent("my_date_mandatory").getRules();

	boolean hasRuleMandatory = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof MandatoryRule) {
		hasRuleMandatory = true;
		break;
	    }
	}
	assertTrue(hasRuleMandatory);

	boolean hasRuleDouble = false;
	for (ValidationRule rule : rules) {
	    if (rule instanceof DateRule) {
		hasRuleDouble = true;
		break;
	    }
	}
	assertTrue(hasRuleDouble);
    }

}
