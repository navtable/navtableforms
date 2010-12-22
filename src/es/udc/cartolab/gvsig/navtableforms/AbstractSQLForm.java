package es.udc.cartolab.gvsig.navtableforms;
/*
 * Copyright (c) 2010. CartoLab (Universidade da Coruña)
 * 
 * This file is part of extArqueoPonte
 * 
 * extArqueoPonte is free software: you can redistribute it and/or
 * modify it under the terms
 * of the GNU General Public License as published by the Free Software
 * Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * extArqueoPonte is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with extArqueoPonte.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.navtableforms.dao.DAOGeneric;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

public abstract class AbstractSQLForm extends AbstractForm {

    protected DAOGeneric dao;
    protected LinkedHashMap<String, String> primarykey;
    protected String aliasInXML;
    protected boolean useSQLite;

    public AbstractSQLForm(FLyrVect layer) {
	super(layer);

	aliasInXML = getAliasInXML();
	dao = getDAO();
    }

    protected abstract String getAliasInXML();

    protected abstract DAOGeneric getDAO();

    protected abstract String getXmlFileName();

    @Override
    protected void fillJTextField(JTextField field) {
	String colName = getNameBeforeDots(field.getName());
	String fieldValue = null;
	try {
	    fieldValue = dao.getDBForSQLiteValue(layer, aliasInXML,
		    currentPosition, colName, primarykey, useSQLite);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} catch (ClassNotFoundException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} finally {
	    field.setText(fieldValue);
	}
    }

    @Override
    protected void fillJCheckBox(JCheckBox checkBox) {
	String colName = getNameBeforeDots(checkBox.getName());
	String fieldValue = null;
	try {
	    fieldValue = dao.getDBForSQLiteValue(layer, aliasInXML,
		    currentPosition, colName, primarykey, useSQLite);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} catch (ClassNotFoundException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} finally {
	    checkBox.setSelected(Boolean.parseBoolean(fieldValue));
	}
    }

    @Override
    protected void fillJTextArea(JTextArea textArea) {
	String colName = getNameBeforeDots(textArea.getName());
	String fieldValue = null;
	try {
	    fieldValue = dao.getDBForSQLiteValue(layer, aliasInXML,
		    currentPosition, colName, primarykey, useSQLite);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} catch (ClassNotFoundException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} finally {
	    textArea.setText(fieldValue);
	}
    }

    @Override
    protected void fillJComboBox(JComboBox combobox) {
	String colName = getNameBeforeDots(combobox.getName());
	String fieldValue = null;
	try {
	    fieldValue = dao.getDBForSQLiteValue(layer, aliasInXML,
		    currentPosition, colName, primarykey, useSQLite);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} catch (ClassNotFoundException e) {
	    logger.error(e.getMessage(), e);
	    fieldValue = null;
	} finally {
	    if (combobox.getItemCount() > 0) {
		combobox.setSelectedIndex(0);
	    }

	    if (fieldValue != null) { // typically it's due to layer has a bad
				      // attribute name
		for (int j = 0; j < combobox.getItemCount(); j++) {
		    if (combobox.getItemAt(j).toString()
			    .compareTo(fieldValue.trim()) == 0) {
			combobox.setSelectedIndex(j);
			break;
		    }
		}
	    }
	}
    }

    @Override
    public void fillValues() {
	setPrimaryKey();
	useSQLite = useSQLite();

	super.fillValues();


    }

    protected void setPrimaryKey() {
	primarykey = new LinkedHashMap<String, String>();
	String[] pkfields = ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(aliasInXML).getPrimaryKey();
	for (String pkfield : pkfields) {
	    primarykey.put(pkfield,
		    Utils.getValueFromLayer(layer, currentPosition, pkfield));
	}
    }

    protected boolean useSQLite() {
	String tablename = ORMLite.getDataBaseObject(getXmlFileName())
		.getTable(aliasInXML).getTableName();
	String[] pkfields = primarykey.keySet().toArray(new String[0]);
	String[] pkvalues = primarykey.values().toArray(new String[0]);
	try {
	    return dao.existsRecord(tablename, pkfields, pkvalues);
	} catch (Exception e) {
	    logger.error(e.getMessage(), e);
	    return false;
	}
    }

    @Override
    public boolean saveRecord() {

	if (isSaveable()) {

	    int currentPos = Long.valueOf(currentPosition).intValue();
	    int[] indexes = getIndexes();
	    Map<String, String> layerValues = formModel.getWidgetValues();
	    String[] attNames = layerValues.keySet().toArray(new String[0]);
	    String[] attValues = layerValues.values().toArray(new String[0]);
	    String[] pkNames = ORMLite.getDataBaseObject(getXmlFileName())
		    .getTable(aliasInXML).getPrimaryKey();
	    String[] pkValues = Utils.getValuesFromLayer(
		    layer,
		    currentPosition,
		    ORMLite.getDataBaseObject(getXmlFileName())
			    .getTable(aliasInXML).getPrimaryKey());

	    try {
		dao.saveDBFandSQLiteValues(layer, currentPos, indexes,
			attNames, attValues, pkNames, pkValues);
		return true;
	    } catch (Exception e) {
		logger.error(e.getMessage(), e);
		return false;
	    }
	}
	return false;
    }

    @Override
    protected void deleteRecord() {
	try {
	    dao.deleteDBFAndSQLRecord(view.getMapControl(), layer, Long
		    .valueOf(currentPosition).intValue(), primarykey.keySet()
		    .toArray(new String[0]),
		    primarykey.values().toArray(new String[0]));
	    refreshGUI();
	} catch (ExpansionFileReadException e) {
	    logger.error(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getMessage(), e);
	} catch (SQLException e) {
	    logger.error(e.getMessage(), e);
	} catch (ClassNotFoundException e) {
	    logger.error(e.getMessage(), e);
	}
    }

    /**
     * @return true if Primary Key of current record has been modified, false
     *         otherwise
     */
    protected boolean isPKRecordChanged() {

	setPrimaryKey();
	LinkedHashMap<String, String> pkFieldsInDialog = new LinkedHashMap<String, String>();
	for (String key : primarykey.keySet()) {
	    pkFieldsInDialog.put(key, formModel.getWidgetValue(key));
	}

	if (pkFieldsInDialog.equals(primarykey)) {
	    return false;
	}
	return true;
    }

    protected boolean isPKAlreadyInUse() {
	if (isPKRecordChanged()) {
	    boolean bool = true;
	    LinkedHashMap<String, String> pkFieldsInDialog = new LinkedHashMap<String, String>();
	    for (String key : primarykey.keySet()) {
		pkFieldsInDialog.put(key, formModel.getWidgetValue(key));
	    }
	    try {
		bool = dao.existsRecord(
			ORMLite.getDataBaseObject(getXmlFileName())
				.getTable(aliasInXML).getTableName(),
			pkFieldsInDialog.keySet().toArray(new String[0]),
			pkFieldsInDialog.values().toArray(new String[0]));
		return bool;
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
		return true;
	    } catch (ClassNotFoundException e) {
		logger.error(e.getMessage(), e);
		return true;
	    }
	}
	return false;
    }

    protected boolean primaryKeyHasErrors() {
	if (isPKAlreadyInUse()) {
	    JOptionPane
		    .showMessageDialog(
			    this,
			    "La clave primaria que ha elegido ya está en uso, por favor, elija otra",
			    PluginServices.getText(this,
				    "Clave primaria en uso"),
			    JOptionPane.ERROR_MESSAGE);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    protected boolean isSaveable() {
	if (validationHasErrors()) {
	    return false;
	} else if (primaryKeyHasErrors()) {
	    return false;
	}
	return true;
    }

}
