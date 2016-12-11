package morse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Input {

    public final List<MorseChar> sequence;
    public final int maxWordSize;
    public final List<String> dictionary;
    public final int maxMorseWordSize;
    public final List<List<MorseChar>> morseDictionary;

    public static Input fromFilename(String filename) {
        int maxWordSize = 0;
        int maxMorseWordSize = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            List<MorseChar> sequence = new ArrayList<>();
            char c;
            while ((c = (char) reader.read()) != '\n') {
                sequence.add(MorseChar.of(c));
            }

            int dictionnarySize = Integer.parseInt(reader.readLine());
            List<String> dictionary = new ArrayList<>(dictionnarySize);
            List<List<MorseChar>> morseDictionary = new ArrayList<>(dictionnarySize);
            String word;
            while ((word = reader.readLine()) != null) {
                if (maxWordSize < word.length()) {
                    maxWordSize = word.length();
                }
                dictionary.add(word.toLowerCase());

                List<MorseChar> morseCharWord = new ArrayList<>();
                for (int i = 0; i < word.length(); i++){
                    c = word.charAt(i);
                    morseCharWord.addAll(Alpha2Morse.of(c).code);
                }

                if (maxMorseWordSize < morseCharWord.size()) {
                    maxMorseWordSize = morseCharWord.size();
                }
                morseDictionary.add(morseCharWord);
            }

            return new Input(sequence, maxWordSize, dictionary, maxMorseWordSize, morseDictionary);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Input(List<MorseChar> sequence,
                 int maxWordSize, List<String> dictionary,
                 int maxMorseWordSize, List<List<MorseChar>> morseDictionary) {
        this.sequence = sequence;
        this.maxWordSize = maxWordSize;
        this.dictionary = dictionary;
        this.maxMorseWordSize = maxMorseWordSize;
        this.morseDictionary = morseDictionary;
    }
}
