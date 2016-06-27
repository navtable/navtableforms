package es.icarto.gvsig.navtableforms.gui.tables;

import es.icarto.gvsig.commons.gvsig2.IEditableSource;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;


public interface IForm {

    public void actionCreateRecord();

    public void actionUpdateRecord(long position);

    public void actionDeleteRecord(long position);

    public IEditableSource getSource();

    public void setModel(AlphanumericTableModel model);

}
