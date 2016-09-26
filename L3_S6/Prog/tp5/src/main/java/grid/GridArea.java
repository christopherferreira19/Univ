package grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridArea extends JComponent {

    private final GridModel model;

    public GridArea(GridModel model) {
        this.model = model;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int tileWidth = getWidth() / model.getN();
                int tileHeight = getHeight() / model.getN();

                int i = e.getX() / tileWidth;
                int j = e.getY() / tileHeight;
                model.toggle(i, j);
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int tileWidth = getWidth() / model.getN();
        int tileHeight = getHeight() / model.getN();

        for (int i = 0; i < getWidth(); i += tileWidth) {
            g2.drawLine(i, 0, i, getHeight());
        }

        for (int j = 0; j < getHeight(); j += tileHeight) {
            g2.drawLine(0, j, getWidth(), j);
        }

        for (int i = 0; i < model.getN(); i++) {
            for (int j = 0; j < model.getN(); j++) {
                int x = i * tileWidth + 2;
                int y = j * tileHeight + 2;
                int width = tileWidth - 4;
                int height = tileHeight - 4;

                if (model.get(i, j)) {
                    circle(g2, x, y, width, height);
                }
                else {
                    cross(g2, x, y, width, height);
                }
            }
        }
    }

    private void cross(Graphics2D g2, int x, int y, int width, int height) {
        g2.drawLine(
                x,
                y,
                x + width,
                y + height);
        g2.drawLine(
                x,
                y + height,
                x + width,
                y);
    }

    private void circle(Graphics2D g2, int x, int y, int width, int height) {
        g2.drawOval(x, y, width, height);
    }
}
