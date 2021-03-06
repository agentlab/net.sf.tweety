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
package net.sf.tweety.logics.fol.lang;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Language;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

/**
 * This class models a first-order language for a given signature.
 * @author Matthias Thimm
 */
public class FolLanguage extends Language {
	
	/**
	 * Creates a new language on the given first-order signature.
	 * @param signature a first-order signature.
	 */
	public FolLanguage(Signature signature){
		super(signature);
		if(!(signature instanceof FolSignature))
			throw new IllegalArgumentException("Signatures for first-order languages must be first-order signatures.");
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Language#isRepresentable(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean isRepresentable(Formula formula){
		if(!(formula instanceof FolFormula)) return false;
		FolSignature folSignature = (FolSignature) this.getSignature();
		FolFormula folFormula = (FolFormula) formula;
		//NOTE: for a full first-order language its just necessary
		// for a formula to be representable by the folSignature and to be well-formed.
		return folSignature.isRepresentable(folFormula) && folFormula.isWellFormed();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Language#isRepresentable(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean isRepresentable(BeliefBase<?> beliefBase){
		if(!(beliefBase instanceof FolBeliefSet)) return false;
		for(Formula f : beliefBase)
			if(!this.isRepresentable(f))
				return false;
		return true;
	}
	

}
