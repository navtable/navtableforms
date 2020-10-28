package es.icarto.gvsig.navtableforms.gui.tables.filter;

import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.commons.gvsig2.IEditableSource;
import es.icarto.gvsig.commons.gvsig2.IRowEdited;
import es.icarto.gvsig.commons.gvsig2.SelectableDataSource;
import es.icarto.gvsig.commons.gvsig2.Value;

public class RowsFilter {

	private static final Logger logger = LoggerFactory.getLogger(RowsFilter.class);

	public static Object[][] getRowsFromSource(IEditableSource source, String fieldFilterName, String fieldFilterValue)
			throws DataException {

		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		int fieldFilterIndex;
		fieldFilterIndex = source.getFieldIndexByName(fieldFilterName);
		for (long index = 0, lasti = source.getRowCount(); index < lasti; index++) {
			IRowEdited row = source.getRow(index);
			String value = row.getAttribute(fieldFilterIndex).toString();
			if (value.equalsIgnoreCase(fieldFilterValue)) {
				rows.add(row.getAttributes());
			}
		}
		return rows.toArray(new Object[1][1]);
	}

	public static Object[][] getRowsFromSource(IEditableSource source, String fieldFilterName, String fieldFilterValue,
			ArrayList<String> columnNames) throws DataException {

		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		int fieldFilterIndex;
		fieldFilterIndex = source.getFieldIndexByName(fieldFilterName);
		ArrayList<Integer> columnIndexes = getIndexesOfColumns(source, columnNames);
		ArrayList<Value> attributes = new ArrayList<Value>();
		for (long index = 0, lasti = source.getRowCount(); index < lasti; index++) {
			IRowEdited row = source.getRow(index);
			String value = row.getAttribute(fieldFilterIndex).toString();
			if (value.equalsIgnoreCase(fieldFilterValue)) {
				attributes.clear();
				for (Integer idx : columnIndexes) {
					attributes.add(row.getAttribute(idx));
				}
				rows.add(attributes.toArray());
			}
		}
		return rows.toArray(new Object[0][0]);
	}

	public static Object[][] getRowsFromSource(SelectableDataSource source, String rowFilterName,
			String rowFilterValue) {

		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		int fieldIndex;
		try {
			fieldIndex = source.getFieldIndexByName(rowFilterName);
			for (long index = 0, lasti = source.getRowCount(); index < lasti; index++) {
				Value[] row = source.getRow(index).getAttributes();
				String indexValue = row[fieldIndex].toString();
				if (indexValue.equalsIgnoreCase(rowFilterValue)) {
					rows.add(row);
				}
			}
			return rows.toArray(new Object[1][1]);
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private static ArrayList<Integer> getIndexesOfColumns(IEditableSource sds, ArrayList<String> columnNames) {

		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < columnNames.size(); i++) {
			int idx;
			idx = sds.getFieldIndexByName(columnNames.get(i));
			indexes.add(new Integer(idx));
		}
		return indexes;
	}

}
