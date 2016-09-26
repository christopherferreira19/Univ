package org.getalp.ll1;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.lexeme.Terminal;
import org.getalp.metagrammar.Grammar;
import org.getalp.metagrammar.GrammarRule;
import org.getalp.metalexer.Lexer;

import java.util.List;
import java.util.Set;

class LL1TableAlgos {

    public static final Terminal MARQUEUR_FIN = new Terminal(Lexer.MARQUEUR_FIN);

    static Set<NonTerminal> createEpsilonSet(Grammar grammar) {
        Set<NonTerminal> epsilonSet = Sets.newLinkedHashSet();

        boolean changed;
        do {
            changed = false;

            for (GrammarRule regle : grammar.rules()) {
                if (epsilonSet.contains(regle.lhs)) {
                    continue;
                }

                boolean sAnnuleTous = true;
                for (Symbol symbole : regle.rhs) {
                    sAnnuleTous = sAnnuleTous
                            && symbole instanceof NonTerminal
                            && epsilonSet.contains(symbole);
                }

                if (sAnnuleTous) {
                    epsilonSet.add(regle.lhs);
                    changed = true;
                }
            }
        } while (changed);

        return epsilonSet;
    }

    static SetMultimap<NonTerminal, Terminal> createFirstSets(
            Grammar grammar,
            Set<NonTerminal> epsilonSet
    ) {
        SetMultimap<NonTerminal, Terminal> firstSets = LinkedHashMultimap.create();

        boolean changed;
        do {
            changed = false;

            for (GrammarRule rule : grammar.rules()) {
                for (Symbol symbol : rule.rhs) {
                    if (symbol.isTerminal()) {
                        changed = changed || firstSets.put(rule.lhs, (Terminal) symbol);
                        break;
                    }
                    else {
                        NonTerminal nonTerminal = (NonTerminal) symbol;
                        changed = changed || firstSets.putAll(rule.lhs, firstSets.get(nonTerminal));
                        if (!epsilonSet.contains(nonTerminal)) {
                            break;
                        }
                    }
                }
            }
        } while (changed);


        return firstSets;
    }

    static SetMultimap<NonTerminal, Terminal> createFollowSets(
            Grammar grammar,
            Set<NonTerminal> epsilonSet,
            SetMultimap<NonTerminal, Terminal> firstSets
    ) {
        SetMultimap<NonTerminal, Terminal> followSets = LinkedHashMultimap.create();

        followSets.put(grammar.source(), MARQUEUR_FIN);
        for (GrammarRule rule : grammar.rules()) {
            List<Symbol> rhs = rule.rhs;
            for (int i = 0; i < rhs.size(); i++) {
                Symbol symbol = rhs.get(i);
                if (symbol.isTerminal()) {
                    continue;
                }

                NonTerminal nonTerminal = (NonTerminal) symbol;
                for (int j = i + 1; j < rhs.size(); j++) {
                    Symbol symbolSuivant = rhs.get(j);
                    if (symbolSuivant.isTerminal()) {
                        followSets.put(nonTerminal, (Terminal) symbolSuivant);
                        break;
                    }
                    else {
                        NonTerminal nonTerminalSuivant = (NonTerminal) symbolSuivant;
                        followSets.putAll(nonTerminal, firstSets.get(nonTerminalSuivant));
                        if (!epsilonSet.contains(nonTerminalSuivant)) {
                            break;
                        }
                    }
                }
            }
        }

        boolean changed;
        do {
            changed = false;

            for (GrammarRule rule : grammar.rules()) {
                List<Symbol> rhs = rule.rhs;

                for (int i = rhs.size() - 1; i >= 0; i--) {
                    Symbol symbol = rhs.get(i);
                    if (symbol.isTerminal()) {
                        break;
                    }

                    NonTerminal nonTerminal = (NonTerminal) symbol;
                    changed = changed || followSets.putAll(nonTerminal, followSets.get(rule.lhs));
                    if (!epsilonSet.contains(nonTerminal)) {
                        break;
                    }
                }
            }
        } while (changed);

        return followSets;
    }
}
