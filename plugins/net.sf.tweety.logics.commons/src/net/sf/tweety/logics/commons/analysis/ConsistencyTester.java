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

import net.sf.tweety.commons.Formula;

/**
 * Classes extending this abstract class are capable of testing
 * whether a given belief set is consistent. 
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 */
public interface ConsistencyTester<S extends Formula> {

	/**
	 * Checks whether the given collection of formulas is consistent.
	 * @param formulas a collection of formulas.
	 * @return "true" iff the given collection of formulas is consistent.
	 */
	boolean isConsistent(Collection<S> formulas);
	
	/**
	 * Checks whether the given formula is consistent.
	 * @param formula a formulas.
	 * @return "true" iff the formula is consistent.
	 */
	default boolean isConsistent(S formula){
		return isConsistent(Collections.singleton(formula));
	}
}
