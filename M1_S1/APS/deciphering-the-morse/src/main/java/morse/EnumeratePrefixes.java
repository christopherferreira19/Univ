package morse;

import java.util.ArrayList;
import java.util.List;

public class EnumeratePrefixes {

    public static int count_sentences(Input input) {
        List<List<Morse>> morseDictionary = dictionaryAlphaToMorse(input.dictionary);
        return count_sentences_rec(input.sequence, morseDictionary);
    }

    private static List<List<Morse>> dictionaryAlphaToMorse(List<String> dictionary) {
        List<List<Morse>> result = new ArrayList<>();
        for (String s : dictionary) {
            List<Morse> morseWord = new ArrayList<>();
            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                morseWord.addAll(MorseAlphabet.of(c).code);
            }
            result.add(morseWord);
        }

        return result;
    }

    public static int count_sentences_rec(List<Morse> morse, List<List<Morse>> dictionary) {
        int count = 0;
        for (int i = 0; i < dictionary.size(); i++) {
            List<Morse> word = dictionary.get(i);
            if (morse.size() < word.size() || !morse.subList(0, word.size()).equals(word)) {
                continue;
            }

            if (morse.size() == word.size()) {
                count += 1;
            }
            else {
                List<Morse> rest = morse.subList(word.size(), morse.size());
                count += count_sentences_rec(rest, dictionary);
            }
        }

        return count;
    }
}
