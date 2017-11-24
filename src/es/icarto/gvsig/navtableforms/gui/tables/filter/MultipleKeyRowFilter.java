package es.icarto.gvsig.navtableforms.gui.tables.filter;

import java.util.Arrays;
import java.util.List;

import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.commons.datasources.FieldNames;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

public class MultipleKeyRowFilter implements IRowFilter {

    private final ValueFormatNT valueFormatNT = new ValueFormatNT();

    private final List<Integer> indexes;
    private final String[] originKeyValues;

    public MultipleKeyRowFilter(FLyrVect layer, String[] destinationKey,
	    String[] originKeyValues) {
	indexes = FieldNames.getIndexesOfColumns(layer,
		Arrays.asList(destinationKey));
	this.originKeyValues = originKeyValues;
    }

    @Override
    public boolean evaluate(IRow row) {
	Value[] values = row.getAttributes();
	boolean flag = true;
	for (int i = 0; i < indexes.size(); i++) {
	    Value value = values[indexes.get(i)];
	    String key = originKeyValues[i];
	    flag = flag && (value.getStringValue(valueFormatNT).equals(key));
	}
	return flag;
    }

}
