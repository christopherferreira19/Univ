package org.getalp.lexeme;

import org.getalp.metalexer.Lexer;

public enum ReservedLexeme implements Lexeme {

    ARROW("->"),
    EPSILON("-eps-"),
    PIPE("|"),
    SEMI_COLON(";"),
    END(Lexer.MARQUEUR_FIN);

    private final String name;

    ReservedLexeme(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
