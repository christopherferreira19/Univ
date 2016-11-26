package morse;

import java.util.Arrays;
import java.util.List;

public enum MorseChar {
    DOT,
    DASH;

    public static MorseChar of(char c) {
        if (c == '.') { return DOT; }
        if (c == '-') { return DASH; }
        throw new RuntimeException("Invalid char " + c + " for MorseChar code");
    }

    public static List<MorseChar> of(String str) {
        MorseChar[] result = new MorseChar[str.length()];
        for (int i = 0; i < str.length(); i++){
            result[i] = of(str.charAt(i));
        }

        return Arrays.asList(result);
    }

    public String toString() {
        return this == DOT ? "." : "-";
    }

    public static String toString(List<MorseChar> list) {
        StringBuilder builder = new StringBuilder();
        for (MorseChar morseChar : list) {
            builder.append(morseChar.toString());
        }

        return builder.toString();
    }
}
