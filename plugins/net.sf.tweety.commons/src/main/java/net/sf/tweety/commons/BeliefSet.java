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
import java.util.Set;

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
	protected final Set<T> formulas = new HashSet<T>();

	/**
	 * Creates a new (empty) belief set.
	 */
	public BeliefSet() {
	}

	/**
	 * Creates a new belief set with the given collection of formulae.
	 * 
	 * @param c
	 *            a collection of formulae.
	 */
	public BeliefSet(Collection<? extends T> formulas) {
		this.formulas.addAll(formulas);
	}

	@Override
	public Collection<T> getFormulas() {
		return formulas;
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
	public boolean remove(T o) {
		return this.formulas.remove(o);
	}

	@Override
	public String toString() {
		return formulas.toString();
	}
}
