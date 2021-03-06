package es.icarto.gvsig.navtableforms.gui.tables.filter;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.IRow;

import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class IRowFilterImplementer implements IRowFilter {

    private ValueFormatNT valueFormatNT;
    private int fieldIndex;
    private String fieldValue;

    public IRowFilterImplementer(int fieldIndex, String fieldValue) {
	this.fieldIndex = fieldIndex;
	this.fieldValue = fieldValue;
	this.valueFormatNT = new ValueFormatNT();
    }

    public boolean evaluate(IRow row) {
	Value value = row.getAttribute(fieldIndex);
	if (value.getStringValue(valueFormatNT).equals(fieldValue)) {
	    return true;
	}
	return false;
    }

}
