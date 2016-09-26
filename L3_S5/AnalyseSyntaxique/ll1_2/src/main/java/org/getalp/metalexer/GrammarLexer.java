package org.getalp.metalexer;

class GrammarLexer implements Lexer {

    private static String NON_TERMINAL = "nonterminal";
    private static String TERMINAL = "terminal";

    private final SequenceDeSymboles sequence;

    GrammarLexer(SequenceDeSymboles sequence) {
        this.sequence = sequence;
    }

    @Override
    public String next() {
        if (!sequence.hasNext()) {
            return MARQUEUR_FIN;
        }

        String token = sequence.next();
        if ("->".equals(token)
                || "-eps-".equals(token)
                || "|".equals(token)
                || ";".equals(token)) {
            return token;
        }

        char c = token.charAt(0);
        if (c == '<') {
            return NON_TERMINAL;
        }

        if (c == '\'' || c == '"') {
            return TERMINAL;
        }

        return Character.isUpperCase(c)
                ? NON_TERMINAL
                : TERMINAL;
    }
}
