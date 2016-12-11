package morse.algo;

import morse.Input;
import morse.MorseChar;

import java.util.List;

public class Tabulation {

    public static int count_sentences(Input input) {
        List<List<MorseChar>> dictionary = Dictionary.alphaToMorse(input.dictionary);
        List<MorseChar> morse = input.sequence;

        int[] tab = new int[morse.size()];
        for (int i = 0; i < morse.size(); i++) {
            tab[i] = 0;
        }

        for (int kk = 0; kk < morse.size(); kk++) {
            int ll = kk;
            int mul = ll == 0 ? 1 : tab[ll - 1];
            if (mul == 0) {
                continue;
            }

            for (int mm = 0; mm < dictionary.size(); mm++) {
                int from = ll;
                List<MorseChar> word = dictionary.get(mm);
                if ((morse.size() - from) < word.size() || !morse.subList(from, from + word.size()).equals(word)) {
                    continue;
                }
            }

            DicoTree node = dico;
            while (node != null && ll < morse.size()) {
                node = (morse.get(ll) == MorseChar.DOT)
                        ? node.dot
                        : node.dash;
                if (node != null) {
                    tab[ll] += mul * node.wordsCount;
                }

                ll++;
            }
        }

        return tab[morse.size() - 1];
    }
}
