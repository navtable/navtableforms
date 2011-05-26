package es.udc.cartolab.gvsig.navtableforms.launcher;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JInternalFrame;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

import es.udc.cartolab.gvsig.navtable.AlphanumericNavTable;

public class AlphanumericNavTableLauncher implements MouseListener {

    private ILauncherForm form;
    private LauncherParams params;

    public AlphanumericNavTableLauncher(ILauncherForm form,
	    LauncherParams params) {
	this.form = form;
	this.params = params;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	IEditableSource source = getTableSource(params.getTableName());
	AlphanumericNavTable ant;
	try {
	    ant = new AlphanumericNavTable(source,
		    params.getAlphanumericNavTableTitle());
	    ant.init();
	    PluginServices.getMDIManager().addCentredWindow(ant);
	    JInternalFrame parent = (JInternalFrame) ant.getRootPane()
		    .getParent();
	    // this listener will call the form passed once
	    // alphanumericnavtable is closed
	    parent.addInternalFrameListener(form);
	} catch (ReadDriverException e1) {
	    e1.printStackTrace();
	}
    }

    private IEditableSource getTableSource(String tableName) {
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if ((w instanceof Table)
		    && (((Table) w).getModel().getName()
			    .equalsIgnoreCase(tableName))) {
		try {
		    IEditableSource source = ((Table) w).getModel().getModelo();

		    DataSourceFactory dsf = source.getRecordset()
			    .getDataSourceFactory();
		    DataSource ds = dsf.executeSQL(params.getSQLQuery(),
			    DataSourceFactory.AUTOMATIC_OPENING);
		    ds.setDataSourceFactory(dsf);
		    SelectableDataSource sds = new SelectableDataSource(ds);
		    EditableAdapter ea = new EditableAdapter();
		    ea.setOriginalDataSource(sds);
		    return ea;
		} catch (DriverLoadException e) {
		    e.printStackTrace();
		    return null;
		} catch (ReadDriverException e) {
		    e.printStackTrace();
		    return null;
		} catch (ParseException e) {
		    e.printStackTrace();
		    return null;
		} catch (SemanticException e) {
		    e.printStackTrace();
		    return null;
		} catch (EvaluationException e) {
		    e.printStackTrace();
		    return null;
		}
	    }
	}
	return null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
