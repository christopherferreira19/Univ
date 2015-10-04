package fr.aumgn.cours.as.algos;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import fr.aumgn.cours.as.grammar.*;

public class RemoveSimples {

    public static Grammar apply(Grammar grammar) {
        SetMultimap<NonTerminal, NonTerminal> flattenSimpleGraph = flattenSimpleGraph(grammar);
        return buildGrammar(grammar, flattenSimpleGraph);
    }

    private static SetMultimap<NonTerminal, NonTerminal> flattenSimpleGraph(Grammar grammar) {
        SetMultimap<NonTerminal, NonTerminal> flattenSimpleGraph = HashMultimap.create();
        for (Productions productions : grammar.allProductions()) {
            for (Symboles symboles : productions.rightSides()) {
                if (symboles.size() != 1) {
                    continue;
                }

                Symbole right = Iterables.getOnlyElement(symboles);
                if (right instanceof NonTerminal) {
                    putSimple(flattenSimpleGraph, productions.leftSide(), (NonTerminal) right);
                }
            }
        }
        return flattenSimpleGraph;
    }

    private static void putSimple(
            Multimap<NonTerminal, NonTerminal> reversedSimple,
            NonTerminal left,
            NonTerminal right
    ) {
        if (left.equals(right)) {
            return;
        }

        for (NonTerminal transitiveLeft : reversedSimple.get(left)) {
            putSimple(reversedSimple, transitiveLeft, right);
        }
        reversedSimple.put(right, left);
    }

    private static Grammar buildGrammar(Grammar grammar, SetMultimap<NonTerminal, NonTerminal> flattenSimpleGraph) {
        Grammar.Builder builder = Grammar.builder().axiome(grammar.axiome());
        for (Productions productions : grammar.allProductions()) {
            for (Symboles right : productions.rightSides()) {
                if (right.size() == 1) {
                    Symbole simpleRight = Iterables.getOnlyElement(right);
                    if (simpleRight instanceof NonTerminal) {
                        continue;
                    }
                }

                for (NonTerminal otherLeft : flattenSimpleGraph.get(productions.leftSide())) {
                    builder.production(otherLeft, right);
                }
                builder.production(productions.leftSide(), right);
            }
        }

        return builder.build();
    }
}
