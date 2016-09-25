package fr.aumgn.cours.as.algos;

import com.google.common.collect.Sets;
import fr.aumgn.cours.as.grammar.*;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class RemoveUnreachables {

    public static Grammar apply(Grammar grammar) {
        Set<Symbole> reachables = reachables(grammar);
        return buildGrammar(grammar, reachables);
    }

    private static Set<Symbole> reachables(Grammar grammar) {
        Set<Symbole> reachables = Sets.newLinkedHashSet();
        reachables.add(grammar.axiome());
        Set<Symbole> newReachables = Sets.newHashSet(reachables);

        do {
            newReachables = newReachables.stream()
                    .filter(reachable -> reachable instanceof NonTerminal)
                    .map(reachable -> grammar.productions((NonTerminal) reachable))
                    .flatMap(productions -> productions.rightSides().stream())
                    .flatMap(Symboles::stream)
                    .filter(s -> !reachables.contains(s))
                    .collect(toSet());
        } while (reachables.addAll(newReachables));

        return reachables;
    }

    private static Grammar buildGrammar(Grammar grammar, Set<Symbole> reachables) {
        Grammar.Builder builder = Grammar.builder().axiome(grammar.axiome());
        for (Symbole reachable : reachables) {
            if (!(reachable instanceof NonTerminal)) {
                continue;
            }

            Productions productions = grammar.productions((NonTerminal) reachable);
            productions.rightSides().stream()
                    .filter(s -> s.stream().allMatch(reachables::contains))
                    .forEach(s -> builder.production(productions.leftSide(), s));
        }

        return builder.build();
    }
}
