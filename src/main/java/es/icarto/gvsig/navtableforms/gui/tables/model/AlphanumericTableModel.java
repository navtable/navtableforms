package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.fmap.dal.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.commons.gvsig2.IEditableSource;
import es.icarto.gvsig.commons.gvsig2.IRow;
import es.icarto.gvsig.commons.gvsig2.SelectableDataSource;
import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public class AlphanumericTableModel extends BaseTableModel {

	private static final Logger logger = LoggerFactory
			.getLogger(AlphanumericTableModel.class);

	private final TableController tableController;
	private I18nResourceManager i18nManager;
	private final TableDocument tableDocument;

	public AlphanumericTableModel(TableDocument tableDocument,
			String[] colNames, String[] colAliases,
			I18nResourceManager i18nManager, IRowFilter filter) {
		super(colNames, colAliases, filter);
		this.i18nManager = i18nManager;
		this.tableDocument = tableDocument;
		this.tableController = new TableController(tableDocument);
		initMetadata();
	}

	public AlphanumericTableModel(TableDocument tableDocument,
			String[] colNames, String[] colAliases,
			I18nResourceManager i18nManager) {
		super(colNames, colAliases);
		this.i18nManager = i18nManager;
		this.tableDocument = tableDocument;
		this.tableController = new TableController(tableDocument);
		initMetadata();
	}

	@Override
	public Object getValueAt(int row, int col) {
		try {
			if (currentRow != row) {
				currentRow = row;
				Long position = rowIndexes.get(currentRow);
				tableController.read(position);
			}
			String value = tableController.getValue(getColumnNameInSource(col));
			int type = tableController.getType(getColumnNameInSource(col));
			if ((type == java.sql.Types.BOOLEAN)
					|| (type == java.sql.Types.BIT)) {
				// If there is no translation, we show the value itself.
				value = (value.length() == 0) ? i18nManager.getString(
						"table_null_value", value) : i18nManager.getString(
						"table_" + value + "_value", value);
			}
			return value;
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public IEditableSource getSource() {
		try {
			return new SelectableDataSource(tableDocument.getStore());
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void create(HashMap<String, String> values) throws Exception {
		long pos = tableController.create(values);
		if (pos != TableController.NO_ROW) {
			initMetadata();
			this.fireTableDataChanged();
		}
	}

	public Map<String, String> read(long row) throws DataException {
		if (currentRow != row) {
			currentRow = row;
			tableController.read(rowIndexes.get(row));
		}
		return tableController.getValuesOriginal();
	}

	public void updateValue(String fieldName, String fieldValue) {
		tableController.setValue(fieldName, fieldValue);
	}

	public void update(long row) throws DataException {
		tableController.update(rowIndexes.get(row));
		this.fireTableDataChanged();
	}

	public void delete(long row) {
		tableController.delete(rowIndexes.get(row));
		this.fireTableDataChanged();
		initMetadata();
	}

	public TableController getController() {
		return tableController;
	}

	@Override
	protected long getModelRowCount() {
		try {
			return tableDocument.getStore().getFeatureCount();
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}

	@Override
	protected IRow getSourceRow(long row) {
		try {
			return new SelectableDataSource(tableDocument.getStore())
					.getRow(row);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void reloadUnderlyingData() {
		try {
			tableDocument.getStore().refresh();
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
