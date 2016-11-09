package net.sf.tweety.logics.fol.prover;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Parser;
import net.sf.tweety.commons.util.Shell;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.writer.FolWriter;
import net.sf.tweety.logics.fol.writer.TptpWriter;

/**
 * JUnitTest to test TPTP priter and Eprover implemnetation
 * 
 * @author Nils Geilen
 *
 */

public class TPTPTest {

	static FolTheoremProver prover;
	FolWriter printer = new TptpWriter();
	Parser<FolFormula> parser = new FolParser();

	@BeforeClass
	public static void init() {
		if (System.getProperty("os.name").matches("Win.*")) {
			System.out.println("Initializing Eprover Test for Windows");
			prover = new EProver("C:/app/E/PROVER/eprover.exe", Shell.getCygwinShell("C:/cygwin64/bin/bash.exe"));
		} else {
			System.out.println("Initializing Eprover Test for Unix");
			prover = new EProver("/home/nils/app/E/PROVER/eprover", Shell.getNativeShell());
			// e = new
			// EProver("/Users/mthimm/Projects/misc_bins/eprover/eprover");
		}
	}

	@Test
	public void test1() throws Exception {
		String source = "type(a) \n type(b) \n type(c) \n" + "a \n !b";
		BeliefBase<FolFormula> b = parser.parseBeliefBase(source);
		printer.printBase(b);
		System.out.println(printer);
		assertFalse(prover.query(b, parser.parseFormula("b")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("a")).getAnswerBoolean());
		assertFalse(prover.query(b, parser.parseFormula("c")).getAnswerBoolean());
		assertFalse(prover.query(b, parser.parseFormula("!c")).getAnswerBoolean());
	}

	@Test
	public void test2() throws Exception {
		String source = "Animal = {horse, cow, lion} \n" + "type(Tame(Animal)) \n" + "type(Ridable(Animal)) \n"
				+ "Tame(cow) \n" + "!Tame(lion) \n" + "Ridable(horse) \n" + "forall X: (!Ridable(X) || Tame(X)) \n";
		BeliefBase<FolFormula> b = parser.parseBeliefBase(source);
		printer.printBase(b);
		System.out.println(printer);
		assertTrue(prover.query(b, parser.parseFormula("Tame(cow)")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("exists X: (Tame(X))")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("Tame(horse)")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("!Ridable(lion)")).getAnswerBoolean());
	}

	@Test
	public void test3() throws Exception {
		String source = "Animal = {horse, cow, lion} \n" + "Plant = {grass, tree} \n" + "type(Eats(Animal, Plant)) \n"
				+ "forall X: (!Eats(X,tree)) \n" + "Eats(cow, grass) \n"
				+ "forall X: (!Eats(cow, X) || Eats(horse, X)) \n" + "exists X: (Eats(lion, X))";
		BeliefBase<FolFormula> b = parser.parseBeliefBase(source);
		printer.printBase(b);
		System.out.println(printer);
		assertFalse(prover.query(b, parser.parseFormula("Eats(lion, tree)")).getAnswerBoolean());
		assertFalse(prover.query(b, parser.parseFormula("!Eats(lion, grass)")).getAnswerBoolean());
		// is not true according to the solver
		// assertTrue(e.query(b, (FolFormula)parser.parseFormula("Eats(lion,
		// grass)")));
		assertFalse(prover.query(b, parser.parseFormula("Eats(horse, tree)")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("!Eats(horse, tree)")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("Eats(horse, grass)")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("exists X: (forall Y: (!Eats(Y, X)))")).getAnswerBoolean());
		assertFalse(prover.query(b, parser.parseFormula("forall X: (forall Y: (Eats(Y, X)))")).getAnswerBoolean());
		assertTrue(prover.query(b, parser.parseFormula("!(forall X: (forall Y: (Eats(Y, X))))")).getAnswerBoolean());
	}

}
