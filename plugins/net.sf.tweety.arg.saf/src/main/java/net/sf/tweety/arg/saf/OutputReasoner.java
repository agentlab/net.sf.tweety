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
package net.sf.tweety.arg.saf;

import java.util.HashSet;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.arg.dung.AbstractExtensionReasoner;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.saf.syntax.ArgumentStructure;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.pl.syntax.Proposition;


/**
 * This class models an abstract reasoner for structured argumentation frameworks. Given a specific
 * semantics "Sem" for Dung theories, inferences drawn using this reasoner bases on a set "output" of
 * propositions defined by:<br> 
 * Output = { a |(forall i there is an AS in E_i: claim(AS)=A)}<br>
 * where E_1,...,E_n are the extensions of the induced Dung theory wrt. semantics "Sem".
 * 
 * @author Matthias Thimm
 */
@Component(service = Reasoner.class)
public class OutputReasoner implements Reasoner<Argument, Proposition> {

	/**
	 * The output of this reasoner.
	 */
	private Set<Proposition> output;
	
	/**
	 * The reasoner used for computing the extensions of the induced Dung theory.
	 */
	private AbstractExtensionReasoner reasoner;
	
	public OutputReasoner() {
		super();
	}

	/**
	 * Creates a new reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 */
	public OutputReasoner(AbstractExtensionReasoner reasoner) {
		super();
//		if(!(beliefBase instanceof StructuredArgumentationFramework))
//			throw new IllegalArgumentException("Knowledge base of class StructuredArgumentationFramework expected.");
		//instantiate new reasoner
//		Class<?>[] parameterTypes = new Class[1];
//		parameterTypes[0] = BeliefBase.class;		
//		Object[] parameters = new Object[1];
//		parameters[0] = ((StructuredArgumentationFramework)beliefBase).toDungTheory();
//		try{
			this.reasoner = reasoner;//(AbstractExtensionReasoner) reasonerClass.getConstructor(parameterTypes).newInstance(parameters);
//		}catch(Exception e){
//			throw new IllegalArgumentException("Reasoner class is not valid.");
//		}			
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Reasoner#query(net.sf.tweety.kr.Formula)
	 */
	@Override
	public Answer query(BeliefBase<Argument> beliefBase, Proposition query) {		
//		if(!(query instanceof Proposition))
//			throw new IllegalArgumentException("Reasoning in structured argumentation frameworls is only defined for propositional queries.");
		Answer answer = new Answer(beliefBase, query);
		boolean bAnswer = this.getOutput(beliefBase).contains(query);
		answer.setAnswer(bAnswer);
		answer.appendText("The answer is: " + bAnswer);
		return answer;
	}
	
	/**
	 * Returns the output this reasoner bases upon.
	 * @return the output this reasoner bases upon.
	 */
	public Set<Proposition> getOutput(BeliefBase<Argument> beliefBase){
		if(this.output == null){
			Set<Extension> extensions = this.reasoner.getExtensions(beliefBase);			
			this.output = new HashSet<Proposition>();			
			for(Proposition p: ((StructuredArgumentationFramework)beliefBase).getSignature()){
				boolean isOutput = true;
				for(Extension e: extensions){
					boolean isInExtension = false;
					for(Argument a: e){
						ArgumentStructure arg = (ArgumentStructure) a; 
						if(arg.getClaim().equals(p)){
							isInExtension = true;
							break;
						}
					}
					if(!isInExtension){
						isOutput = false;
						break;
					}
				}
				if(isOutput) this.output.add(p);
			}
		}
		return this.output;
	}

}
