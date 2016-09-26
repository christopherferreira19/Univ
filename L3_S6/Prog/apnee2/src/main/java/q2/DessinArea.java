package q2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DessinArea extends JComponent {

    private static final Paint TRANSPARENT = new Color(0, 0, 0, 0);

    private BufferedImage buf;

    public DessinArea() {
        this.buf = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buf == null || buf.getWidth() < getWidth() || buf.getHeight() < getHeight()) {
            resizeBuf();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.drawImage(buf, 0, 0, null);
    }

    private void resizeBuf() {
        BufferedImage newBuf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D newG2 = newBuf.createGraphics();
        newG2.drawImage(buf, 0, 0, null);
        this.buf = newBuf;
    }

    public void draw(Point from, Point to) {
        Graphics2D g2 = buf.createGraphics();
        g2.setPaint(Color.black);
        g2.drawLine(from.x, from.y, to.x, to.y);
        repaint();
    }

    public void erase(Point around) {
        Graphics2D g2 = buf.createGraphics();
        g2.setPaint(TRANSPARENT);
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(around.x - 5, around.y - 5, 10, 10);
        repaint();
    }
}
