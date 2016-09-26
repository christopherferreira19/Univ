package org.getalp.lexeme;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Symbol implements Lexeme {

    public final String name;

    Symbol(String name) {
        this.name = checkNotNull(name);
    }

    public abstract boolean isNonTerminal();

    public abstract boolean isTerminal();
}
