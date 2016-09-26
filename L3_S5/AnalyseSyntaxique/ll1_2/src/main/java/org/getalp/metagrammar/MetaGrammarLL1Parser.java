package org.getalp.metagrammar;

import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.ReservedLexeme;
import org.getalp.lexeme.Symbol;
import org.getalp.ll1.LL1Table;
import org.getalp.metalexer.MetaLexer;
import org.getalp.metalexer.SequenceDeSymboles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetaGrammarLL1Parser {

    private final MetaLexer metaLexer;
    private final Grammar grammar;
    private boolean firstLhs;

    public MetaGrammarLL1Parser(SequenceDeSymboles sequence) {
        this.metaLexer = new MetaLexer(sequence);
        this.grammar = new Grammar();
        this.firstLhs = true;
    }

    private void nextIfReserved(ReservedLexeme reserve) {
        if (metaLexer.lexeme() == reserve) {
            metaLexer.next();
        }
        else {
            throw new ParserException("Expected " + reserve + " got " + metaLexer.lexeme());
        }
    }

    private NonTerminal nextIfNonTerminal() {
        if (metaLexer.lexeme() instanceof NonTerminal) {
            NonTerminal lu = (NonTerminal) metaLexer.lexeme();
            metaLexer.next();
            return lu;
        }
        else {
            throw new ParserException("Expected non terminal got " + metaLexer.lexeme());
        }
    }

    private Symbol nextIfSymbol() {
        if (metaLexer.lexeme() instanceof Symbol) {
            Symbol lu = (Symbol) metaLexer.lexeme();
            metaLexer.next();
            return lu;
        }
        else {
            throw new ParserException("Expected symbol got " + metaLexer.lexeme());
        }
    }

    public Grammar parse() {
        parseGrammar();
        return grammar;
    }

    private void parseGrammar() {
        if (metaLexer.lexeme() != ReservedLexeme.END) {
            parseRule();
            parseGrammar();
        }
    }

    private void parseRule() {
        NonTerminal lhs = nextIfNonTerminal();
        if (firstLhs) {
            grammar.setSource(lhs);
            firstLhs = false;
        }

        nextIfReserved(ReservedLexeme.ARROW);
        parseRuleRhs(lhs);
        nextIfReserved(ReservedLexeme.SEMI_COLON);
    }

    private void parseRuleRhs(NonTerminal lhs) {
        List<Symbol> rhs = parseSymbolSequence();
        grammar.addRule(new GrammarRule(lhs, rhs));
        parseRuleRhsRest(lhs);
    }

    private void parseRuleRhsRest(NonTerminal lhs) {
        if (metaLexer.lexeme() == ReservedLexeme.PIPE) {
            nextIfReserved(ReservedLexeme.PIPE);
            List<Symbol> rhs = parseSymbolSequence();
            grammar.addRule(new GrammarRule(lhs, rhs));
            parseRuleRhsRest(lhs);
        }
    }


    private List<Symbol> parseSymbolSequence() {
        List<Symbol> symboles = new ArrayList<Symbol>();
        if (metaLexer.lexeme() instanceof Symbol) {
            symboles.add(nextIfSymbol());
            parseSymbolSequenceRest(symboles);
        }
        else {
            nextIfReserved(ReservedLexeme.EPSILON);
        }

        return symboles;
    }

    private void parseSymbolSequenceRest(List<Symbol> symboles) {
        if (metaLexer.lexeme() instanceof Symbol) {
            symboles.add(nextIfSymbol());
            parseSymbolSequenceRest(symboles);
        }
    }

    public static void main(String[] args) {
        MetaGrammarLL1Parser parser = new MetaGrammarLL1Parser(
                new SequenceDeSymboles(new File("src/test/resources/meta.txt")));
        Grammar grammar = parser.parse();
        System.out.print(grammar);
        LL1Table table = new LL1Table(grammar);
        System.out.println("Table LL1");
        System.out.println(table.prettyPrint());
    }
}
