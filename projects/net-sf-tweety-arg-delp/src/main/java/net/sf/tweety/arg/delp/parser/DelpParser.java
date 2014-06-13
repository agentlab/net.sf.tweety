/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* Generated By:JavaCC: Do not edit this line. DelpParser.java */
package net.sf.tweety.arg.delp.parser;

import net.sf.tweety.arg.delp.*;
import net.sf.tweety.arg.delp.syntax.*;
import net.sf.tweety.commons.*;
import net.sf.tweety.logics.fol.syntax.*;
import net.sf.tweety.logics.commons.syntax.interfaces.*;
import net.sf.tweety.logics.commons.syntax.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
  * This class implements a parser for defeasible logic programs. The BNF for defeasible
  * logic program files is given by (start symbol is THEORY)
  * <br>
  * <br>THEORY			::== (EXPRESSION)*
  * <br>EXPRESSION		::== FACT | STRICTRULE | DEFEASIBLERULE
  * <br>FACT			::== LITERAL + "."
  * <br>STRICTRULE		::== LITERAL + "<-" + RULEBODY + "."
  * <br>DEFEASIBLERULE	::== LITERAL + "-<" + RULEBODY + "."
  * <br>RULEBODY		::== LITERAL | LITERAL + "," + RULEBODY
  * <br>LITERAL			::== "~" + ATOM | ATOM
  * <br>ATOM			::== PREDNAME | PREDNAME + "(" + TERMLIST + ")"
  * <br>TERMLIST		::== TERM | TERM + "," + TERMLIST
  * <br>TERM			::== VARIABLE | CONSTANT
  *
  * <br>PREDNAME is a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with an uppercase letter at the beginning.
  * <br>VARIABALE is a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with an uppercase letter at the beginning.
  * <br>CONSTANT is  a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with an lowercase letter at the beginning.
  */
@SuppressWarnings("all")
public class DelpParser extends Parser<DefeasibleLogicProgram> implements DelpParserConstants {

        private FolSignature signature = new FolSignature();

        public DelpParser(){
        }

        public DefeasibleLogicProgram parseBeliefBase(Reader reader) throws ParserException{
                try
                {
                        //DelpParser theParser = new DelpParser(reader);
                        return DelpParser.Theory(this.signature);
                }catch(ParseException e){
                        throw new ParserException(e);
                }
        }

        public Formula parseFormula(Reader reader) throws ParserException{
                throw new UnsupportedOperationException("This operation is not supported.");
        }

        public FolSignature getSignature(){
                return this.signature;
        }

  static final public DefeasibleLogicProgram Theory(FolSignature signature) throws ParseException {
        DefeasibleLogicProgram delp = new DefeasibleLogicProgram();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
      case 10:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      Expression(delp,signature);
    }
    jj_consume_token(0);
        {if (true) return delp;}
    throw new Error("Missing return statement in function");
  }

  static final public void Expression(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        FolFormula lit;
        Set<FolFormula> body = new HashSet<FolFormula>();
        FolFormula b;
    lit = Literal(delp,signature);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 6:
      jj_consume_token(6);
                                        delp.add(new DelpFact(lit));
      break;
    case 7:
      jj_consume_token(7);
      b = Literal(delp,signature);
                                        body.add(b);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 8:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(8);
        b = Literal(delp,signature);
                                        body.add(b);
      }
      jj_consume_token(6);
                                        delp.add(new StrictRule(lit,body));
      break;
    case 9:
      jj_consume_token(9);
      b = Literal(delp,signature);
                                        body.add(b);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 8:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        jj_consume_token(8);
        b = Literal(delp,signature);
                                        body.add(b);
      }
      jj_consume_token(6);
                                        delp.add(new DefeasibleRule(lit,body));
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public FolFormula Literal(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        FOLAtom atom;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      atom = Atom(delp,signature);
                {if (true) return atom;}
      break;
    case 10:
      jj_consume_token(10);
      atom = Atom(delp,signature);
                {if (true) return new Negation(atom);}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public FOLAtom Atom(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        Token p;
        List<Term<?>> terms = new ArrayList<Term<?>>();
        Term<?> t;
    p = jj_consume_token(NAME);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 11:
      jj_consume_token(11);
      t = Term(delp,signature);
                                        terms.add(t);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 8:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_4;
        }
        jj_consume_token(8);
        t = Term(delp,signature);
                                        terms.add(t);
      }
      jj_consume_token(12);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
          if(!signature.containsPredicate(p.image)){
                signature.add(new Predicate(p.image,terms.size()));
          }
          Predicate pred = signature.getPredicate(p.image);
          if(pred.getArity() != terms.size())
                {if (true) throw new ParseException("Wrong arity of predicate \u005c"" + p.image + "\u005c"");}
          {if (true) return new FOLAtom(pred,terms);}
    throw new Error("Missing return statement in function");
  }

  static final public Term Term(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        Token t;
    t = jj_consume_token(NAME);
                if(Pattern.compile("^[A-Z]").matcher(t.image).find())
                        {if (true) return new Variable(t.image);}
            if(!signature.containsConstant(t.image)){
                        signature.add(new Constant(t.image));
                }
                {if (true) return signature.getConstant(t.image);}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public DelpParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[7];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x420,0x100,0x100,0x2c0,0x420,0x100,0x800,};
   }

  /** Constructor with InputStream. */
  public DelpParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public DelpParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new DelpParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public DelpParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new DelpParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public DelpParser(DelpParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(DelpParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[13];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 7; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 13; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
