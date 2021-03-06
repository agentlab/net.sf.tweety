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
package net.sf.tweety.logics.pl.syntax;

import java.util.Set;

import net.sf.tweety.logics.commons.LogicalSymbols;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;

/**
 * A tautological formula.
 * @author Matthias Thimm
 */
public class Tautology extends SpecialFormula {

	/**
	 * Creates a new tautology.
	 */
	public Tautology() {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return LogicalSymbols.TAUTOLOGY();
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Tautology;
	}

	@Override
	public int hashCode() {
		return 13;
	}

	@Override
	public Tautology clone() {
		return new Tautology();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.pl.syntax.PropositionalFormula#getModels(net.sf.tweety.logics.pl.syntax.PropositionalSignature)
	 */
	@Override
	public Set<PossibleWorld> getModels(PropositionalSignature sig) {
		return PossibleWorld.getAllPossibleWorlds(sig);
	}
}
