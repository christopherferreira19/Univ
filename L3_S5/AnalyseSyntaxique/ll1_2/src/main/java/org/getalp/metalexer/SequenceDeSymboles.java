package org.getalp.metalexer;

import java.io.*;

/**
 * Created by serasset on 22/10/15.
 */
public class SequenceDeSymboles {

    Reader in;
    int cc;
    boolean exhausted = false;

    public SequenceDeSymboles(Reader in) {
        this.in = in;
        advanceAndIgnoreWhiteSpaces();
    }

    public SequenceDeSymboles(File f) {
        try {
            this.in = new InputStreamReader(new FileInputStream(f), "UTF-8");
            advanceAndIgnoreWhiteSpaces();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public SequenceDeSymboles(String s) {
        this.in = new StringReader(s);
        advanceAndIgnoreWhiteSpaces();
    }

    public SequenceDeSymboles(InputStream in) {
        try {
            this.in = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        advanceAndIgnoreWhiteSpaces();
    }

    private boolean isIgnorableChar(int cc) {
        return Character.isSpaceChar(cc) || cc == '\n' || cc == '\r' || cc == '\t';
    }

    private void advanceAndIgnoreWhiteSpaces() {
        try {
            cc = in.read();
            while (cc != -1 && isIgnorableChar(cc)) {
                cc = in.read();
            }
            if (cc == -1) exhausted = true;
        } catch (IOException e) {
            e.printStackTrace();
            exhausted = true;
        }
    }

    private String collectUntil(char c) {
        StringBuffer res = new StringBuffer();
        try {
            res.append((char)cc);
            cc = in.read();
            while (cc != -1 && cc != c) {
                res.append((char)cc);
                cc = in.read();
            }
            if (cc == -1)
                exhausted = true;
            else
                res.append((char)cc);
            advanceAndIgnoreWhiteSpaces();
        } catch (IOException e) {
            e.printStackTrace();
            exhausted = true;
        }
        return res.toString();
    }

    private String collectNonBreakingString() {
        StringBuffer res = new StringBuffer();
        try {
            while (cc != -1 && ! isIgnorableChar(cc)) {
                res.append((char)cc);
                cc = in.read();
            }
            if (cc == -1) exhausted = true;
            advanceAndIgnoreWhiteSpaces();
        } catch (IOException e) {
            e.printStackTrace();
            exhausted = true;
        }
        return res.toString();
    }

    /**
     *
     * @return true iff the sequence contains more symbols
     */
    public boolean hasNext() {
        return ! exhausted;
    }

    /**
     * returns the next symbol in the sequence.
     * @return
     */
    public String next() {
        String res = null;
        if (cc == '<') {
            res = collectUntil('>');
        } else if (cc == '\'') {
            res = collectUntil('\'');
        } else if (cc == '\"') {
            res = collectUntil('\"');
        } else {
            // cc cannot be a space, treat it as a simple Symbol (non separator string)
            res = collectNonBreakingString();
        }
        return res;
    }

}
