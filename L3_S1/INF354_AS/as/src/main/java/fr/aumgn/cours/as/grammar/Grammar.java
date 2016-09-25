package fr.aumgn.cours.as.grammar;

import com.google.common.collect.*;

import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Verify.verify;
import static com.google.common.base.Verify.verifyNotNull;
import static fr.aumgn.cours.as.utils.Guava8.toImmutableSet;

public class Grammar {

    public static Builder builder() {
        return new Builder();
    }

    private final NonTerminal axiome;
    private final ImmutableMap<NonTerminal, Productions> productionsByLeftSide;

    Grammar(NonTerminal axiome, ImmutableMap<NonTerminal, Productions> productionsByLeftSide) {
        this.axiome = axiome;
        this.productionsByLeftSide = productionsByLeftSide;
    }

    public NonTerminal axiome() {
        return axiome;
    }

    private Stream<Symbole> symbolesStream() {
        return Stream.concat(
                productionsByLeftSide.keySet().stream(),
                productionsByLeftSide.values().stream()
                        .map(Productions::rightSides)
                        .flatMap(Set::stream)
                        .flatMap(Symboles::stream));
    }

    public ImmutableSet<Symbole> vocabulaire() {
        return symbolesStream().collect(toImmutableSet());
    }

    public ImmutableSet<NonTerminal> nonTerminals() {
        return symbolesStream()
                .filter(symbole -> symbole instanceof NonTerminal)
                .map(symbole -> (NonTerminal) symbole)
                .collect(toImmutableSet());
    }

    public ImmutableSet<Terminal> terminals() {
        return symbolesStream()
                .filter(symbole -> symbole instanceof Terminal)
                .map(symbole -> (Terminal) symbole)
                .collect(toImmutableSet());
    }

    public Productions productions(NonTerminal left) {
        return productionsByLeftSide.get(left);
    }

    public ImmutableCollection<Productions> allProductions() {
        return productionsByLeftSide.values();
    }

    public List<String> toStrings() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Productions productions : productionsByLeftSide.values()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(productions.leftSide());
            if (productions.leftSide().equals(axiome)) {
                stringBuilder.append("*");
            }

            stringBuilder.append(" → ");
            for (Iterator<Symboles> iterator = productions.rightSides().iterator(); iterator.hasNext(); ) {
                stringBuilder.append(iterator.next());
                if (iterator.hasNext()) {
                    stringBuilder.append(" | ");
                }
            }

            builder.add(stringBuilder.toString());
        }

        return builder.build();
    }

    public static class Builder {

        private NonTerminal axiome;
        private final ImmutableSetMultimap.Builder<NonTerminal, Symboles> rawProductionsBuilder;

        private Builder() {
            this.axiome = null;
            this.rawProductionsBuilder = ImmutableSetMultimap.builder();
        }

        public Builder axiome(String axiome) {
            Symbole symbole = Symboles.of(axiome).getOne();
            verify(symbole instanceof NonTerminal);
            return axiome((NonTerminal) symbole);
        }

        public Builder axiome(NonTerminal axiome) {
            this.axiome = axiome;
            return this;
        }

        public Builder production(String productionString) {
            String[] parts = productionString.split("→");
            checkArgument(parts.length == 2);

            Symbole leftSymbole = Symboles.of(parts[0]).getOne();
            verify(leftSymbole instanceof NonTerminal);
            NonTerminal left = (NonTerminal) leftSymbole;
            for (String right : parts[1].split("\\|")) {
                production(left, Symboles.of(right));
            }
            return this;
        }

        public Builder production(NonTerminal left, Symbole right) {
            return production(left, Symboles.of(right));
        }

        public Builder production(NonTerminal left, Symboles right) {
            rawProductionsBuilder.put(left, right);
            return this;
        }

        public Grammar build() {
            verifyNotNull(axiome);
            ImmutableSetMultimap<NonTerminal, Symboles> rawProductions = rawProductionsBuilder.build();
            verify(rawProductions.containsKey(axiome));

            ImmutableMap.Builder<NonTerminal, Productions> productionsBuilder = ImmutableMap.builder();
            for (Map.Entry<NonTerminal, Set<Symboles>> entry : Multimaps.asMap(rawProductions).entrySet()) {
                productionsBuilder.put(entry.getKey(), new Productions(entry.getKey(), ImmutableSet.copyOf(entry.getValue())));
            }

            return new Grammar(axiome, productionsBuilder.build());
        }
    }
}
