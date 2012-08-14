package es.icarto.gvsig.navtableforms.gui.tables;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.IRow;

import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class IRowFilterImplementer implements IRowFilter {

    private int fieldIndex;
    private String fieldValue;

    public IRowFilterImplementer(int fieldIndex, String fieldValue) {
	this.fieldIndex = fieldIndex;
	this.fieldValue = fieldValue;
    }

    public boolean evaluate(IRow row) {
	Value value = row.getAttribute(fieldIndex);
	if (value.getStringValue(new ValueFormatNT()).equals(fieldValue)) {
	    return true;
	}
	return false;
    }

}
