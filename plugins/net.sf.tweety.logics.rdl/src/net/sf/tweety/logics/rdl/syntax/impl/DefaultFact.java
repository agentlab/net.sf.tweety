package net.sf.tweety.logics.rdl.syntax.impl;

import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.rdl.syntax.RdlFact;

public class DefaultFact implements RdlFact {
	
	FolFormula formula;

	public DefaultFact(FolFormula formula) {
		this.formula = formula;
	}

	@Override
	public Signature getSignature() {
		return formula.getSignature();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.rdl.syntax.RdlFact#getFormula()
	 */
	@Override
	public FolFormula getFormula() {
		return formula;
	}

}
