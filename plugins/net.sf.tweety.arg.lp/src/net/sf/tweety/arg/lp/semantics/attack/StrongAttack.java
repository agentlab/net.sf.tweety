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
package net.sf.tweety.arg.lp.semantics.attack;

import net.sf.tweety.arg.lp.syntax.Argument;


/**
 * This notion of attack models the strong attack relation. 
 * A strongly attacks B iff 
 *   (1) A attacks B and 
 *   (2) B does not undercut A.
 *  
 * @author Sebastian Homann
 *
 */
public class StrongAttack implements AttackStrategy {

	/** Singleton instance. */
	private static StrongAttack instance = new StrongAttack();
	
	private Attack attack = Attack.getInstance();
	private Undercut undercut = Undercut.getInstance();
	
	/** Private constructor. */
	private StrongAttack(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static StrongAttack getInstance(){
		return StrongAttack.instance;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.NotionOfAttack#attacks(net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument, net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument)
	 */
	public boolean attacks(Argument a, Argument b) {
		return attack.attacks(a, b) && (! undercut.attacks(b, a) );
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "strong attack";
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy#toAbbreviation()
	 */
	public String toAbbreviation() {
		return "sa";
	}
}
