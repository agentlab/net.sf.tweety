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
import java.util.Iterator;

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
public interface BeliefBase<T extends Formula> extends Collection<T>, Cloneable {

	/**
	 * Returns the signature of the language of this knowledge base.
	 * 
	 * @return the signature of the language of this knowledge base.
	 */
	Signature getSignature();

	default boolean addAll(Collection<? extends T> formulas) {
		return formulas.stream().map(formula -> add(formula)).reduce(false, (a, b) -> a || b);
	}

	@Override
	default boolean removeAll(Collection<?> formulas) {
		return formulas.stream().map(formula -> remove(formula)).reduce(false, (a, b) -> a || b);
	}

	@Override
	default boolean contains(Object o) {
		return stream().filter(formula -> formula.equals(o)).findFirst().isPresent();
	}

	@Override
	default <E> E[] toArray(E[] a) {
		return stream().toArray(size -> a);
	}

	@Override
	default Object[] toArray() {
		return stream().toArray();
	}

	@Override
	default boolean isEmpty() {
		return stream().count() == 0;
	}

	@Override
	default int size() {
		return (int) stream().count();
	}

	@Override
	default Iterator<T> iterator() {
		return stream().iterator();
	}
	
	BeliefBase<T> clone();

}
