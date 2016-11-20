package morse;

import java.util.ArrayList;
import java.util.List;

public class EnumeratePrefixesMemoization {

    public static int count_sentences(Input input) {
        List<List<Morse>> morseDictionary = dictionaryAlphaToMorse(input.dictionary);
        int[] memo = new int[input.sequence.size()];
        for (int i = 0; i < input.sequence.size(); i++) {
            memo[i] = -1;
        }
        return count_sentences_rec(input.sequence, 0, morseDictionary, memo);
    }

    private static List<List<Morse>> dictionaryAlphaToMorse(List<String> dictionary) {
        List<List<Morse>> result = new ArrayList<>();
        for (String s : dictionary) {
            List<Morse> morseWord = new ArrayList<>();
            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                morseWord.addAll(MorseAlphabet.of(c).code);
            }
            System.out.println(s + " => " + Morse.toString(morseWord));
            result.add(morseWord);
        }

        return result;
    }

    public static int count_sentences_rec(List<Morse> morse, int from, List<List<Morse>> dictionary, int[] memo) {
        if (memo[from] != -1) {
            return memo[from];
        }

        int count = 0;
        for (int i = 0; i < dictionary.size(); i++) {
            List<Morse> word = dictionary.get(i);
            if ((morse.size() - from) < word.size() || !morse.subList(from, from + word.size()).equals(word)) {
                continue;
            }

            if ((morse.size() - from) == word.size()) {
                count += 1;
            }
            else {
                count += count_sentences_rec(morse, from + word.size(), dictionary, memo);
            }
        }

        memo[from] = count;
        return count;
    }
}
