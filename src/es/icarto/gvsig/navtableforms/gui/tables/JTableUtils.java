package es.icarto.gvsig.navtableforms.gui.tables;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableUtils {

    public static boolean hasRows(JTable table) {
	TableModel model = table.getModel();
	if ((model.getRowCount() > 0) && (!firstRowIsVoid(model))) {
	    return true;
	}
	return false;
    }

    private static boolean firstRowIsVoid(TableModel model) {
	boolean isVoid = true;
	for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
	    if (model.getValueAt(0, colIndex) == null) {
		isVoid = true;
	    } else {
		isVoid = false;
		break;
	    }
	}
	return isVoid;
    }

}
