package net.sf.tweety.logics.rdl;

import java.util.Collection;

import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * Implements a naive reasoner for default logic based on exhaustive 
 * application of defaults in process trees.
 * 
 * @author Matthias Thimm
 */
public class NaiveDefaultReasoner extends Reasoner{

	public NaiveDefaultReasoner(BeliefBase beliefBase) {
		super(beliefBase);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Answer query(Formula query) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Collection<FolFormula>> getAllExtensions(){
		//TODO implement me
		return null;
	}
	
}
