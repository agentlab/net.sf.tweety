package net.sf.tweety.arg.dung;

import java.util.Comparator;

public class DungTheoryComparator implements Comparator<DungTheory> {

	@Override
	public int compare(DungTheory o1, DungTheory o2) {
		// DungTheory implements Comparable in order to 
		// have a fixed (but arbitrary) order among all theories
		// for that purpose we just use the hash code.
		return o1.hashCode() - o2.hashCode();
	}

}
