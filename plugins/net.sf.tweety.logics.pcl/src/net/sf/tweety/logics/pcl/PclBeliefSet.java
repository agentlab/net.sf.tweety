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
package net.sf.tweety.logics.pcl;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;


/**
 * This class models a belief set on probabilistic conditional logic, i.e. a set of
 * probabilistic conditionals.
 * 
 * @author Matthias Thimm
 *
 */
@Component(service = BeliefBase.class)
public class PclBeliefSet extends BeliefSet<ProbabilisticConditional> {

	/**
	 * Creates a new (empty) conditional belief set.
	 */
	public PclBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new conditional belief set with the given collection of
	 * conditionals.
	 * @param conditionals a collection of conditionals.
	 */
	public PclBeliefSet(Collection<? extends ProbabilisticConditional> conditionals){
		super(conditionals);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefSet#getSignature()
	 */
	@Override
	public Signature getSignature() {
		PropositionalSignature sig = new PropositionalSignature();
		for(ProbabilisticConditional c: formulas)
			sig.addAll(((PropositionalSignature)c.getSignature()));			
		return sig;
	}	
	
}
