package es.udc.cartolab.gvsig.navtableforms.ormlite;

import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.cartolab.gvsig.navtableforms.validation.DomainValues;
import es.udc.cartolab.gvsig.navtableforms.validation.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DomainReader {

    String table = null;
    String columnAlias = null;
    String columnValue = null;

    public DomainReader() {
    }

    public void setTable(String name) {
	this.table = name;
    }

    public void setColumnAlias(String name) {
	this.columnAlias = name;
    }

    public void setColumnValue(String name) {
	this.columnValue = name;
    }

    private String[] getFieldColumns() {
	if (columnAlias != null) {
	    String[] cols = new String[2];
	    cols[0] = columnValue;
	    cols[1] = columnAlias;
	    return cols;
	} else {
	    String[] cols = new String[1];
	    cols[0] = columnValue;
	    return cols;
	}
    }

    public DomainValues getDomainValues() {
	if (table != null && columnValue != null) {
	    ArrayList<KeyValue> list = new ArrayList<KeyValue>();
	    DBSession ds = DBSession.getCurrentSession();
	    try {
		String[][] values = ds.getTable(table, ds.getSchema(),
			getFieldColumns(), "", null, false);
		// ds.getDistinctValues(table, columns[0]);
		for (int i = 0; i < values.length; i++) {
		    KeyValue kv = new KeyValue();
		    kv.setKey(values[i][0]);
		    kv.setValue(values[i][1]);
		    list.add(kv);
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
