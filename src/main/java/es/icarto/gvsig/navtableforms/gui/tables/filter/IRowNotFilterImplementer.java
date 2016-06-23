package es.icarto.gvsig.navtableforms.gui.tables.filter;

import com.iver.cit.gvsig.fmap.core.IRow;

public class IRowNotFilterImplementer implements IRowFilter {

    private IRowFilter filter;

    public IRowNotFilterImplementer(IRowFilter filter) {
	this.filter = filter;
    }

    public boolean evaluate(IRow row) {
	return !filter.evaluate(row);
    }

}
