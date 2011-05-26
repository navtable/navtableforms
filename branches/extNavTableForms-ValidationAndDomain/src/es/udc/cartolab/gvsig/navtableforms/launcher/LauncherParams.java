package es.udc.cartolab.gvsig.navtableforms.launcher;

public class LauncherParams {

    private ILauncherForm form;
    private String tableName;
    private String antTitle;

    public LauncherParams(ILauncherForm form, String tableName, String antTitle) {
	this.tableName = tableName;
	this.antTitle = antTitle;
	this.form = form;
    }

    public String getTableName() {
	return this.tableName;
    }

    public String getAlphanumericNavTableTitle() {
	return this.antTitle;
    }

    /**
     * Get SQLQuery from form. This way, dinamic values as the ones from widgets
     * could be retrieved in real time. The ID used to get the sql query is the
     * name of alphanumericnavtable, which allows the form to have several sql
     * queries.
     * 
     * @return a SQL query from the form. The keyID will be the
     *         alphanumericnavtable name
     */
    public String getSQLQuery() {
	return form.getSQLQuery(antTitle);
    }
}
