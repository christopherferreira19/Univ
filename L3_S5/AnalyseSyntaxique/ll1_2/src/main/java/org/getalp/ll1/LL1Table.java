package org.getalp.ll1;

import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Symbol;
import org.getalp.lexeme.Terminal;
import org.getalp.metagrammar.Grammar;
import org.getalp.metagrammar.GrammarRule;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

public class LL1Table {

    private final Map<NonTerminal, SetMultimap<String, GrammarRule>> contents;
    private final boolean isValidLL1;

    public LL1Table(Grammar grammar) {
        this.contents = Maps.newLinkedHashMap();

        Set<NonTerminal> epsilonSet = LL1TableAlgos.createEpsilonSet(grammar);
        SetMultimap<NonTerminal, Terminal> firstSets = LL1TableAlgos.createFirstSets(grammar, epsilonSet);
        SetMultimap<NonTerminal, Terminal> followSets = LL1TableAlgos.createFollowSets(grammar, epsilonSet, firstSets);

        for (GrammarRule rule : grammar.rules()) {
            boolean sAnnule = true;
            for (Symbol symbol : rule.rhs) {
                if (symbol.isTerminal())  {
                    put(rule.lhs, (Terminal) symbol, rule);
                    sAnnule = false;
                    break;
                }
                else {
                    NonTerminal nonTerminal = (NonTerminal) symbol;
                    putForAllTerminaux(rule.lhs, firstSets.get(nonTerminal), rule);

                    if (!epsilonSet.contains(nonTerminal)) {
                        sAnnule = false;
                        break;
                    }
                }
            }

            if (sAnnule) {
                putForAllTerminaux(rule.lhs, followSets.get(rule.lhs), rule);
            }
        }

        this.isValidLL1 = verifyValidLL1();
    }

    private boolean put(NonTerminal nonTerminal, Terminal terminal, GrammarRule rule) {
        SetMultimap<String, GrammarRule> setMultimap = contents.get(nonTerminal);
        if (setMultimap == null) {
            setMultimap = LinkedHashMultimap.create();
            contents.put(nonTerminal, setMultimap);
        }

        return setMultimap.put(terminal.name, rule);
    }

    private boolean putForAllTerminaux(
            NonTerminal nonTerminal,
            Iterable<Terminal> terminals,
            GrammarRule rule
    ) {
        boolean change = false;
        for (Terminal terminal : terminals) {
            change = put(nonTerminal, terminal, rule) || change;
        }
        return change;
    }

    private boolean verifyValidLL1() {
        for (SetMultimap<String, GrammarRule> value : contents.values()) {
            for (Set<GrammarRule> rules : Multimaps.asMap(value).values()) {
                if (rules.size() > 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isValidLL1() {
        return isValidLL1;
    }

    public Set<GrammarRule> get(NonTerminal nonTerminal, String terminal) {
        SetMultimap<String, GrammarRule> setMultimap = contents.get(nonTerminal);
        return setMultimap == null
                ? ImmutableSet.<GrammarRule>of()
                : setMultimap.get(terminal);
    }

    public Optional<GrammarRule> getOne(NonTerminal nonTerminal, String terminal) {
        checkState(isValidLL1);
        Set<GrammarRule> rules = get(nonTerminal, terminal);
        return rules.isEmpty()
                ? Optional.<GrammarRule>absent()
                : Optional.of(rules.iterator().next());
    }

    @Override
    public String toString() {
        return contents.toString();
    }

    public String prettyPrint() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<NonTerminal, SetMultimap<String, GrammarRule>> nonTerminalEntry
                : contents.entrySet()) {
            builder.append(nonTerminalEntry.getKey());
            builder.append(": {\n");

            Set<Map.Entry<String, Set<GrammarRule>>> terminalEntries =
                    Multimaps.asMap(nonTerminalEntry.getValue()).entrySet();
            for (Map.Entry<String, Set<GrammarRule>> terminalEntry : terminalEntries) {
                for (GrammarRule grammarRule : terminalEntry.getValue()) {
                    builder.append("  ");
                    builder.append(terminalEntry.getKey());
                    builder.append(": ");
                    builder.append(grammarRule);
                    builder.append("\n");
                }
            }

            builder.append("}\n");
        }

        return builder.toString();
    }
}
