package morse.algo;

import morse.Input;
import morse.MorseChar;

import java.util.List;

public class TabulationDicoTree {

    public static int count_sentences(Input input) {
        DicoTree dico = DicoTree.create(input.dictionary);
        List<MorseChar> morse = input.sequence;

        int[] tab = new int[morse.size()];
        for (int i = 0; i < morse.size(); i++) {
            tab[i] = 0;
        }

        for (int i = 0; i < morse.size(); i++) {
            int j = i;
            int mul = j == 0 ? 1 : tab[j - 1];
            if (mul == 0) {
                continue;
            }

            DicoTree node = dico;
            while (node != null && j < morse.size()) {
                node = (morse.get(j) == MorseChar.DOT)
                        ? node.dot
                        : node.dash;
                if (node != null) {
                    tab[j] += mul * node.wordsCount;
                }

                j++;
            }
        }

        return tab[morse.size() - 1];
    }
}
