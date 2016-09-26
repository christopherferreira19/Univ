package org.getalp.metagrammar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.lexeme.Terminal;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class GrammarTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GrammarTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( GrammarTest.class );
    }

    /**
     * Simple MetaLexer test
     */
    public void testGrammar1()
    {
        Grammar g = new Grammar();
        NonTerminal e = new NonTerminal("E");
        NonTerminal t = new NonTerminal("T");
        Symbol plus = new Terminal("+");
        ArrayList<Symbol> rhs1 = new ArrayList();
        rhs1.add(e); rhs1.add(plus); rhs1.add(t);
        g.addRule(new GrammarRule(e, rhs1));
        ArrayList<Symbol> rhs2 = new ArrayList();
        rhs2.add(t);
        g.addRule(new GrammarRule(e, rhs2));

        assertEquals(2, g.nonTerminalsSize());
        assertEquals(1, g.terminalsSize());

    }

}
