/*
 * Copyright (c) 2011. iCarto
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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iver.cit.gvsig.fmap.drivers.FieldDescription;

import es.icarto.gvsig.navtableforms.ormlite.domain.DBDomainReader;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainReader;
import es.icarto.gvsig.navtableforms.ormlite.domain.FileDomainReader;
import es.icarto.gvsig.navtableforms.validation.rules.ValidationRule;

/**
 * SAX parser to build from a XML structure several objects needed for
 * validation.
 * 
 * @author Andrés Maneiro <amaneiro@icarto.es>
 * @author Jorge López <jlopez@cartolab.es>
 * 
 */
public class XMLSAXParser extends DefaultHandler {

    // TODO check where gvSIG do that and take values from there
    static final String GVSIG_DEFAULT_STRING = "";
    static final int GVSIG_DEFAULT_INT = 0;
    static final double GVSIG_DEFAULT_DOUBLE = 0.0;
    static final boolean GVSIG_DEFAULT_BOOLEAN = false;

    private String xmlFile = null;
    private ORMLiteDataBase dbo = null;
    private ORMLiteAplicationDomain ad = null;

    private ORMLiteDataBase.ORMLiteTable tmpTable = null;
    private String tmpLayerAlias = null;
    private List<String> tmpPK = null;
    private String tmpVal = null;
    private DomainReader tmpDomainReader = null;
    private FieldDescription tmpFieldDescription = null;

    private static Logger logger = Logger.getLogger("SAX Parser");

    public XMLSAXParser(String xmlFile) {
	tmpPK = new ArrayList<String>();

	setXMLFile(xmlFile);
	setDBO(new ORMLiteDataBase());
	setAD(new ORMLiteAplicationDomain());

	parseDocument();
    }

    private String getXMLFile() {
	return xmlFile;
    }

    private String getXMLFileDir() {
	return xmlFile.substring(0, xmlFile.lastIndexOf(File.separator) + 1);
    }

    private void setXMLFile(String xmlFile) {
	this.xmlFile = xmlFile;
    }

    public ORMLiteDataBase getDBO() {
	return this.dbo;
    }

    private void setDBO(ORMLiteDataBase newDBO) {
	this.dbo = newDBO;
    }

    public ORMLiteAplicationDomain getAD() {
	return ad;
    }

    public void setAD(ORMLiteAplicationDomain ad) {
	this.ad = ad;
    }

    private void parseDocument() {

	// get a factory
	SAXParserFactory spf = SAXParserFactory.newInstance();
	try {

	    // get a new instance of parser
	    SAXParser sp = spf.newSAXParser();

	    // parse the file and also register this class for call backs
	    sp.parse(getXMLFile(), this);

	} catch (SAXException se) {
	    logger.error(se.getMessage(), se);
	} catch (ParserConfigurationException pce) {
	    logger.error(pce.getMessage(), pce);
	} catch (IOException ie) {
	    logger.error(ie.getMessage(), ie);
	}
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
	if (qName.equalsIgnoreCase("LAYER")) {
	    tmpLayerAlias = attributes.getValue("alias");

	    // set table
	    tmpTable = getDBO().new ORMLiteTable();
	    tmpTable.setTableAlias(tmpLayerAlias);
	} else if (qName.equalsIgnoreCase("FIELD")) {
	    // set field
	    tmpFieldDescription = new FieldDescription();
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

	// set tmp table structure
	else if (qName.equalsIgnoreCase("TABLENAME")) {
	    tmpTable.setTableName(tmpVal);
	} else if (qName.equalsIgnoreCase("PKELEMENT")) {
	    tmpPK.add(tmpVal);
	} else if (qName.equalsIgnoreCase("PRIMARYKEY")) {
	    String[] aux = new String[tmpPK.size()];
	    for (int i = 0; i < tmpPK.size(); i++) {
		aux[i] = tmpPK.get(i);
	    }
	    tmpTable.setPrimaryKey(aux);
	    tmpPK.removeAll(tmpPK);
	}

	else if (qName.equalsIgnoreCase("DRTYPE")) {
	    if (tmpVal.equalsIgnoreCase("DB")) {
		tmpDomainReader = new DBDomainReader();
	    } else if (tmpVal.equalsIgnoreCase("FILE")) {
		tmpDomainReader = new FileDomainReader();
	    }
	}

	// set tmp domain db reader configuration
	else if (qName.equalsIgnoreCase("DRDBTABLE")) {
	    if (tmpDomainReader instanceof DBDomainReader) {
		((DBDomainReader) tmpDomainReader).setTable(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBSCHEMA")) {
	    if (tmpDomainReader instanceof DBDomainReader) {
		((DBDomainReader) tmpDomainReader).setSchema(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBCOLUMNALIAS")) {
	    if (tmpDomainReader instanceof DBDomainReader) {
		((DBDomainReader) tmpDomainReader).setColumnAlias(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBCOLUMNVALUE")) {
	    if (tmpDomainReader instanceof DBDomainReader) {
		((DBDomainReader) tmpDomainReader).setColumnValue(tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRDBFOREIGNKEY")) {
	    if (tmpDomainReader instanceof DBDomainReader) {
		((DBDomainReader) tmpDomainReader).addColumnForeignKey(tmpVal);
	    }
	}

	// set tmp domain file reader configuration
	else if (qName.equalsIgnoreCase("DRFILENAME")) {
	    if (tmpDomainReader instanceof FileDomainReader) {
		((FileDomainReader) tmpDomainReader).setFileName(this
			.getXMLFileDir()
			+ tmpVal);
	    }
	} else if (qName.equalsIgnoreCase("DRFILEFIELDALIAS")) {
	    if (tmpDomainReader instanceof FileDomainReader) {
		((FileDomainReader) tmpDomainReader).setFieldAlias(tmpVal);
	    }
	}

	// save tmp values in DBO and FLS objects
	else if (qName.equalsIgnoreCase("LAYER")) {
	    getDBO().addTable(tmpLayerAlias, tmpTable);
	}

	// save validation rule for the field
	else if (qName.equalsIgnoreCase("VALIDATIONRULE")) {
	    ValidationRule rule = getAD().createRule(tmpVal);
	    getAD().addRule(tmpFieldDescription.getFieldName(), rule);
	}

	// save tmp values of DomainReader in AplicationDomain
	else if (qName.equalsIgnoreCase("DOMAINREADER")) {
	    getAD().addDomainValues(tmpFieldDescription.getFieldName(),
		    tmpDomainReader.getDomainValues());
	}

    }

}
