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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.Contradiction;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * This class models a brute force c-reasoner for conditional logic. Reasoning
 * is performed by computing a minimal c-representation for the given knowledge
 * base.<br>
 * 
 * A c-representation for a conditional knowledge base R={r1,...,rn} is a
 * ranking function k such that k accepts every conditional in R (k |= R) and if
 * there are numbers k0,k1+,k1-,...,kn+,kn- with<br>
 * 
 * k(w)=k0 + \sum_{w verifies ri} ki+ + \sum_{w falsifies ri} kj-
 * 
 * for every w. A c-representation is minimal if k0+...+kn- is minimal.<br>
 * 
 * The c-representation is computed using a brute force approach.
 * 
 * <br>
 * <br>
 * See Gabriele Kern-Isberner. Conditionals in nonmonotonic reasoning and belief
 * revision. Lecture Notes in Computer Science, Volume 2087. 2001.
 * 
 * @author Matthias Thimm
 */
@Component(service = Reasoner.class)
public class BruteForceCReasoner implements Reasoner<Conditional, Formula> {

	/** Logger. */
	// static private Logger log =
	// LoggerFactory.getLogger(BruteForceCReasoner.class);

	/**
	 * The c-representation for the given knowledge base. Once this ranking
	 * function has been computed it is used for subsequent queries in order to
	 * avoid unnecessary computations.
	 */
	private RankingFunction crepresentation = null;

	/**
	 * The current vectors of kappa values.
	 */
	private List<Integer[]> kappa;

	/**
	 * The number of conditionals in the given knowledge base.
	 */
	private int numConditionals;

	/**
	 * Maps the indices of the kappa vector to their corresponding conditionals.
	 */
	private Map<Integer, Conditional> indexToConditional;

	/**
	 * indicates whether the computed c-representation is simple.
	 */
	private boolean simple = false;

	// /**
	// * Creates a new c-representation reasoner for the given knowledge base.
	// * @param beliefBase a knowledge base.
	// * @param simple whether the computed c-representation is simple.
	// */
	// public BruteForceCReasoner(BeliefBase beliefBase, boolean simple){
	// super(beliefBase);
	// if(!(beliefBase instanceof ClBeliefSet))
	// throw new IllegalArgumentException("Knowledge base of class ClBeliefSet
	// expected.");
	// this.simple = simple;
	// }

	/**
	 * Creates a new simple c-representation reasoner for the given knowledge
	 * base.
	 * 
	 */
	public BruteForceCReasoner() {
	}

	public BruteForceCReasoner(boolean simple) {
		this.simple = simple;
	}

	/**
	 * Returns the c-representation this reasoner bases on.
	 * 
	 * @return the c-representation this reasoner bases on.
	 */
	public RankingFunction getCRepresentation(BeliefBase<Conditional> beliefBase) {
		if (this.crepresentation == null)
			this.crepresentation = this.computeCRepresentation(beliefBase);
		return this.crepresentation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.kr.Reasoner#query(net.sf.tweety.kr.Formula)
	 */
	@Override
	public Answer query(BeliefBase<Conditional> beliefBase, Formula query) {
		if (!(query instanceof Conditional) && !(query instanceof PropositionalFormula))
			throw new IllegalArgumentException(
					"Reasoning in conditional logic is only defined for conditional and propositional queries.");
		RankingFunction crepresentation = this.getCRepresentation(beliefBase);
		if (query instanceof Conditional) {
			Conditional conditional = (Conditional) query;
			Answer answer = new Answer(beliefBase, conditional);
			boolean bAnswer = crepresentation.satisfies(conditional);
			answer.setAnswer(bAnswer);
			answer.appendText("The answer is: " + bAnswer);
			return answer;
		}
		if (query instanceof PropositionalFormula) {
			int rank = crepresentation.rank(query);
			Answer answer = new Answer(beliefBase, query);
			answer.setAnswer(new Double(rank));
			answer.appendText("The rank of the query is " + rank + " (the query is " + ((rank == 0) ? ("") : ("not "))
					+ "believed)");
			return answer;
		}
		return null;
	}

	/**
	 * Computes a minimal c-representation for this reasoner's knowledge base.
	 * 
	 * @return a minimal c-representation for this reasoner's knowledge base.
	 */
	private RankingFunction computeCRepresentation(BeliefBase<Conditional> beliefBase) {
		ArrayList<PropositionalFormula> list = new ArrayList<PropositionalFormula>();
		BeliefBase<Conditional> beliefset = beliefBase.clone();
		this.filter(list, beliefset);

		this.numConditionals = beliefset.size();
		int i = 0;
		this.indexToConditional = new HashMap<Integer, Conditional>();
		for (Conditional f : beliefset) {
			this.indexToConditional.put(i++, f);
			if (!this.simple)
				this.indexToConditional.put(i++, f);
		}
		Integer[] kappa = null;
		RankingFunction candidate = this.constructRankingFunction(beliefBase, kappa);
		while (!candidate.satisfies(beliefBase)) {
			kappa = this.increment(kappa);
			candidate = this.constructRankingFunction(beliefBase, kappa);
			// String debugMessage = "["+kappa[0];
			// for(int j=1; j< kappa.length;j++)
			// debugMessage += "," + kappa[j];
			// debugMessage += "]";
			// BruteForceCReasoner.log.debug(debugMessage);
		}

		if (list.size() > 0) {
			for (PropositionalFormula pl : list) {
				for (PossibleWorld world : candidate.getPossibleWorlds()) {
					if (world.satisfies(pl)) {
						candidate.setRank(world, RankingFunction.INFINITY);
						System.out.println("set rank INFINITY for : " + world.toString());
					}
				}
			}
		}
		candidate.normalize();
		return candidate;
	}

	private void filter(ArrayList<PropositionalFormula> list, BeliefBase<Conditional> beliefset) {
		BeliefBase<Conditional> copy = beliefset.clone();
		for (Conditional c : copy) {
			if (c.getConclusion() instanceof Contradiction) {
				System.out.println("list add: " + c.getPremise().toString() + " remove: " + c.toString());
				list.addAll(c.getPremise());
				beliefset.remove(c);
				for (int i = 0; i < c.getPremise().toArray().length; i++) {
					Conditional c1 = new Conditional((PropositionalFormula) c.getPremise().toArray()[i]);
					System.out.println("Add to beliefset: " + c1.toString());
					beliefset.add(c1);
				}
			}
		}
	}

	/**
	 * Constructs a ranking function with the given kappa values
	 * [k1+,k1-,...,kn+,kn-], i.e. for every possible world w set<br>
	 * k(w)=k0 + \sum_{w verifies ri} ki+ + \sum_{w falsifies ri} kj-
	 * 
	 * @param kappa
	 */
	private RankingFunction constructRankingFunction(BeliefBase<Conditional> beliefBase, Integer[] kappa) {
		RankingFunction candidate = new RankingFunction(
				(PropositionalSignature) beliefBase.getSignature());
		if (kappa == null)
			return candidate;
		for (PossibleWorld w : candidate.getPossibleWorlds()) {
			int sum = 0;
			if (this.simple) {
				for (int idx = 0; idx < kappa.length; idx++) {
					if (RankingFunction.falsifies(w, this.indexToConditional.get(idx))) {
						sum += kappa[idx];
					}
				}
			} else {
				for (int idx = 0; idx < kappa.length; idx += 2) {
					if (RankingFunction.verifies(w, this.indexToConditional.get(idx))) {
						sum += kappa[idx];
					}
				}
				for (int idx = 1; idx < kappa.length; idx += 2) {
					if (RankingFunction.falsifies(w, this.indexToConditional.get(idx))) {
						sum += kappa[idx];
					}
				}
			}
			candidate.setRank(w, sum);
		}
		return candidate;
	}

	/**
	 * This method increments the given array by one value.
	 * 
	 * @param kappa
	 *            an array of integers.
	 * @return an array of integers.
	 */
	private Integer[] increment(Integer[] kappa) {
		if (this.kappa == null) {
			Integer[] first;
			if (this.simple)
				first = new Integer[this.numConditionals];
			else
				first = new Integer[2 * this.numConditionals];
			first[0] = 1;
			for (int i = 1; i < first.length; i++)
				first[i] = 0;
			this.kappa = new ArrayList<Integer[]>();
			this.kappa.add(first);
		} else {
			boolean overflow = true;
			int j = 0;
			while (overflow && j < this.kappa.size()) {
				overflow = this.incrementStep(this.kappa.get(j));
				j++;
			}
			if (overflow) {
				// add new vector
				Integer[] newVec;
				if (this.simple)
					newVec = new Integer[this.numConditionals];
				else
					newVec = new Integer[2 * this.numConditionals];
				newVec[0] = 1;
				for (int i = 1; i < newVec.length; i++)
					newVec[i] = 0;
				this.kappa.add(newVec);
			}
		}
		// compute the actual kappa values
		Integer[] newKappa;
		if (this.simple)
			newKappa = new Integer[this.numConditionals];
		else
			newKappa = new Integer[2 * this.numConditionals];
		for (int i = 0; i < newKappa.length; i++) {
			newKappa[i] = 0;
			for (Integer[] v : this.kappa)
				newKappa[i] += v[i];
		}
		return newKappa;
	}

	/**
	 * This method increments the given vector (which composes of exactly one
	 * "1" entry and zeros otherwise), e.g. [0,0,1,0] -> [0,0,0,1] and [0,0,0,1]
	 * -> [1,0,0,0]
	 * 
	 * @param kappaRow
	 *            a vector of zeros and one "1"
	 * @return "true" if there is an overflow, i.e. when [0,0,0,1] -> [1,0,0,0],
	 *         otherwise "false".
	 */
	private boolean incrementStep(Integer[] kappaRow) {
		int length = kappaRow.length;
		if (kappaRow[length - 1] == 1) {
			kappaRow[length - 1] = 0;
			kappaRow[0] = 1;
			return true;
		} else {
			for (int i = 0; i < length - 1; i++) {
				if (kappaRow[i] == 1) {
					kappaRow[i] = 0;
					kappaRow[i + 1] = 1;
					return false;
				}
			}
		}
		throw new IllegalArgumentException("Argument must contain at least one value \"1\"");
	}
}
