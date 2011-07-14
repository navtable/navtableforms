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

public class ORMLite {

    private static XMLSAXParser fp = null;
    private static ORMLiteDataBase dbo = null;
    private static ORMLiteLayerSet fls = null;
    private static ORMLiteAplicationDomain ad = null;

    public static synchronized ORMLiteDataBase getDataBaseObject(String xmlFile) {
	if (fp == null) {
	    fp = new XMLSAXParser(xmlFile);
	    dbo = fp.getDBO();
	    fls = fp.getFLS();
	    ad = fp.getAD();
	}
	return dbo;
    }

    public static synchronized ORMLiteAplicationDomain getAplicationDomainObject(
	    String xmlFile) {
	if (fp == null) {
	    fp = new XMLSAXParser(xmlFile);
	    dbo = fp.getDBO();
	    fls = fp.getFLS();
	    ad = fp.getAD();
	}
	return ad;
    }

}
