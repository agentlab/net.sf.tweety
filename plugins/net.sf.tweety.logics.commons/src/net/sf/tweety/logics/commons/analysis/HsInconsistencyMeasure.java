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
import java.util.HashSet;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Interpretation;
import net.sf.tweety.commons.InterpretationIterator;

/**
 * This class implements the Hitting Set inconsistency measure as proposed in [Thimm, 2014, in preparation].
 * The inconsistency value is defined as one plus the minimal number of interpretations, s.t. every formula of
 * the belief set is satisfied by at least one interpretation. This is equivalent in the cardinality of
 * a minimal partitioning of the knowledge base such that each partition is consistent.
 * 
 * @author Matthias Thimm
 *
 * @param <S> some formula type
 * @param <T> some belief set type
 */
@Component(service = InconsistencyMeasure.class)
public class HsInconsistencyMeasure<S extends Formula> implements InconsistencyMeasure<S> {

	/** Used for iterating over interpretations of the underlying language. */
	private InterpretationIterator<?> it;
	
	public HsInconsistencyMeasure() {
		super();
	}

	/** 
	 * Creates a new inconsistency measure that uses the interpretations given
	 * by the given iterator.
	 * @param it some interpretation iterator.
	 */
	public HsInconsistencyMeasure(InterpretationIterator<?> it){
		this.it = it;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<S> formulas) {
		// re-initialize interpretation iterator with correct signature
		this.it = it.reset(formulas);
		// check empty set of formulas
		if(formulas.isEmpty())
			return 0d;
		// this is not very efficient but works
		for(int card = 1; card <= formulas.size(); card++){
			Collection<Interpretation> hittingSet = this.getHittingSet(formulas, card, new HashSet<Interpretation>());
			if(hittingSet != null){				
				return new Double(hittingSet.size()-1);
			}
		}
		// if no hitting set has been found there is a contradictory formula and we return Infinity as inconsistency value.
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Determines a hitting set of the given cardinality. If no such hitting set exists null is returned.
	 * @param formulas a collection of formulas
	 * @param card some cardinality.
	 * @param interpretations in addition to the card interpretations also use this set for satisfying formulas. 
	 * @return a hitting set or null.
	 */
	private Collection<Interpretation> getHittingSet(Collection<S> formulas, int card, Collection<Interpretation> interpretations){
		InterpretationIterator<?> it = this.it.reset();
		Collection<Interpretation> newInts;
		Collection<Interpretation> cand;
		while(it.hasNext()){
			Interpretation i = it.next();
			if(interpretations.contains(i)) continue;
			newInts = new HashSet<Interpretation>(interpretations);
			newInts.add(i);
			if(card > 1){
				cand = this.getHittingSet(formulas, card-1, newInts);
				if(cand != null)
					return cand;
			}else{
				if(this.isHittingSet(formulas, newInts))
					return newInts;
			}
		}
		return null;
	}
			
	/**
	 * Checks whether the given candidate is a hitting set.
	 * @param formulas some set of formulas
	 * @param candidate some set of interpretation.
	 * @return "true" if the candidate is a hitting set.
	 */
	private boolean isHittingSet(Collection<S> formulas, Collection<Interpretation> candidate){
		boolean sat;
		for(S f: formulas){
			sat = false;
			for(Interpretation i: candidate){
				if(i.satisfies(f)){
					sat = true;
					break;
				}				
			}
			if(!sat) return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "HS";
	}
}
