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

import java.util.Collection;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefBaseMachineShop;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.logics.commons.analysis.MusEnumerator;
import net.sf.tweety.logics.commons.analysis.NaiveMusEnumerator;
import net.sf.tweety.logics.pcl.DefaultMeReasoner;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;

/**
 * This consistency restorer determines the new probabilities of conditionals
 * by computing the ME-distribution of each maximal consistent subset of the 
 * knowledge base, then convex combining those yielding a distribution P,
 * and extracting the new probabilities from P. 
 * 
 * @author Matthias Thimm
 */
@Component(service = BeliefBaseMachineShop.class)
public class ConvexAggregatingMaxConsMeMachineShop implements BeliefBaseMachineShop<ProbabilisticConditional> {

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase<ProbabilisticConditional> repair(BeliefBase<ProbabilisticConditional> beliefBase) {
		if(!(beliefBase instanceof PclBeliefSet))
			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		PclDefaultConsistencyTester tester = new PclDefaultConsistencyTester();
		if(tester.isConsistent(beliefSet))
			return beliefSet;
		MusEnumerator<ProbabilisticConditional> mu = new NaiveMusEnumerator<ProbabilisticConditional>(tester);
		Collection<Collection<ProbabilisticConditional>> maxCons = mu.maximalConsistentSubsets(beliefSet);
		// for each maximal consistent subset determine its ME-distribution
		@SuppressWarnings("unchecked")
		ProbabilityDistribution<PossibleWorld>[] distributions = new ProbabilityDistribution[maxCons.size()];
		int cnt = 0;
		for(Collection<ProbabilisticConditional> mc: maxCons){
			PclBeliefSet bs = new PclBeliefSet();
			for(Formula f: mc) 
				bs.add((ProbabilisticConditional) f);
			// name the signature explicitly in order to ensure that the distributions
			// are defined on the same set. 
			distributions[cnt] = new DefaultMeReasoner().getMeDistribution(bs);			
			cnt++;
		}
		double[] factors = new double[maxCons.size()];
		for(int i = 0; i < maxCons.size();  i++)
			factors[i] = 1f/maxCons.size();
		ProbabilityDistribution<PossibleWorld> p = ProbabilityDistribution.convexCombination(factors, distributions);
		// prepare result
		PclBeliefSet result = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet)
			result.add(new ProbabilisticConditional(pc, p.probability(pc)));
		return result;
	}

}
