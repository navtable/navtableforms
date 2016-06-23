package es.icarto.gvsig.navtableforms.calculation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;

public abstract class Calculation {

    private static final Logger logger = Logger.getLogger(Calculation.class);

    protected JTextField resultWidget;
    protected HashMap<String, JComponent> operands;
    protected OperandComponentListener handler;
    private final Map<String, JComponent> formWidgets;
    protected IValidatableForm form;
    protected final NumberFormat formatter = DoubleFormatNT
	    .getBigDecimalFormat();
    private boolean calculateOnlyWhenValid = true;

    // TODO: Maybe this class should have a fillValues method. It will be used
    // to re-calculate the value and set it if the operator values have changed
    // outside the form
    public Calculation(IValidatableForm form) {
	this.form = form;
	formWidgets = form.getWidgets();

	resultWidget = (JTextField) formWidgets.get(resultName());
	operands = new HashMap<String, JComponent>();
	for (String name : operandNames()) {
	    operands.put(name, formWidgets.get(name));
	}

	this.handler = new OperandComponentListener();
    }

    protected void setCalculateOnlyWhenValid(boolean calculateOnlyWhenValid) {
	this.calculateOnlyWhenValid = calculateOnlyWhenValid;
    }

    protected abstract String resultName();

    protected abstract String[] operandNames();

    protected void setValue(boolean valid) {
	if (calculateOnlyWhenValid && !valid) {
	    return;
	}

	String value = "";
	if (!allEmpty()) {
	    value = calculate();
	}
	resultWidget.setText(value);
	form.getFormController().setValue(resultName(), value);
	// We must assert that the new value in resultWidgets, passes the
	// validation (or not)
	form.validateForm();
    }

    protected abstract String calculate();

    public void setListeners() {
	for (JComponent widget : operands.values()) {
	    if (widget instanceof JFormattedTextField) {
		((JFormattedTextField) widget).addKeyListener(handler);
	    } else if (widget instanceof JTextField) {
		((JTextField) widget).addKeyListener(handler);
	    } else if (widget instanceof JComboBox) {
		((JComboBox) widget).addActionListener(handler);
	    } else if (widget instanceof JCheckBox) {
		((JCheckBox) widget).addActionListener(handler);
	    } else if (widget instanceof JTextArea) {
		((JTextArea) widget).addKeyListener(handler);
	    }
	}
    }

    public void removeListeners() {
	for (JComponent widget : operands.values()) {
	    if (widget instanceof JFormattedTextField) {
		((JFormattedTextField) widget).removeKeyListener(handler);
	    } else if (widget instanceof JTextField) {
		((JTextField) widget).removeKeyListener(handler);
	    } else if (widget instanceof JComboBox) {
		((JComboBox) widget).removeActionListener(handler);
	    } else if (widget instanceof JCheckBox) {
		((JCheckBox) widget).removeActionListener(handler);
	    } else if (widget instanceof JTextArea) {
		((JTextArea) widget).removeKeyListener(handler);
	    }
	}
    }

    public class OperandComponentListener implements KeyListener,
    ActionListener {

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

	@Override
	public void actionPerformed(ActionEvent e) {
	    delegate();
	}

	private void delegate() {
	    if (!form.isFillingValues()) {
		form.validateForm();
		setValue(!form.getValidatorForm().hasValidationErrors());
	    }
	}
    }

    /**
     *
     * @return true when all the operator components are empty
     */
    protected boolean allEmpty() {
	for (JComponent c : operands.values()) {
	    if (c instanceof JFormattedTextField) {
		if (!((JFormattedTextField) c).getText().trim().isEmpty()) {
		    return false;
		}
	    } else if (c instanceof JTextField) {
		if (!((JTextField) c).getText().trim().isEmpty()) {
		    return false;
		}
	    } else if (c instanceof JComboBox) {
		JComboBox jcomboBox = ((JComboBox) c);
		final Object selectedItem = jcomboBox.getSelectedItem();
		if ((selectedItem != null)
			&& !selectedItem.toString().trim().isEmpty()) {
		    return false;
		}
	    } else if (c instanceof JTextArea) {
		if (!((JTextArea) c).getText().trim().isEmpty()) {
		    return false;
		}
	    }

	}
	return true;
    }

    protected BigDecimal operandValue(String name) {
	JComponent jComponent = operands.get(name);
	String importe = "";
	if (jComponent instanceof JTextField) {
	    importe = ((JTextField) jComponent).getText().trim();
	} else if (jComponent instanceof JComboBox) {
	    Object selectedItem = ((JComboBox) jComponent).getSelectedItem();
	    if (selectedItem != null) {
		if (selectedItem instanceof KeyValue) {
		    importe = ((KeyValue) selectedItem).getKey();
		} else {
		    importe = selectedItem.toString().trim();
		}
	    }
	}

	BigDecimal value = new BigDecimal(0);
	if (!importe.isEmpty()) {
	    try {
		value = (BigDecimal) formatter.parse(importe);
	    } catch (ParseException e) {
		logger.error(e.getStackTrace(), e);
	    }
	}
	return value;
    }
}
