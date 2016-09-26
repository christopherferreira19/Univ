package org.getalp.metagrammar;

import com.google.common.collect.Lists;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;

import java.util.ArrayList;
import java.util.List;


public class Grammar {

    protected List<GrammarRule> rules;
    protected NonTerminal source;
    protected ArrayList<Symbol> terminals = Lists.newArrayList();
    protected ArrayList<NonTerminal> nonTerminals = Lists.newArrayList();

    public Grammar() {
        this.rules = new ArrayList<GrammarRule>();
    }

    public Grammar(List<GrammarRule> rules) {
        for (GrammarRule rule : rules) {
            this.addRule(rule);
        }
    }

    public void addRule(GrammarRule r) {
        rules.add(r);
        // Add rule's symbols to grammar
        addIfNotThere(r.lhs, nonTerminals);
        for (Symbol rh : r.rhs) {
            if (rh.isNonTerminal())
                addIfNotThere((NonTerminal) rh, nonTerminals);
            else
                addIfNotThere(rh, terminals);
        }
    }

    private <E> void addIfNotThere(E s, ArrayList<? super E> l) {
        if (! l.contains(s)) l.add(s);
    }

    public void setSource(NonTerminal s) {
        this.source = s;
    }

    public NonTerminal source() {
        return source;
    }

    public List<GrammarRule> rules() {
        return rules;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Source: "); sb.append(source + "\n");
        sb.append("Rules:\n");
        for (GrammarRule gr: rules) {
            sb.append(gr + "\n");
        }
        return sb.toString();
    }

    public List<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public List<Symbol> getTerminals() {
        return terminals;
    }

    /**
     * return the number of non terminals in the grammar
     */
    public int nonTerminalsSize() {
        return nonTerminals.size();
    }

    /**
     * return the non terminal number i.
     * @param i the position of the requested non terminal (starts at 0)
     * @return the requested non terminal symbol
     */
    public NonTerminal getNonTerminal(int i) {
        return nonTerminals.get(i);
    }

    /**
     * return the number of the given non terminal symbol.
     * @param s the symbol whose number is requested
     * @return
     */
    public int indexOfNonTerminal(Symbol s) {
        return nonTerminals.indexOf(s);
    }

    /**
     * return the number of terminals in the grammar
     */
    public int terminalsSize() {
        return terminals.size();
    }

    /**
     * return the terminal number i.
     * @param i the position of the requested terminal (starts at 0)
     * @return the requested terminal symbol
     */
    public Symbol getTerminal(int i) {
        return terminals.get(i);
    }

    /**
     * return the number of the given terminal symbol.
     * @param s the symbol whose number is requested
     * @return
     */
    public int indexOfTerminal(Symbol s) {
        return terminals.indexOf(s);
    }
}
