package morse.algo;

import morse.Alpha2Morse;
import morse.MorseChar;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    public static List<List<MorseChar>> alphaToMorse(List<String> dictionary) {
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
}
