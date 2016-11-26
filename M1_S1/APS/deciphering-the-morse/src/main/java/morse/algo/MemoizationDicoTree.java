package morse.algo;

import morse.Input;
import morse.MorseChar;

import java.util.List;

public class MemoizationDicoTree {

    public static int count_sentences(Input input) {
        DicoTree dicoTree = DicoTree.create(input.dictionary);
        int[] memo = new int[input.sequence.size()];
        for (int i = 0; i < input.sequence.size(); i++) {
            memo[i] = -1;
        }
        return count_sentences_rec(input.sequence, 0, dicoTree, memo);
    }

    public static int count_sentences_rec(List<MorseChar> morse, int from, DicoTree dicoRoot, int[] memo) {
        if (memo[from] != -1) {
            return memo[from];
        }

        int count = 0;

        int i = from;
        DicoTree node = dicoRoot;
        for (;;) {
            node = (morse.get(i) == MorseChar.DOT)
                    ? node.dot
                    : node.dash;
            i++;

            if (node == null) {
                break;
            }
            else if (i >= morse.size()) {
                count += node.wordsCount;
                break;
            }

            count += node.wordsCount * count_sentences_rec(morse, i, dicoRoot, memo);
        }

        memo[from] = count;
        return count;
    }
}
