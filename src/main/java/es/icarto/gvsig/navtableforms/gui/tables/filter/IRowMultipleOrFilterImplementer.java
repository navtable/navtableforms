package es.icarto.gvsig.navtableforms.gui.tables.filter;

import java.util.List;

import es.icarto.gvsig.commons.gvsig2.IRow;


public class IRowMultipleOrFilterImplementer implements IRowFilter {

    private List<IRowFilter> filters;

    public IRowMultipleOrFilterImplementer(List<IRowFilter> filters) {
	this.filters = filters;
    }

    public boolean evaluate(IRow row) {
	for (IRowFilter filter : filters) {
	    if (filter.evaluate(row)) {
		return true;
	    }
	}
	return false;
    }

}
