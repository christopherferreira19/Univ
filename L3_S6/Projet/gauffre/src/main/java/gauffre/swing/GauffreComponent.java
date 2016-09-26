package gauffre.swing;

import gauffre.Coords;
import gauffre.moteur.Gauffre;
import gauffre.moteur.Moteur;
import gauffre.moteur.MoteurObserver;

import javax.swing.*;
import java.awt.*;

class GauffreComponent extends JComponent implements MoteurObserver {

    private static final Color TILE_COLOR = new Color(92, 51, 23);
    private static final Color TILE_OVERLAY_COLOR = new Color(112, 71, 43);
    private static final Color TILE_PAUSED_COLOR = new Color(92, 70, 59);
    private static final Color BG_COLOR = new Color(240, 240, 240);
    private static final Color POISON_COLOR = new Color(180, 10, 10);
    private static final Color POISON_PAUSED_COLOR = new Color(126, 46, 46);

    private static int PREFERRED_TILE_SIZE = 60;

    private final Moteur moteur;

    private int overlayL;
    private int overlayC;
    private int dx;
    private int dy;

    GauffreComponent(Moteur moteur) {
        this.moteur = moteur;
        clearOverlay();
        this.dx = 0;
        this.dy = 0;
        moteur.registerObserver(this);

        setPreferredSize(new Dimension(
                moteur.getGauffre().getWidth() * PREFERRED_TILE_SIZE,
                moteur.getGauffre().getHeight() * PREFERRED_TILE_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Gauffre gauffre = moteur.getGauffre();

        int tileWidth = getWidth() / gauffre.getWidth();
        int tileHeight = getHeight() / gauffre.getHeight();


        g2.setPaint(BG_COLOR);
        g2.fillRect(0, 0, getWidth(),  getHeight());

        int tileSize = tileHeight <= tileWidth
                ? tileHeight
                : tileWidth ;

        this.dx = (getWidth() - tileSize * gauffre.getWidth()) / 2;// + 2;
        this.dy = (getHeight() - tileSize * gauffre.getHeight()) / 2;// + 2;

        Color tileColor = moteur.isPaused()
                ? TILE_PAUSED_COLOR
                : TILE_COLOR;

        for (int l = 0; l < gauffre.getHeight(); l++) {
            for (int c = 0; c < gauffre.getWidth(); c++) {
                if (!gauffre.getState(l, c)) {
                    break;
                }

                int x = c * tileSize + dx;
                int y = l * tileSize + dy;

                g2.setPaint(overlayL <= l && overlayC <= c
                    ? TILE_OVERLAY_COLOR
                    : tileColor);
                g2.fillRect(x, y, tileSize - 1, tileSize - 1);
            }
        }

        g2.setPaint(moteur.isPaused() ? POISON_PAUSED_COLOR : POISON_COLOR);
        g2.fillRect(dx, dy, tileSize - 1, tileSize - 1);
    }

    @Override
    public void changed() {
        repaint();
    }

    Coords pointToCoords(Point point) {
        Gauffre gauffre = moteur.getGauffre();
        int tileWidth = getWidth() / gauffre.getWidth();
        int tileHeight = getHeight() / gauffre.getHeight();
        int tileSize = tileHeight <= tileWidth
                ? tileHeight
                : tileWidth ;
        return new Coords((point.y - dy) / tileSize, (point.x - dx) / tileSize);
    }

    void clearOverlay() {
        setOverlay(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    void setOverlay(int l, int c) {
        if (overlayL == l && overlayC == c) {
            return;
        }

        if (l == 0 && c == 0) {
            clearOverlay();
            return;
        }

        overlayL = l;
        overlayC = c;
        repaint();
    }
}
