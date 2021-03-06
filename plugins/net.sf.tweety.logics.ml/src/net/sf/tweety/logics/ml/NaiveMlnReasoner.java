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
package net.sf.tweety.logics.ml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.osgi.service.component.annotations.Component;

import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.ParserException;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.semantics.HerbrandBase;
import net.sf.tweety.logics.fol.semantics.HerbrandInterpretation;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;
import net.sf.tweety.logics.ml.syntax.MlnFormula;

/**
 * This class implements a naive reasoner for MLNs.
 * 
 * @author Matthias Thimm
 */
/**
 * @author mthimm
 *
 */
@Component(service = Reasoner.class)
public class NaiveMlnReasoner extends AbstractMlnReasoner {

	/** Directory for temporary files. */
	private String tempDirectory = null;
	
	/** If the model has already been computed this file contains it. */
	private File archivedFile = null;
	
	/**
	 * Creates a new NaiveMlnReasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 */
	public NaiveMlnReasoner(){
//		this(beliefBase, (FolSignature) beliefBase.getSignature());
	}
	
	/**
	 * Creates a new NaiveMlnReasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 * @param signature another signature (if the probability distribution should be defined 
	 * on that one (that one should subsume the signature of the Markov logic network)
	 */
	public NaiveMlnReasoner(FolSignature signature){
		super(signature);		
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#reset()
	 */
	public void reset(){
		this.archivedFile = null;
	}
	
	/** Sets the path of the directory for temporary files.
	 * @param str a file path
	 */
	public void setTempDirectory(String str){
		this.tempDirectory = str;
	}
	
	/** Computes the model of the given MLN.
	 * @return a file where the model is stored.
	 */
	private File computeModel(BeliefBase<MlnFormula> beliefBase){
		//1.) write all possible worlds of the signature into a text file
		// (Note: we avoid doing this in memory due to exponential size)
		try {
			HerbrandBase hBase = new HerbrandBase(this.getSignature());			
			FileWriter fstream;
			FileInputStream inStream;	
			boolean isFirst = true;
			File currentFile = File.createTempFile("naive_mln",null,new File(this.tempDirectory));
			currentFile.deleteOnExit();
			for(FOLAtom a: hBase.getAtoms()){
				if(isFirst){					
					fstream = new FileWriter(currentFile.getAbsoluteFile());
					BufferedWriter out = new BufferedWriter(fstream);
					out.append(a.toString());
					out.newLine();
					out.newLine();
					isFirst = false;
					out.close();
				}else{
					File temp = File.createTempFile("naive_mln",null,new File(this.tempDirectory));
					temp.deleteOnExit();
					fstream = new FileWriter(temp.getAbsoluteFile());
					inStream = new FileInputStream(currentFile.getAbsoluteFile());
					BufferedWriter out = new BufferedWriter(fstream);
					DataInputStream in = new DataInputStream(inStream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String strLine;
					while((strLine = br.readLine()) != null){
						out.append(strLine);
						out.newLine();
						if(strLine.equals(""))
							out.append(a.toString());
						else out.append(strLine + ";" + a.toString());
						out.newLine();
					}
					in.close();
					out.close();
					currentFile = temp;
				}				
			}
			// 2.) for each possible world compute its impact; also, sum up all impacts
			double sum = 0;
			File temp = File.createTempFile("naive_mln",null,new File(this.tempDirectory));
			temp.deleteOnExit();
			fstream = new FileWriter(temp.getAbsoluteFile());
			inStream = new FileInputStream(currentFile.getAbsoluteFile());
			BufferedWriter out = new BufferedWriter(fstream);
			DataInputStream in = new DataInputStream(inStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			boolean emptyLine = false;
			double weight;
			while((strLine = br.readLine()) != null){
				if(strLine.equals("")){
					if(emptyLine) continue;
					else emptyLine = true;
				}
				HerbrandInterpretation hInt = this.parseInterpretation(strLine);
				weight = this.computeWeight(beliefBase, hInt);
				sum += weight;
				out.append(strLine + "#" + weight);
				out.newLine();
			}
			in.close();
			out.close();
			currentFile = temp;
			
			// 3.) normalize by sum
			temp = File.createTempFile("naive_mln",null,new File(this.tempDirectory));
			temp.deleteOnExit();
			fstream = new FileWriter(temp.getAbsoluteFile());
			inStream = new FileInputStream(currentFile.getAbsoluteFile());
			out = new BufferedWriter(fstream);
			in = new DataInputStream(inStream);
			br = new BufferedReader(new InputStreamReader(in));			
			while((strLine = br.readLine()) != null){
				if(strLine.equals("")) break;					
				StringTokenizer tokenizer = new StringTokenizer(strLine,"#");
				try{
					if(tokenizer.countTokens() == 1)
						out.append("#" + (new Double(tokenizer.nextToken())/sum));
					else
						out.append(tokenizer.nextToken() + "#" + (new Double(tokenizer.nextToken())/sum));
					out.newLine();
				}catch(Exception e){
					
				}				
			}
			in.close();
			out.close();
			return temp;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#doQuery(net.sf.tweety.logics.firstorderlogic.syntax.FolFormula)
	 */
	@Override
	public double doQuery(BeliefBase<MlnFormula> beliefBase, FolFormula query) {		
		if(this.archivedFile == null)
			this.archivedFile = this.computeModel(beliefBase);
		
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(this.archivedFile.getAbsoluteFile());			
			DataInputStream in = new DataInputStream(inStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			double prob = 0;
			String strLine;
			while((strLine = br.readLine()) != null){
				StringTokenizer tokenizer = new StringTokenizer(strLine,"#");
				try{
					HerbrandInterpretation hInt;
					if(tokenizer.countTokens() == 1)
						hInt = new HerbrandInterpretation();
					else hInt = this.parseInterpretation(tokenizer.nextToken());
					if(hInt.satisfies(query))
						prob += new Double(tokenizer.nextToken());					
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
			in.close();
			return prob;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;	
	}

	/** Constructs a Herbrand interpretation from the given string
	 * @param str a string.
	 * @return a Herbrand interpretation
	 */
	private HerbrandInterpretation parseInterpretation(String str){
		StringTokenizer tokenizer = new StringTokenizer(str, ";");
		Collection<FOLAtom> atoms = new HashSet<FOLAtom>();
		FolParser parser = new FolParser();
		parser.setSignature(this.getSignature());
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			try {
				atoms.add( (FOLAtom)parser.parseFormula(token));
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new HerbrandInterpretation(atoms);
	}
}
