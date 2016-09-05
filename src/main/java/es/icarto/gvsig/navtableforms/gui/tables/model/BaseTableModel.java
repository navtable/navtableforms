package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.Arrays;

import javax.swing.table.AbstractTableModel;

import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;

@SuppressWarnings("serial")
public abstract class BaseTableModel extends AbstractTableModel {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseTableModel.class);

	private final I18nResourceManager i18nManager;
	protected final String[] colNames;
	protected final String[] colAliases;
	private final FeaturePagingHelper set;

	// protected HashMap<Long, Long> rowIndexes;

	// protected int rowCount;
	protected int colCount;

	protected IController iController;

	protected BaseTableModel(String[] colNames, String[] colAliases,
			FeaturePagingHelper set, I18nResourceManager i18nManager) {
		this.set = set;
		this.colNames = colNames;
		this.colAliases = colAliases;
		this.i18nManager = i18nManager;
	}

	protected void initMetadata() {
		// rowIndexes = getRowIndexes();
		// rowCount = rowIndexes.size();
		if (colNames != null) {
			colCount = colNames.length;
		} else {
			colCount = 0;
		}
	}

	// protected HashMap<Long, Long> getRowIndexes() {
	// long indexInJTable = 0;
	// this.rowIndexes = new HashMap<Long, Long>();
	// try {
	// if (filter != null) {
	// for (long indexInSource = 0, num = getModelRowCount(); indexInSource <
	// num; indexInSource++) {
	// if (filter.evaluate(getSourceRow(indexInSource))) {
	// rowIndexes.put(indexInJTable, indexInSource);
	// indexInJTable++;
	// }
	// }
	// } else {
	// for (long indexInSource = 0, num = getModelRowCount(); indexInSource <
	// num; indexInSource++) {
	// rowIndexes.put(indexInSource, indexInSource);
	// }
	// }
	// return rowIndexes;
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// rowIndexes.clear();
	// return rowIndexes;
	// }
	// }

	@Override
	public Object getValueAt(int row, int col) {
		// Long position = rowIndexes.get(row);
		long position = row;
		try {
			Feature feat = set.getFeatureAt(position);
			iController.read(feat);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		String value = iController.getValue(getColumnNameInSource(col));
		int type = iController.getType(getColumnNameInSource(col));
		if ((type == java.sql.Types.BOOLEAN) || (type == java.sql.Types.BIT)) {
			// If there is no translation, we show the value itself.
			value = (value.length() == 0) ? i18nManager.getString(
					"table_null_value", value) : i18nManager.getString("table_"
							+ value + "_value", value);
		}
		return value;
	}

	public Feature getFeatureAt(long position) {
		try {
			return set.getFeatureAt(position);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return colCount;
	}

	@Override
	public int getRowCount() {
		return (int) set.getTotalSize();
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

	// public long convertRowIndexToModel(long row) {
	// return rowIndexes.get(row);
	// }

	public void dataChanged() {
		initMetadata();
		try {
			set.reload();
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
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

	// protected abstract long getModelRowCount();

	// protected abstract IRow getSourceRow(long rowIndex);

	/**
	 * Forces to reload the underlying datastore. For example call reload in the
	 * FLyrVect or the IEditableSource. In embed tables, where the data can
	 * change via triggers the layers/tables loaded in gvSIG will not change if
	 * this method is not called
	 *
	 */
	public void reloadUnderlyingData() {
		initMetadata();
		try {
			set.reload();
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
