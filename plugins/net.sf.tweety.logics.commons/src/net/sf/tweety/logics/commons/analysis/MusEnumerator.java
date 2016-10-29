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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.util.SetTools;

/**
 * Interface for classes enumerating MUSes (minimal unsatisfiable sets) and
 * MCSs (maximal consistent sets). 
 * 
 * @author Matthias Thimm
 *
 */
public interface MusEnumerator<S extends Formula> extends BeliefSetConsistencyTester<S> {
	
	/**
	 * This method returns the minimal inconsistent subsets of the given
	 * set of formulas. 
	 * @param formulas a set of formulas.
	 * @return the minimal inconsistent subsets of the given
	 *  set of formulas
	 */
	Collection<Collection<S>> minimalInconsistentSubsets(Collection<S> formulas);
		
	/**
	 * This method returns the maximal consistent subsets of the given
	 * set of formulas
	 * @param formulas a set of formulas
	 * @return the maximal consistent subsets of the given
	 *  set of formulas.
	 */
	default Collection<Collection<S>> maximalConsistentSubsets(Collection<S> formulas){
		Set<Set<S>> md_sets = minimalCorrectionSubsets(formulas);
		// every maximal consistent subset is the complement of a minimal correction set
		Collection<Collection<S>> result = new HashSet<Collection<S>>();;
		Collection<S> tmp;
		for(Collection<S> ms: md_sets){
			tmp = new HashSet<S>(formulas);
			tmp.removeAll(ms);
			result.add(tmp);
		}			
		return result;
	}
	
	/**
	 * This method returns the minimal correction subsets of the given
	 * set of formulas (i.e. the complements of maximal consistent subsets)
	 * @param formulas a set of formulas
	 * @return the minimal corrections subsets of the given set of formulas.
	 */
	default Set<Set<S>> minimalCorrectionSubsets(Collection<S> formulas){
		// we use the duality of minimal inconsistent subsets, minimal correction sets, and maximal consistent
		// subsets to compute the set of maximal consistent subsets
		Collection<Collection<S>> mis = minimalInconsistentSubsets(formulas);
		// makes sets out of this
		Set<Set<S>> mi_sets = new HashSet<Set<S>>();
		for(Collection<S> m: mis)
			mi_sets.add(new HashSet<S>(m));
		SetTools<S> settools = new SetTools<S>();
		// get the minimal correction sets, i.e. irreducible hitting sets of minimal inconsistent subsets
		Set<Set<S>> md_sets =  settools.irreducibleHittingSets(mi_sets);
		return md_sets;
	}
	
	/**
	 * Computes the maximal (wrt. cardinality) partitioning {K1,...,Kn}
	 * of K (ie. K is a disjoint union of K1,...,Kn) such that MI(K)
	 * is a disjoint union of MI(K1),...,MI(Kn).
	 * @param formulas a set of formulas K
	 * @return the MI components of K
	 */
	default Collection<Collection<S>> getMiComponents(Collection<S> formulas){
		List<Collection<S>> comp = new LinkedList<Collection<S>>(minimalInconsistentSubsets(formulas));
		boolean changed;
		do{
			changed = false;
			for(int i = 0; i < comp.size(); i++){
				for(int j = i+1; j< comp.size(); j++){
					if(!Collections.disjoint(comp.get(i), comp.get(j))){
						changed = true;
						comp.get(i).addAll(comp.get(j));
						comp.remove(j);
						break;
					}					
				}
				if(changed) break;
			}
		}while(changed);
		return comp;
	}
	
	default boolean isConsistent(Collection<S> formulas){
		return minimalInconsistentSubsets(formulas).isEmpty();
	}
	
}
