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
package net.sf.tweety.logics.pl.semantics;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import net.sf.tweety.commons.Interpretation;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Contradiction;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.Tautology;

/**
 * A three-valued interpretation for propositional logic from Priest's three
 * valued logic (3VL) [Priest, G.: Logic of paradox. Journal of Philosophical
 * Logic 8, 219-241 (1979)].
 * 
 * Every proposition gets one of three truth values: true, false, both.
 * 
 * @author Matthias Thimm
 */
public class PriestWorld implements Interpretation<PropositionalFormula> {

	/** The three truth values. */
	public enum TruthValue {
		TRUE {
			@Override
			public boolean getClassical() {
				return true;
			}

			@Override
			public TruthValue neg() {
				return FALSE;
			}

			@Override
			public TruthValue and(TruthValue v) {
				return v;
			}

			@Override
			public TruthValue or(TruthValue v) {
				return v == BOTH ? BOTH : TRUE;
			}

			@Override
			public String toString() {
				return "T";
			}
		},

		FALSE {
			@Override
			public boolean getClassical() {
				return false;
			}

			@Override
			public TruthValue neg() {
				return TRUE;
			}

			@Override
			public TruthValue and(TruthValue v) {
				return v == BOTH ? BOTH : FALSE;
			}

			@Override
			public TruthValue or(TruthValue v) {
				return v;
			}

			@Override
			public String toString() {
				return "F";
			}
		},

		BOTH {
			@Override
			public boolean getClassical() {
				return true;
			}

			@Override
			public TruthValue neg() {
				return BOTH;
			}

			@Override
			public TruthValue and(TruthValue v) {
				return BOTH;
			}

			@Override
			public TruthValue or(TruthValue v) {
				return BOTH;
			}

			@Override
			public String toString() {
				return "B";
			}
		};

		public abstract boolean getClassical();

		public abstract TruthValue neg();

		public abstract TruthValue and(TruthValue v);

		public abstract TruthValue or(TruthValue v);

	};

	/** The truth values of the propositions. */
	private final Map<Proposition, TruthValue> values = new HashMap<Proposition, TruthValue>();

	// /**
	// * Creates a new world where all propositions get the truth value FALSE.
	// */
	// public PriestWorld() {
	// this.values = new HashMap<Proposition, TruthValue>();
	// }

	/**
	 * Sets the value of the given proposition.
	 * 
	 * @param p
	 *            some proposition.
	 * @param val
	 *            some truth value.
	 */
	public void set(Proposition p, TruthValue val) {
		this.values.put(p, val);
	}

	/**
	 * Returns the truth value of the given proposition.
	 * 
	 * @param p
	 *            a proposition
	 * @return a truth value.
	 */
	public TruthValue get(Proposition p) {
		if (!this.values.containsKey(p))
			return TruthValue.FALSE;
		return this.values.get(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.Formula)
	 */
	@Override
	public boolean satisfies(PropositionalFormula formula) {
		return this.satisfies3VL(formula).getClassical();
	}

	/**
	 * Determines the 3-valued truth value of the given formula.
	 * 
	 * @param formula
	 *            some formula
	 * @return the 3-valued truth value of the formula.
	 * @throws IllegalArgumentException
	 */
	public TruthValue satisfies3VL(PropositionalFormula formula) {
		// if(!(formula instanceof PropositionalFormula))
		// throw new IllegalArgumentException("Formula " + formula + " is not a
		// propositional formula.");
		if (formula instanceof Contradiction)
			return TruthValue.FALSE;
		if (formula instanceof Tautology)
			return TruthValue.TRUE;
		if (formula instanceof Proposition)
			return this.get((Proposition) formula);
		if (formula instanceof Negation)
			return this.satisfies3VL(((Negation) formula).getFormula()).neg();
		if (formula instanceof Conjunction) {
			Conjunction c = (Conjunction) formula;
			TruthValue val = TruthValue.TRUE;
			for (PropositionalFormula f : c)
				val = val.and(this.satisfies3VL(f));
			return val;
		}
		if (formula instanceof Disjunction) {
			Disjunction d = (Disjunction) formula;
			TruthValue val = TruthValue.FALSE;
			for (PropositionalFormula f : d)
				val = val.or(this.satisfies3VL(f));
			return val;
		}
		throw new IllegalArgumentException("Propositional formula " + formula + " is of unknown type.");
	}

	/**
	 * Returns the binary base of this world, i.e. the set of all propositions
	 * which are assigned either to TRUE or FALSE.
	 * 
	 * @return the binary base of this world.
	 */
	public Collection<Proposition> getBinarybase() {
		Collection<Proposition> binarybase = new HashSet<Proposition>();
		for (Proposition p : this.values.keySet())
			if (!this.values.get(p).equals(TruthValue.BOTH))
				binarybase.add(p);
		return binarybase;
	}

	/**
	 * Returns the conflict base of this world, i.e. the set of all propositions
	 * which are assigned to BOTH.
	 * 
	 * @return the conflict base of this world.
	 */
	public Collection<Proposition> getConflictbase() {
		Collection<Proposition> cbase = new HashSet<Proposition>();
		for (Proposition p : this.values.keySet())
			if (this.values.get(p).equals(TruthValue.BOTH))
				cbase.add(p);
		return cbase;
	}

	/**
	 * Returns the conflict base of this world, i.e. the set of all propositions
	 * which are assigned to BOTH.
	 * 
	 * @return the conflict base of this world.
	 */
	public Stream<Proposition> getConflictStream() {
		// Collection<Proposition> cbase = new HashSet<Proposition>();
		return values.entrySet().stream().filter(entry -> entry.getValue() == TruthValue.BOTH).map(Entry::getKey);
		// for (Proposition p : this.values.keySet())
		// if (this.values.get(p).equals(TruthValue.BOTH))
		// cbase.add(p);
		// return cbase;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.BeliefBase)
	// */
	// @Override
	// public boolean satisfies(BeliefBase<PropositionalFormula> beliefBase)
	// throws IllegalArgumentException {
	// if (!(beliefBase instanceof PlBeliefSet))
	// throw new IllegalArgumentException("Propositional knowledge base
	// expected.");
	// PlBeliefSet pKb = (PlBeliefSet) beliefBase;
	// for (Formula f : pKb)
	// if (!this.satisfies(f))
	// return false;
	// return true;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.values.toString();
	}
}
