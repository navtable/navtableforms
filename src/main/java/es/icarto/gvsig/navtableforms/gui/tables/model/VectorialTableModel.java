package es.icarto.gvsig.navtableforms.gui.tables.model;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;

@SuppressWarnings("serial")
public class VectorialTableModel extends BaseTableModel {

    private static final Logger logger = Logger
	    .getLogger(VectorialTableModel.class);
    
    protected FLyrVect layer;

    public VectorialTableModel(FLyrVect layer, String[] colNames,
	    String[] colAliases) {
	super(colNames, colAliases);
	this.layer = layer;
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
	try {
	    return layer.getRecordset().getFieldValue(rowIndexes.get(row),
		    layer.getRecordset().getFieldIndexByName(colNames[col]));
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    protected int getModelRowCount() {
	try {
	    return (int) layer.getRecordset().getRowCount();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    @Override
    protected IRow getSourceRow(int row) {
	try {
	    return layer.getSource().getFeature(row);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public void reloadUnderlyingData() {
	try {
	    layer.reload();
	} catch (ReloadLayerException e) {
	    logger.error(e.getStackTrace(), e);
	}
    }

}
