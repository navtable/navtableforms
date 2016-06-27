package es.icarto.gvsig.navtableforms.gui.tables.model;


import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.icarto.gvsig.commons.gvsig2.IRow;
import es.icarto.gvsig.commons.gvsig2.SelectableDataSource;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;

@SuppressWarnings("serial")
public class VectorialTableModel extends BaseTableModel {

   
private static final Logger logger = LoggerFactory
		.getLogger(VectorialTableModel.class);
    
    protected FLyrVect layer;
	private SelectableDataSource sds;

    public VectorialTableModel(FLyrVect layer, String[] colNames,
	    String[] colAliases) {
	super(colNames, colAliases);
	this.layer = layer;
	try {
		sds = new SelectableDataSource(layer.getFeatureStore());
	} catch (DataException e) {
		logger.error(e.getMessage(), e);
	}
	initMetadata();
    }

    public VectorialTableModel(FLyrVect layer, String[] colNames,
	    String[] colAliases, IRowFilter filter) {
	super(colNames, colAliases, filter);
	this.layer = layer;
	initMetadata();
    }

    @Override
    public Object getValueAt(int row, int col) {
	    return sds.getFieldValue(rowIndexes.get(row),
		    sds.getFieldIndexByName(colNames[col]));
    }

    @Override
    protected long getModelRowCount() {
	try {
		return layer.getFeatureStore().getFeatureCount();
	} catch (DataException e) {
		logger.error(e.getMessage(), e);
	    return 0;
	}
    }

    @Override
    protected IRow getSourceRow(long row) {
	try {
		
	    return sds.getRow(row);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
	    return null;
	}
    }

    @Override
    public void reloadUnderlyingData() {
	try {
	    layer.reload();
	    initMetadata();
	    sds = new SelectableDataSource(layer.getFeatureStore());
	} catch (ReloadLayerException e) {
		logger.error(e.getMessage(), e);
	} catch (DataException e) {
		logger.error(e.getMessage(), e);
	}
    }

}
