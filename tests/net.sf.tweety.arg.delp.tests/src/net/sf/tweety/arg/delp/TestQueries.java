package net.sf.tweety.arg.delp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.tweety.arg.delp.parser.DelpParser;
import net.sf.tweety.arg.delp.semantics.GeneralizedSpecificity;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * Testing some example KBs with various queries.
 *
 * @author Linda.Briesemeister
 */
public final class TestQueries {

    private final static Logger LOGGER = Logger.getLogger(TestQueries.class.getName());

    private final static DelpParser PARSER_BIRDS = new DelpParser();
    private static DelpReasoner REASONER_BIRDS;
    private final static DelpParser PARSER_NIXON = new DelpParser();
    private static DelpReasoner REASONER_NIXON;
    private final static DelpParser PARSER_STOCKS = new DelpParser();
    private static DelpReasoner REASONER_STOCKS;
    private final static DelpParser PARSER_COUNTER = new DelpParser();
    private static DelpReasoner REASONER_COUNTER;
    private final static DelpParser PARSER_HOBBES = new DelpParser();
    private static DelpReasoner REASONER_HOBBES;
    private final static DelpParser PARSER_DTREE = new DelpParser();
    private static DelpReasoner REASONER_DTREE;
    
    static DefeasibleLogicProgram delp;

    @BeforeClass
    public static void initParsers() {
//        DefeasibleLogicProgram delp;
        try {
            delp = PARSER_BIRDS.parseBeliefBase(Utilities.getKB("/birds.txt"));
            REASONER_BIRDS = new DelpReasoner(new GeneralizedSpecificity());
            delp = PARSER_NIXON.parseBeliefBase(Utilities.getKB("/nixon.txt"));
            REASONER_NIXON = new DelpReasoner(new GeneralizedSpecificity());
            delp = PARSER_STOCKS.parseBeliefBase(Utilities.getKB("/stocks.txt"));
            REASONER_STOCKS = new DelpReasoner(new GeneralizedSpecificity());
            delp = PARSER_COUNTER.parseBeliefBase(Utilities.getKB("/counterarg.txt"));
            REASONER_COUNTER = new DelpReasoner(new GeneralizedSpecificity());
            delp = PARSER_HOBBES.parseBeliefBase(Utilities.getKB("/hobbes.txt"));
            REASONER_HOBBES = new DelpReasoner(new GeneralizedSpecificity());
            delp = PARSER_DTREE.parseBeliefBase(Utilities.getKB("/dtree.txt"));
            REASONER_DTREE = new DelpReasoner(new GeneralizedSpecificity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DelpAnswer query(DefeasibleLogicProgram delp, DelpReasoner reasoner, DelpParser parser, String query) throws IOException {
        // perform query
        Formula formula = parser.parseFormula(query);
        DelpAnswer answer = (DelpAnswer) reasoner.query(delp, (FolFormula) formula);
        LOGGER.info("DeLP answer to query '"+formula+"' = "+answer);
        return answer;
    }

    @Test
    public void birds() throws IOException {
        DelpAnswer answer;
        // tina
        answer = query(delp, REASONER_BIRDS, PARSER_BIRDS, "Flies(tina)");
        assertEquals("Tina should fly", DelpAnswer.Type.YES, answer.getType());
        answer = query(delp, REASONER_BIRDS, PARSER_BIRDS, "~Flies(tina)");
        assertEquals("Tina should fly", DelpAnswer.Type.NO, answer.getType());
        // tweety
        answer = query(delp, REASONER_BIRDS, PARSER_BIRDS, "Flies(tweety)");
        assertEquals("Tweety does not fly", DelpAnswer.Type.NO, answer.getType());
        answer = query(delp, REASONER_BIRDS, PARSER_BIRDS, "~Flies(tweety)");
        assertEquals("Tweety does not fly", DelpAnswer.Type.YES, answer.getType());
    }

    @Test
    public void nixon() throws IOException {
        DelpAnswer answer;
        answer = query(delp, REASONER_NIXON, PARSER_NIXON, "~pacifist(nixon)"); // UNDECIDED
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
        answer = query(delp, REASONER_NIXON, PARSER_NIXON, "pacifist(nixon)"); // UNDECIDED
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
        answer = query(delp, REASONER_NIXON, PARSER_NIXON, "has_a_gun(nixon)"); // YES
        assertEquals(DelpAnswer.Type.YES, answer.getType());
    }

    @Test
    public void stocks() throws IOException {
        DelpAnswer ans = query(delp, REASONER_STOCKS, PARSER_STOCKS, "buy_stock(acme)");
        assertEquals("Buying stock ACME should be supported", DelpAnswer.Type.YES, ans.getType());
    }

    @Test
    public void counterarguments() throws IOException {
        DelpAnswer answer;
        answer = query(delp, REASONER_COUNTER, PARSER_COUNTER, "a");
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
        answer = query(delp, REASONER_COUNTER, PARSER_COUNTER, "c");
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
    }

    @Test
    public void hobbes() throws IOException {
        DelpAnswer answer;
        answer = query(delp, REASONER_HOBBES, PARSER_HOBBES, "~dangerous(hobbes)");
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
    }

    @Test
    public void dtree() throws IOException {
        DelpAnswer answer;
        answer = query(delp, REASONER_DTREE, PARSER_DTREE, "a"); // UNDECIDED
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
        answer = query(delp, REASONER_DTREE, PARSER_DTREE, "~b"); // YES
        assertEquals(DelpAnswer.Type.YES, answer.getType());
        answer = query(delp, REASONER_DTREE, PARSER_DTREE, "b"); // NO
        assertEquals(DelpAnswer.Type.NO, answer.getType());
    }

    @Test
    public void quoted() throws IOException {
        DelpAnswer answer;
        DelpParser parser = new DelpParser();
        DefeasibleLogicProgram delp = parser.parseBeliefBase(
                "saw(\"1.2.3.4\",\"foo.png\").\n"+
                "visited(IP) -< saw(IP,STR).\n"+
                "src(\"1.2.3.5\").");
        DelpReasoner reasoner = new DelpReasoner(new GeneralizedSpecificity());
        answer = query(delp, reasoner, parser, "visited(\"1.2.3.4\")"); // YES
        assertEquals(DelpAnswer.Type.YES, answer.getType());
        answer = query(delp, reasoner, parser, "visited(\"1.2.3.5\")"); // UNDECIDED
        assertEquals(DelpAnswer.Type.UNDECIDED, answer.getType());
    }

    @Test // currently too slow: state-space explosion!
    public void moreQuoted() throws IOException {
        DelpAnswer answer;
        DelpParser parser = new DelpParser();
        DefeasibleLogicProgram delp = parser.parseBeliefBase("% modeling web defacement\n" +
                "web_defaced(STR,IP1) -< cmd_injection(IP1),\n" +
                "   saw(STR,IP2),\n" +
                "   same_realm(IP1,IP2).\n" +
                "~web_defaced(STR,IP1) -< ~cmd_injection(IP1),\n" +
                "   visited(IP2),\n" +
                "   same_realm(IP1,IP2).\n" +
                "\n" +
                "cmd_injection(IP) -< HTTP_POST(IP),\n" +
                "   POST_contains(IP).\n" +
                "~cmd_injection(IP) -< HTTP_GET(IP).\n"+
                "~cmd_injection(IP) -< ~POST_contains(IP).\n" +
                "~cmd_injection(IP) -< ~POST_contains(IP).\n"+
                "\n"+
                "same_realm(IP1,IP2) <- slash24(IP1,PRE),slash24(IP2,PRE).\n"+
                "\n"+
                "% operative presumptions:\n" +
                "visited(\"1.2.3.4\") -< true.\n" +
                "saw(\"fr.jpg\",\"1.2.3.4\") -< true.\n" +
                "HTTP_POST(\"1.2.3.9\") -< true.\n" +
                "POST_contains(\"1.2.3.9\") -< true.\n" +
                "\n" +
                "% operative facts:\n"+
                "slash24(\"1.2.3.4\",\"1.2.3\").\n" +
                "slash24(\"1.2.3.9\",\"1.2.3\").\n" +
                "true.");
        DelpReasoner reasoner = new DelpReasoner(new GeneralizedSpecificity());
        answer = query(delp, reasoner, parser, "same_realm(\"1.2.3.4\",\"1.2.3.9\")"); // YES
        assertEquals(DelpAnswer.Type.YES, answer.getType());
        answer = query(delp, reasoner, parser, "web_defaced(\"fr.jpg\",\"1.2.3.9\")"); // YES
        assertEquals(DelpAnswer.Type.YES, answer.getType());
    }
}