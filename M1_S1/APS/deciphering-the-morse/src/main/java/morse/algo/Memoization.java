package morse.algo;

import morse.Alpha2Morse;
import morse.Input;
import morse.MorseChar;

import java.util.ArrayList;
import java.util.List;

public class Memoization {

    public static int count_sentences(Input input) {
        List<List<MorseChar>> morseDictionary = dictionaryAlphaToMorse(input.dictionary);
        int[] memo = new int[input.sequence.size()];
        for (int i = 0; i < input.sequence.size(); i++) {
            memo[i] = -1;
        }
        return count_sentences_rec(input.sequence, 0, morseDictionary, memo);
    }

    private static List<List<MorseChar>> dictionaryAlphaToMorse(List<String> dictionary) {
        List<List<MorseChar>> result = new ArrayList<>();
        for (String s : dictionary) {
            List<MorseChar> morseCharWord = new ArrayList<>();
            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                morseCharWord.addAll(Alpha2Morse.of(c).code);
            }
            result.add(morseCharWord);
        }

        return result;
    }

    public static int count_sentences_rec(List<MorseChar> morseChar, int from, List<List<MorseChar>> dictionary, int[] memo) {
        if (memo[from] != -1) {
            return memo[from];
        }

        int count = 0;
        for (int i = 0; i < dictionary.size(); i++) {
            List<MorseChar> word = dictionary.get(i);
            if ((morseChar.size() - from) < word.size() || !morseChar.subList(from, from + word.size()).equals(word)) {
                continue;
            }

            if ((morseChar.size() - from) == word.size()) {
                count += 1;
            }
            else {
                count += count_sentences_rec(morseChar, from + word.size(), dictionary, memo);
            }
        }

        memo[from] = count;
        return count;
    }
}
