package org.getalp.metalexer;

import org.getalp.lexeme.Lexeme;
import org.getalp.lexeme.ReservedLexeme;
import org.getalp.lexeme.NonTerminal;
import org.getalp.lexeme.Terminal;

public class MetaLexer {

    private final SequenceDeSymboles sequence;
    private Lexeme lexeme;

    public MetaLexer(SequenceDeSymboles sequence) {
        this.sequence = sequence;
        next();
    }

    public Lexeme lexeme() {
        return lexeme;
    }

    public void next() {
        this.lexeme = computeNext();
    }

    private Lexeme computeNext() {
        if (!sequence.hasNext()) {
            return ReservedLexeme.END;
        }

        String token = sequence.next();
        if ("->".equals(token)) {
            return ReservedLexeme.ARROW;
        }

        if ("-eps-".equals(token)) {
            return ReservedLexeme.EPSILON;
        }

        if ("|".equals(token)) {
            return ReservedLexeme.PIPE;
        }

        if (";".equals(token)) {
            return ReservedLexeme.SEMI_COLON;
        }

        char c = token.charAt(0);
        if (c == '<') {
            return new NonTerminal(token.substring(1, token.length() - 1));
        }

        if (c == '\'' || c == '"') {
            return new Terminal(token.substring(1, token.length() - 1));
        }

        return Character.isUpperCase(c)
                ? new NonTerminal(token)
                : new Terminal(token);
    }
}
