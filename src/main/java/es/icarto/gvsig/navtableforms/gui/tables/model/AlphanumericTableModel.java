package es.icarto.gvsig.navtableforms.gui.tables.model;

import org.gvsig.app.project.documents.table.TableDocument;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.dataacces.TableController;

@SuppressWarnings("serial")
public class AlphanumericTableModel extends BaseTableModel {

	private static final Logger logger = LoggerFactory.getLogger(AlphanumericTableModel.class);

	private final TableDocument tableDocument;

	public AlphanumericTableModel(TableDocument tableDocument, String[] colNames, String[] colAliases,
			I18nResourceManager i18nManager, FeaturePagingHelper set) {
		super(colNames, colAliases, set, i18nManager);
		this.tableDocument = tableDocument;
		this.iController = new TableController(tableDocument);
		initMetadata();
	}

	// public IEditableSource getSource() {
	// try {
	// return new SelectableDataSource(tableDocument.getStore());
	// } catch (DataException e) {
	// logger.error(e.getMessage(), e);
	// }
	// return null;
	// }

	// public void create(HashMap<String, String> values) throws Exception {
	// long pos = iController.create(values);
	// if (pos != TableController.NO_ROW) {
	// initMetadata();
	// this.fireTableDataChanged();
	// }
	// }

	public IController getController() {
		return iController;
	}

	// @Override
	// protected long getModelRowCount() {
	// try {
	// return tableDocument.getStore().getFeatureCount();
	// } catch (DataException e) {
	// logger.error(e.getMessage(), e);
	// return 0;
	// }
	// }

	// @Override
	// protected IRow getSourceRow(long row) {
	// try {
	// return new SelectableDataSource(tableDocument.getStore())
	// .getRow(row);
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// return null;
	// }
	// }

	@Override
	public void reloadUnderlyingData() {
		try {
			tableDocument.getStore().refresh();
		} catch (DataException e) {
			logger.error(e.getMessage(), e);
		}
		super.reloadUnderlyingData();
	}

}
