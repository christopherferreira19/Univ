package morse.gen;

import morse.Input;
import morse.MorseChar;

import java.util.*;

public class Generator {

    private static final char[] alphabet = "abcdefghijklmnopqrstuvxyz".toCharArray();

    public static Input generate(int L, int N, int Mlow, int Mhigh) {
        Random rand = new Random();
        List<MorseChar> sequence = new ArrayList<>();

        for (int i = 0; i < L; i++) {
            sequence.add(rand.nextBoolean() ? MorseChar.DOT : MorseChar.DASH);
        }

        /*Set<String> dictionarySet = new HashSet<>();
        while (dictionarySet.size() < N) {
            int wordSize = rand.nextInt(Mhigh - Mlow + 1) + Mlow;
            StringBuilder builder = new StringBuilder(wordSize);
            for (int i = 0; i < wordSize; i++) {
                int ci = rand.nextInt(alphabet.length);
                builder.append(alphabet[ci]);
            }

            dictionarySet.add(builder.toString());
        }

        List<String> dictionary = new ArrayList<>();
        dictionary.addAll(dictionarySet);*/
        List<String> dictionary = new ArrayList<>();
        for (char c : alphabet) {
            dictionary.add(String.valueOf(c));
        }
        return new Input(sequence, dictionary);
    }
}
