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
package net.sf.tweety.logics.pl;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.EntailmentRelation;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/**
 * This class implements the classical inference operator. A query, i.e. a
 * formula in propositional logic can be inferred by a knowledge base, iff every
 * model of the knowledge base is also a model of the query.
 * 
 * @author Matthias Thimm
 *
 */
@Component(service = Reasoner.class)
public class ClassicalInference implements Reasoner<PropositionalFormula, PropositionalFormula> {

	/** The actual reasoning mechanism. */
	private EntailmentRelation<PropositionalFormula> entailment;

	public ClassicalInference() {
		super();
	}

	public ClassicalInference(EntailmentRelation<PropositionalFormula> entailment) {
		// super(beliefBase);
		this.entailment = entailment;
		// if(!(beliefBase instanceof PlBeliefSet))
		// throw new IllegalArgumentException("Classical inference is only
		// defined for propositional knowledgebases.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.kr.Reasoner#query(net.sf.tweety.kr.Formula)
	 */
	@Override
	public Answer query(BeliefBase<PropositionalFormula> beliefBase, PropositionalFormula query) {
		Answer answer = new Answer(beliefBase, query);
		if (this.entailment.entails(beliefBase, query)) {
			answer.setAnswer(true);
			answer.appendText("The answer is: true");
		} else {
			answer.setAnswer(false);
			answer.appendText("The answer is: false");
		}
		return answer;
	}
}
