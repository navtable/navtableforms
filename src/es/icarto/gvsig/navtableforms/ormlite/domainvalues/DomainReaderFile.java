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

package es.icarto.gvsig.navtableforms.ormlite.domainvalues;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * DomainReader which reads the values from a file.
 * The file must be in the same dir as the xml.
 * 
 * XML syntax example:
 * 
 * <DOMAINREADER>
 *	<DRTYPE>file</DRTYPE>
 *	<DRFILENAME>example1</DRFILENAME>
 *	<DRFILEFIELDALIAS>gestion</DRFILEFIELDALIAS>
 * </DOMAINREADER>
 * 
 * @author Jorge López <jlopez@cartolab.es>
 * 
 */
public class DomainReaderFile implements DomainReader {

    String fileName = null;
    String fieldAlias = null;

    public DomainReaderFile() {
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public void setFieldAlias(String fieldAlias) {
	this.fieldAlias = fieldAlias;
    }

    public DomainValues getDomainValues() {
	if (fileName != null && fieldAlias != null) {
	    ArrayList<KeyValue> list = new ArrayList<KeyValue>();
	    BufferedReader fileReader = null;
	    try {
		String line;
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
		while ((line = fileReader.readLine()) != null) {
		    String tokens[] = line.split("=");
		    if (tokens.length == 2) {
			String attrName = tokens[0].toUpperCase();
			if (fieldAlias.toUpperCase().compareTo(attrName) == 0) {
			    String values[] = tokens[1].split(";");
			    for (String value : values) {
				if (value != "") {
				    KeyValue kv = new KeyValue();
				    if (value.contains(",")) {
					kv.setValue(value.split(",")[0]);
					kv.setKey(value.split(",")[1]);
				    } else {
					kv.setKey(value);
					kv.setValue(value);
				    }
				    list.add(kv);
				}
			    }
			    break;
			}
		    }
		}
	    } catch (FileNotFoundException e) {
		e.printStackTrace(System.out);
		return null;
	    } catch (IOException e) {
		e.printStackTrace(System.out);
		return null;
	    } finally {
	    	if (fileReader != null) {
	    		try {
					fileReader.close();
				} catch (IOException e) {
				}
	    	}
	    }
	    return new DomainValues(list);
	}
	return null;
    }
}
