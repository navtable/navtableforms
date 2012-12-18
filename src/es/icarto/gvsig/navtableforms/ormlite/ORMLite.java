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

import org.apache.log4j.Logger;

public class ORMLite {

    private ORMLiteAppDomain appDomain = null;
    private String xmlFile;

    private Logger logger = Logger.getLogger("ORMLite logger");

    public ORMLite(String xmlFile) {
	this.xmlFile = xmlFile;
    }

    public ORMLiteAppDomain getAppDomain() {
	if (appDomain == null) {
	    try {
		XMLSAXParser fp = new XMLSAXParser(xmlFile);
		appDomain = fp.getAD();
	    } catch (Exception e) {
		logger.error(e.getMessage(), e.getCause());
		appDomain = null;
	    }
	}
	return appDomain;
    }

    public ORMLiteAppDomain reloadAppDomain() {
	XMLSAXParser fp;
	try {
	    fp = new XMLSAXParser(xmlFile);
	    appDomain = fp.getAD();
	} catch (Exception e) {
	    logger.error(e.getMessage(), e.getCause());
	    appDomain = null;
	}
	return appDomain;
    }

}
