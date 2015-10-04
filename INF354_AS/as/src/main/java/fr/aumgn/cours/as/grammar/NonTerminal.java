package fr.aumgn.cours.as.grammar;

import static com.google.common.base.Preconditions.checkNotNull;

public class NonTerminal implements Symbole {

    public static NonTerminal of(String name) {
        return new NonTerminal(checkNotNull(name));
    }

    public static NonTerminal of(char c) {
        return of("" + c);
    }

    private final String name;

    NonTerminal(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NonTerminal && name.equals(((NonTerminal) obj).name);
    }

    @Override
    public String toString() {
        return name;
    }
}
