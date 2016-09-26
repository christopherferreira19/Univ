package terrain;

import java.awt.*;

public class Statut {

    public static final Statut DUMMY = new Statut();

    private final Color[] colors;
    private final String[] texts;

    public Statut() {
        int length = Dir.values().length + 1;
        this.colors = new Color[length];
        this.texts = new String[length];
        for (int i = 0; i < length; i++) {
            this.colors[i] = Color.WHITE;
            this.texts[i] = "";
        }
    }

    private Statut(Color[] colors, String[] texts) {
        this.colors = colors;
        this.texts = texts;
    }

    private int idx(Dir dir) {
        return dir == null ? 0 : dir.ordinal() + 1;
    }

    public Color getColor(Dir dir) {
        return colors[idx(dir)];
    }

    public String getText(Dir dir) {
        return texts[idx(dir)];
    }

    public void resetColor(Dir dir) {
        colors[idx(dir)] = Color.WHITE;
    }

    public void resetText(Dir dir) {
        texts[idx(dir)] = "";
    }

    public void setColor(Dir dir, Color color) {
        colors[idx(dir)] = color;
    }

    public void setText(Dir dir, String text) {
        texts[idx(dir)] = text;
    }

    public Statut clone() {
        Color[] colorsCopy = new Color[colors.length];
        String[] textsCopy = new String[texts.length];
        System.arraycopy(colors, 0, colorsCopy, 0, colorsCopy.length);
        System.arraycopy(texts, 0, textsCopy, 0, textsCopy.length);
        return new Statut(colorsCopy, textsCopy);
    }
}
