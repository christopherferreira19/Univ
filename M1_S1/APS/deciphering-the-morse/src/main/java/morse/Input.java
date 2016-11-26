package morse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Input {

    public final List<MorseChar> sequence;
    public final List<String> dictionary;

    public static Input fromFilename(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            List<MorseChar> sequence = new ArrayList<>();
            char c;
            while ((c = (char) reader.read()) != '\n') {
                sequence.add(MorseChar.of(c));
            }

            int dictionnarySize = Integer.parseInt(reader.readLine());
            List<String> dictionary = new ArrayList<>(dictionnarySize);
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }

            return new Input(sequence, dictionary);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Input(List<MorseChar> sequence, List<String> dictionary) {
        this.sequence = sequence;
        this.dictionary = dictionary;
    }
}
