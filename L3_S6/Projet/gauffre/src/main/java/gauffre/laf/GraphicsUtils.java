package gauffre.laf;

import gauffre.laf.control.Control;

import java.awt.*;

public class GraphicsUtils {

    private GraphicsUtils() {
    }

    public static Object setupAntiAliasing(Graphics2D g2d) {
        Object old = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return old;
    }

    public static void restoreAntiAliasing(Graphics2D g2d, Object oldRenderingHint) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldRenderingHint);
    }

    public static Composite setupAlphaComposite(Graphics2D g2d, Float alpha) {
        Composite comp = g2d.getComposite();
        if (alpha == null) {
            return comp;
        }

        float currentComposite = 1f;
        if (comp != null && comp instanceof AlphaComposite) {
            currentComposite = ((AlphaComposite) comp).getAlpha();
        }

        AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentComposite * alpha);
        g2d.setComposite(newComposite);

        return comp;
    }

    public static void restoreComposite(Graphics2D g2d, Composite oldComposite) {
        g2d.setComposite(oldComposite);
    }

    public static void drawRounded(Graphics g, int x, int y, int width, int height, Control control) {
        int midWidth = width / 2 + 1;
        int midHeight = height / 2 + 1;
        int midX = x + midWidth;
        int midY = y + midHeight;

        drawCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedTopLeft(), x, y);
        drawCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedTopRight(), midX, y);
        drawCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedBottomLeft(), x, midY);
        drawCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedBottomRight(), midX, midY);
    }

    private static void drawCorner(Graphics g, int x, int y, int width, int height, int arc,
                                   int clipWidth, int clipHeight, boolean round, int clipX, int clipY) {
        Shape oldClip = g.getClip();
        g.clipRect(clipX, clipY, clipWidth, clipHeight);
        if (round) {
            g.drawRoundRect(x, y, width, height, arc, arc);
        }
        else {
            g.drawRect(x, y, width, height);
        }
        g.setClip(oldClip);
    }

    public static void fillRounded(Graphics g, int x, int y, int width, int height, Control control) {
        int midWidth = width / 2 + 1;
        int midHeight = height / 2 + 1;
        int midX = x + midWidth;
        int midY = y + midHeight;

        fillCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedTopLeft(), x, y);
        fillCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedTopRight(), midX, y);
        fillCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedBottomLeft(), x, midY);
        fillCorner(g, x, y, width, height, control.getArc(), midWidth, midHeight, control.isRoundedBottomRight(), midX, midY);
    }

    private static void fillCorner(Graphics g, int x, int y, int width, int height, int arc,
                                   int clipWidth, int clipHeight, boolean round, int clipX, int clipY) {
        Shape oldClip = g.getClip();
        g.clipRect(clipX, clipY, clipWidth, clipHeight);
        if (round) {
            g.fillRoundRect(x, y, width, height, arc, arc);
        }
        else {
            g.fillRect(x, y, width, height);
        }
        g.setClip(oldClip);
    }
}
