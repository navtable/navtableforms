package es.icarto.gvsig.navtableforms.gui.tables.filter;

import es.icarto.gvsig.commons.gvsig2.IRow;

public interface IRowFilter {

	public boolean evaluate(IRow row);

}
