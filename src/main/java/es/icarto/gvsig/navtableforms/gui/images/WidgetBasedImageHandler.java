package es.icarto.gvsig.navtableforms.gui.images;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.commons.utils.ImageUtils;
import es.icarto.gvsig.navtableforms.IValidatableForm;

public class WidgetBasedImageHandler implements ImageHandler, KeyListener, ActionListener {

	private final String imgComp;
	private final JComponent widget;
	private final IValidatableForm form;
	private final String folderPath;
	private final ImageComponent image;

	private String extension = ".png";
	private ImageIcon emptyImage;

	public WidgetBasedImageHandler(String imgComp, String widgetName, String folderPath, IValidatableForm form) {
		this.imgComp = imgComp;
		this.folderPath = folderPath.endsWith(File.separator) ? folderPath : folderPath + File.separator;
		this.form = form;
		this.image = (ImageComponent) form.getFormPanel().getComponentByName(imgComp);

		widget = this.form.getWidgets().get(widgetName);
	}

	public void setEmptyImage(String imgPath) {
		emptyImage = ImageUtils.getScaled(imgPath, new Dimension(105, 60));
	}

	public void setExtension(String ext) {
		extension = ext.startsWith(".") ? ext : "." + ext;
	}

	@Override
	public void setListeners() {
		if (widget instanceof JTextField) {
			((JTextField) widget).addKeyListener(this);
		} else if (widget instanceof JComboBox) {
			((JComboBox) widget).addActionListener(this);
		}
	}

	@Override
	/**
	 * The name of the components this handler is associated on, and commonly the
	 * name of the handler itself used in maps
	 */
	public String getName() {
		return imgComp;
	}

	@Override
	public void removeListeners() {
		if (widget instanceof JTextField) {
			((JTextField) widget).removeKeyListener(this);
		} else if (widget instanceof JComboBox) {
			((JComboBox) widget).removeActionListener(this);
		}
	}

	@Override
	public void fillValues() {
		String value = "";
		if (widget instanceof JTextField) {
			value = ((JTextField) widget).getText();
		} else if (widget instanceof JComboBox) {
			Object tmpValue = ((JComboBox) widget).getSelectedItem();
			value = (tmpValue != null) ? tmpValue.toString() : "";
		}

		String imgPath = folderPath + value.trim() + extension;
		// System.out.println(image.getBounds());
		// ImageIcon icon = ImageUtils.getScaled(imgPath,
		// image.getBounds().getSize());
		ImageIcon icon = ImageUtils.getScaled(imgPath, new Dimension(105, 60));
		if (icon == null) {
			icon = emptyImage;
		}
		image.setIcon(icon);
		image.repaint();

	}

	@Override
	public void fillEmptyValues() {
		fillValues();
	}

	private void delegate() {
		if (!form.isFillingValues()) {
			form.validateForm();
			fillValues();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		delegate();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		delegate();
	}

}
