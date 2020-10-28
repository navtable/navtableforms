package es.icarto.gvsig.navtableforms.gui.images;

public interface ImageHandler {

	void setListeners();

	String getName();

	void removeListeners();

	void fillValues();

	void fillEmptyValues();

}
