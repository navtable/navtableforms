package es.udc.cartolab.gvsig.navtableforms;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import es.udc.cartolab.fileslink.FilesLink;

public abstract class AbstractSQLFormWithFilesLink extends AbstractSQLForm {

	JButton filesLinkB = null;

	public AbstractSQLFormWithFilesLink(FLyrVect layer) {
		super(layer);
	}


	@Override
	protected JPanel getActionsToolBar(){
		JPanel actionsToolBar = new JPanel(new FlowLayout());
		actionsToolBar.add(getButton("copySelectedB"));
		actionsToolBar.add(getButton("copyPreviousB"));
		actionsToolBar.add(getButton("zoomB"));
		actionsToolBar.add(getButton("selectionB"));
		actionsToolBar.add(getButton("saveB"));
		actionsToolBar.add(getButton("removeB"));

		filesLinkB = getNavTableButton(filesLinkB, "/fileslink.png", "filesLinkTooltip");
		actionsToolBar.add(filesLinkB);

		return actionsToolBar;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);

		if (e.getSource() == filesLinkB){
			FilesLink fl = new FilesLink("registro_material");
			fl.showFiles(currentPosition);
		}
	}

}
