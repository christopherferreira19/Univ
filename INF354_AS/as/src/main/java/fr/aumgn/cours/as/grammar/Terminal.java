package fr.aumgn.cours.as.grammar;

import java.util.Objects;

public class Terminal implements Symbole {

    private static final Terminal EPSILON = new Terminal(null);

    public static Terminal epsilon() {
        return EPSILON;
    }

    public static Terminal of(char character) {
        return new Terminal(character);
    }

    private final Character character;

    private Terminal(Character character) {
        this.character = character;
    }

    @Override
    public int hashCode() {
        return character == null ? 0 : character.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Terminal && Objects.equals(character, ((Terminal) obj).character);
    }

    @Override
    public String toString() {
        return character == null ? "ε" : "" + character;
    }
}
