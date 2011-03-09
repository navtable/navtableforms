/*
 * Copyright (c) 2010. Cartolab (Universidade da Coru�a)
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
package es.udc.cartolab.gvsig.navtableforms.ormlite;

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

import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLiteDataBase.ORMLiteTable;

/**
 * SAX parser to build Fonsagua Database Object from a XML estructure.
 * 
 * @author Andr�s Maneiro <andres.maneiro@cartolab.es>
 *
 */
public class XMLSAXParser extends DefaultHandler{

	// TODO check where gvSIG do that and take values from there
	static final String GVSIG_DEFAULT_STRING = "";
	static final int GVSIG_DEFAULT_INT = 0;
	static final double GVSIG_DEFAULT_DOUBLE = 0.0;
	static final boolean GVSIG_DEFAULT_BOOLEAN = false;

	String xmlFile = null;
	private ORMLiteDataBase dbo = null;
	private ORMLiteLayerSet fls = null;

	// to maintain context -while parsing- about the DB
	private ORMLiteDataBase.ORMLiteTable tmpTable = null;
	private String tmpLayerAlias = null;
	private List<String> tmpPK = null;
	private String tmpVal = null;

	// to maintain context -while parsing- about the layers
	private ORMLiteLayerSet.CartoLayer tmpLayer = null;
	private FieldDescription tmpFieldDescription = null;
	private int tmpDBFIndex = 0;
	private int tmpType = -1;

	private static Logger logger = null;

	public XMLSAXParser(String xmlFile) {
		tmpPK = new ArrayList<String>();
		tmpDBFIndex = 0;

		setXMLFile(xmlFile);
		setDBO(new ORMLiteDataBase());
		setFLS(new ORMLiteLayerSet());

		parseDocument();
		// printData();
	}

	private String getXMLFile() {
		return xmlFile;
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
	 * tags, ...)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		tmpVal = new String(ch, start, length);
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
			tmpFieldDescription.setFieldLength(Integer.parseInt(tmpVal));
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
			tmpFieldDescription.setFieldDecimalCount(Integer.parseInt(tmpVal));
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

		// save tmp values in DBO and FLS objects
		else if (qName.equalsIgnoreCase("LAYER")) {
			getDBO().addTable(tmpLayerAlias, tmpTable);
			getFLS().addLayer(tmpLayerAlias, tmpLayer);
		}

	}

}




