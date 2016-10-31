package net.sf.tweety.arg.aba;

import java.util.ArrayList;
import java.util.Collection;

import net.sf.tweety.arg.aba.syntax.InferenceRule;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;

public class ABATheory <T extends Invertable> implements BeliefBase {
	
	private Collection<InferenceRule<T>> rules = new ArrayList<>();

	public ABATheory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Signature getSignature() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DungTheory asDungTheory(){
		return null;
	}

	@Override
	public Collection getFormulas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(Formula formula) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Formula formula) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
