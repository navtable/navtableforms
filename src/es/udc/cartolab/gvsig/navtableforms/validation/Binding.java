package es.udc.cartolab.gvsig.navtableforms.validation;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.DefaultValidationResultModel;

public abstract class Binding<FormModel> extends PresentationModel<Model> {

	private final ValidationResultModel validationResultModel;


	// Instance Creation ******************************************************

	public Binding(Model model) {
		super(model);
		validationResultModel = new DefaultValidationResultModel();
		initEventHandling();
		updateValidationResult();
	}


	// Exposing Models ********************************************************

	public ValidationResultModel getValidationResultModel() {
		return validationResultModel;
	}


	// Initialization *********************************************************

	/**
	 * Listens to changes in all properties of the current Order
	 * and to Order changes.
	 */
	private void initEventHandling() {
		PropertyChangeListener handler = new ValidationUpdateHandler();
		addBeanPropertyChangeListener(handler);
		getBeanChannel().addValueChangeListener(handler);
	}


	// Event Handling *********************************************************

	private void updateValidationResult() {
		Model model = getBean();
		ValidationResult result = getValidator().validate(model);
		validationResultModel.setResult(result);
	}

	protected abstract Validator getValidator();


	/**
	 * Validates the order using an OrderValidator and
	 * updates the validation result.
	 */
	private final class ValidationUpdateHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			updateValidationResult();
		}

	}

}
