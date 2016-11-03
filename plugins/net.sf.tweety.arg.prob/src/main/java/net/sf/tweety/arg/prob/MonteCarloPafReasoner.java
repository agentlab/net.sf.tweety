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
package net.sf.tweety.arg.prob;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.arg.dung.AbstractExtensionReasoner;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.math.probability.Probability;

/**
 * This class implements the Monte Carlo algorithm for estimating
 * probabilities of extensions in probabilistic argumentation frameworks
 * from [Li, Oren, Norman. Probabilistic Argumentation Frameworks. TAFA'2011].
 * 
 * @author Matthias Thimm
 */
@Component(service = Reasoner.class)
public class MonteCarloPafReasoner implements Reasoner<Argument, Argument> {

	/** Semantics for plain AAFs. */
	private int semantics;
	/** The number of runs of the Monte Carlo simulation. */
	private int numberOfTrials;
	/** The inference type used for estimating acceptability probability
	 * 	of single arguments (credulous or skeptical inference).*/
	private int inferenceType;
	
	public MonteCarloPafReasoner() {
		super();
	}

	/**
	 * Creates a new reasoner for the given framework
	 * @param aaf some probabilistic argumentation framework
	 * @param semantics semantics used for determining extensions.
	 * @param numberOfTrials The number of runs of the Monte Carlo simulation
	 * @param inferenceType The inference type used for estimating acceptability probability
	 * 	of single arguments (credulous or skeptical inference).
	 */
	public MonteCarloPafReasoner(int semantics, int inferenceType, int numberOfTrials) {
		super();
		this.semantics = semantics;
		this.numberOfTrials = numberOfTrials;
		this.inferenceType = inferenceType;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.commons.Reasoner#query(net.sf.tweety.commons.Formula)
	 */
	@Override
	public Answer query(BeliefBase<Argument> beliefBase, Argument query) {
//		if(!(query instanceof Argument))
//			throw new IllegalArgumentException("Formula of class argument expected");
//		Argument arg = (Argument) query;
		int count = 0;
		for(int i = 0; i < this.numberOfTrials; i++){
			DungTheory sub = ((ProbabilisticArgumentationFramework) beliefBase).sample();
			AbstractExtensionReasoner r = AbstractExtensionReasoner.getReasonerForSemantics(this.semantics, this.inferenceType);
			if(r.query(sub, query).getAnswerBoolean())
				count++;
		}
		Answer ans = new Answer(beliefBase, query);
		ans.setAnswer(new Double(count)/this.numberOfTrials);
		return ans;
	}

	/**
	 * Estimates the probability that the given set of
	 * arguments is an extension
	 * @param ext some set of arguments
	 * @return the estimated probability of the given set to be 
	 * an extension
	 */
	public Probability query(BeliefBase<Argument> beliefBase, Extension ext){
		int count = 0;
		for(int i = 0; i < this.numberOfTrials; i++){
			DungTheory sub = ((ProbabilisticArgumentationFramework) beliefBase).sample();
			AbstractExtensionReasoner r = AbstractExtensionReasoner.getReasonerForSemantics(this.semantics, this.inferenceType);
			if(r.getExtensions(sub).contains(ext))
				count++;
		}
		return new Probability(new Double(count)/this.numberOfTrials);
	}
	
}
