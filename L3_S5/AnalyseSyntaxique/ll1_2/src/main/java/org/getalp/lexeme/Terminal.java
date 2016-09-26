package org.getalp.lexeme;

import com.google.common.base.Preconditions;

public class Terminal extends Symbol {

    public Terminal(String name) {
        super(name);
        Preconditions.checkArgument(!name.contains(" "), "Terminal cannot contain spaces");
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Terminal)) {
            return false;
        }

        Terminal other = (Terminal) object;
        return name.equals(other.name);

    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name.isEmpty()
                || Character.isUpperCase(name.charAt(0))
                || "|".equals(name)
                || ";".equals(name)
                || "->".equals(name)
                || "-eps-".equals(name)
                ? "'" + name + "'"
                : name;
    }
}
