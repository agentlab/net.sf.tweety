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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefBaseMachineShop;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.parser.PclParser;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.Logarithm;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

/**
 * This approach to consistency restoration follows the approach proposed in [Thimm, DKB, 2011].
 * 
 * @author Matthias Thimm
 */
@Component(service = BeliefBaseMachineShop.class)
public class MinimumAggregatedDistanceMachineShop implements BeliefBaseMachineShop<ProbabilisticConditional> {

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
		// construct convex optimization problem
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature)beliefSet.getSignature());
		// for each conditional we need a probability function; and an extra one for distance minimization
		// and add constraints to ensure those are actual probability functions that satisfy their given conditional
		Map<ProbabilisticConditional,Map<PossibleWorld,Variable>> pc2vars = new HashMap<ProbabilisticConditional,Map<PossibleWorld,Variable>>();
		int cnt = 0;
		for(ProbabilisticConditional pc: beliefSet){
			Map<PossibleWorld,Variable> prob = new HashMap<PossibleWorld,Variable>();
			Term t = null;
			int w_cnt = 0;
			for(PossibleWorld w: worlds){
				Variable var = new FloatVariable("w" + cnt + "_" + w_cnt++,0,1);
				prob.put(w, var);
				if(t == null)
					t = var;
				else t = t.add(var);
			}
			pc2vars.put(pc, prob);
			problem.add(new Equation(t,new FloatConstant(1)));
			Term leftSide = null;
			Term rightSide = null;
			if(pc.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(pc.getConclusion())){
						if(leftSide == null)
							leftSide = prob.get(w);
						else leftSide = leftSide.add(prob.get(w));
					}
				rightSide = new FloatConstant(pc.getProbability().getValue());
			}else{				
				PropositionalFormula body = pc.getPremise().iterator().next();
				PropositionalFormula head_and_body = pc.getConclusion().combineWithAnd(body);
				for(PossibleWorld w: worlds){
					if(w.satisfies(head_and_body)){
						if(leftSide == null)
							leftSide = prob.get(w);
						else leftSide = leftSide.add(prob.get(w));
					}
					if(w.satisfies(body)){
						if(rightSide == null)
							rightSide = prob.get(w);
						else rightSide = rightSide.add(prob.get(w));
					}					
				}
				if(rightSide == null)
					rightSide = new FloatConstant(0);
				else rightSide = rightSide.mult(new FloatConstant(pc.getProbability().getValue()));
			}
			if(leftSide == null)
				leftSide = new FloatConstant(0);
			if(rightSide == null)
				rightSide = new FloatConstant(0);
			problem.add(new Equation(leftSide,rightSide));
			cnt++;
		}
		Map<PossibleWorld,Variable> prob_main = new HashMap<PossibleWorld,Variable>();
		Term t = null;
		int w_cnt = 0;		
		for(PossibleWorld w: worlds){
			Variable var = new FloatVariable("w_" + w_cnt++,0,1);
			prob_main.put(w, var);
			if(t == null)
				t = var;
			else t = t.add(var);
		}
		problem.add(new Equation(t,new FloatConstant(1)));
		// construct target function: minimize sum of squared cross-entropies
		// from each p_i to p
		Term targetFunction = null;
		for(ProbabilisticConditional pc: beliefSet){
			t = null;
			for(PossibleWorld w: worlds){
				if(t == null)
					t = pc2vars.get(pc).get(w).mult(new Logarithm(pc2vars.get(pc).get(w))).minus(pc2vars.get(pc).get(w).mult(new Logarithm(prob_main.get(w))));
				else t = t.add(pc2vars.get(pc).get(w).mult(new Logarithm(pc2vars.get(pc).get(w))).minus(pc2vars.get(pc).get(w).mult(new Logarithm(prob_main.get(w)))));
			}
			if(targetFunction == null)
				targetFunction = t.mult(t);
			else targetFunction = targetFunction.add(t.mult(t));			
		}
		problem.setTargetFunction(targetFunction);	
		try{			
			Map<Variable,Term> solution = Solver.getDefaultGeneralSolver().solve(problem);
			// construct probability distribution
			ProbabilityDistribution<PossibleWorld> p = new ProbabilityDistribution<PossibleWorld>(beliefSet.getSignature());
			for(PossibleWorld w: worlds)
				p.put(w, new Probability(solution.get(prob_main.get(w)).doubleValue()));
			// prepare result
			PclBeliefSet result = new PclBeliefSet();
			for(ProbabilisticConditional pc: beliefSet)
				result.add(new ProbabilisticConditional(pc,p.probability(pc)));							
			return result;					
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the minimal distance to a consistent knowledge base is not feasible.");
		}		
	}

	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		PclParser parser = new PclParser();
		PclBeliefSet kb = (PclBeliefSet) parser.parseBeliefBase("(a)[0.3]\n(a)[0.7]");
		System.out.println("INITIAL: " + kb);
		
		System.out.println();
		System.out.println();
		
		MinimumAggregatedDistanceMachineShop mshop = new MinimumAggregatedDistanceMachineShop();
		
		System.out.println("RESULT: " + mshop.repair(kb));
	}
	
}
