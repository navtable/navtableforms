package es.icarto.gvsig.navtableforms.gui.tables;

import com.iver.cit.gvsig.fmap.edition.IEditableSource;


public interface IForm {

    public void open(long position);

    public void delete(long position);

    public IEditableSource getSource();

    public void setModel(TableModelAlphanumeric model);

}
