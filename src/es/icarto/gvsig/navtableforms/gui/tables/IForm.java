package es.icarto.gvsig.navtableforms.gui.tables;

import com.iver.cit.gvsig.fmap.edition.IEditableSource;


public interface IForm {

    public void createRecord(long position);

    public void updateRecord(long position);

    public void deleteRecord(long position);

    public IEditableSource getSource();

    public void setModel(TableModelAlphanumeric model);

}
