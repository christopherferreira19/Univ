package org.getalp.metagrammar;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.lexeme.Terminal;
import org.getalp.metalexer.SequenceDeSymboles;

public class MetaGrammarLL1ParserTest extends TestCase {

    public MetaGrammarLL1ParserTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(MetaGrammarLL1ParserTest.class);
    }

    public void testParseVide() {
        SequenceDeSymboles sequence = new SequenceDeSymboles("");
        MetaGrammarLL1Parser parser = new MetaGrammarLL1Parser(sequence);

        Grammar grammar = parser.parse();
        assertTrue(grammar.rules().isEmpty());
    }

    private void assertInvalid(String str, String reason) {
        MetaGrammarLL1Parser parser = new MetaGrammarLL1Parser(new SequenceDeSymboles(str));

        try {
            parser.parse();
        }
        catch (ParserException exc) {
            return;
        }

        fail("Expected [" + str + "] to be invalid, reason : " + reason);
    }

    public void testParseInvalid() {
        assertInvalid("S -> a", "Missing ';'");
        assertInvalid("a -> A ;", "LHS with Terminal");
        assertInvalid("S A -> A ;", "LHS with two symbols");
        assertInvalid("S -> a -> a ;", "Misplaced '->'");
        assertInvalid("S -> | a ;", "Misplaced '|'");
        assertInvalid("S -> a | ;", "Misplaced '|'");
        assertInvalid("S -> a ;\nS -> b", "Missing ';'");
    }

    private GrammarRule rule(NonTerminal lhs, Symbol... rhs) {
        return new GrammarRule(lhs, Lists.newArrayList(rhs));
    }

    public void testParseGrammar() {
        NonTerminal S = new NonTerminal("S");
        NonTerminal A = new NonTerminal("A");
        NonTerminal B = new NonTerminal("B");
        NonTerminal C = new NonTerminal("C");
        NonTerminal quotedReserved = new NonTerminal("quoted reserved");
        Terminal a = new Terminal("a");
        Terminal At = new Terminal("A");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        Terminal Ct = new Terminal("C");
        Terminal pipe = new Terminal("|");
        Terminal semiColon = new Terminal(";");
        Terminal epsilon = new Terminal("-eps-");
        Terminal arrow = new Terminal("->");

        SequenceDeSymboles sequence = new SequenceDeSymboles(
                "S -> A B | C | <quoted reserved>;\n" +
                "A -> a ; A -> 'A' ;\n" +
                "B -> b | -eps- ;\n" +
                "C -> c | 'C' ;\n" +
                "<quoted reserved> -> '|' | ';' | '-eps-' | '->' ;\n"
        );
        MetaGrammarLL1Parser parser = new MetaGrammarLL1Parser(sequence);

        Grammar grammar = parser.parse();

        assertEquals(S, grammar.source());

        ImmutableSet<GrammarRule> expected = ImmutableSet.of(
                rule(S, A, B),
                rule(S, C),
                rule(S, quotedReserved),
                rule(A, a),
                rule(A, At),
                rule(B, b),
                rule(B),
                rule(C, c),
                rule(C, Ct),
                rule(quotedReserved, pipe),
                rule(quotedReserved, semiColon),
                rule(quotedReserved, epsilon),
                rule(quotedReserved, arrow)
        );
        assertEquals(expected, ImmutableSet.copyOf(grammar.rules()));
    }
}
