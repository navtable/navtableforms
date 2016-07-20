package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.commons.gvsig2.IRow;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;

@SuppressWarnings("serial")
public abstract class BaseTableModel extends AbstractTableModel {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseTableModel.class);
	protected static final long NO_ROW = -1;

	protected IRowFilter filter = null;

	protected String[] colNames;
	protected String[] colAliases;

	protected HashMap<Long, Long> rowIndexes;
	protected long currentRow = NO_ROW;

	protected int rowCount;
	protected int colCount;

	protected BaseTableModel(String[] colNames, String[] colAliases) {
		this.colNames = colNames;
		this.colAliases = colAliases;
	}

	protected BaseTableModel(String[] colNames, String[] colAliases,
			IRowFilter filter) {
		this.colNames = colNames;
		this.colAliases = colAliases;
		this.filter = filter;
	}

	protected void initMetadata() {
		rowIndexes = getRowIndexes();
		rowCount = rowIndexes.size();
		if (colNames != null) {
			colCount = colNames.length;
		} else {
			colCount = 0;
		}
		currentRow = NO_ROW;
	}

	protected HashMap<Long, Long> getRowIndexes() {
		long indexInJTable = 0;
		this.rowIndexes = new HashMap<Long, Long>();
		try {
			if (filter != null) {
				for (long indexInSource = 0, num = getModelRowCount(); indexInSource < num; indexInSource++) {
					if (filter.evaluate(getSourceRow(indexInSource))) {
						rowIndexes.put(indexInJTable, indexInSource);
						indexInJTable++;
					}
				}
			} else {
				for (long indexInSource = 0, num = getModelRowCount(); indexInSource < num; indexInSource++) {
					rowIndexes.put(indexInSource, indexInSource);
				}
			}
			return rowIndexes;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rowIndexes.clear();
			currentRow = NO_ROW;
			return rowIndexes;
		}
	}

	@Override
	public int getColumnCount() {
		return colCount;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public String getColumnName(int column) {
		return colAliases[column];
	}

	public String getColumnNameInSource(int column) {
		return colNames[column];
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public long convertRowIndexToModel(long row) {
		return rowIndexes.get(row);
	}

	public void dataChanged() {
		initMetadata();
		this.fireTableDataChanged();
	}

	public int[] getMaxLengths() {
		int[] maxLengths = new int[getColumnCount()];
		Arrays.fill(maxLengths, 0);

		for (int i = 0; i < getRowCount(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				int l = getValueAt(i, j).toString().length();
				if (l > maxLengths[j]) {
					maxLengths[j] = l;
				}
			}
		}
		return maxLengths;
	}

	protected abstract long getModelRowCount();

	protected abstract IRow getSourceRow(long rowIndex);

	@Override
	public abstract Object getValueAt(int row, int col);

	/**
	 * Forces to reload the underlying datastore. For example call reload in the
	 * FLyrVect or the IEditableSource. In embed tables, where the data can
	 * change via triggers the layers/tables loaded in gvSIG will not change if
	 * this method is not called
	 *
	 */
	public abstract void reloadUnderlyingData();

}
