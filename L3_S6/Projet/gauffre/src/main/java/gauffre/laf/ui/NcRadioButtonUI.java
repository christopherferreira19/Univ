package gauffre.laf.ui;

import gauffre.laf.GraphicsUtils;
import gauffre.laf.Theme;
import gauffre.laf.control.Control;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;

public class NcRadioButtonUI extends BasicRadioButtonUI implements ControlUI {

    private static final int RADIO_ICON_WIDTH = 16;
    private static final int RADIO_ICON_HEIGHT = 16;

    protected final Control control;

    public static ComponentUI createUI(JComponent b) {
        return new NcRadioButtonUI();
    }

    public NcRadioButtonUI() {
        this.control = new Control(false);
        control.setArc(4);
    }

    @Override
    public Control getControl() {
        return control;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        ((AbstractButton) c).setIconTextGap(8);
        control.bindTo(c);
        c.setOpaque(false);
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        icon = createIcon();
    }

    protected Icon createIcon() {
        return new RadioIcon();
    }

    private class RadioIcon implements Icon {

        @Override
        public int getIconWidth() {
            return RADIO_ICON_WIDTH;
        }

        @Override
        public int getIconHeight() {
            return RADIO_ICON_HEIGHT;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            Object oldAntiAliasing = GraphicsUtils.setupAntiAliasing(g2d);

            control.setEnabled(c.isEnabled());

            g2d.setPaint(control.controlPaint(x, y, 16));
            g2d.fillOval(x, y, RADIO_ICON_WIDTH, RADIO_ICON_HEIGHT);

            g2d.setColor(control.borderPaint(c.hasFocus()));
            g2d.drawOval(x, y, RADIO_ICON_WIDTH, RADIO_ICON_HEIGHT);
            g2d.setPaint(control.internalBorderPaint(x + 1, y + 1, RADIO_ICON_HEIGHT - 2));
            g2d.drawOval(x + 1, y + 1, RADIO_ICON_WIDTH - 2, RADIO_ICON_HEIGHT - 2);

            if (control.isSelected()) {
                Color color = Theme.FOREGROUND;
                if (!control.isEnabled()) {
                    color = color.brighter().brighter();
                }
                g2d.setPaint(new GradientPaint(x + 4, y + 4, color.brighter().brighter(),
                        x + 12, y + 12, color));
                g2d.fillOval(x + 4, y + 4, 9, 9);
            }

            GraphicsUtils.restoreAntiAliasing(g2d, oldAntiAliasing);
        }
    }
}
