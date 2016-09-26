package gauffre.laf.ui;

import gauffre.laf.Theme;
import gauffre.laf.control.Control;
import gauffre.laf.icons.ArrowIcons;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class NcScrollBarUI extends BasicScrollBarUI {

    private final Control control = new Control(false);

    public static ComponentUI createUI(JComponent c) {
        return new NcScrollBarUI();
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);

        control.setRounded(false);
        control.setArmed(false);
    }

    @Override
    public JButton createDecreaseButton(int orientation) {
        return createButton(orientation);
    }

    @Override
    public JButton createIncreaseButton(int orientation) {
        return createButton(orientation);
    }

    private JButton createButton(int orientation) {
        JButton b = new JButton(ArrowIcons.of(orientation));
        ((ControlUI) b.getUI()).getControl().setRounded(false);
        int size = UIManager.getInt("ScrollBar.width");
        b.setPreferredSize(new Dimension(size, size));
        b.setFocusable(false);
        return b;
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        super.paintTrack(g, c, trackBounds);
        g.setColor(Theme.CONTROL_BORDER_COLOR);
        if (scrollbar.getOrientation() == HORIZONTAL) {
            int y = trackBounds.y + trackBounds.height - 1;
            g.drawLine(trackBounds.x, trackBounds.y, trackBounds.x + trackBounds.width - 1, trackBounds.y);
            g.drawLine(trackBounds.x, y, trackBounds.x + trackBounds.width - 1, y);
        }
        else {
            int x = trackBounds.x + trackBounds.width - 1;
            g.drawLine(trackBounds.x, trackBounds.y, trackBounds.x, trackBounds.y + trackBounds.height - 1);
            g.drawLine(x, trackBounds.y, x, trackBounds.y + trackBounds.height - 1);
        }
    }

    @Override
    public void paintThumb(Graphics g, JComponent c, Rectangle rect) {
        control.setEnabled(c.isEnabled());
        control.setRollover(isThumbRollover());
        control.setArmed(isDragging);

        control.paint(g, rect);
        control.paintBorder(false, g, rect.x, rect.y, rect.width, rect.height);
    }
}
