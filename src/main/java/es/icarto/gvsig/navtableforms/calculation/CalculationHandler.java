package es.icarto.gvsig.navtableforms.calculation;

import java.util.ArrayList;
import java.util.List;

public class CalculationHandler {

    List<Calculation> list = new ArrayList<Calculation>();

    public void add(Calculation calculation) {
	list.add(calculation);
    }

    public void setListeners() {
	for (Calculation c : list) {
	    c.setListeners();
	}
    }

    public void removeListeners() {
	for (Calculation c : list) {
	    c.removeListeners();
	}
    }

}
