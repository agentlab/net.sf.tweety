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
package net.sf.tweety.beliefdynamics;

import java.util.Collection;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;

/**
 * This is the interface for a classic belief base revision operator, ie. an
 * operator that takes some set of formulas and a single formula and revises the
 * former by the latter.
 *
 * @param <T>
 *            The type of formulas that this operator works on.
 * 
 * @author Matthias Thimm
 * @author Dmitriy Shishkin
 */
public interface BaseRevisionOperator<T extends Formula> {

	/**
	 * Revises the given collection of formulas by the given formula.
	 * 
	 * @param base
	 *            some collection of formulas.
	 * @param formula
	 *            a formula
	 * @return the revised collection.
	 */
	Collection<T> revise(Collection<T> base, T formula);

	/**
	 * Revises the given belief base by the given formula.
	 * 
	 * @param base
	 *            some belief base.
	 * @param formula
	 *            a formula
	 * @return the revised collection.
	 */
	default Collection<T> revise(BeliefBase<T> base, T formula) {
		return revise(base, formula);
	}

}
