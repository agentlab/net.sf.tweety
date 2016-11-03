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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * This class models a belief set, i.e. a set of formulae of some formalism.
 * 
 * @param <T>
 *            The type of the beliefs in this belief set.
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 * @author Dmitriy Shishkin
 */
public abstract class BeliefSet<T extends Formula> implements BeliefBase<T> {

	/**
	 * The set of formulas of this belief base.
	 */
	protected final Set<T> formulas;

	/**
	 * Creates a new (empty) belief set.
	 */
	public BeliefSet() {
		this.formulas = new HashSet<T>();
	}

	/**
	 * Creates a new belief set with the given collection of formulae.
	 * 
	 * @param c
	 *            a collection of formulae.
	 */
	public BeliefSet(Set<T> formulas) {
		this.formulas = formulas;
	}

	/**
	 * Creates a new belief set with the given collection of formulae.
	 * 
	 * @param c
	 *            a collection of formulae.
	 */
	public BeliefSet(Collection<? extends T> formulas) {
		this.formulas = new HashSet<T>();
		this.formulas.addAll(formulas);
	}

	@Override
	public boolean add(T f) {
		return this.formulas.add(f);
	}

	@Override
	public void clear() {
		this.formulas.clear();
	}

	@Override
	public boolean remove(Object o) {
		return this.formulas.remove(o);
	}

	@Override
	public String toString() {
		return formulas.toString();
	}

	public void forEach(Consumer<? super T> action) {
		formulas.forEach(action);
	}

	@Override
	public int size() {
		return formulas.size();
	}

	@Override
	public boolean isEmpty() {
		return formulas.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return formulas.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return formulas.iterator();
	}

	@Override
	public Object[] toArray() {
		return formulas.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return formulas.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return formulas.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return formulas.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return formulas.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return formulas.removeAll(c);
	}

	@Override
	public boolean equals(Object o) {
		return formulas.equals(o);
	}

	@Override
	public int hashCode() {
		return formulas.hashCode();
	}

	@Override
	public Spliterator<T> spliterator() {
		return formulas.spliterator();
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		return formulas.removeIf(filter);
	}

	@Override
	public Stream<T> stream() {
		return formulas.stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return formulas.parallelStream();
	}

	@Override
	@SuppressWarnings("unchecked")
	public BeliefSet<T> clone(){
		try {
			return (BeliefSet<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
