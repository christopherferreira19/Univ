package dragndrop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DragPane extends JComponent implements DragObserver {

    private final DragModel drag;

    public DragPane(JFrame frame, DragModel drag) {
        this.drag = drag;
        drag.register(this);

        MouseHandler mouseHandler = new MouseHandler(frame);
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    public void started() {
        repaint();
    }

    @Override
    public void updated() {
        repaint();
    }

    @Override
    public void cancelled() {
        repaint();
    }

    @Override
    public void stopped() {
        repaint();
    }

    private class MouseHandler extends GlassPaneMouseRedispatcher {

        public MouseHandler(JFrame frame) {
            super(frame);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            drag.update(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            drag.stop();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (drag.isOngoing()) {
            Image image = drag.getImage();
            int x = drag.getPoint().x - image.getWidth(null) / 2;
            int y = drag.getPoint().y - image.getHeight(null) / 2;
            g2.drawImage(image, x, y, null);
            g2.setPaint(Color.LIGHT_GRAY);
            g2.drawRect(x, y, image.getWidth(null), image.getHeight(null));
        }
    }
}
