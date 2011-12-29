package es.icarto.gvsig.navtableforms.utils;

import java.text.DecimalFormat;

public class NavTableFormats {

    private DecimalFormat doubleFormatOnDisplay;
    private DecimalFormat doubleFormatOnEdit;
    private static final String FORMAT_DOUBLE_ON_DISPLAY = "###,###,###,###,###,##0.00";
    private static final String FORMAT_DOUBLE_ON_EDIT = "##################.##";

    public DecimalFormat getDoubleFormatForDisplayingInstance() {
	if(doubleFormatOnDisplay != null) {
	    return doubleFormatOnDisplay;
	}
	doubleFormatOnDisplay = new DecimalFormat(FORMAT_DOUBLE_ON_DISPLAY);
	return doubleFormatOnDisplay;
    }

    public DecimalFormat getDoubleFormatForEditingInstance() {
	if(doubleFormatOnEdit != null) {
	    return doubleFormatOnEdit;
	}
	doubleFormatOnEdit = new DecimalFormat(FORMAT_DOUBLE_ON_EDIT);
	return doubleFormatOnEdit;	
    }

}
