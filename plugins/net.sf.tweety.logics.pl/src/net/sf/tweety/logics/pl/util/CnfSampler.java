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
package net.sf.tweety.logics.pl.util;

import java.util.Random;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBaseSampler;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * A simple sampler for propositional belief bases. This sampler
 * always generates belief bases in CNF, i.e. every formula
 * appearing in the belief base is a disjunction of literals.
 * 
 * @author Matthias Thimm
 */
@Component(service = BeliefBaseSampler.class)
public class CnfSampler extends BeliefBaseSampler<PlBeliefSet>{

	/** The maximum ratio of variables appearing in a single formula. */
	private double maxVariableRatio;
	
	public CnfSampler() {
		super();
	}

	/**
	 * Creates a new sampler for the given signature.
	 * @param signature
	 * @param maxVariableRatio the maximum ratio (a value between 0 and 1) of variables
	 * of the signature appearing in some formula.
	 */
	public CnfSampler(Signature signature, double maxVariableRatio) {
		super(signature);
		if(!(signature instanceof PropositionalSignature))
			throw new IllegalArgumentException("Signature of type \"PropositionalSignature\" expected. ");
		this.maxVariableRatio = maxVariableRatio;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseSampler#randomSample(int, int)
	 */
	@Override
	public PlBeliefSet randomSample(int minLength, int maxLength) {
		PlBeliefSet beliefSet = new PlBeliefSet();
		Random rand = new Random();
		int length;
		if(maxLength - minLength > 0)
			length = minLength + rand.nextInt(maxLength - minLength);
		else length = minLength;
		while(beliefSet.size() < length){
			beliefSet.add(this.randomFormula());
		}
		return beliefSet;
	}
	
	/**
	 * Samples a random formula (a disjunction of literals).
	 * @return a random formula (a disjunction of literals).
	 */
	public PropositionalFormula randomFormula(){
		PropositionalSignature sig = (PropositionalSignature)this.getSignature();
		Disjunction d = new Disjunction();		
		Random rand = new Random();
		for(Proposition p: sig){
			if(rand.nextDouble() <= this.maxVariableRatio){
				if(rand.nextBoolean())
					d.add(p);
				else d.add(new Negation(p));
			}
			if(d.size()+1 > this.maxVariableRatio * sig.size())
				break;
		}
		// at least one literal should be added
		if(d.isEmpty()){
			if(rand.nextBoolean())
				d.add((Proposition)sig.toArray()[rand.nextInt(sig.size())]);
			else
				d.add(new Negation((Proposition)sig.toArray()[rand.nextInt(sig.size())]));
		}
		return d;
	}

}
