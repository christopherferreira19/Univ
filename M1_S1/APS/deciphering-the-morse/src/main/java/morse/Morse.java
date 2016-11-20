package morse;

import java.util.Arrays;
import java.util.List;

public enum Morse {
    DOT,
    DASH;

    public static Morse of(char c) {
        if (c == '.') { return DOT; }
        if (c == '-') { return DASH; }
        throw new RuntimeException("Invalid char " + c + " for Morse code");
    }

    public static List<Morse> of(String str) {
        Morse[] result = new Morse[str.length()];
        for (int i = 0; i < str.length(); i++){
            result[i] = of(str.charAt(i));
        }

        return Arrays.asList(result);
    }

    public String toString() {
        return this == DOT ? "." : "-";
    }

    public static String toString(List<Morse> list) {
        StringBuilder builder = new StringBuilder();
        for (Morse morse : list) {
            builder.append(morse.toString());
        }

        return builder.toString();
    }
}
