package es.udc.cartolab.gvsig.navtableforms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

import es.udc.cartolab.gvsig.navtable.ToggleEditing;
import es.udc.cartolab.gvsig.navtableforms.Utils;
import es.udc.cartolab.gvsig.navtableforms.ormlite.ORMLite;

/**
 * Abstract class defining Data Access Model for SQLite and DBF.
 * 
 * How exceptions are managed: along this class -and because it intends to be a library to be reused- any exception is catch
 * but they are thrown, for every specific DAO inheriting from this to manage them.
 *
 * @author Andrés Maneiro <amaneiro@cartolab.es>
 * @author Francisco Puga <fpuga@cartolab.es>
 * @author Javier Estévez <jestevez@cartolab.es>
 */
public abstract class DAOGeneric {

    protected Connection conn = null;
    private String sqlitepath = null;

    private ToggleEditing te = null;

    /**
     * Constructor
     * 
     * @param dbpath
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected DAOGeneric(String dbpath) throws SQLException,
	    ClassNotFoundException {
	this.sqlitepath = dbpath;
	te = new ToggleEditing();
	conn = getConnection(this.sqlitepath);
    }

    /**
     * @param sqlitepath
     * @return the connection with the database
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private Connection getConnection(String sqlitepath) throws SQLException,
	    ClassNotFoundException {
	Class.forName("org.sqlite.JDBC");
	return DriverManager.getConnection("jdbc:sqlite:" + sqlitepath);
    }

    protected abstract String[] getPrimaryKey(String tableName);

    protected abstract String getXmlFileName();

    public String getDBForSQLiteValue(FLyrVect layer, String layerNameInXML,
	    long currentPosition, String fieldname,
	    LinkedHashMap<String, String> primaryKey, boolean useSQLite)
	    throws SQLException, ClassNotFoundException {

	String text = null;
	text = Utils.getValueFromLayer(layer, currentPosition, fieldname);
	if (useSQLite) {
	    String tablename = ORMLite.getDataBaseObject(getXmlFileName())
		    .getTable(layerNameInXML).getTableName();
	    String[] pkfields = primaryKey.keySet().toArray(new String[0]);
	    String[] pkvalues = primaryKey.values().toArray(new String[0]);
	    String aux = getSQLValue(tablename, fieldname, pkfields, pkvalues);
	    if (aux != null & aux.length() > 0) {
		text = aux;
	    }
	}
	return text;
    }

    /**
     * @return a new connection if there is none, or the current one
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected boolean getConnection() throws SQLException,
	    ClassNotFoundException {
	conn = getConnection(this.sqlitepath);
	if (conn != null) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Reloads the DAO in case the preferences changed.
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected void reloadConnection(String newDBPath) throws SQLException,
	    ClassNotFoundException {
	this.sqlitepath = newDBPath;
	getConnection(this.sqlitepath);
    }

    /**
     * @param table
     *            = name of the table to check
     * @param pkNames
     *            = names of the intended primarykey
     * @param pkValues
     *            = values of the intended primarykey
     * @return true if pkNames are the primary key for the given table AND
     *         pkValues is a combination in use of the primary key.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected boolean isRecord(String table, String[] pkNames, String[] pkValues)
	    throws SQLException, ClassNotFoundException {

	// There should be at most one record with the keyvalues.
	String[][] res = getSQLValues(table, pkNames, pkNames, pkValues);
	if ((res != null) && (res.length == 1)) {
	    return true;
	}
	return false;
    }

    /**
     * @param the
     *            name of the table to query
     * @return Array with all column names of the table
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String[] getColumnNames(String tablename) throws SQLException,
	    ClassNotFoundException {

	String query = "SELECT * FROM " + tablename + " LIMIT 1";

	if (conn == null) {
	    getConnection();
	}

	Statement stat = conn.createStatement();
	ResultSet rs = stat.executeQuery(query);

	ResultSetMetaData rm = rs.getMetaData();
	String sArray[] = new String[rm.getColumnCount()];
	for (int ctr = 1; ctr <= sArray.length; ctr++) {
	    String s = rm.getColumnName(ctr);
	    sArray[ctr - 1] = s;
	}
	rs.close();

	return sArray;

    }

    /**
     * Format a resultSet to a one dimension array
     * 
     * @param rs
     * @param fieldName
     * @return
     * @throws SQLException
     */
    protected String[] ResultSet2Vector(ResultSet rs, String fieldName)
	    throws SQLException {

	ArrayList<String> listResult = new ArrayList<String>();

	while (rs.next()) {
	    String tmp = rs.getString(fieldName);
	    if ((tmp == null) || (tmp.length() == 0)) {
		return (new String[] { "" });
	    }

	    listResult.add(tmp);
	}

	rs.close();

	return listResult.toArray(new String[] {});

    }

    /**
     * Format a resultSet to a two dimension array
     * 
     * @param rs
     * @param fieldNames
     * @return
     * @throws SQLException
     */
    protected String[][] ResultSet2Matrix(ResultSet rs, String[] fieldNames)
	    throws SQLException {
	String[][] result = null;

	ArrayList<String[]> listResult = new ArrayList<String[]>();

	while (rs.next()) {
	    String[] values = new String[fieldNames.length];
	    for (int i = 0; i < fieldNames.length; i++) {
		String val = rs.getString(fieldNames[i]);
		if ((val == null) || (val.compareTo("") == 0)) {
		    val = " ";
		}
		values[i] = val;
	    }
	    listResult.add(values);
	}

	rs.close();

	result = listResult.toArray(new String[0][0]);

	return result;
    }

    /**
     * @param whereFields
     *            = names of the columns of database
     * @param whereValues
     *            = values
     * @return the "where" SQL built
     */
    private String buildWhere(String[] whereFields, String[] whereValues) {

	String where = "";
	for (int i = 0; i < whereValues.length; i++) {
	    where = where + whereFields[i] + " = '" + whereValues[i] + "'";
	    if (i != (whereFields.length - 1)) {
		where = where + " AND ";
	    }
	}
	return where;
    }

    private String buildGroupBy(String[] groupByFields) {

	String group = "";
	for (int i = 0; i < groupByFields.length; i++) {
	    group = group + groupByFields[i];
	    if (i != groupByFields.length - 1) {
		group = group + ", ";
	    }
	}
	return group;
    }

    /**
     * @deprecated Use {@link #getSQLValue(String, String, String[], String[])}
     *             instead
     * 
     * @param tableName
     * @param name
     * @param idAlt
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Deprecated
    public String getSQLValue(String tableName, String field, String[] pkValues)
	    throws SQLException, ClassNotFoundException {
	return getSQLValue(tableName, field, getPrimaryKey(tableName), pkValues);
    }

    /**
     * @param table
     *            = name of the table to query
     * @param fieldName
     *            = name of the field to retrieve
     * @param whereNames
     *            = names of the fields to build the where clause
     * @param whereValues
     *            = values
     * @return A ResulSet containing all the registers.
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String getSQLValue(String tablename, String fieldname,
	    String[] whereNames, String[] whereValues) throws SQLException,
	    ClassNotFoundException {

	String aux = null;
	String[][] res = null;
	res = getSQLValues(tablename, new String[] { fieldname }, whereNames,
		whereValues);
	if (res.length < 1) {
	    aux = null;
	} else {
	    aux = res[0][0];
	}

	return aux;
    }

    /**
     * @param table
     *            = name of the table to query
     * @param fieldNames
     *            = fields to retrieve
     * @return A ResulSet containing
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public String[][] getSQLValues(String table, String[] fieldNames)
	    throws SQLException, ClassNotFoundException {

	return getSQLValues(table, fieldNames, new String[] { "" },
		new String[] { "" }, new String[] { "" });
    }


    /**
     * 
     * This method build a query like: SELECT fieldNames[0], fieldNames[1] FROM
     * tableName WHERE whereFields[0] = whereValues[0] AND whereFields[1] =
     * whereValues[1];
     * 
     * @param tableName
     *            = Name of the table to query.
     * @param fieldNames
     *            = Fields to retrieve from the database.
     * @param whereFields
     *            = Fields to build "where" query. Use new String[]{""} if want
     *            to retrieve all values.
     * @param whereValues
     *            = Values to build "where" query.
     * @return A ResultSet containing the registers that match with the query.
     * @throws ClassNotFoundException
     * @throws SQLException
     * 
     */
    public String[][] getSQLValues(String tablename, String[] fieldNames,
	    String[] whereFields, String[] whereValues) throws SQLException,
	    ClassNotFoundException {

	return getSQLValues(tablename, fieldNames, whereFields, whereValues,
		new String[] { "" });
    }

    /**
     * 
     * This method build a query like: SELECT fieldNames[0], fieldNames[1] FROM
     * tableName WHERE whereFields[0] = whereValues[0] AND whereFields[1] =
     * whereValues[1] GROUP BY groupByFields[0], groupByFields[1];
     * 
     * @param tableName
     *            = Name of the table to query.
     * @param fieldNames
     *            = Fields to retrieve from the database.
     * @param whereFields
     *            = Fields to build "where" query. Use new String[]{""} if want
     *            to retrieve all values.
     * @param whereValues
     *            = Values to build "where" query.
     * @param groupByFields
     *            = fields to build the group by clause. Use new String[]{""} if
     *            want to not use GROUP BY.
     * @return A ResultSet containing the registers that match with the query.
     * @throws ClassNotFoundException
     * @throws SQLException
     * 
     */
    public String[][] getSQLValues(String tableName, String[] fieldNames,
	    String[] whereFields, String[] whereValues, String[] groupByFields)
	    throws SQLException, ClassNotFoundException {

	return ResultSet2Matrix(
		getSQLValuesAsResultSet(tableName, fieldNames, whereFields,
			whereValues, groupByFields), fieldNames);
    }


    /**
     * 
     * This method build a query like: SELECT fieldName FROM tableName WHERE
     * whereFields[0] = whereValues[0] AND whereFields[1] = whereValues[1];
     * 
     * @param tableName
     *            = Name of the table to query.
     * @param fieldName
     *            = Field to retrieve from the database.
     * @param whereFields
     *            = Fields to build "where" query. Use new String[]{""} if want
     *            to retrieve all values.
     * @param whereValues
     *            = Values to build "where" query.
     * @return A ResultSet containing the registers that match with the query.
     * @throws ClassNotFoundException
     * @throws SQLException
     * 
     */
    public String[] getSQLValues(String tableName, String fieldName,
	    String[] whereFields, String[] whereValues) throws SQLException,
	    ClassNotFoundException {
	return ResultSet2Vector(
		getSQLValuesAsResultSet(tableName, new String[] { fieldName },
			whereFields, whereValues, new String[0]), fieldName);
    }

    /**
     * 
     * This method build a query like: SELECT fieldNames[0], fieldNames[1] FROM
     * tableName WHERE whereFields[0] = whereValues[0] AND whereFields[1] =
     * whereValues[1] GROUP BY groupByFields[0], groupByFields[1];
     * 
     * @param tableName
     *            = Name of the table to query.
     * @param fieldNames
     *            = Fields to retrieve from the database.
     * @param whereFields
     *            = Fields to build "where" query. Use new String[]{""} if want
     *            to retrieve all values.
     * @param whereValues
     *            = Values to build "where" query.
     * @param groupByFields
     *            = fields to build the group by clause. Use new String[]{""} if
     *            want to not use GROUP BY.
     * @return A ResultSet containing the registers that match with the query.
     * @throws ClassNotFoundException
     * @throws SQLException
     * 
     */
    protected ResultSet getSQLValuesAsResultSet(String tableName,
	    String[] fieldNames, String[] whereFields, String[] whereValues,
	    String[] groupByFields) throws SQLException, ClassNotFoundException {

	if (whereValues.length != whereFields.length) {
	    return null;
	}

	if (conn == null) {
	    getConnection();
	}

	Statement stat = conn.createStatement();

	// BUILD SQL STATEMENT
	String query = "SELECT ";
	if (fieldNames.length > 1) {
	    for (int i = 0; i < fieldNames.length - 1; i++) {
		query = query + fieldNames[i] + ", ";
	    }
	    query = query + fieldNames[fieldNames.length - 1];
	} else {
	    query = query + " DISTINCT " + fieldNames[0];
	}

	query = query + " FROM " + tableName;

	if ((whereFields.length > 1) || (!whereFields[0].equals(""))) {
	    if (groupByFields.length > 1 && !groupByFields[0].equals("")) {
		query = query + " WHERE "
			+ buildWhere(whereFields, whereValues) + " GROUP BY "
			+ buildGroupBy(groupByFields) + ";";
	    } else {
		query = query + " WHERE "
			+ buildWhere(whereFields, whereValues);
	    }
	}

	return stat.executeQuery(query);
    }

    /**
     * Save values in the DBF file AND SQLite database.
     * 
     * @deprecated Use
     *             {@link #saveDBFandSQLiteValues(FLyrVect, int, int[], String[], String[], String[], String[])}
     * 
     * @param layer
     *            = layer where save values. The name of the table in the
     *            database is got from it.
     * @param rowPosition
     *            = number of register
     * @param attIndexes
     *            = indexes of the fields in the DBF
     * @param attNames
     *            = names of the fields in the database
     * @param attValues
     *            = values
     * @param keyValues
     *            = values of the primary key in the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Deprecated
    public void saveDBFandSQLiteValues(FLyrVect layer, int rowPosition,
	    int[] attIndexes, String[] attNames, String[] attValues,
	    String[] keyValues) throws ClassNotFoundException, SQLException {

	saveDBFandSQLiteValues(layer, rowPosition, attIndexes, attNames,
		attValues, getPrimaryKey(layer.getName()), keyValues);
    }

    /**
     * Save values in the DBF file AND SQLite database.
     * 
     * @param layer
     *            = layer where save values. The name of the table in the
     *            database is got from it.
     * @param rowPosition
     *            = number of register
     * @param attIndexes
     *            = indexes of the fields in the DBF
     * @param attNames
     *            = names of the fields in the database
     * @param attValues
     *            = values
     * @param keyNames
     *            = names of the primary key in the database
     * @param keyValues
     *            = values of the primary key in the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void saveDBFandSQLiteValues(FLyrVect layer, int rowPosition,
	    int[] attIndexes, String[] attNames, String[] attValues,
	    String[] keyNames, String[] keyValues)
	    throws ClassNotFoundException, SQLException {

	String tablename = layer.getName();
	saveDBFandSQLiteValues(layer, tablename, rowPosition, attIndexes,
		attNames, attValues, keyNames, keyValues);
    }

    /**
     * Save values in the DBF file AND SQLite database.
     * 
     * @deprecated Use
     *             {@link #saveDBFandSQLiteValues(FLyrVect, String, int, int[], String[], String[], String[], String[])}
     *             instead.
     * 
     * @param layer
     *            = layer where save values
     * @param tablename
     *            = table where save values
     * @param rowPosition
     *            = number of register
     * @param attIndexes
     *            = indexes of the fields in the DBF
     * @param attNames
     *            = names of the fields in the database
     * @param attValues
     *            = values
     * @param keyNames
     *            = names of the primary key in the database
     * @param keyValues
     *            = values of the primary key in the database
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Deprecated
    public void saveDBFandSQLiteValues(FLyrVect layer, String tableName,
	    int rowPosition, int[] attIndexes, String[] attNames,
	    String[] attValues, String[] keyValues)
	    throws ClassNotFoundException, SQLException {

	saveDBFandSQLiteValues(layer, tableName, rowPosition, attIndexes,
		attNames, attValues, getPrimaryKey(tableName), keyValues);
    }

    /**
     * Save values in the DBF file AND SQLite database.
     * 
     * @param layer
     *            = layer where save values
     * @param tablename
     *            = table where save values
     * @param rowPosition
     *            = number of register
     * @param attIndexes
     *            = indexes of the fields in the DBF
     * @param attNames
     *            = names of the fields in the database
     * @param attValues
     *            = values
     * @param keyNames
     *            = names of the primary key in the database
     * @param keyValues
     *            = values of the primary key in the database
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void saveDBFandSQLiteValues(FLyrVect layer, String tablename,
	    int rowPosition, int[] attIndexes, String[] attNames,
	    String[] attValues, String[] keyNames, String[] keyValues)
	    throws ClassNotFoundException, SQLException {

	te.startEditing(layer);

	boolean cancelSave = false;
	try {
	    saveSQLValues(tablename, attNames, attValues, keyNames, keyValues);
	    te.modifyValues(layer, rowPosition, attIndexes, attValues);
	} catch (SQLException e) {
	    cancelSave = true;
	    throw new SQLException("SQLException while saving", e);
	} catch (ClassNotFoundException e) {
	    cancelSave = true;
	    throw new ClassNotFoundException(
		    "ClassNotFoundException while saving", e);
	} finally {
	    te.stopEditing(layer, cancelSave);
	}
    }

    /**
     * Save values into the SQLite database.
     * 
     * Build an UPDATE query if whereNames are the «primary key» for the
     * database AND whereValues are an used combination of the «primary key».
     * Build an INSERT query otherwise.
     * 
     * @param tablename
     *            = Name of the table to save the values in
     * @param attrNames
     *            = Field columns to modified
     * @param attrValues
     *            = Values to set on each Field
     * @param whereNames
     *            = Attribute names to match during the update query
     * @param whereValues
     *            = Attribute values to match during the update query
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void saveSQLValues(String table, String[] attrNames,
	    String[] attrValues, String[] whereNames, String[] whereValues)
	    throws SQLException, ClassNotFoundException {

	String statement = "";
	PreparedStatement prep = null;

	if (conn == null) {
	    getConnection();
	}

	boolean isRegister = isRecord(table, whereNames, whereValues);
	if (isRegister) {

	    // BUILD UPDATE QUERY: UPDATE table SET attr1 = value1, attr2 =
	    // value2 WHERE pk1 = pkvalue1, pk2 = pkvalue2;

	    statement = "UPDATE " + table + " SET ";
	    for (int i = 0; i < attrNames.length; i++) {
		statement = statement + attrNames[i] + " =? ";
		if (i != attrNames.length - 1) {
		    statement = statement + ", ";
		}
	    }
	    statement = statement + " WHERE ";
	    String where = "";
	    for (int i = 0; i < whereNames.length; i++) {
		where = where + whereNames[i] + " =?";
		if (i != (whereNames.length - 1)) {
		    where = where + " AND ";
		}
	    }
	    statement = statement + where + ";";

	    prep = conn.prepareStatement(statement);

	    for (int i = 0; i < attrValues.length; i++) {
		prep.setString(i + 1, attrValues[i]);
	    }
	    for (int i = 0; i < whereValues.length; i++) {
		prep.setString((attrValues.length + i + 1), whereValues[i]);
	    }

	    prep.executeUpdate();

	} else {

	    // BUILD INSERT QUERY: INSERT INTO table (attr1, attr2) VALUES
	    // (value1, value2);

	    statement = "INSERT INTO " + table + "(" + attrNames[0];
	    for (int i = 1; i < attrNames.length; i++) {
		statement = statement + ", " + attrNames[i] + " ";
	    }
	    statement = statement + ") VALUES (?";
	    for (int i = 0; i < attrValues.length - 1; i++) {
		statement = statement + ",?";
	    }
	    statement = statement + ");";

	    prep = conn.prepareStatement(statement);

	    for (int i = 0; i < attrValues.length; i++) {
		prep.setString(i + 1, attrValues[i]);
	    }

	    prep.executeUpdate();
	}

    }

    /**
     * Deletes a record from the database.
     * 
     * @param tableName
     *            name of the table
     * @param pkField
     *            = the primary key name of the record to delete
     * @param pkValue
     *            = the primary key value
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void deleteRecord(String tableName, String whereField,
	    String whereValue) throws SQLException, ClassNotFoundException {

	deleteRecord(tableName, new String[] { whereField },
		new String[] { whereValue });
    }

    /**
     * Deletes a record from the database.
     * 
     * @param tableName
     *            name of the table
     * @param pkFields
     *            the primary key names of the record to delete
     * @param pkValues
     *            the primary key values
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void deleteRecord(String tableName, String[] whereFields,
	    String[] whereValues) throws SQLException, ClassNotFoundException {

	if (conn == null) {
	    getConnection();
	}

	String statement = "DELETE FROM " + tableName + " WHERE ";
	String where = "";
	for (int i = 0; i < whereFields.length; i++) {
	    where = where + whereFields[i] + " =?";
	    if (i != (whereFields.length - 1)) {
		where = where + " AND ";
	    }
	}

	statement = statement + where + ";";

	PreparedStatement prep = conn.prepareStatement(statement);
	for (int i = 0; i < whereValues.length; i++) {
	    prep.setString(i + 1, whereValues[i]);
	}

	prep.executeUpdate();

    }

    /**
     * Deletes a record from the database.
     * 
     * @deprecated Use
     *             {@link #deleteDBFAndSQLRecord(MapControl, FLyrVect, int, String[], String[])}
     *             instead
     * 
     * @param tableName
     *            name of the table
     * @param pkFields
     *            the primary key names of the record to delete
     * @param pkValues
     *            the primary key values
     * @throws ReadDriverException
     * @throws ExpansionFileReadException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Deprecated
    public void deleteDBFAndSQLRecord(MapControl map, FLyrVect layer,
	    int rowPosition, String[] keyValues)
	    throws ExpansionFileReadException, ReadDriverException,
	    SQLException, ClassNotFoundException {

	deleteDBFAndSQLRecord(map, layer, rowPosition,
		getPrimaryKey(layer.getName()), keyValues);
    }

    /**
     * Delete a record from the database & DBF file.
     * 
     * @param map
     * @param layer
     * @param rowPosition
     * @param keyName
     *            = name of the primaryKey
     * @param keyValue
     *            = value of the primaryKey
     * @throws ReadDriverException
     * @throws ExpansionFileReadException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void deleteDBFAndSQLRecord(MapControl map, FLyrVect layer,
	    int rowPosition, String primaryKey, String keyValue)
	    throws ExpansionFileReadException, ReadDriverException,
	    SQLException, ClassNotFoundException {

	deleteDBFAndSQLRecord(map, layer, rowPosition,
		new String[] { primaryKey }, new String[] { keyValue });
    }

    /**
     * Delete a record from the database & DBF file.
     * 
     * @param map
     * @param layer
     * @param rowPosition
     * @param keyNames
     *            = names of the primaryKey
     * @param keyValues
     *            = values of the primaryKey
     * @throws ReadDriverException
     * @throws ExpansionFileReadException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void deleteDBFAndSQLRecord(MapControl map, FLyrVect layer,
	    int rowPosition, String[] primaryKey, String[] keyValues)
	    throws ExpansionFileReadException, ReadDriverException,
	    SQLException, ClassNotFoundException {

	String tableName = layer.getName();

	boolean layerEditing = true;
	// View view = (View) PluginServices.getMDIManager().getActiveWindow();
	ReadableVectorial feats = layer.getSource();


	feats.start();


	if (rowPosition > -1) {

	    ToggleEditing te = new ToggleEditing();

	    if (!layer.isEditing()) {
		layerEditing = false;
		te.startEditing(layer);
	    }

	    CADExtension.initFocus();
	    CADExtension.setCADTool("_selection", true);
	    CADExtension.getEditionManager().setMapControl(map);
	    CADExtension.getCADToolAdapter().configureMenu();

	    VectorialLayerEdited vle = CADExtension.getCADTool().getVLE();
	    VectorialEditableAdapter vea = vle.getVEA();

	    vea.removeRow(rowPosition, CADExtension.getCADTool().getName(),
		    EditionEvent.GRAPHIC);

	    if (!layerEditing) {
		te.stopEditing(layer, false);
	    }

	    deleteRecord(tableName, primaryKey, keyValues);
	}
    }

    public boolean existsRecord(String tablename, String[] primaryKey,
	    String[] keyValues) throws SQLException, ClassNotFoundException {
	return getSQLValues(tablename, primaryKey, primaryKey, keyValues).length > 0;
    }
}
