package org.getalp.lexeme;

public class NonTerminal extends Symbol {

    public NonTerminal(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NonTerminal)) {
            return false;
        }

        NonTerminal other = (NonTerminal) object;
        return name.equals(other.name);
    }

    @Override
    public boolean isNonTerminal() {
        return true;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + 1;
    }

    @Override
    public String toString() {
        return name.isEmpty() || name.contains(" ") || !Character.isUpperCase(name.charAt(0))
                ? "<" + name + ">"
                : name;
    }
}
