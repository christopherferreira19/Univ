package fr.aumgn.cours.as.algos;

import com.google.common.collect.Queues;
import fr.aumgn.cours.as.grammar.*;

import java.util.Deque;

import static com.google.common.base.Verify.verify;
import static java.util.stream.Collectors.joining;

public class ChomskyNormalForm {

    public static Grammar apply(Grammar grammar) {
        Grammar.Builder builder = Grammar.builder().axiome(grammar.axiome());

        for (NonTerminal leftSide : grammar.nonTerminals()) {
            for (Symboles rightSide : grammar.productions(leftSide).rightSides()) {
                if (rightSide.size() == 1) {
                    // Assumes grammar is clean hence the following statement must be true
                    verify(rightSide.get(0) instanceof Terminal);
                    builder.production(leftSide, rightSide);
                    continue;
                }

                Deque<NonTerminal> nonTerminals = Queues.newArrayDeque();
                for (Symbole symbole : rightSide) {
                    if (symbole instanceof NonTerminal) {
                        nonTerminals.add((NonTerminal) symbole);
                    }
                    else {
                        NonTerminal nonTerminal = NonTerminal.of(symbole.toString() + "'");
                        builder.production(nonTerminal, symbole);
                        nonTerminals.add(nonTerminal);
                    }
                }

                NonTerminal newLeftSide = leftSide;
                while (nonTerminals.size() > 2) {
                    NonTerminal firstSymbole = nonTerminals.pop();
                    NonTerminal rest = NonTerminal.of(nonTerminals.stream()
                            .map(NonTerminal::toString)
                            .collect(joining("", "<", ">")));
                    builder.production(newLeftSide, Symboles.of(firstSymbole, rest));

                    newLeftSide = rest;
                }

                builder.production(newLeftSide, Symboles.of(nonTerminals));
            }
        }

        return builder.build();
    }
}
