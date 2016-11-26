package morse.algo;

import morse.Alpha2Morse;
import morse.Input;
import morse.MorseChar;

import java.util.ArrayList;
import java.util.List;

public class Recursive {

    public static int count_sentences(Input input) {
        List<List<MorseChar>> morseDictionary = dictionaryAlphaToMorse(input.dictionary);
        return count_sentences_rec(input.sequence, morseDictionary);
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

    public static int count_sentences_rec(List<MorseChar> morseChar, List<List<MorseChar>> dictionary) {
        int count = 0;
        for (int i = 0; i < dictionary.size(); i++) {
            List<MorseChar> word = dictionary.get(i);
            if (morseChar.size() < word.size() || !morseChar.subList(0, word.size()).equals(word)) {
                continue;
            }

            if (morseChar.size() == word.size()) {
                count += 1;
            }
            else {
                List<MorseChar> rest = morseChar.subList(word.size(), morseChar.size());
                count += count_sentences_rec(rest, dictionary);
            }
        }

        return count;
    }
}
