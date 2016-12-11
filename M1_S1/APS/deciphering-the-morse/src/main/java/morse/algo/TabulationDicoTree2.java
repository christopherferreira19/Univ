package morse.algo;

import morse.Input;
import morse.MorseChar;

import java.util.Arrays;
import java.util.List;

public class TabulationDicoTree2 {

    private static class CircularArray {
        private final int[] ary;
        private int first;

        private CircularArray(int size) {
            this.ary = new int[size];
            Arrays.fill(ary, 0);
            this.first = 0;
        }

        private void add(int index, int val) {
            index += first;
            if (index >= ary.length) {
                index -= ary.length;
            }
            ary[index] += val;
        }

        private int removeFirst() {
            int value = ary[first];
            ary[first] = 0;
            first++;
            if (first >= ary.length) {
                first -= ary.length;
            }
            return value;
        }
    }

    public static int count_sentences(Input input) {
        DicoTree dico = DicoTree.create(input.dictionary);
        List<MorseChar> morse = input.sequence;

        CircularArray tab = new CircularArray(input.maxMorseWordSize);
        int previous = 1;
        for (int i = 0; i < morse.size(); i++) {
            int j = i;
            if (previous > 0) {
                DicoTree node = dico;
                while (node != null && j < morse.size()) {
                    node = (morse.get(j) == MorseChar.DOT)
                            ? node.dot
                            : node.dash;
                    if (node != null) {
                        tab.add(j - i, previous * node.wordsCount);
                    }

                    j++;
                }
            }

            previous = tab.removeFirst();
        }

        return previous;
    }
}
