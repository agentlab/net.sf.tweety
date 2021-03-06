package net.sf.tweety.logics.fol.prover;

import java.io.File;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.commons.util.Shell;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.writer.Prover9Writer;

/**
 * Invokes Prover9 (https://www.cs.unm.edu/~mccune/mace4/) and returns its
 * results.
 * 
 * @author Nils Geilen
 *
 */
@Component(service = Reasoner.class)
public class Prover9 extends FolTheoremProver {

	/**
	 * String representation of the prover9 binary path, directory, where the
	 * temporary files are stored
	 */
	private String binaryLocation;

	/**
	 * Shell to run prover9
	 */
	private Shell bash;
	
	public Prover9() {
		super();
	}

	/**
	 * Constructs a new instance pointing to a specific prover 9
	 * 
	 * @param binaryLocation
	 *            of the prover9 executable on the hard drive
	 * @param bash
	 *            shell to run commands
	 */
	public Prover9(String binaryLocation, Shell bash) {
		super();
		this.binaryLocation = binaryLocation;
		this.bash = bash;
	}

	/**
	 * Constructs a new instance pointing to a specific prover9
	 * 
	 * @param binaryLocation
	 *            of the prover9 executable on the hard drive
	 */
	public Prover9(String binaryLocation) {
		this(binaryLocation, Shell.getNativeShell());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.tweety.logics.fol.prover.FolTheoremProver#equivalent(net.sf.tweety
	 * .logics.fol.FolBeliefSet, net.sf.tweety.logics.fol.syntax.FolFormula,
	 * net.sf.tweety.logics.fol.syntax.FolFormula)
	 */
	@Override
	public boolean equivalent(FolBeliefSet kb, FolFormula a, FolFormula b) {
		try {
			File file = File.createTempFile("tmp", ".txt");
			Prover9Writer printer = new Prover9Writer(new PrintWriter(file));
			printer.printBase(kb);
			printer.printEquivalence(a, b);
			printer.close();
			return eval(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * invokes prover9
	 * 
	 * @param file
	 *            input fle for prover9
	 * @return query result
	 */
	private boolean eval(File file) throws Exception {
		String cmd = binaryLocation + " -f " + file.getAbsolutePath();
		//System.out.println(cmd);
		String output = null;
		output = bash.run(cmd);
		// output = Exec.invokeExecutable(cmd, -1, true);
		//System.out.print(output);
		if (Pattern.compile("Exiting with .+ proof").matcher(output).find())
			return true;
		if (Pattern.compile("Exiting with failure").matcher(output).find())
			return false;
		throw new RuntimeException("Failed to invoke prover9: Prover9 returned no result which can be interpreted.");
	}

	/**
	 * returns the path of the provers binary
	 * 
	 * @return the path of the provers binary
	 */
	public String getBinaryLocation() {
		return binaryLocation;
	}

	/**
	 * Change path of the binary
	 * 
	 * @param binaryLocation
	 *            the new path of the binary
	 */
	public void setBinaryLocation(String binaryLocation) {
		this.binaryLocation = binaryLocation;
	}

	@Override
	public Answer query(BeliefBase<FolFormula> beliefBase, FolFormula query) {
		try {
			File file = File.createTempFile("tmp", ".txt");
			Prover9Writer printer = new Prover9Writer(new PrintWriter(file));
			printer.printBase(beliefBase);
			printer.printQuery(query);
			printer.close();
			boolean result = eval(file);
			if(result){
				Answer answer = new Answer(beliefBase, query);
				answer.setAnswer(true);
				answer.appendText("The answer is: true");
				return answer;
			} else {
				Answer answer = new Answer(beliefBase, query);
				answer.setAnswer(false);
				answer.appendText("The answer is: false");
				return answer;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
