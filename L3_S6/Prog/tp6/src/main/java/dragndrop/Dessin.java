package dragndrop;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Dessin extends JComponent implements DragObserver {

    private final DragModel drag;

    private BufferedImage buf;

    public Dessin(DragModel drag) {
        this.drag = drag;
        this.buf = null;
        drag.register(this);
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


    @Override
    public void started() {
    }

    @Override
    public void updated() {
    }

    @Override
    public void cancelled() {
    }

    @Override
    public void stopped() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Point point = SwingUtilities.convertPoint(
                frame.getGlassPane(),
                drag.getPoint(),
                this);

        Graphics2D g2 = buf.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver);
        Image image = drag.getImage();
        int x = point.x - image.getWidth(null) / 2;
        int y = point.y - image.getHeight(null) / 2;
        g2.drawImage(image, x, y, null);
        repaint();
    }
}
