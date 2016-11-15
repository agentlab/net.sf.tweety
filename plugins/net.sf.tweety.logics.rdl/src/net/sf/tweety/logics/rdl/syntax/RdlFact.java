package net.sf.tweety.logics.rdl.syntax;

import net.sf.tweety.logics.fol.syntax.FolFormula;

public interface RdlFact extends RdlFormula {

	FolFormula getFormula();

}