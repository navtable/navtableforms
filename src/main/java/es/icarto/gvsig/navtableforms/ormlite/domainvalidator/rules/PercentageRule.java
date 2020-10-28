/*
 * Copyright (c) 2013. iCarto
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

package es.icarto.gvsig.navtableforms.ormlite.domainvalidator.rules;

import java.text.NumberFormat;
import java.text.ParseException;

import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;

public class PercentageRule extends ValidationRule {

	private NumberFormat format;

	public PercentageRule() {
		format = DoubleFormatNT.getDisplayingFormat();
	}

	@Override
	public boolean validate(String value) {
		return isEmpty(value) || isPercentage(value);
	}

	private boolean isPercentage(String value) {
		try {
			double double_value = Math.abs(format.parse(value).doubleValue());
			if ((double_value >= 0) && (double_value <= 100)) {
				return true;
			}
			return false;
		} catch (ParseException nfe) {
			return false;
		}
	}

}
