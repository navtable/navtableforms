package es.udc.cartolab.gvsig.navtableforms.ormlite;

public class ORMLite {

    private static XMLSAXParser fp = null;
    private static ORMLiteDataBase dbo = null;
    private static ORMLiteLayerSet fls = null;

    public static synchronized ORMLiteDataBase getDataBaseObject(String xmlFile) {
	if (fp == null) {
	    fp = new XMLSAXParser(xmlFile);
	    dbo = fp.getDBO();
	    fls = fp.getFLS();
	}
	return dbo;
    }

    public static synchronized ORMLiteLayerSet getLayerSetObject(String xmlFile) {
	if (fp == null) {
	    fp = new XMLSAXParser(xmlFile);
	    dbo = fp.getDBO();
	    fls = fp.getFLS();
	}
	return fls;
    }
}
