package q1;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DessinListener extends MouseAdapter {

    private final DessinModel model;
    private Point previousPoint;

    public DessinListener(DessinModel model) {
        this.model = model;
        this.previousPoint = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousPoint = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = e.getPoint();
        if (previousPoint != null) {
            model.addLine(previousPoint, point);
        }

        previousPoint = point;
    }
}
