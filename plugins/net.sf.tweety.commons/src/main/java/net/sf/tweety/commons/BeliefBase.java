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
 * This class captures an abstract knowledge base, i.e. some set of formulas in
 * a given knowledge representation language, that can be asked queries.
 * 
 * @param <T>
 *            The type of the beliefs in this belief base.
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 * @author Dmitriy Shishkin
 */
public interface BeliefBase<T extends Formula> {

	/**
	 * Returns the signature of the language of this knowledge base.
	 * 
	 * @return the signature of the language of this knowledge base.
	 */
	Signature getSignature();

	Collection<T> getFormulas();

	boolean add(T formula);

	default boolean addAll(Collection<? extends T> formulas) {
		return formulas.stream().map(formula -> add(formula)).reduce(false, (a, b) -> a || b);
	}

	boolean remove(T formula);

	default boolean removeAll(Collection<? extends T> formulas) {
		return formulas.stream().map(formula -> remove(formula)).reduce(false, (a, b) -> a || b);
	}

	void clear();

	default boolean isEmpty() {
		return getFormulas().isEmpty();
	}

	default int size() {
		return getFormulas().size();
	}

}
