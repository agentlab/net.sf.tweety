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
 * An iterator over interpretations.
 * @author Matthias Thimm
 *
 * @param <T> The actual type of interpretations
 */
public interface InterpretationIterator<T extends Interpretation> extends Iterator<T>{

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext();

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public T next();

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove();
	
	/**
	 * Initializes a new reseted iterator. 
	 * @return a reseted iterator.
	 */
	public InterpretationIterator<T> reset();	
	
	/**
	 * Initializes a new reseted iterator for the given signature. 
	 * @param some signature.
	 * @return a reseted iterator for the given signature.
	 */
	public InterpretationIterator<T> reset(Signature sig);
	
	/**
	 * Initializes a new reseted iterator for the given signature derived from
	 * the given set of formulas. 
	 * @param a set of formulas.
	 * @return a reseted iterator for the given signature derived from
	 * the given set of formulas. 
	 */
	public InterpretationIterator<T> reset(Collection<? extends Formula> formulas);
}
