package gauffre.laf;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {

    public static Font regular() {
        return REGULAR;
    }

    public static Font italic() {
        return REGULAR_ITALIC;
    }

    public static Font bold() {
        return BOLD;
    }

    public static Font boldItalic() {
        return BOLD_ITALIC;
    }

    private static final Font REGULAR = load("SourceSansPro-Regular.ttf");
    private static final Font REGULAR_ITALIC = load("SourceSansPro-It.ttf");
    private static final Font BOLD = load("SourceSansPro-Bold.ttf");
    private static final Font BOLD_ITALIC = load("SourceSansPro-BoldIt.ttf");

    private static Font load(String filename) {
        InputStream stream = Fonts.class.getResourceAsStream("fonts/" + filename);
        if (stream == null) {
            throw new RuntimeException("Font file not found");
        }

        try {
            Font loaded = Font.createFont(Font.TRUETYPE_FONT, stream);
            Font derived = loaded.deriveFont(14f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(derived);
            return derived;
        } catch (FontFormatException exc) {
            throw new RuntimeException(exc);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private Fonts() {
    }
}
