package fr.aumgn.cours.as.algos;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import fr.aumgn.cours.as.grammar.*;

import java.util.Set;

import static fr.aumgn.cours.as.utils.Guava8.toImmutableList;
import static java.util.stream.IntStream.range;

public class RemoveEpsilons {

    public static Grammar apply(Grammar grammar) {
        Set<Symbole> producingEpsilon = producingEpsilon(grammar);
        return buildGrammar(grammar, producingEpsilon);
    }

    private static Set<Symbole> producingEpsilon(Grammar grammar) {
        Set<Symbole> producesEpsilon = Sets.newHashSet();
        producesEpsilon.add(Terminal.epsilon());
        int previousSize;
        do {
            previousSize = producesEpsilon.size();
            for (Productions productions : grammar.allProductions()) {
                if (producesEpsilon.contains(productions.leftSide())) {
                    continue;
                }

                productions.rightSides().stream()
                        .filter(symboles -> symboles.stream().allMatch(producesEpsilon::contains))
                        .forEach(symboles -> producesEpsilon.add(productions.leftSide()));
            }
        } while (previousSize != producesEpsilon.size());
        return producesEpsilon;
    }

    private static Grammar buildGrammar(Grammar grammar, Set<Symbole> producingEpsilon) {
        Grammar.Builder builder = Grammar.builder();
        if (producingEpsilon.contains(grammar.axiome())) {
            NonTerminal newAxiome = NonTerminal.of(grammar.axiome().toString() + "'");
            builder.axiome(newAxiome);
            builder.production(newAxiome, grammar.axiome());
            builder.production(newAxiome, Terminal.epsilon());
        }
        else {
            builder.axiome(grammar.axiome());
        }

        for (Productions production : grammar.allProductions()) {
            for (Symboles rightSide : production.rightSides()) {
                if (rightSide.equals(Symboles.epsilon())) {
                    continue;
                }

                Set<Integer> indexProducingEpsilon = Sets.newHashSet();
                int indice = 0;
                for (Symbole symbole : rightSide) {
                    if (producingEpsilon.contains(symbole)) {
                        indexProducingEpsilon.add(indice);
                    }
                    indice++;
                }

                for (Set<Integer> combination : Sets.powerSet(indexProducingEpsilon)) {
                    ImmutableList<Symbole> newRightSide = range(0, rightSide.size())
                            .filter(i -> !combination.contains(i))
                            .mapToObj(rightSide::get)
                            .collect(toImmutableList());
                    if (!newRightSide.isEmpty()) {
                        builder.production(production.leftSide(), Symboles.of(newRightSide));
                    }
                }
            }
        }


        return builder.build();
    }
}
