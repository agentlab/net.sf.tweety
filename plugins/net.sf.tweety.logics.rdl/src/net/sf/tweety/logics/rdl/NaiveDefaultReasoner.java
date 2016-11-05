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
package net.sf.tweety.logics.rdl;

import java.util.Collection;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.prover.FolTheoremProver;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.rdl.semantics.DefaultProcessTree;

/**
 * Implements a naive reasoner for default logic based on exhaustive application
 * of defaults in process trees.
 * 
 * @author Matthias Thimm, Nils Geilen
 */
@Component(service = Reasoner.class)
public class NaiveDefaultReasoner implements Reasoner<FolFormula, FolFormula> {

//	 DefaultProcessTree tree;
	//
	// public NaiveDefaultReasoner(BeliefBase beliefBase) {
	// super(beliefBase);
	// if (!(beliefBase instanceof DefaultTheory))
	// throw new IllegalArgumentException("BeliefBase has to be a
	// DefaultTheory");
	// tree = new DefaultProcessTree((DefaultTheory) beliefBase);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.commons.Reasoner#query(net.sf.tweety.commons.Formula)
	 */
	@Override
	public Answer query(BeliefBase<FolFormula> beliefBase, FolFormula query) {
		if (!query.isGround())
			throw new IllegalArgumentException("Query is not grounded.");
		if (!(beliefBase instanceof DefaultTheory)) {
			throw new IllegalArgumentException("BeliefBase has to be a DefaultTheory");
		}
		Answer answer = new Answer(beliefBase, query);
		answer.setAnswer(false);
		for (Collection<FolFormula> extension : getAllExtensions(beliefBase)) {
			FolBeliefSet fbs = (FolBeliefSet) extension;
			FolTheoremProver prover = FolTheoremProver.getDefaultProver();
			if (prover.query(fbs, (FolFormula) query).getAnswerBoolean()) {
				answer.setAnswer(true);
				break;
			}
		}
		return answer;
	}

	 /**
	 * @return all extensions of the default theory
	 */
	 public Collection<Collection<FolFormula>> getAllExtensions(BeliefBase<FolFormula> beliefBase) {
		 DefaultProcessTree tree = new DefaultProcessTree((DefaultTheory) beliefBase);
		 return tree.getExtensions();
	 }

}
