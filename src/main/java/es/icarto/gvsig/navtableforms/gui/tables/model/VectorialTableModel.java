package es.icarto.gvsig.navtableforms.gui.tables.model;

import org.gvsig.fmap.dal.feature.paging.FeaturePagingHelper;
import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;

@SuppressWarnings("serial")
public class VectorialTableModel extends BaseTableModel {

	private static final Logger logger = LoggerFactory
			.getLogger(VectorialTableModel.class);

	protected FLyrVect layer;

	public VectorialTableModel(FLyrVect layer, String[] colNames,
			String[] colAliases, FeaturePagingHelper set,
			I18nResourceManager i18nManager) {
		super(colNames, colAliases, set, i18nManager);
		this.layer = layer;
		initMetadata();
	}

	// @Override
	// protected long getModelRowCount() {
	// try {
	// return layer.getFeatureStore().getFeatureCount();
	// } catch (DataException e) {
	// logger.error(e.getMessage(), e);
	// return 0;
	// }
	// }

	// @Override
	// protected IRow getSourceRow(long row) {
	// try {
	//
	// return sds.getRow(row);
	// } catch (Exception e) {
	// logger.error(e.getMessage(), e);
	// return null;
	// }
	// }

	@Override
	public void reloadUnderlyingData() {
		try {
			layer.reload();
		} catch (ReloadLayerException e) {
			logger.error(e.getMessage(), e);
		}
		super.reloadUnderlyingData();
	}

}
