package es.icarto.gvsig.navtableforms.gui.tables;

import org.gvsig.fmap.dal.feature.Feature;

import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;

public interface IForm {

	public void actionCreateRecord();

	public void actionUpdateRecord(Feature feat);

	public void actionDeleteRecord(Feature feat);

	public void setModel(AlphanumericTableModel model);

}
