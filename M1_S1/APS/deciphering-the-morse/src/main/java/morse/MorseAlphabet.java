package morse;

import java.util.List;

public enum MorseAlphabet {

    A(".-"),
    B("-..."),
    C("-.-."),
    D("-.."),
    E("."),
    F("..-."),
    G("--."),
    H("...."),
    I(".."),
    J(".---"),
    K("-.-"),
    L(".-.."),
    M("--"),
    N("-."),
    O("---"),
    P(".--."),
    Q("--.-"),
    R(".-."),
    S("..."),
    T("-"),
    U("..-"),
    V("...-"),
    W(".--"),
    X("-..-"),
    Y("-.--"),
    Z("--.."),
    ;

    private static final int A_NUMERIC_VALUE = Character.getNumericValue('a');

    final List<Morse> code;

    MorseAlphabet(String str) {
        this.code = Morse.of(str);
    }

    public static MorseAlphabet of(char c) {
        return values()[Character.getNumericValue(c) - A_NUMERIC_VALUE];
    }
}
