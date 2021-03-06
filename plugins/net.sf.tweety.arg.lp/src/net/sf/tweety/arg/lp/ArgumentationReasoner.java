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
package net.sf.tweety.arg.lp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.arg.lp.semantics.AttackRelation;
import net.sf.tweety.arg.lp.semantics.attack.AttackStrategy;
import net.sf.tweety.arg.lp.syntax.Argument;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;

/**
 * This class models a reasoner for extended logic programming based arguments using
 * the fixpoint semantics from [1] parameterised by a notion of attack x for the opponent
 * and another notion of attack y as a defense for the proponent. This base implementation
 * only allows to query whether an argument A is x/y-justified in a ELP P.
 * A is called x/y-acceptable wrt. a set of arguments S if for every argument B in P such 
 * that (B,A) \in x there exists an argument C \in S such that (C,B) \in y. 
 * The set of acceptable arguments for P is defined as the fixpoint J_{P,x/y} of the function
 *  F_{P,x/y}(S) = { A | A is x/y-acceptable with regard to S}
 *  
 *  In [1] it is shown that J_{a/u} equals Dung's grounded semantics, J_{u/su} equals the
 *  well founded semantics for normal logic programs and J_{u/a} equals the well-founded
 *  semantics for logic programs with explicit negation.
 *  
 * 
 * [1] Ralf Schweimeier and Michael Schroeder: A Parameterised Hierarchy of 
 * Argumentation Semantics for Extended Logic Programming and its 
 * Application to the Well-founded Semantics. 
 * In: Theory and Practice of Logic Programming, 5(1-2):207-242, 2003.
 * 
 * @author Sebastian Homann
 */
@Component(service = Reasoner.class)
public class ArgumentationReasoner implements Reasoner<Argument, Formula> {
	private AttackRelation attack;
	private AttackRelation defense;
	
	private AttackStrategy attackStrategy;
	private AttackStrategy defenceStrategy;
	
	public ArgumentationReasoner() {
		super();
	}

	/**
	 * Creates a new ArgumentationReasoner for the given belief base and parameterised
	 * by a notion of attack for the opponent and another notion of attack for the defense
	 * @param attackStrategy
	 * @param defenceStrategy
	 */
	public ArgumentationReasoner(AttackStrategy attackStrategy, AttackStrategy defenceStrategy) {
		super();
		this.attackStrategy = attackStrategy;
		this.defenceStrategy = defenceStrategy;
//		if(!(beliefBase instanceof ArgumentationKnowledgeBase)) {
//			throw new IllegalArgumentException("Knowledge base of class ArgumentationKnowledgebase expected.");
//		}
//		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) beliefBase;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.Reasoner#query(net.sf.tweety.Formula)
	 */
	@Override
	public Answer query(BeliefBase<Argument> beliefBase, Formula query) {
		if(! (query instanceof Argument) ) {
			throw new IllegalArgumentException("Formula of class Argument expected.");
		}
		Argument a = (Argument) query;
		this.attack = new AttackRelation(beliefBase, attackStrategy);
		this.defense = new AttackRelation(beliefBase, defenceStrategy);
		Answer answer = new Answer(beliefBase, query);
		answer.setAnswer(getJustifiedArguments(beliefBase).contains(a));
		return answer;
	}
	
	/**
	 * An argument is called x/y-overruled, if it is attacked by an 
	 * x/y-justified argument.
	 * 
	 * @param arg an argument
	 * @return true iff arg is x-attacked by an x/y-justified argument
	 */
	public boolean isOverruled(BeliefBase<Argument> beliefBase, Argument arg) {
		for(Argument attacker : getJustifiedArguments(beliefBase)) {
			if(attack.attacks(attacker, arg)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * An argument is called x/y-justified if it is contained in J_{P,x/y}.
	 * See class description for details.
	 * 
	 * @param arg an argument
	 * @return true iff arg is x/y-justified
	 */
	public boolean isJustified(BeliefBase<Argument> beliefBase, Argument arg) {
		return query(beliefBase, arg).getAnswerBoolean();
	}
	
	/**
	 * An argument is called x/y-defensible if it is neither x/y-justified
	 * nor x/y-overruled.
	 * @param arg an argument
	 * @return true iff arg is neither x/y-justified nor x/y-overruled.
	 */
	public boolean isDefensible(BeliefBase<Argument> beliefBase, Argument arg) {
		return (! isOverruled(beliefBase, arg) ) && (! isJustified(beliefBase, arg) );
	}
	
	/**
	 * Returns the set of x/y-justified arguments using a bottom-up fixpoint calculation
	 * @return the set of x/y-justified arguments
	 */
	public Set<Argument> getJustifiedArguments(BeliefBase<Argument> kb) {
//		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) beliefBase;
		Collection<Argument> arguments = kb;
		Set<Argument> result = new HashSet<Argument>();
		
		// fixpoint calculation: add defended arguments until nothing changes
		boolean changes = true;
		while(changes) {
			changes = false;
			for(Argument arg : arguments) {
				if(isAcceptable(arguments, result, arg)) {
					if(!result.contains(arg)) {
						result.add(arg);
						changes = true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of overruled arguments, i.e. the set of arguments,
	 * which are attacked by a justified argument.
	 * @return the set of overruled arguments.
	 */
	public Set<Argument> getOverruledArguments(BeliefBase<Argument> kb) {
//		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) this.getKnowledgeBase();
		Collection<Argument> arguments = kb;
		Set<Argument> result = new HashSet<Argument>();
		Set<Argument> justifiedArguments = getJustifiedArguments(kb);
		for(Argument candidate : arguments) {
			for(Argument justified : justifiedArguments) {
				if(attack.attacks(justified, candidate)) {
					result.add(candidate);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of defensible arguments, i.e. the set of arguments,
	 * that are neither justified nor overruled.
	 * @return the set of defensible arguments.
	 */
	public Set<Argument> getDefensibleArguments(BeliefBase<Argument> kb) {
//		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) this.getKnowledgeBase();
		Set<Argument> result = new HashSet<Argument>();
		Collection<Argument> arguments = kb;
		Set<Argument> justifiedArguments = getJustifiedArguments(kb);
		Set<Argument> overruledArguments = getOverruledArguments(kb);
		for(Argument candidate : arguments) {
			if(!justifiedArguments.contains(candidate)) {
				if(!overruledArguments.contains(candidate)) {
					result.add(candidate);
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns true iff the argument toCheck is x/y-acceptable wrt. the set of defendingArguments.
	 * A is called x/y-acceptable wrt. a set of arguments S if for every argument B in P such 
     * that (B,A) \in x there exists an argument C \in S such that (C,B) \in y.
     *  
	 * @param arguments
	 * @param defendingArguments
	 * @param toCheck
	 * @return true iff toCheck is x/y-acceptable wrt. defendingArguments
	 */
	private boolean isAcceptable(Collection<Argument> arguments, Set<Argument> defendingArguments, Argument toCheck) {
		Set<Argument> attackingArguments = attack.getAttackingArguments(toCheck);
		for(Argument attackingArgument : attackingArguments) {
			// check if  there is an argument in defendingArguments, that defends against the attacker
			if(! defense.attacks(defendingArguments, attackingArgument)) {
				// no defense against attacker
				return false;
			}
		}
		return true;
	}

}
