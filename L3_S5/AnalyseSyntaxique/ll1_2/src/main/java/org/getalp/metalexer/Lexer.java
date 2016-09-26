package org.getalp.metalexer;

import java.io.*;

public interface Lexer {

    String MARQUEUR_FIN = "-$-";

    String next();

    interface Factory {

        Lexer createLexer(Reader stream);
    }

    Factory CHARACTER = new Factory() {

        @Override
        public Lexer createLexer(Reader reader) {
            return new CharacterLexer(reader);
        }
    };

    Factory GRAMMAR = new Factory() {

        @Override
        public Lexer createLexer(Reader stream) {
            return new GrammarLexer(new SequenceDeSymboles(stream));
        }
    };
}
