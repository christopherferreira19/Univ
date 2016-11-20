package morse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Input {

    final List<Morse> sequence;
    final List<String> dictionary;

    public static Input fromFilename(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            List<Morse> sequence = new ArrayList<>();
            char c;
            while ((c = (char) reader.read()) != '\n') {
                sequence.add(Morse.of(c));
            }

            int dictionnarySize = Integer.parseInt(reader.readLine());
            List<String> dictionary = new ArrayList<>(dictionnarySize);
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word);
            }

            return new Input(sequence, dictionary);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Input(List<Morse> sequence, List<String> dictionary) {
        this.sequence = sequence;
        this.dictionary = dictionary;
    }
}
