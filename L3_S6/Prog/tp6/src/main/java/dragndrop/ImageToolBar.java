package dragndrop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ImageToolBar extends JComponent {

    private static final int X = 4;
    private static final int POUSSEUR_Y = 50;
    private static final int SAC_Y = 200;
    private static final int BUT_Y = 350;

    private final DragModel dragModel;

    private final Image but;
    private final Image pousseur;
    private final Image sac;

    public ImageToolBar(DragModel drag) {
        this.dragModel = drag;
        this.but = readImage("But.png");
        this.pousseur = readImage("Pousseur.png");
        this.sac = readImage("Sac.png");

        int width = X * 2 + Math.max(
                but.getWidth(null),
                Math.max(
                        pousseur.getWidth(null),
                        sac.getWidth(null)));

        Dimension size;

        size = getPreferredSize();
        size.width = width;
        setPreferredSize(size);

        size = getMaximumSize();
        size.width = width;
        setMaximumSize(size);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (checkImage(but, X, BUT_Y, p)) {
                    drag.start(but, p);
                }
                else if (checkImage(pousseur, X, POUSSEUR_Y, p)) {
                    drag.start(pousseur, p);
                }
                else if (checkImage(sac, X, SAC_Y, p)) {
                    drag.start(sac, p);
                }
            }

            private boolean checkImage(Image image, int x, int y, Point point) {
                return point.x >= x && point.x < x + image.getWidth(null)
                        && point.y >= y && point.y < y + image.getHeight(null);
            }
        });
    }

    private Image readImage(String name) {
        try {
            return ImageIO.read(ImageToolBar.class.getResource(name));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setComposite(AlphaComposite.SrcOver);
        g2.drawImage(pousseur, X, POUSSEUR_Y, null);
        g2.drawImage(sac, X, SAC_Y, null);
        g2.drawImage(but, X, BUT_Y, null);

        g2.setPaint(Color.WHITE);
        g2.drawRect(X, POUSSEUR_Y, pousseur.getWidth(null), pousseur.getHeight(null));
        g2.drawRect(X, SAC_Y, sac.getWidth(null), sac.getHeight(null));
        g2.drawRect(X, BUT_Y, but.getWidth(null), but.getHeight(null));

    }
}
