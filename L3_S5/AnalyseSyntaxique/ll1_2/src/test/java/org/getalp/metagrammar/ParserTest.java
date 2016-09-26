package org.getalp.metagrammar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.getalp.ll1.LL1Table;
import org.getalp.metalexer.Lexer;
import org.getalp.metalexer.SequenceDeSymboles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class ParserTest extends TestCase {

    public ParserTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(ParserTest.class);
    }

    private Parser parser(String filename, Lexer.Factory lexerFactory) {
        InputStream rsc = ParserTest.class.getResourceAsStream(filename);
        SequenceDeSymboles sequence = new SequenceDeSymboles(rsc);
        MetaGrammarLL1Parser metaParser = new MetaGrammarLL1Parser(sequence);

        Grammar grammar = metaParser.parse();
        LL1Table table = new LL1Table(grammar);
        if (!table.isValidLL1()) {
            fail("Grammar not LL(1)");
        }

        return new Parser(lexerFactory, grammar, table);
    }

    private void assertReconnait(boolean reconnu, Parser parser, Reader reader) {
        assertEquals(reconnu, parser.reconnaitre(reader));
    }

    private void assertReconnaitResource(boolean reconnu, Parser parser, String name) {
        InputStream rsc = ParserTest.class.getResourceAsStream(name);
        assertReconnait(reconnu, parser, new InputStreamReader(rsc));
    }

    private void assertReconnaitString(boolean reconnu, Parser parser, String s) {
        assertReconnait(reconnu, parser, new StringReader(s));
    }

    public void testRandom() {
        Parser parser = parser("/random.txt", Lexer.CHARACTER);

        assertReconnaitString(true, parser, "");
        assertReconnaitString(true, parser, "abc");
        assertReconnaitString(true, parser, "ac");
        assertReconnaitString(true, parser, "ae");
        assertReconnaitString(true, parser, "ee");

        assertReconnaitString(false, parser, "abce");
        assertReconnaitString(false, parser, "abcedab");
        assertReconnaitString(false, parser, "abbcca");
        assertReconnaitString(false, parser, "abcabc");
    }

    public void testParentheses() {
        Parser parser = parser("/parentheses.txt", Lexer.CHARACTER);

        assertReconnaitString(true, parser, "");
        assertReconnaitString(true, parser, "()");
        assertReconnaitString(true, parser, "((()))()");
        assertReconnaitString(true, parser, "(()())()");

        assertReconnaitString(false, parser, ")(");
        assertReconnaitString(false, parser, "(()()()");
        assertReconnaitString(false, parser, "(()()()))");
        assertReconnaitString(false, parser, "(()(())");
    }

    public void testArithmetique() {
        Parser parser = parser("/arithmetique.txt", Lexer.CHARACTER);

        assertReconnaitString(true, parser, "123");
        assertReconnaitString(true, parser, "-123");
        assertReconnaitString(true, parser, "-+-123");
        assertReconnaitString(true, parser, "(123)");
        assertReconnaitString(true, parser, "123 + 45");
        assertReconnaitString(true, parser, "123 * 678");
        assertReconnaitString(true, parser, "123 / 678");
        assertReconnaitString(true, parser, "123 + 456 % 789");
        assertReconnaitString(true, parser, "-+12 + +34 * -67 % --89");

        assertReconnaitString(false, parser, "*123");
        assertReconnaitString(false, parser, "/123");
        assertReconnaitString(false, parser, "%123");
        assertReconnaitString(false, parser, "()");
        assertReconnaitString(false, parser, "(1");
        assertReconnaitString(false, parser, "1)");

    }

    public void testReguliere() {
        Parser parser = parser("/reguliere.txt", Lexer.CHARACTER);

        assertReconnaitString(true, parser, "a");
        assertReconnaitString(true, parser, "(a)");
        assertReconnaitString(true, parser, "a*");
        assertReconnaitString(true, parser, "abc");
        assertReconnaitString(true, parser, "ab.c");
        assertReconnaitString(true, parser, "abc*");
        assertReconnaitString(true, parser, "ab.c*");
        assertReconnaitString(true, parser, "a + b");
        assertReconnaitString(true, parser, "a + bc");
        assertReconnaitString(true, parser, "a + b.c");
        assertReconnaitString(true, parser, "(a + b)c");
        assertReconnaitString(true, parser, "ε");

        assertReconnaitString(false, parser, "");
        assertReconnaitString(false, parser, "a.");
        assertReconnaitString(false, parser, ".a");
        assertReconnaitString(false, parser, "(a");
        assertReconnaitString(false, parser, "a)");
        assertReconnaitString(false, parser, "*a");
        assertReconnaitString(false, parser, "a +");
        assertReconnaitString(false, parser, "+ a");
        assertReconnaitString(false, parser, "a..b");
        assertReconnaitString(false, parser, "a +. b");
        assertReconnaitString(false, parser, "a ++ b");
        assertReconnaitString(false, parser, "a .+ b");
        assertReconnaitString(false, parser, "a.*b");
        assertReconnaitString(false, parser, "a +* b");
    }

    public void testMeta() {
        Parser parser = parser("/meta.txt", Lexer.GRAMMAR);

        assertReconnaitString(true, parser, "");
        assertReconnaitString(true, parser, "A -> -eps- ;");
        assertReconnaitString(true, parser, "A -> B ;");
        assertReconnaitString(true, parser, "A -> B | C | D | E ;");
        assertReconnaitString(true, parser, "<a> -> B ;");
        assertReconnaitString(true, parser, "ABCD -> EFGH ;");
        assertReconnaitString(true, parser, "A -> B C D ; B -> C D ;");

        assertReconnaitResource(true, parser, "/random.txt");
        assertReconnaitResource(true, parser, "/parentheses.txt");
        assertReconnaitResource(true, parser, "/exemple.txt");
        assertReconnaitResource(true, parser, "/arithmetique.txt");
        assertReconnaitResource(true, parser, "/reguliere.txt");
        assertReconnaitResource(true, parser, "/meta.txt");
        assertReconnaitResource(true, parser, "/meta2.txt");

        assertReconnaitString(false, parser, ";");
        assertReconnaitString(false, parser, "A ;");
        assertReconnaitString(false, parser, "A B -> C ;");
        assertReconnaitString(false, parser, "a -> B ;");
    }
}
