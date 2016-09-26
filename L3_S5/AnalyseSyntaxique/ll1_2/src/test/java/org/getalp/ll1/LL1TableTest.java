package org.getalp.ll1;

import com.google.common.collect.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.lexeme.Terminal;
import org.getalp.metagrammar.Grammar;
import org.getalp.metagrammar.GrammarRule;

import java.util.Set;

import static org.getalp.ll1.LL1TableAlgos.*;
import static org.getalp.ll1.LL1TableAlgos.MARQUEUR_FIN;

public class LL1TableTest extends TestCase {

    private static final NonTerminal S = new NonTerminal("S");
    private static final NonTerminal A = new NonTerminal("A");
    private static final NonTerminal B = new NonTerminal("B");
    private static final NonTerminal C = new NonTerminal("C");
    private static final NonTerminal D = new NonTerminal("D");
    private static final NonTerminal E = new NonTerminal("E");
    private static final NonTerminal F = new NonTerminal("F");
    private static final Terminal s = new Terminal("s");
    private static final Terminal a = new Terminal("a");
    private static final Terminal b = new Terminal("b");
    private static final Terminal c = new Terminal("c");
    private static final Terminal d = new Terminal("d");

    private Grammar grammar;

    public LL1TableTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LL1TableTest.class);
    }

    public void setUp() throws Exception {
        grammar = new Grammar();
        grammar.setSource(S);
    }

    private void rule(NonTerminal lhs, Symbol... rhs) {
        grammar.addRule(new GrammarRule(lhs, Lists.newArrayList(rhs)));
    }

    public void testEpsilonSet() {
        rule(A, C, D); // A s'annule par C et D
        rule(C);
        rule(D);
        rule(B, E, F); // B ne s'annule pas à cause de F
        rule(E);
        rule(F, a);

        Set<NonTerminal> epsilonSet = createEpsilonSet(grammar);
        Set<NonTerminal> expected = ImmutableSet.of(A, C, D, E);
        assertEquals(expected, epsilonSet);
    }

    public void testFirstSets() {
        rule(A, a, B, C);
        rule(B, C, D);
        rule(C, B);
        rule(D, E, F);
        rule(E, b, E);
        rule(E);
        rule(F, A);

        Set<NonTerminal> epsilonSet = createEpsilonSet(grammar);

        SetMultimap<NonTerminal, Terminal> firstSets = createFirstSets(grammar, epsilonSet);
        SetMultimap<NonTerminal, Terminal> expected = ImmutableSetMultimap.<NonTerminal, Terminal>builder()
                .putAll(A, a)
                .putAll(D, a, b)
                .putAll(E, b)
                .putAll(F, a)
                .build();
        assertEquals(expected, firstSets);
    }

    public void testFollowSets() {
        rule(S, s, A, a);
        rule(S, D);
        rule(A, B, C);
        rule(B, b);
        rule(C, c);
        rule(C);
        rule(D, d);

        Set<NonTerminal> epsilonSet = createEpsilonSet(grammar);
        SetMultimap<NonTerminal, Terminal> firstSets = createFirstSets(grammar, epsilonSet);

        SetMultimap<NonTerminal, Terminal> followSets = createFollowSets(grammar, epsilonSet, firstSets);
        SetMultimap<NonTerminal, Terminal> expected = ImmutableSetMultimap.<NonTerminal, Terminal>builder()
                .putAll(S, MARQUEUR_FIN)
                .putAll(A, a)
                .putAll(B, a, c)
                .putAll(C, a)
                .putAll(D, MARQUEUR_FIN)
                .build();
        assertEquals(expected, followSets);
    }
}
