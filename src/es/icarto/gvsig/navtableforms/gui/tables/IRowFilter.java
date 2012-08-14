package es.icarto.gvsig.navtableforms.gui.tables;

import com.iver.cit.gvsig.fmap.core.IRow;

public interface IRowFilter {

    public boolean evaluate(IRow row);

}
