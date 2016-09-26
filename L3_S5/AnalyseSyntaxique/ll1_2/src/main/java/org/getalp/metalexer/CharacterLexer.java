package org.getalp.metalexer;

import java.io.IOException;
import java.io.Reader;

class CharacterLexer implements Lexer {

    private final Reader reader;
    private boolean exhausted;
    private String next;

    CharacterLexer(Reader reader) {
        this.reader = reader;
        this.exhausted = false;
        this.next = readNext();
    }

    @Override
    public String next() {
        String current = next;
        next = readNext();
        return current;
    }


    private boolean isIgnorableChar(int cc) {
        return Character.isSpaceChar(cc) || cc == '\n' || cc == '\r' || cc == '\t';
    }

    private String readNext() {
        if (exhausted) {
            return MARQUEUR_FIN;
        }

        int cc;
        try {
            cc = reader.read();
            while (cc != -1 && isIgnorableChar(cc)) {
                cc = reader.read();
            }

            if (cc == -1) {
                exhausted = true;
                return MARQUEUR_FIN;
            }
        } catch (IOException e) {
            e.printStackTrace();
            exhausted = true;
            return MARQUEUR_FIN;
        }

        return String.valueOf((char) cc);
    }
}
