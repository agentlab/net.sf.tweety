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
package net.sf.tweety.beliefdynamics.selectiverevision.argumentative;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.arg.deductive.CompilationReasoner;
import net.sf.tweety.arg.deductive.DeductiveKnowledgeBase;
import net.sf.tweety.arg.deductive.accumulator.Accumulator;
import net.sf.tweety.arg.deductive.categorizer.Categorizer;
import net.sf.tweety.beliefdynamics.selectiverevision.MultipleTransformationFunction;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/**
 * This class implements the argumentative transformation functions proposed in [Kruempelmann:2011].
 * 
 * @author Matthias Thimm
 */
public class ArgumentativeTransformationFunction implements MultipleTransformationFunction<PropositionalFormula> {

	/**
	 * The categorizer used by this transformation function.
	 */
	private Categorizer categorizer;
	
	/**
	 * The accumulator used by this transformation function.
	 */
	private Accumulator accumulator;
	
	/**
	 * Whether this transformation function is skeptical.
	 */
	private boolean isSkeptical;
	
	/**
	 * The belief set used by this transformation function.
	 */
	private PlBeliefSet beliefSet;
	
	/**
	 * Creates a new argumentative transformation function.
	 * @param categorizer The categorizer used by this transformation function.
	 * @param accumulator The accumulator used by this transformation function.
	 * @param beliefSet The belief set used by this transformation function.
	 * @param isSkeptical Whether this transformation function is skeptical.
	 */
	public ArgumentativeTransformationFunction(Categorizer categorizer, Accumulator accumulator, PlBeliefSet beliefSet, boolean isSkeptical){
		this.categorizer = categorizer;
		this.accumulator = accumulator;
		this.beliefSet = beliefSet;
		this.isSkeptical = isSkeptical;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.selectiverevision.MultipleTransformationFunction#transform(java.util.Collection)
	 */
	@Override
	public Collection<PropositionalFormula> transform(Collection<PropositionalFormula> formulas) {
		Collection<PropositionalFormula> transformedSet = new HashSet<PropositionalFormula>();
		DeductiveKnowledgeBase joinedBeliefSet = new DeductiveKnowledgeBase(this.beliefSet);
		joinedBeliefSet.addAll(formulas);
		CompilationReasoner reasoner = new CompilationReasoner(this.categorizer, this.accumulator);
		for(PropositionalFormula f: formulas){
			Double result = reasoner.query(joinedBeliefSet, f).getAnswerDouble();
			if(this.isSkeptical){
				if(result > 0)
					transformedSet.add(f);
			}else if(result >= 0)
				transformedSet.add(f);			
		}		
		return transformedSet;
	}	
}
