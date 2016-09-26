package gauffre.laf.control;

import gauffre.laf.Theme;

import javax.swing.border.Border;
import java.awt.*;

class ControlBorder implements Border {

    private Control control;

    public ControlBorder(Control control) {
        this.control = control;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return (Insets) Theme.CONTROL_BORDER_INSETS.clone();
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        control.paintBorder(c.hasFocus(), g, x, y, w, h);
    }
}
