package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.utils.TOCTableManager;

public class TableModelFactory {

    public static TableModelAlphanumeric createFromTable(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    String[] columnNames,
	    String[] columnAliases)
		    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableByName(sourceTable).getModel()
		.getModelo();
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(
		fieldIndex, rowFilterValue);
	return new TableModelAlphanumeric(model, filter, columnNames, columnAliases);
    }

    public static TableModelAlphanumeric createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableByName(sourceTable).getModel()
		.getModelo();
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new TableModelAlphanumeric(model,
		new IRowMultipleOrFilterImplementer(filters), columnNames,
		columnAliases);
    }

}
