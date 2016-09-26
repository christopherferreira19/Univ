package fr.aumgn.cours.as.grammar;

import com.google.common.collect.ImmutableSet;

public class Productions {

    private final NonTerminal leftSide;
    private final ImmutableSet<Symboles> rightSides;

    Productions(NonTerminal leftSide, ImmutableSet<Symboles> rightSides) {
        this.leftSide = leftSide;
        this.rightSides = rightSides;
    }

    public NonTerminal leftSide() {
        return leftSide;
    }

    public ImmutableSet<Symboles> rightSides() {
        return rightSides;
    }
}
