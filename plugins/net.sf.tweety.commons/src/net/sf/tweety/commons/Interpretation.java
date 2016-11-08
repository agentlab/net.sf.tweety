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
package net.sf.tweety.commons;

import java.util.Collection;

/**
 * An interpretation for some logical language.
 * 
 * @author Matthias Thimm
 * @author Dmitriy Shishkin
 */
public interface Interpretation<T extends Formula> {

	/**
	 * Checks whether this interpretation satisfies the given formula.
	 * 
	 * @param formula
	 *            a formula.
	 * @return {@code true} if this interpretation satisfies the given formula.
	 * @throws IllegalArgumentException
	 *             if the formula does not correspond to the expected language.
	 */
	boolean satisfies(T formula);

	/**
	 * Checks whether this interpretation satisfies all given formulas.
	 * 
	 * @param formulas
	 *            a collection of formulas.
	 * @return {@code true} if this interpretation satisfies all given formulas.
	 * @throws IllegalArgumentException
	 *             if at least one formula does not correspond to the expected
	 *             language.
	 */
	default boolean satisfies(Collection<? extends T> formulas) {
		return formulas.stream().allMatch(this::satisfies);
	}

	/**
	 * Checks whether this interpretation satisfies the given knowledge base.
	 * 
	 * @param beliefBase
	 *            a knowledge base.
	 * @return {@code true} if this interpretation satisfies the given knowledge
	 *         base.
	 * @throws IllegalArgumentException
	 *             if the knowledgebase does not correspond to the expected
	 *             language.
	 */
	default boolean satisfies(BeliefBase<T> beliefBase) {
		return beliefBase.stream().allMatch(this::satisfies);
	}
}
