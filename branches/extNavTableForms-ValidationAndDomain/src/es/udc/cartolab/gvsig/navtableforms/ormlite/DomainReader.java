package es.udc.cartolab.gvsig.navtableforms.ormlite;

import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.cartolab.gvsig.navtableforms.validation.DomainValues;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DomainReader {

    String table;
    String column;

    public DomainReader() {
    }

    public void setTable(String name) {
	this.table = name;
    }

    public void setColumn(String name) {
	this.column = name;
    }

    public DomainValues getDomainValues() {
	if (table != null && column != null) {
	    ArrayList<String> list = new ArrayList<String>();
	    DBSession ds = DBSession.getCurrentSession();
	    try {
		String[] values = ds.getDistinctValues(table, column);
		for (int i = 0; i < values.length; i++) {
		    list.add(values[i]);
		}
	    } catch (SQLException e) {
		e.printStackTrace(System.out);
		return null;
	    } catch (NullPointerException e) {
		e.printStackTrace(System.out);
		return null;
	    }
	    return new DomainValues(list);
	}
	return null;
    }
}
