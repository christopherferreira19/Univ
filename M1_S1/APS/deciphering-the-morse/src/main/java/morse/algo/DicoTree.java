package morse.algo;

import morse.Alpha2Morse;
import morse.MorseChar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DicoTree {

    int wordsCount;
    DicoTree dot;
    DicoTree dash;
    List<String> words; // for debug

    DicoTree() {
        this.wordsCount = 0;
        this.dot = null;
        this.dash = null;
        this.words = new ArrayList<>();
    }

    public static DicoTree create(List<String> dico) {
        DicoTree root = new DicoTree();
        for (String word : dico) {
            DicoTree node = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                List<MorseChar> code = Alpha2Morse.of(c).code;
                for (MorseChar morseChar : code) {
                    if (morseChar == MorseChar.DOT) {
                        node = (node.dot == null)
                                ? (node.dot = new DicoTree())
                                : node.dot;
                    }
                    else {
                        node = (node.dash == null)
                                ? (node.dash = new DicoTree())
                                : node.dash;
                    }
                }
            }

            node.wordsCount += 1;
            node.words.add(word);
        }

        return root;
    }
}
