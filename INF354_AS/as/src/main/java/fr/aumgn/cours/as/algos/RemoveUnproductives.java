package fr.aumgn.cours.as.algos;

import com.google.common.collect.Sets;
import fr.aumgn.cours.as.grammar.Grammar;
import fr.aumgn.cours.as.grammar.Productions;
import fr.aumgn.cours.as.grammar.Symbole;

import java.util.Set;

public class RemoveUnproductives {

    public static Grammar apply(Grammar grammar) {
        Set<Symbole> productives = productives(grammar);
        return buildGrammar(grammar, productives);
    }

    private static Set<Symbole> productives(Grammar grammar) {
        Set<Symbole> productives = Sets.newHashSet(grammar.terminals());

        int previousSize;
        do {
            previousSize = productives.size();
            for (Productions productions : grammar.allProductions()) {
                if (productives.contains(productions.leftSide())) {
                    continue;
                }

                productions.rightSides().stream()
                        .filter(rightSide -> rightSide.stream().allMatch(productives::contains))
                        .forEach(rightSide -> productives.add(productions.leftSide()));
            }
        } while (previousSize != productives.size());
        return productives;
    }

    private static Grammar buildGrammar(Grammar grammar, Set<Symbole> productives) {
        Grammar.Builder builder = Grammar.builder().axiome(grammar.axiome());
        for (Productions productions : grammar.allProductions()) {
            productions.rightSides().stream()
                    .filter(rightSide -> rightSide.stream().allMatch(productives::contains))
                    .forEach(rightSide -> builder.production(productions.leftSide(), rightSide));
        }

        return builder.build();
    }
}
