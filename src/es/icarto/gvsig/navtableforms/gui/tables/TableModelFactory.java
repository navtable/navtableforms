package es.icarto.gvsig.navtableforms.gui.tables;

import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.utils.TOCTableManager;

public class TableModelFactory {

    public static TableModelAlphanumeric createFromTable(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    List<String> columnNames,
	    List<String> columnAliases)
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

}
