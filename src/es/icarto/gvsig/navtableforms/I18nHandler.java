package es.icarto.gvsig.navtableforms;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import com.jeta.forms.components.border.TitledBorderLabel;

import es.icarto.gvsig.navtableforms.gui.i18n.I18nResourceManager;


/**
 * This class provides base i18n support for NavTableForms.
 * 
 * Default i18n support is provided for static texts in Abeille forms (or similar).
 * The widget name is used as the i18n key (see {@link #getTranslation()} for the specific syntax). 
 * 
 * @author Jorge López Fernández <jlopez@cartolab.es>
 *
 */
public class I18nHandler {

    private static final String i18nPrefix = "i18n";
    private II18nForm i18nForm;
    private I18nResourceManager i18nManager;

    public I18nHandler(II18nForm i18nForm) {
	this.i18nForm = i18nForm;
	this.i18nManager = new I18nResourceManager(i18nForm.getI18nResources());
    }

    public I18nResourceManager getResourceManager() {
	return i18nManager;
    }

    public void translateFormStaticTexts() {
	translateContainerStaticTexts(i18nForm.getFormPanel());
    }

    /**
     * This method is used to apply internationalization onto the container static texts
     * (labels, buttons, border titles and tab names) using the
     * {@link es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource} instances retrieved
     * from the form.
     */
    public void translateContainerStaticTexts(Container c) {
	for (int i = 0, len = c.getComponentCount(); i < len; i++) {
	    Component comp = c.getComponent(i);
	    translateComponentText(comp);
	    if (comp instanceof Container) {
		translateContainerStaticTexts((Container) comp);
	    }
	}
    }

    private void translateComponentText(Component comp) {
	String name = comp.getName();
	if (comp instanceof JLabel) {
	    JLabel label = (JLabel) comp;
	    label.setText(getTranslation(name, label.getText()));
	} else if (comp instanceof JButton) {
	    JButton btn = (JButton) comp;
	    btn.setText(getTranslation(name, btn.getText()));
	} else if (comp instanceof TitledBorderLabel) {
	    TitledBorderLabel title = (TitledBorderLabel) comp;
	    title.setText(getTranslation(name, title.getText()));
	} else if (comp instanceof JTabbedPane) {
	    JTabbedPane panel = (JTabbedPane) comp;
	    for (int j = 0, leng = panel.getTabCount(); j < leng; j++) {
		panel.setTitleAt(j, getTranslation(getTabKey(name, j + 1), panel.getTitleAt(j)));
	    }
	}
    }

    /**
     * The widget name must follow a syntax, which is
     *
     *     i18n.<i18n_key>[.<nr>][.<format_code>]*
     *
     * The first section, 'i18n', is just a prefix for identifying widgets which require i18n
     * for their texts.
     * Then comes the i18n id key itself (the one which should be used as key in the
     * {@link es.icarto.gvsig.navtableforms.gui.i18n.resource.I18nResource} instances).
     * The optional section that follows should contain a plain integer, and is intended
     * to allow us to use the same i18n id key in multiple widgets without causing problems
     * by them having the same name (e.g. 'i18n.msg.1', 'i18n.msg.2', etc.).
     * Optional format codes provide simple formatting options (see {@link #formatValue()}).
     * We accept multiple formatting codes, and they are applied in the order in which they appear.
     * E.g.: if 'msg' i18n id returns 'test case', then 'i18n.msg.lower.dd.title' -> 'Test Case:'
     * Keep in mind that both the nr and the format codes are optional, but if we use both, then
     * the number should come before the codes.
     */
    private String getTranslation(String key, String defaultValue) {
	if (key == null) {
	    return defaultValue;
	}
	String[] splitName = key.split("\\.");
	if (!i18nPrefix.equals(splitName[0]) || 
		!i18nManager.containsKey(splitName[1])) {
	    return defaultValue;
	}

	String value = i18nManager.getString(splitName[1]);

	if (value.length() > 0 && splitName.length > 2) {
	    int formatStartIdx;
	    try {
		Integer.parseInt(splitName[2]);
		formatStartIdx = 3;
	    } catch (NumberFormatException e) {
		formatStartIdx = 2;
	    }
	    for (int i = formatStartIdx, len = splitName.length; i < len; i++) {
		value = formatValue(value, splitName[i]);
	    }
	}
	return value;
    }

    /**
     * Tab panels are a special case for i18n, because they can have multiple texts but
     * a sole widget name.
     * Because of this, we identify their tabs' texts by combining the tab panel name
     * with the tab number, starting by 1.
     * E.g.: if the tab panel name is 'i18n.tab', then we should use 'tab_1', 'tab_2'...
     * in the i18n resource.
     * Trailing numbers and formatting are still accepted for the tab panel's name, and
     * the formatting will be applied in all the tabs' names.
     */
    private String getTabKey(String panelKey, int tabNr) {
	if (panelKey == null) {
	    return null;
	}
	String[] splitName = panelKey.split("\\.");
	if (splitName.length < 2) {
	    return null;
	}
	return 	panelKey.replaceFirst(splitName[0] + "\\." + splitName[1],
		splitName[0] + "\\." + splitName[1] + "_" + tabNr);
    }

    /**
     * We provide some formatting codes for making easier to display
     * i18n strings in different ways w/o having to store them multiple
     * times.
     * As of now we support the following codes:
     * 
     * - dd: adds trailing double dots.
     * - upper: displays the text in uppercase.
     * - lower: displays the text in lowercase.
     * - title: displays the text in title case (capitalizing the first
     *  letter of each word).
     * - html: wraps the current text in html tags.
     */
    private static String formatValue(String value, String formatCode) {
	if ("dd".equals(formatCode)) {
	    return value + ":";
	}
	if ("upper".equals(formatCode)) {
	    return value.toUpperCase();
	}
	if ("lower".equals(formatCode)) {
	    return value.toLowerCase();
	}
	if ("title".equals(formatCode)) {
	    return toTitleCase(value);
	}
	if ("html".equals(formatCode)) {
	    return "<html>" + value + "</html>";
	}
	return value;
    }

    private static String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
}
