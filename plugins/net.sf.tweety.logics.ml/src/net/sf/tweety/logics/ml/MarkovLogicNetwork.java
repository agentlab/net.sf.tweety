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
package net.sf.tweety.logics.ml;

import java.io.Serializable;
import java.util.Collection;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.fol.syntax.FolSignature;
import net.sf.tweety.logics.ml.syntax.MlnFormula;

/**
 * Instances of this class represent Markov Logic Networks [Domingos et. al.].
 * 
 * @author Matthias Thimm
 */
@Component(service = BeliefBase.class)
public class MarkovLogicNetwork extends BeliefSet<MlnFormula> implements Serializable {

	private static final long serialVersionUID = 3313039501304912746L;

	/**
	 * Creates a new (empty) MLN.
	 */
	public MarkovLogicNetwork(){
		super();
	}
	
	/**
	 * Creates a new conditional MLN with the given collection of
	 * MLN formulas.
	 * @param formulas a collection of MLN formulas.
	 */
	public MarkovLogicNetwork(Collection<? extends MlnFormula> formulas){
		super(formulas);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefSet#getSignature()
	 */
	@Override
	public Signature getSignature() {
		FolSignature sig = new FolSignature();
		for(MlnFormula formula: formulas){
			sig.addAll(formula.getPredicates());
			sig.addAll(formula.getTerms(Constant.class));
			sig.addAll(formula.getFunctors());
		}
		return sig;
	}

}
