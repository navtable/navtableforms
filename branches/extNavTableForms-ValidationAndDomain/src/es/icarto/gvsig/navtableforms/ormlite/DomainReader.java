package es.icarto.gvsig.navtableforms.ormlite;

import java.sql.SQLException;
import java.util.ArrayList;

import es.icarto.gvsig.navtableforms.validation.DomainValues;
import es.icarto.gvsig.navtableforms.validation.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DomainReader {

    String table = null;
    String columnAlias = null;
    String columnValue = null;
    ArrayList<String> columnForeignKey = new ArrayList<String>();

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

    public void addColumnForeignKey(String name) {
	this.columnForeignKey.add(name);
    }

    private String[] convertArrayToString(ArrayList<String> array) {
	String[] strings = new String[array.size()];
	for (int i = 0; i < array.size(); i++) {
	    strings[i] = array.get(i);
	}
	return strings;
    }

    private String[] getFieldColumns() {
	if (columnAlias == null) {
	    columnAlias = columnValue;
	}
	if (columnForeignKey == null) {
	    columnForeignKey.add(columnValue);
	}
	ArrayList<String> cols = new ArrayList<String>();
	cols.add(columnValue);
	cols.add(columnAlias);
	for (String fk : columnForeignKey) {
	    cols.add(fk);
	}
	return convertArrayToString(cols);
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
		    for (int fkIndex = 0; fkIndex < columnForeignKey.size(); fkIndex++) {
			kv.addForeignKey(values[i][2 + fkIndex]);
		    }
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
