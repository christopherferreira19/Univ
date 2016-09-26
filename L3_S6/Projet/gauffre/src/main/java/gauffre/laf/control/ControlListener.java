package gauffre.laf.control;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ControlListener extends MouseAdapter implements FocusListener {

    private final Control control;

    ControlListener(Control control) {
        this.control = control;
    }

    @Override
    public void focusGained(FocusEvent e) {
        ((JComponent) e.getSource()).repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        ((JComponent) e.getSource()).repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        control.setRollover(true);
        ((JComponent) e.getSource()).repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        control.setRollover(false);
        ((JComponent) e.getSource()).repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        control.setArmed(true);
        ((JComponent) e.getSource()).repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        control.setArmed(false);
        ((JComponent) e.getSource()).repaint();
    }
}
