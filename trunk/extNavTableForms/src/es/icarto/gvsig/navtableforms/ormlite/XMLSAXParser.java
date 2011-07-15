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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;

import es.icarto.gvsig.navtableforms.ormlite.ORMLiteDataBase.ORMLiteTable;
import es.icarto.gvsig.navtableforms.validation.ValidationRule;

/**
 * SAX parser to build from a XML estructure several objects needed for
 * validation.
 * 
 * @author Andr�s Maneiro <andres.maneiro@cartolab.es>
 * @author Jorge López <jlopez@cartolab.es>
 * 
 */
public class XMLSAXParser extends DefaultHandler {

    // TODO check where gvSIG do that and take values from there
    static final String GVSIG_DEFAULT_STRING = "";
    static final int GVSIG_DEFAULT_INT = 0;
    static final double GVSIG_DEFAULT_DOUBLE = 0.0;
    static final boolean GVSIG_DEFAULT_BOOLEAN = false;

    String xmlFile = null;
    private ORMLiteDataBase dbo = null;
    private ORMLiteLayerSet fls = null;
    private ORMLiteAplicationDomain ad = null;

    // to maintain context -while parsing- about the DB
    private ORMLiteDataBase.ORMLiteTable tmpTable = null;
    private String tmpLayerAlias = null;
    private List<String> tmpPK = null;
    private String tmpVal = null;
    private DomainReader tmpDomainReader = null;

    // to maintain context -while parsing- about the layers
    private ORMLiteLayerSet.CartoLayer tmpLayer = null;
    private FieldDescription tmpFieldDescription = null;
    private int tmpDBFIndex = 0;
    private int tmpType = -1;

    private static Logger logger = Logger.getLogger("SAX Parser");

    public XMLSAXParser(String xmlFile) {
	tmpPK = new ArrayList<String>();
	tmpDBFIndex = 0;

	setXMLFile(xmlFile);
	setDBO(new ORMLiteDataBase());
	setFLS(new ORMLiteLayerSet());
	setAD(new ORMLiteAplicationDomain());

	parseDocument();
	// printData();
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

    public ORMLiteLayerSet getFLS() {
	return fls;
    }

    public void setFLS(ORMLiteLayerSet fls) {
	this.fls = fls;
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

    /**
     * Iterate through the list and print the contents
     */
    private void printData() {

	System.out.println("No of Tables '" + getDBO().getTableList().size()
		+ "'.");

	for (ORMLiteTable ft : getDBO().getTableList().values()) {
	    System.out.println("Table " + ft.getTableName() + " with CODE "
		    + ft.getTableAlias() + " has PK ");

	    String[] aux = ft.getPrimaryKey();
	    System.out.print("Table " + ft.getTableName() + " con PK ");
	    for (String pkElement : aux) {
		System.out.print(pkElement + " ");
	    }
	    System.out.print("\n");
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
	    tmpDBFIndex = 0;

	    // set layer
	    tmpLayer = getFLS().new CartoLayer();
	    tmpLayer.setLayerAlias(tmpLayerAlias);

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
	} else if (qName.equalsIgnoreCase("FIELDTYPE")) {
	    tmpType = FieldDescription.stringToType(tmpVal);
	    tmpFieldDescription.setFieldType(tmpType);
	} else if (qName.equalsIgnoreCase("FIELDALIAS")) {
	    tmpFieldDescription.setFieldAlias(tmpVal);
	} else if (qName.equalsIgnoreCase("FIELDLENGTH")) {
	    tmpFieldDescription.setFieldLength(getAsInteger(tmpVal));
	} else if (qName.equalsIgnoreCase("DEFAULTVALUE")) {
	    switch (tmpType) {
	    case Types.VARCHAR:
		tmpFieldDescription.setDefaultValue(ValueFactory
			.createValue(tmpVal));
		break;
	    case Types.INTEGER:
		if (tmpVal == "") {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(GVSIG_DEFAULT_INT));
		} else {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(Integer.parseInt(tmpVal)));
		}
		break;
	    case Types.DOUBLE:
		if (tmpVal == "") {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(GVSIG_DEFAULT_DOUBLE));
		} else {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(Double.parseDouble(tmpVal)));
		}
		break;
	    case Types.BOOLEAN:
		if (tmpVal == "") {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(GVSIG_DEFAULT_BOOLEAN));
		} else {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(Boolean.parseBoolean(tmpVal)));
		}
		break;
	    case Types.DATE:
		if (tmpVal == "") {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(GVSIG_DEFAULT_STRING));
		} else {
		    tmpFieldDescription.setDefaultValue(ValueFactory
			    .createValue(Date.parse(tmpVal)));
		}
		break;
	    default:
		tmpFieldDescription.setDefaultValue(ValueFactory
			.createNullValue());
	    }
	} else if (qName.equalsIgnoreCase("FIELDDECIMALCOUNT")) {
	    tmpFieldDescription.setFieldDecimalCount(getAsInteger(tmpVal));
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

	// set tmp layer structure
	else if (qName.equalsIgnoreCase("NAMEOFLAYER")) {
	    tmpLayer.setLayerName(tmpVal);
	} else if (qName.equalsIgnoreCase("GEOMETRY")) {
	    tmpLayer.setLayerGeometryFromString(tmpVal);
	} else if (qName.equalsIgnoreCase("FIELD")) {
	    tmpLayer.addField(tmpDBFIndex++, tmpFieldDescription);
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
	    getFLS().addLayer(tmpLayerAlias, tmpLayer);
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

    private int getAsInteger(String tmpVal) {
	if (tmpVal != "") {
	    return Integer.parseInt(tmpVal);
	} else {
	    return 0;
	}
    }

}
