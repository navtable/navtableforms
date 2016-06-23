package es.icarto.gvsig.navtableforms.gui.tables;

import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;


public interface IForm {

    public void actionCreateRecord();

    public void actionUpdateRecord(long position);

    public void actionDeleteRecord(long position);

    public IEditableSource getSource();

    public void setModel(AlphanumericTableModel model);

}
