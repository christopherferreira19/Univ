package morse;

import java.util.List;

public enum Alpha2Morse {

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

    public final List<MorseChar> code;

    Alpha2Morse(String str) {
        this.code = MorseChar.of(str);
    }

    public static Alpha2Morse of(char c) {
        return values()[Character.getNumericValue(c) - A_NUMERIC_VALUE];
    }
}
