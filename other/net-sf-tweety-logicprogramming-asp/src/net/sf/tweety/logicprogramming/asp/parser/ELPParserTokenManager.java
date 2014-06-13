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
/* Generated By:JavaCC: Do not edit this line. ELPParserTokenManager.java */
package net.sf.tweety.logicprogramming.asp.parser;
import java.util.*;
import java.util.regex.*;
import net.sf.tweety.logicprogramming.asp.syntax.*;

/** Token Manager. */
public class ELPParserTokenManager implements ELPParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0xc0L) != 0L)
         {
            jjmatchedKind = 14;
            return 14;
         }
         if ((active0 & 0x400000L) != 0L)
         {
            jjmatchedKind = 15;
            return -1;
         }
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 14;
            return 22;
         }
         return -1;
      case 1:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 1;
            return 21;
         }
         if ((active0 & 0xc0L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 1;
            return 14;
         }
         return -1;
      case 2:
         if ((active0 & 0xc0L) != 0L)
            return 14;
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 9;
            jjmatchedPos = 2;
            return 14;
         }
         return -1;
      case 3:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 3;
            return 14;
         }
         return -1;
      case 4:
         if ((active0 & 0x100L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 4;
            return 14;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 32:
         jjmatchedKind = 1;
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 35:
         return jjStopAtPos(0, 5);
      case 40:
         return jjStopAtPos(0, 20);
      case 41:
         return jjStopAtPos(0, 21);
      case 44:
         return jjStopAtPos(0, 19);
      case 45:
         return jjStopAtPos(0, 24);
      case 46:
         return jjStopAtPos(0, 17);
      case 58:
         jjmatchedKind = 12;
         return jjMoveStringLiteralDfa1_0(0x40000L);
      case 61:
         return jjStopAtPos(0, 15);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x100L);
      case 110:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 123:
         return jjStopAtPos(0, 10);
      case 124:
         return jjStopAtPos(0, 13);
      case 125:
         return jjStopAtPos(0, 11);
      case 126:
         return jjStopAtPos(0, 23);
      default :
         return jjMoveNfa_0(4, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 45:
         if ((active0 & 0x40000L) != 0L)
            return jjStopAtPos(1, 18);
         break;
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x100L);
      case 110:
         return jjMoveStringLiteralDfa2_0(active0, 0x80L);
      case 111:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 118:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 32:
         if ((active0 & 0x10000L) != 0L)
            return jjStopAtPos(2, 16);
         break;
      case 116:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 14);
         else if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(2, 7, 14);
         break;
      case 120:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa4_0(active0, 0x100L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 110:
         return jjMoveStringLiteralDfa5_0(active0, 0x100L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 116:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(5, 8, 14);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 25;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 4:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                     jjCheckNAdd(14);
                  }
                  else if ((0x70000c0000000000L & l) != 0L)
                  {
                     if (kind > 15)
                        kind = 15;
                  }
                  else if (curChar == 33)
                     jjCheckNAdd(16);
                  if (curChar == 62)
                     jjCheckNAdd(16);
                  else if (curChar == 60)
                     jjCheckNAdd(16);
                  break;
               case 22:
               case 14:
                  if ((0x3ff200000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(14);
                  break;
               case 21:
                  if ((0x3ff200000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(14);
                  break;
               case 13:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(14);
                  break;
               case 15:
                  if ((0x70000c0000000000L & l) != 0L && kind > 15)
                     kind = 15;
                  break;
               case 16:
                  if (curChar == 61 && kind > 15)
                     kind = 15;
                  break;
               case 17:
                  if (curChar == 60)
                     jjCheckNAdd(16);
                  break;
               case 18:
                  if (curChar == 62)
                     jjCheckNAdd(16);
                  break;
               case 19:
                  if (curChar == 33)
                     jjCheckNAdd(16);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 4:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                     jjCheckNAdd(14);
                  }
                  if (curChar == 109)
                     jjAddStates(0, 1);
                  else if (curChar == 115)
                     jjstateSet[jjnewStateCnt++] = 11;
                  else if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if (curChar == 99)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 22:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                     jjCheckNAdd(14);
                  }
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 23;
                  else if (curChar == 97)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 21:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                     jjCheckNAdd(14);
                  }
                  if (curChar == 120)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  break;
               case 0:
                  if (curChar == 116 && kind > 9)
                     kind = 9;
                  break;
               case 1:
                  if (curChar == 110)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               case 2:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 3:
                  if (curChar == 111)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 5:
                  if (curChar == 115 && kind > 9)
                     kind = 9;
                  break;
               case 6:
                  if (curChar == 101)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 7:
                  if (curChar == 109)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 8:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if (curChar == 109 && kind > 9)
                     kind = 9;
                  break;
               case 11:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 12:
                  if (curChar == 115)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(14);
                  break;
               case 14:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(14);
                  break;
               case 20:
                  if (curChar == 109)
                     jjAddStates(0, 1);
                  break;
               case 23:
                  if (curChar == 110 && kind > 9)
                     kind = 9;
                  break;
               case 24:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 25 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   22, 24, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\43", "\156\157\164", "\151\156\164", 
"\155\141\170\151\156\164", null, "\173", "\175", "\72", "\174", null, null, "\40\166\40", "\56", 
"\72\55", "\54", "\50", "\51", "\75", "\176", "\55", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x1ffffe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[25];
private final int[] jjstateSet = new int[50];
protected char curChar;
/** Constructor. */
public ELPParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public ELPParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 25; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 13 && (0x2600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

}
