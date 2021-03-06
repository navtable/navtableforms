/*
 * Copyright (c) 2011. iCarto
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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator;

import java.util.ArrayList;

public class ValidatorForm {

    private boolean hasValidationErrors = false;
    private final ArrayList<ValidatorComponent> cvs = new ArrayList<ValidatorComponent>();

    public boolean hasValidationErrors() {
        return hasValidationErrors;
    }

    public String getMessages() {
        // TODO: improve by allowing to define messages
        return "El formulario tiene errores";
    }

    public void setValidationErrors(boolean bol) {
        hasValidationErrors = bol;
    }

    public void addComponentValidator(ValidatorComponent cv) {
        for (ValidatorComponent existent : cvs) {
            if (existent.getComponentName().equals(cv.getComponentName())) {
                return;
            }
        }
        cvs.add(cv);
    }

    public ArrayList<ValidatorComponent> getComponentValidators() {
        return cvs;
    }

    public ValidatorComponent getComponentValidator(String name) {
        for (ValidatorComponent cv : cvs) {
            if (cv.getComponentName().equalsIgnoreCase(name)) {
                return cv;
            }
        }
        return null;
    }

    public void validate() {
        setValidationErrors(false);
        for (ValidatorComponent cv : cvs) {
            if (!cv.validate()) {
                setValidationErrors(true);
            }
        }
    }
}