package org.getalp.metalexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MetaLexerTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MetaLexerTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MetaLexerTest.class );
    }

    /**
     * Simple MetaLexer test
     */
    public void testMetaLexer1()
    {
        SequenceDeSymboles ml = new SequenceDeSymboles("    <123> 'ab\"c' \"ab'c\"       Abc  abc    ");
        assertEquals("<123>", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("'ab\"c'", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("\"ab'c\"", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("Abc", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("abc", ml.next());
        assertFalse(ml.hasNext());
    }

    /**
     * Simple MetaLexer test
     */
    public void testMetaLexer2()
    {
        SequenceDeSymboles ml = new SequenceDeSymboles("<123> 'ab\"c' \"ab'c\"       Abc  abc    ");
        assertEquals("<123>", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("'ab\"c'", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("\"ab'c\"", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("Abc", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("abc", ml.next());
        assertFalse(ml.hasNext());
    }

    /**
     * Simple MetaLexer test
     */
    public void testMetaLexer3()
    {
        SequenceDeSymboles ml = new SequenceDeSymboles("<123> 'ab\"c' \"ab'c\"       Abc  abc");
        assertEquals("<123>", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("'ab\"c'", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("\"ab'c\"", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("Abc", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("abc", ml.next());
        assertFalse(ml.hasNext());
    }

    /**
     * Simple MetaLexer test
     */
    public void testMetaLexerWithGrammarRules()
    {
        SequenceDeSymboles ml = new SequenceDeSymboles("A -> <12 3> 123 'a b\"c' |  \"ab'c\" ; \n  \t    Abc -> abc '+'*+");
        assertEquals("A", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("->", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("<12 3>", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("123", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("'a b\"c'", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("|", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("\"ab'c\"", ml.next());
        assertTrue(ml.hasNext());
        assertEquals(";", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("Abc", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("->", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("abc", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("'+'", ml.next());
        assertTrue(ml.hasNext());
        assertEquals("*+", ml.next());
        assertFalse(ml.hasNext());
    }
}
