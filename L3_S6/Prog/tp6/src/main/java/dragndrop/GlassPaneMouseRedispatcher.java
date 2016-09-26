package dragndrop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GlassPaneMouseRedispatcher implements MouseListener, MouseMotionListener {

    private final JFrame frame;

    public GlassPaneMouseRedispatcher(JFrame frame) {
        this.frame = frame;
    }

    private void redispatchMouseEvent(MouseEvent e) {
        Point glassPanePoint = e.getPoint();
        Container container = frame.getContentPane();
        Point containerPoint = SwingUtilities.convertPoint(
                frame.getGlassPane(),
                glassPanePoint,
                container);

        if (containerPoint.y >= 0) {
            //The mouse event is probably over the content pane.
            //Find out exactly which component it's over.
            Component component = SwingUtilities.getDeepestComponentAt(
                    container,
                    containerPoint.x,
                    containerPoint.y);

            if (component != null) {
                //Forward events over the check box.
                Point componentPoint = SwingUtilities.convertPoint(
                        frame.getGlassPane(),
                        glassPanePoint,
                        component);
                component.dispatchEvent(new MouseEvent(component,
                        e.getID(),
                        e.getWhen(),
                        e.getModifiers(),
                        componentPoint.x,
                        componentPoint.y,
                        e.getClickCount(),
                        e.isPopupTrigger()));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        redispatchMouseEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        redispatchMouseEvent(e);
    }
}
