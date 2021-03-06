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
package net.sf.tweety.math.opt.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.commons.util.VectorTools;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.opt.ConstraintSatisfactionProblem;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.IntegerConstant;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;


/**
 * This class implements a gradient descent involving Hessian correction
 * for solving unconstrained optimization problems.
 * @author Matthias Thimm
 */
public class HessianGradientDescent extends Solver {

	/**
	 * Logger.
	 */
	private Logger log = LoggerFactory.getLogger(HessianGradientDescent.class);
	
	private static final double PRECISION = 0.00001;
	
	/**
	 * The starting point for the solver.
	 */
	private Map<Variable,Term> startingPoint;
	
	public HessianGradientDescent(Map<Variable,Term> startingPoint) {
		this.startingPoint = startingPoint;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve(ConstraintSatisfactionProblem problem) throws GeneralMathException {
		if(problem.size() > 0)
			throw new IllegalArgumentException("The gradient descent method works only for optimization problems without constraints.");
		this.log.trace("Solving the following optimization problem using hessian gradient descent:\n===BEGIN===\n" + problem + "\n===END===");
		Term func = ((OptimizationProblem)problem).getTargetFunction();
		if(((OptimizationProblem)problem).getType() == OptimizationProblem.MAXIMIZE)
			func = new IntegerConstant(-1).mult(func);	
		// variables need to be ordered
		List<Variable> variables = new ArrayList<Variable>(func.getVariables());
		List<Term> gradient = new LinkedList<Term>();		
		for(Variable v: variables)
			gradient.add(func.derive(v).simplify());
		List<List<Term>> hessian = new LinkedList<List<Term>>();
		for(Term g: gradient){
			List<Term> row = new LinkedList<Term>();
			for(Variable v: variables)
				row.add(g.derive(v).simplify());
			hessian.add(row);
		}
		int idx = 0;
		double[] currentGuess = new double[variables.size()];
		for(Variable v: variables){
			currentGuess[idx] = this.startingPoint.get(v).doubleValue();
			idx++;
		}		
		double[][] evaluatedHessian;
		double[] dir = new double[variables.size()];
		double[] evaluatedGradient = new double[variables.size()];
		double distance;
		this.log.trace("Starting optimization.");
		while(true){
			evaluatedGradient = Term.evaluateVector(gradient, currentGuess, variables);
			distance = VectorTools.manhattanDistanceToZero(evaluatedGradient);
			this.log.trace("Current manhattan distance of gradient to zero: " + distance);
			if(distance < HessianGradientDescent.PRECISION)
				break;
			evaluatedHessian = Term.evaluateMatrix(hessian, currentGuess, variables);
			dir = this.getDirection(evaluatedHessian, evaluatedGradient);
			currentGuess = this.bestGuess(currentGuess, dir, gradient, variables);
		}
		Map<Variable,Term> result = new HashMap<Variable,Term>();
		idx = 0;
		for(Variable v: variables)
			result.put(v, new FloatConstant(currentGuess[idx++]));
		this.log.trace("Optimum found: " + result);
		return result;
	}

	/**
	 * Find the best guess.
	 * @param currentGuess
	 * @param gradient
	 */
	private double[] bestGuess(double[] currentGuess, double[] dir, List<Term> gradient, List<Variable> variables){
		double upperBound = 1;
		double currentDistance = VectorTools.manhattanDistanceToZero(Term.evaluateVector(gradient, currentGuess, variables));
		double newDistance;
		double[] newGuess = new double[variables.size()];
		double currentStep = upperBound;
		int loop = 0; 
		while(true){
			for(int idx = 0; idx < variables.size(); idx++)
				newGuess[idx] = currentGuess[idx] + currentStep * dir[idx];
			newDistance = VectorTools.manhattanDistanceToZero(Term.evaluateVector(gradient, newGuess, variables));
			if(newDistance < currentDistance)
				return newGuess;
			else currentStep /= 2;
			loop++;
			if(loop == 1000) return newGuess;
		}
	}
	
	/**
	 * Find the best direction.
	 * @param approxHessian
	 * @param evaluatedGradient
	 */
	private double[] getDirection(double[][] approxHessian, double[] evaluatedGradient){
		ConstraintSatisfactionProblem problem = new ConstraintSatisfactionProblem();
		List<Variable> variables = new LinkedList<Variable>();
		for(int i = 0; i < evaluatedGradient.length; i++)
			variables.add(new FloatVariable("X" + i));
		for(int i = 0; i < evaluatedGradient.length; i++){
			Term t = null;
			for(int j = 0; j < evaluatedGradient.length; j++){
				Term n = variables.get(j).mult(new FloatConstant(approxHessian[i][j]));
				if(t == null)
					t = n;
				else t = t.add(n);
			}
			problem.add(new Equation(t,new FloatConstant(-evaluatedGradient[i])));
		}
		ApacheCommonsSimplex solver = new ApacheCommonsSimplex();
		try{
			Map<Variable,Term> solution = solver.solve(problem);
		double[] result = new double[variables.size()];
		int idx = 0;
		for(Variable v: variables)
			result[idx++] = solution.get(v).doubleValue();	
		return result;
		}catch(Exception e){
			throw new RuntimeException();
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#isInstalled()
	 */
	public static boolean isInstalled() throws UnsupportedOperationException{
		// as this is a native implementation it is always installed
		return true;
	}
}
