/*
 * Copyright (c) 2011, 2013. iCarto
 *
 * This file is part of extNavTableForms
 *
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package es.icarto.gvsig.navtableforms.ormlite;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iver.cit.gvsig.fmap.drivers.FieldDescription;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.DomainRulesFactory;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules.ValidationRule;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainReader;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainReaderDB;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainReaderFile;
import es.icarto.gvsig.navtableforms.ormlite.widgetsdependency.DependencyReader;

/**
 * SAX parser to build from a XML structure several objects needed for
 * validation.
 * 
 * @author Andrés Maneiro <amaneiro@icarto.es>
 * @author Jorge López <jlopez@cartolab.es>
 * @author Pablo Sanxiao <psanxiao@icarto.es>
 * @author Franciscoo Puga <fpuga@icarto.es>
 * 
 */
public class XMLSAXParser extends DefaultHandler {

    // TODO check where gvSIG do that and take values from there
    static final String GVSIG_DEFAULT_STRING = "";
    static final int GVSIG_DEFAULT_INT = 0;
    static final double GVSIG_DEFAULT_DOUBLE = 0.0;
    static final boolean GVSIG_DEFAULT_BOOLEAN = false;

    private String xmlFile = null;
    private ORMLiteAppDomain ad = null;

    private String tmpVal = null;
    private DomainReader tmpDomainReader = null;
    private FieldDescription tmpFieldDescription = null;
    private DependencyReader tmpDependencyReader = null;
    private String latestDpnComponent, latestDpnValue;

    private static Logger logger = Logger.getLogger("SAX Parser");

    public XMLSAXParser(String xmlFile) throws ParserConfigurationException,
	    SAXException, IOException {
	setXMLFile(xmlFile);
	setAD(new ORMLiteAppDomain());

	parseDocument();
    }

    private String getXMLFile() {
	return xmlFile;
    }

    private String getXMLFileDir() {
    	return new File(xmlFile).getParent() + File.separator;
    }

    private void setXMLFile(String xmlFile) {
	this.xmlFile = xmlFile;
    }

    public ORMLiteAppDomain getAD() {
	return ad;
    }

    public void setAD(ORMLiteAppDomain ad) {
	this.ad = ad;
    }

    private void parseDocument() throws ParserConfigurationException,
	    SAXException, IOException {

	// get a factory
	SAXParserFactory spf = SAXParserFactory.newInstance();

	// get a new instance of parser
	SAXParser sp = spf.newSAXParser();

	// parse the file and also register this class for call backs
	sp.parse(getXMLFile(), this);

    }

    // Event Handlers
    /**
     * Callback called every time SAX parser gets a new tag. ie: " < field > "
     */
    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	// reset
	tmpVal = "";
	if (qName.equalsIgnoreCase("FIELD")) {
	    // set field
	    tmpFieldDescription = new FieldDescription();
	}

	else if (qName.equalsIgnoreCase("ENABLEIF")) {
	    tmpDependencyReader = new DependencyReader();
	}

	else if (qName.equalsIgnoreCase("CONDITION")) {
	    latestDpnComponent = null;
	    latestDpnValue = null;
	}
    }

    /**
     * Callback called every time SAX parser gets text (spaces, text between
     * tags, ...).
     * 
     * SAX parsers may return all contiguous character data in a single chunk,
     * or they may split it into several chunks. See:
     * http://download.oracle.com/javase/1.5.0/docs/api/org/xml/sax/
     * ContentHandler.html#characters%28char%5b%5d,%20int,%20int%29
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	tmpVal = tmpVal + new String(ch, start, length);
    }

    /**
     * Callback called every time SAX parser gets a end tag like "< / field >"
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {

	// set tmp field structure
	if (qName.equalsIgnoreCase("FIELDNAME")) {
	    tmpFieldDescription.setFieldName(tmpVal);
	}

	else if (qName.equalsIgnoreCase("DRTYPE")) {
	    if (tmpVal.equalsIgnoreCase("DB")) {
		tmpDomainReader = new DomainReaderDB();
	    } else if (tmpVal.equalsIgnoreCase("FILE")) {
		tmpDomainReader = new DomainReaderFile();
	    }
	}

	// set tmp domain db reader configuration
	else if (qName.equalsIgnoreCase("DRDBTABLE")) {
	    if (tmpDomainReader instanceof DomainReaderDB) {
		((DomainReaderDB) tmpDomainReader).setTable(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBSCHEMA")) {
	    if (tmpDomainReader instanceof DomainReaderDB) {
		((DomainReaderDB) tmpDomainReader).setSchema(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBCOLUMNALIAS")) {
	    if (tmpDomainReader instanceof DomainReaderDB) {
		((DomainReaderDB) tmpDomainReader).setColumnAlias(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBCOLUMNVALUE")) {
	    if (tmpDomainReader instanceof DomainReaderDB) {
		((DomainReaderDB) tmpDomainReader).setColumnValue(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBFOREIGNKEY")) {
	    if (tmpDomainReader instanceof DomainReaderDB) {
		((DomainReaderDB) tmpDomainReader).addColumnForeignKey(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRADDVOIDVALUE")) {
		tmpDomainReader.setAddVoidValue(Boolean.parseBoolean(tmpVal));
	}

	// set tmp domain file reader configuration
	else if (qName.equalsIgnoreCase("DRFILENAME")) {
	    if (tmpDomainReader instanceof DomainReaderFile) {
		((DomainReaderFile) tmpDomainReader).setFileName(this
			.getXMLFileDir() + tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRFILEFIELDALIAS")) {
	    if (tmpDomainReader instanceof DomainReaderFile) {
		((DomainReaderFile) tmpDomainReader).setFieldAlias(tmpVal);
	    }
	}

	// save validation rule for the field
	else if (qName.equalsIgnoreCase("VALIDATIONRULE")) {
	    ValidationRule rule = DomainRulesFactory.createRule(tmpVal);
	    if (rule != null) {
		if (getAD().getDomainValidatorForComponent(
			tmpFieldDescription.getFieldName()) == null) {
		    getAD().addDomainValidator(
			    tmpFieldDescription.getFieldName(),
			    new ValidatorDomain(null));
		}
		getAD().getDomainValidatorForComponent(
			tmpFieldDescription.getFieldName()).addRule(rule);
	    }
	}

	// save tmp values of DomainReader in AplicationDomain
	else if (qName.equalsIgnoreCase("DOMAINREADER")) {
	    getAD().addDomainValues(tmpFieldDescription.getFieldName(),
		    tmpDomainReader.getDomainValues());
	}

	// save tmp values of widgets dependency
	else if (qName.equalsIgnoreCase("COMPONENT")) {
	    latestDpnComponent = tmpVal;
	}

	else if (qName.equalsIgnoreCase("VALUE")) {
	    latestDpnValue = tmpVal;
	}

	else if (qName.equalsIgnoreCase("CONDITION")) {
	    if ((latestDpnValue != null) && (latestDpnComponent != null)) {
		tmpDependencyReader.addCondition(latestDpnComponent,
			latestDpnValue);
	    }
	}

	// save tmp values of DependencyReader in ApplicationDomain
	else if (qName.equalsIgnoreCase("ENABLEIF")) {
	    getAD().addDependencyValues(tmpFieldDescription.getFieldName(),
		    tmpDependencyReader);
	}

	else if (qName.equalsIgnoreCase("NONEDITABLE")) {
	    getAD().addNonEditableComponent(tmpFieldDescription.getFieldName(),
		    true);
	}
    }

}
