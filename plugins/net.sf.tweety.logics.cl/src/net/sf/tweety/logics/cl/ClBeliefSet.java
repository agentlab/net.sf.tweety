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
package net.sf.tweety.logics.cl;

import java.util.Collection;

import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * This class models a belief set on conditional logic, i.e. a set of conditionals.
 * 
 * @author Matthias Thimm
 *
 */
public class ClBeliefSet extends BeliefSet<Conditional> {
	
	/**
	 * Creates a new (empty) conditional belief set.
	 */
	public ClBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new conditional belief set with the given collection of
	 * conditionals.
	 * @param conditionals a collection of conditionals.
	 */
	public ClBeliefSet(Collection<? extends Conditional> conditionals){
		super(conditionals);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefBase#getSignature()
	 */
	@Override
	public PropositionalSignature getSignature(){
		PropositionalSignature sig = new PropositionalSignature();
		for(Conditional c: formulas){
			sig.addAll(c.getPremise().iterator().next().getAtoms());
			sig.addAll(c.getConclusion().getAtoms());
		}
		return sig;
	}

	@Override
	public ClBeliefSet clone(){
		ClBeliefSet copy = new ClBeliefSet();
		copy.addAll(formulas);
		return copy;
	}
}
