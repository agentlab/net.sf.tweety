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
package net.sf.tweety.logics.pl.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.commons.analysis.InconsistencyMeasure;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.analysis.InconsistencyMeasureFactory;
import net.sf.tweety.logics.pl.analysis.InconsistencyMeasureFactory.Measure;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.MarcoMusEnumerator;
import net.sf.tweety.logics.pl.sat.PlMusEnumerator;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.opt.solver.ApacheCommonsSimplex;

public class InconsistencyMeasurementValueDistributionTest {

	public static double round(double unrounded, int precision, int roundingMode){
		if(unrounded == Double.POSITIVE_INFINITY)
			return Double.POSITIVE_INFINITY;
		if(unrounded == Double.NaN)
			return Double.NaN;	
		BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();		
	}
	
	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		//args = new String[1];
		//args[0] = "/Users/mthimm/Desktop/plfiles/sig1_l4_s3/";
				
		SatSolver.setDefaultSolver(new Sat4jSolver());
		PlMusEnumerator.setDefaultEnumerator(new MarcoMusEnumerator("/home/shared/incmes/marco_py-1.0/marco.py"));//new MarcoMusEnumerator("/home/shared/incmes/marco_py-1.0/marco.py")
		Solver.setDefaultLinearSolver(new ApacheCommonsSimplex());
		
		
		PlParser parser = new PlParser();
		PlBeliefSet bs;
		Map<Double,Integer> distr;
		InconsistencyMeasure<PropositionalFormula> im;
		double v;
		for(Measure m: InconsistencyMeasureFactory.Measure.values()){
			//skip drastic
			if(m.equals(InconsistencyMeasureFactory.Measure.DRASTIC)){
				System.out.println("Skipped drastic measure...");
				continue;
			}
			//skip mi
			if(m.equals(InconsistencyMeasureFactory.Measure.MI)){
				System.out.println("Skipped mi measure...");
				continue;
			}
			//skip eta
			if(m.equals(InconsistencyMeasureFactory.Measure.ETA)){
				System.out.println("Skipped eta measure...");
				continue;
			}
			//skip mic
			if(m.equals(InconsistencyMeasureFactory.Measure.MIC)){
				System.out.println("Skipped mic measure...");
				continue;
			}
			//skip contension
			if(m.equals(InconsistencyMeasureFactory.Measure.CONTENSION)){
				System.out.println("Skipped contension measure...");
				continue;
			}
			im = InconsistencyMeasureFactory.getInconsistencyMeasure(m);
			distr = new HashMap<Double,Integer>();
			for(File file: new File(args[0]).listFiles()){
				bs = parser.parseBeliefBaseFromFile(file.getAbsolutePath());
				v = im.inconsistencyMeasure(bs);
				v = round(v, 3, BigDecimal.ROUND_HALF_UP);
				if(distr.containsKey(v))
					distr.put(v, distr.get(v) + 1);
				else distr.put(v, 1);
			}			
			System.out.println(m.id + "\t" + distr);
		}
		
	}
}
