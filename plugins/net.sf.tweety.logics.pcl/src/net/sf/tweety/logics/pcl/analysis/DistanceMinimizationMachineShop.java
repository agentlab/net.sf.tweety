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
package net.sf.tweety.logics.pcl.analysis;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefBaseMachineShop;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.math.probability.Probability;

/**
 * This consistency restorer uses the distance minimization inconsistency measure
 * to restore consistency. 
 * 
 * @author Matthias Thimm
 *
 */
@Component(service = BeliefBaseMachineShop.class)
public class DistanceMinimizationMachineShop implements BeliefBaseMachineShop<ProbabilisticConditional>  {

	/**
	 * The p-norm parameter.
	 */
	private int p = 1;
	
	/**
	 * Creates a new restorer for p=1.
	 */
	public DistanceMinimizationMachineShop(){
		this(1);
	}
	
	/**
	 * Creates a new restorer for the given p.
	 * @param p some parameter for the p-norm.
	 */
	public DistanceMinimizationMachineShop(int p){
		this.p = p;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase<ProbabilisticConditional> repair(BeliefBase<ProbabilisticConditional> beliefSet) {
//		if(!(beliefBase instanceof PclBeliefSet))
//			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
//		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		PclDefaultConsistencyTester tester = new PclDefaultConsistencyTester();
		if(tester.isConsistent(beliefSet))
			return beliefSet;
		PclBeliefSet newBeliefSet = new PclBeliefSet();
		DistanceMinimizationInconsistencyMeasure m = new DistanceMinimizationInconsistencyMeasure(this.p);
		for(ProbabilisticConditional pc: beliefSet)
			newBeliefSet.add(new ProbabilisticConditional(pc, new Probability(pc.getProbability().doubleValue()+m.getDeviation(beliefSet, pc))));		
		return newBeliefSet;
	}

}
