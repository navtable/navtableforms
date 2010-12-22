/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of extNavTableForms
 * 
 * extNavTableForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * extNavTableForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extNavTableForms.
 * If not, see <http://www.gnu.org/licenses/>.
*/
package es.udc.cartolab.gvsig.navtableforms.validation;

import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FormParserUtils {

    public static Vector<JComponent> getTBsFromContainer(Container c) {
	return getTBsFromContainerRecursive(c, 0);
    }

    public static Vector<JComponent> getTBsFromContainerRecursive(Container c,
	    int level) {
	Vector<JComponent> v = new Vector<JComponent>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component aux = c.getComponent(count++);

	    if (aux instanceof JTable) {
		v.add((JComponent) aux);
		return v;
	    }
	    if (aux instanceof Container) {
		Vector<JComponent> aux2 = getTBsFromContainerRecursive(
			(Container) aux, (level + 1));
		if ((aux2 != null) && (aux2.size() > 0)) {
		    for (int i = 0; i < aux2.size(); i++) {
			Object e = aux2.get(i);
			if ((e != null) && (e instanceof JComponent)) {
			    v.add((JComponent) e);
			}
		    }
		}
	    }
	}
	return v;
    }

    public static Vector<JComponent> getFTFsFromContainer(Container c) {
	return getFTFsFromContainerRecursive(c, 0);
    }

    private static Vector<JComponent> getFTFsFromContainerRecursive(
	    Container c, int level) {

	Vector<JComponent> v = new Vector<JComponent>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component aux = c.getComponent(count++);

	    if (aux instanceof JFormattedTextField) {
		v.add((JComponent) aux);
		return v;
	    }
	    if (aux instanceof Container) {
		Vector<JComponent> aux2 = getFTFsFromContainerRecursive(
			(Container) aux, (level + 1));
		if ((aux2 != null) && (aux2.size() > 0)) {
		    for (int i = 0; i < aux2.size(); i++) {
			Object e = aux2.get(i);
			if ((e != null) && (e instanceof JComponent)) {
			    v.add((JComponent) e);
			}
		    }
		}
	    }
	}
	return v;

    }

    public static Vector<JComponent> getWidgetsWithContentFromContainer(
	    Container c) {
	return getWidgetsWithContentFromContainerRecursive(c, 0);
    }

    private static Vector<JComponent> getWidgetsWithContentFromContainerRecursive(
	    Container c, int level) {

	Vector<JComponent> v = new Vector<JComponent>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component aux = c.getComponent(count++);
	    if (aux instanceof JTextField) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JFormattedTextField) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JCheckBox) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JTextArea) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JComboBox) {
		v.add((JComponent) aux);
		return v;
	    }

	    if (aux instanceof Container) {
		Vector<JComponent> aux2 = getWidgetsWithContentFromContainerRecursive(
			(Container) aux, (level + 1));
		if ((aux2 != null) && (aux2.size() > 0)) {
		    for (int i = 0; i < aux2.size(); i++) {
			Object e = aux2.get(i);
			if ((e != null) && (e instanceof JComponent)) {
			    v.add((JComponent) e);
			}
		    }

		}
	    }
	}
	return v;
    }

    public static Vector<JButton> getBTFromContainer(Container c) {
	return getBTFromContainerRecursive(c, 0);
    }

    private static Vector<JButton> getBTFromContainerRecursive(Container c,
	    int level) {

	Vector<JButton> v = new Vector<JButton>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component aux = c.getComponent(count++);

	    if (aux instanceof JButton) {
		v.add((JButton) aux);
		return v;
	    }
	    if (aux instanceof Container) {
		// avoid getting scrollbar & combobox buttons
		if (!(aux instanceof JScrollBar) && !(aux instanceof JComboBox)) {
		    Vector<JButton> aux2 = getBTFromContainerRecursive(
			    (Container) aux, (level + 1));
		    if ((aux2 != null) && (aux2.size() > 0)) {
			for (int i = 0; i < aux2.size(); i++) {
			    Object e = aux2.get(i);
			    if ((e != null) && (e instanceof JButton)) {
				v.add((JButton) e);
			    }
			}
		    }
		}
	    }
	}
	return v;

    }

    /*
     * @deprecated using instead {@link #getWidgetsWithContent(Container c)}
     */
    @Deprecated
    public static Vector<JComponent> getWidgetsFromContainer(Container c) {
	return getWidgetsFromContainerRecursive(c, 0);
    }

    /*
     * @deprecated using instead {@link
     * #getWidgetsWithContentRecursive(Container c, int level)}
     */
    @Deprecated
    private static Vector<JComponent> getWidgetsFromContainerRecursive(
	    Container c, int level) {

	Vector<JComponent> v = new Vector<JComponent>();
	int count = 0;
	while (c.getComponentCount() > count) {
	    Component aux = c.getComponent(count++);
	    // if ( aux.getClass().toString().contains("javax.swing")) {
	    // System.out.println(count + "(" +level+"): "+ aux.getName() + ": "
	    // + aux.getClass());
	    // }
	    if (aux instanceof JComboBox) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JTextField) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JFormattedTextField) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JTextArea) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JCheckBox) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JTable) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JRadioButton) {
		v.add((JComponent) aux);
		return v;
	    } else if (aux instanceof JButton) {
		v.add((JComponent) aux);
		return v;
	    }

	    // if ((aux instanceof JComponent) && (level > 1)
	    // && !(aux instanceof JETABean)
	    // && !(aux instanceof JETALabel)) {
	    // System.out.println(count + "(" +level+"): "+ aux.getName() + ": "
	    // + aux.getClass());
	    // }
	    if (aux instanceof Container) {
		if (!(aux instanceof JScrollBar)) {
		    Vector<JComponent> aux2 = getWidgetsFromContainerRecursive(
			    (Container) aux, (level + 1));
		    if ((aux2 != null) && (aux2.size() > 0)) {
			for (int i = 0; i < aux2.size(); i++) {
			    Object e = aux2.get(i);
			    if ((e != null) && (e instanceof JComponent)) {
				v.add((JComponent) e);
			    }
			}
		    }
		}
	    }
	}
	return v;
    }

}
