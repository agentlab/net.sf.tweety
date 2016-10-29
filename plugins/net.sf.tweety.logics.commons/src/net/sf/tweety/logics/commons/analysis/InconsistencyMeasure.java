/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.logics.commons.analysis;

import java.util.Collection;
import java.util.Collections;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;

/**
 * Classes implementing this interface represent inconsistency measures on
 * belief bases.
 * @param <T>
 *            The type of belief bases this measure supports.
 * 
 * @author Matthias Thimm
 * @author Dmitriy Shishkin
 */
public interface InconsistencyMeasure<T extends Formula> {

	/** Tolerance. */
	public static final double MEASURE_TOLERANCE = 0.005;

	default Double inconsistencyMeasure(T formula) {
		return inconsistencyMeasure(Collections.singleton(formula));
	}

	/**
	 * This method measures the inconsistency of the given belief base.
	 * 
	 * @param formula
	 *            a belief base.
	 * @return a Double indicating the degree of inconsistency.
	 */
	default Double inconsistencyMeasure(BeliefBase<T> beliefBase) {
		return this.inconsistencyMeasure(beliefBase.getFormulas());
	}

	/**
	 * This method measures the inconsistency of the given set of formulas.
	 * 
	 * @param formulas
	 *            a collection of formulas.
	 * @return a Double indicating the degree of inconsistency.
	 */
	Double inconsistencyMeasure(Collection<T> formulas);
}
