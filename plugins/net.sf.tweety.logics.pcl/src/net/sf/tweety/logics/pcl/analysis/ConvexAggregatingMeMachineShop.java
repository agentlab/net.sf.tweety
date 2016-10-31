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

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefBaseMachineShop;
import net.sf.tweety.logics.pcl.DefaultMeReasoner;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;

/**
 * This consistency restorer determines the new probabilities of conditionals
 * by computing the ME-distribution of each single conditional, then convex
 * combining those yielding a distribution P, and extracting the new probabilities
 * from P. 
 * 
 * @author Matthias Thimm
 */
public class ConvexAggregatingMeMachineShop implements BeliefBaseMachineShop<ProbabilisticConditional> {

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase<ProbabilisticConditional> repair(BeliefBase<ProbabilisticConditional> beliefSet) {
//		if(!(beliefBase instanceof PclBeliefSet))
//			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
//		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		PclDefaultConsistencyTester tester = new PclDefaultConsistencyTester();
		if(tester.isConsistent(beliefSet.getFormulas()))
			return beliefSet;
		// for each conditional determine its ME-distribution
		@SuppressWarnings("unchecked")
		ProbabilityDistribution<PossibleWorld>[] distributions = new ProbabilityDistribution[beliefSet.size()];
		int cnt = 0;
		for(ProbabilisticConditional pc: beliefSet.getFormulas()){
			PclBeliefSet bs = new PclBeliefSet();
			bs.add(pc);
			// name the signature explicitly in order to ensure that the distributions
			// are defined on the same set. 
			distributions[cnt] = new DefaultMeReasoner().getMeDistribution(bs);			
			cnt++;
		}
		double[] factors = new double[beliefSet.size()];
		for(int i = 0; i < beliefSet.size();  i++)
			factors[i] = 1f/beliefSet.size();
		ProbabilityDistribution<PossibleWorld> p = ProbabilityDistribution.convexCombination(factors, distributions);
		// prepare result
		PclBeliefSet result = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet.getFormulas())
			result.add(new ProbabilisticConditional(pc, p.probability(pc)));
		return result;
	}

}
