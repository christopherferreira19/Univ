package q3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DessinListener extends MouseAdapter {

    private final DessinArea area;

    private Point previousPoint;

    public DessinListener(DessinArea area) {
        this.area = area;

        this.previousPoint = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (area.isCopyState()) {
            area.startCopySelect(e.getPoint());
        }
        else if (area.isPasteState()){
            area.paste(e.getPoint());
        }
        else {
            area.nextBuf();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (area.isCopyState()) {
            area.stopCopySelect(e.getPoint());
        }
        else {
            previousPoint = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (area.isCopyState()) {
            area.updateCopySelect(e.getPoint());
        }
        else {
            Point point = e.getPoint();
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (previousPoint != null) {
                    area.draw(previousPoint, point);
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                area.erase(point);
            }

            previousPoint = point;
        }
    }
}
